package org.sudu.experiments.editor;

import org.sudu.experiments.Const;
import org.sudu.experiments.Debug;
import org.sudu.experiments.editor.ui.FindUsagesItemBuilder;
import org.sudu.experiments.editor.ui.FindUsagesItemData;
import org.sudu.experiments.editor.ui.FindUsagesView;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.ui.ToolbarConst.fireOnHover;
import static org.sudu.experiments.ui.ToolbarItemBuilder.ti;

public class EditorUi {
  static boolean developer = false;

  public final WindowManager windowManager;

  Window usagesWindow;
  EditorColorScheme theme;

  public EditorUi(WindowManager wm) {
    windowManager = wm;
  }

  public void setTheme(EditorColorScheme theme) {
    this.theme = theme;
    if (usagesWindow != null) usagesWindow.setTheme(theme.dialogItem);
    windowManager.setPopupTheme(theme);
  }

  public void dispose() {
    if (usagesWindow != null) {
      disposeUsagesWindow();
    }
  }

  void showUsagesWindow(
      V2i position,
      List<Pos> usagesList,
      EditorComponent editor,
      String elementName
  ) {
    showUsagesWindow(
        position,
        editor,
        FindUsagesItemBuilder.buildUsagesItems(usagesList, editor),
        elementName
    );
  }

  void showUsagesWindow(
      V2i position,
      Location[] locs,
      EditorComponent editor,
      String elementName
  ) {
    showUsagesWindow(
        position,
        editor,
        FindUsagesItemBuilder.buildDefItems(locs, editor),
        elementName
    );
  }

  void showUsagesWindow(
      V2i position,
      EditorComponent editor,
      FindUsagesItemData[] actions,
      String elementName
  ) {
    if (usagesWindow != null) disposeUsagesWindow();
    UiContext uiContext = windowManager.uiContext;
    FindUsagesView usagesView = new FindUsagesView(uiContext, () -> {
      windowManager.uiContext.setFocus(editor);
      disposeUsagesWindow();
    });

    usagesView.setItems(actions);
    usagesView.setTheme(theme.dialogItem, theme.usagesFont);

    usagesWindow = new Window(uiContext);
    usagesWindow.setBypassHitTest(true);
    ScrollView scrollView = new ScrollView(usagesView);
    scrollView.setScrollColor(theme.dialogItem.dialogScrollLine, theme.dialogItem.dialogScrollBg);
    usagesWindow.setContent(scrollView);
    usagesWindow.setTheme(theme.dialogItem);
    usagesWindow.setTitle("Usages of " + elementName);
    windowManager.addWindow(usagesWindow);
    int minY = usagesWindow.titleHeight() + uiContext.toPx(2);
    V2i limitedPosition = usagesView.setLimitedPosition(position, minY);
    V2i size = usagesView.calculateSize(limitedPosition);
    usagesWindow.setPosition(limitedPosition, size);

    uiContext.setFocus(usagesView);
  }

  private void disposeUsagesWindow() {
    windowManager.removeWindow(usagesWindow);
    usagesWindow.dispose();
    usagesWindow = null;
  }

  public void displayPopup(V2i pos, Supplier<ToolbarItem[]> actions) {
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont, pos, actions);
  }

  public void hidePopup() {
    windowManager.hidePopupMenu();
  }

  void displayNoUsagesPopup(V2i position) {
    displayPopup(position, noDefOrUsagesPop());
  }

  private Supplier<ToolbarItem[]> noDefOrUsagesPop() {
    return ArrayOp.supplier(
        new ToolbarItem(windowManager::hidePopupMenu,
            "No definition or usages"));
  }

  public void showEditorMenu(
      V2i position, EditorComponent editor,
      ThemeControl themeApi,
      FontApi fontApi,
      CleartypeControl cleartypeControl,
      Supplier<String[]> fonts
  ) {
    displayPopup(position,
        builder(editor, fonts, themeApi,
            fontApi, cleartypeControl).build(position));
  }

  public PopupMenuBuilder builder(
      EditorComponent editor,
      Supplier<String[]> fonts,
      ThemeControl themeApi,
      FontApi fontApi,
      CleartypeControl cleartypeControl
  ) {
    return new PopupMenuBuilder(
        editor,
        fonts, fontApi, cleartypeControl,
        themeApi);
  }

  public interface CleartypeControl {
    void enableCleartype(boolean en);
  }

  public interface FontApi {
    void increaseFont();
    void decreaseFont();
    void changeFont(String f);
    void setFontPow(float p);
  }

  public class PopupMenuBuilder {

    final EditorComponent editor;
    final Supplier<String[]> fonts;
    final FontApi fontApi;
    final CleartypeControl cleartypeControl;
    final ThemeControl themeApi;

    public PopupMenuBuilder(
        EditorComponent editor,
        Supplier<String[]> fonts,
        FontApi fontApi,
        CleartypeControl cleartypeControl,
        ThemeControl themeApi
    ) {
      this.editor = editor;
      this.fonts = fonts;
      this.fontApi = fontApi;
      this.cleartypeControl = cleartypeControl;
      this.themeApi = themeApi;
    }

    public Supplier<ToolbarItem[]> build(V2i eventPosition, ToolbarItem opener) {

      ToolbarItemBuilder tbb = new ToolbarItemBuilder();

      gotoItems(eventPosition, tbb);
      cutCopyPaste(tbb);
      toggleSyncPoint(tbb, eventPosition);
      if (opener != null) tbb.addItem(opener);
      //noinspection ConstantValue
      if (developer) tbb.addItem("rendering debug", rDebugMenu());
      tbb.addItem("Language >", languageItems());
      tbb.addItem("Settings >", settingsItems());
      if (developer) {
        tbb.addItem("Development >", devItems(opener == null));
      }
      return tbb.supplier();
    }

    public Supplier<ToolbarItem[]> build(V2i eventPosition) {
      return build(eventPosition, null);
    }

    private Supplier<ToolbarItem[]> cleartypeItems() {
      return ArrayOp.supplier(
          ti("Greyscale", () -> cleartypeControl.enableCleartype(false)),
          ti("Subpixel", () -> cleartypeControl.enableCleartype(true)));
    }

    private void cutCopyPaste(ToolbarItemBuilder tbb) {
      if (window().isClipboardSupported()) {
        if (!editor.readonly) {
          tbb.addItem("Cut", this::cutAction);
        }
        tbb.addItem("Copy", this::copyAction);
        if (!editor.readonly && window().isReadClipboardTextSupported()) {
          tbb.addItem("Paste", this::pasteAction);
        }
      }
    }

    private void toggleSyncPoint(ToolbarItemBuilder tbb, V2i eventPos) {
      if (editor.hasSyncPoint(eventPos)) {
        tbb.addItem("Remove Sync Point", () -> toggleSyncPointAction(eventPos));
      } else {
        tbb.addItem("Add Sync Point", () -> toggleSyncPointAction(eventPos));
      }
    }

    private void toggleSyncPointAction(V2i eventPos) {
      windowManager.hidePopupMenu();
      editor.toggleSyncPoint(eventPos);
    }

    private org.sudu.experiments.Window window() {
      return editor.context.window;
    }

    private void pasteAction() {
      windowManager.hidePopupMenu();
      window().readClipboardText(
          editor::handleInsert,
          onError("readClipboardText error: "));
    }

    private void cutAction() {
      windowManager.hidePopupMenu();
      editor.onCopy(copyHandler(), true);
    }

    private void copyAction() {
      windowManager.hidePopupMenu();
      editor.onCopy(copyHandler(), false);
    }

    private Consumer<String> copyHandler() {
      return text -> window().writeClipboardText(text,
          org.sudu.experiments.Const.emptyRunnable,
          onError("writeClipboardText error: "));
    }

    private Supplier<ToolbarItem[]> languageItems() {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();
      for (var lang: Languages.getAllLanguages()) {
        tbb.addItem(lang, () -> editor.setLanguage(lang));
      }
      return tbb.supplier();
    }

    private Supplier<ToolbarItem[]> settingsItems() {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();
      tbb.addItem("Theme >", themes(), fireOnHover);
      tbb.addItem("Font size >", fontSize());
      tbb.addItem("Fonts >", fontSelect(), fireOnHover);
      if (editor.context.graphics.cleartypeSupported) {
        tbb.addItem("Text antialiasing >", cleartypeItems());
      }
      return tbb.supplier();
    }

    private Supplier<ToolbarItem[]> devItems(boolean showPicker) {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();
      tbb.addItem("parser >", parser());
      if (showPicker)
        tbb.addItem("open ...", this::showOpenFilePicker);
      tbb.addItem("font pow >", fontPow(), fireOnHover);
      return tbb.supplier();
    }

    void showOpenFilePicker() {
      window().showOpenFilePicker(
          windowManager.hidePopupMenuThen(
              f -> editor.openFile(f, Const.emptyRunnable)));
    }

    private void gotoItems(V2i eventPosition, ToolbarItemBuilder tbb) {
      Model model = editor.model();
      String language = model.language();
      String scheme = model.uriScheme();
      EditorRegistrations reg = editor.registrations;

      var declarationProvider = reg.findDeclarationProvider(language, scheme);

      if (declarationProvider != null) {
        tbb.addItem("Go to Declaration",
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DECL));
      }

      var definitionProvider = reg.findDefinitionProvider(language, scheme);

      if (definitionProvider != null) {
        tbb.addItem("Go to Definition",
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DEF));
      }

      var refProvider = reg.findReferenceProvider(language, scheme);

      if (refProvider != null) {
        tbb.addItem("Go to References",
            () -> findUsages(eventPosition));
      }

      tbb.addItem("Go to (local)",
          () -> findUsagesDefDecl(eventPosition, null));
    }

    private Supplier<ToolbarItem[]> parser() {
      return ArrayOp.supplier(
          ti("Int", editor::debugPrintDocumentIntervals),
          ti("Iter", editor::iterativeParsing),
          ti("VP", editor::parseViewport),
          ti("Resolve", editor::resolveAll),
          ti("Rep", editor::parseFullFile));
    }

    private Supplier<ToolbarItem[]> rDebugMenu() {
      var array = ArrayOp.array(
          ti("↓ move", editor::moveDown),
          ti("■ stop", editor::stopMove),
          ti("↑ move", editor::moveUp),
          ti("toggleXOffset", editor::toggleXOffset),
          ti("toggleTails", editor::toggleTails));
      if (editor.debugFlags[7] != null) {
        array = ArrayOp.add(array,
            ti("toggleCodeLineRemap", editor.debugFlags[7]));
      }
      return ArrayOp.supplier(array);
    }

    private Supplier<ToolbarItem[]> themes() {
      return ArrayOp.supplier(
          ti("Darcula", themeApi::toggleDarcula),
          ti("Dark", themeApi::toggleDark),
          ti("Light", themeApi::toggleLight)
      );
    }

    private Supplier<ToolbarItem[]> fontSize() {
      return ArrayOp.supplier(
          ti("↑ increase", fontApi::increaseFont),
          ti("↓ decrease", fontApi::decreaseFont));
    }

    private Runnable fontPow(float pow) {
      return () -> fontApi.setFontPow(pow);
    }

    private Supplier<ToolbarItem[]> fontPow() {
      return () -> {
        ToolbarItem[] items = new ToolbarItem[5 + 12];
        int p = 0;
        for (int i = 0; i < 5; i++) {
          float value = (4 + i) / 8.f;
          items[p++] = new ToolbarItem(fontPow(value), Float.toString(value));
        }
        for (int i = 0; i < 12; i++) {
          float value = 1 + (1 + i) / 4.f;
          items[p++] = new ToolbarItem(fontPow(value), Float.toString(value));
        }
        return items;
      };
    }

    private Supplier<ToolbarItem[]> fontSelect() {
      return () -> {
        String[] fonts = this.fonts.get();
        ToolbarItem[] items = new ToolbarItem[fonts.length];
        for (int i = 0; i < items.length; i++) {
          var font = fonts[i];
          Runnable runnable = () -> fontApi.changeFont(font);
          items[i] = new ToolbarItem(runnable, font);
        }
        return items;
      };
    }

    private void findUsages(V2i eventPosition) {
      String language = editor.model.language();
      String scheme = editor.model.uriScheme();
      ReferenceProvider.Provider provider = editor.registrations.findReferenceProvider(language, scheme);
      windowManager.hidePopupMenu();
      editor.findUsages(eventPosition, provider);
    }

    private void findUsagesDefDecl(V2i eventPosition, DefDeclProvider.Type type) {
      windowManager.hidePopupMenu();
      Model model = editor.model();
      String language = model.language();
      String scheme = model.uriScheme();
      EditorRegistrations reg = editor.registrations;
      DefDeclProvider.Provider provider = type != null ? switch (type) {
        case DEF -> reg.findDefinitionProvider(language, scheme);
        case DECL -> reg.findDeclarationProvider(language, scheme);
      } : null;
      editor.findUsages(eventPosition, provider);
    }
  }

  static Consumer<Throwable> onError(String s) {
    return throwable -> Debug.consoleInfo(s + throwable.getMessage());
  }
}
