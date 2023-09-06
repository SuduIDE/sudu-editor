package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.demo.ui.colors.EditorColorScheme;
import org.sudu.experiments.demo.ui.window.ScrollView;
import org.sudu.experiments.demo.ui.window.Window;
import org.sudu.experiments.demo.ui.window.WindowManager;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.demo.ui.ToolbarConst.fireOnHover;
import static org.sudu.experiments.demo.ui.ToolbarItemBuilder.ti;

class EditorUi implements MouseListener, InputListeners.ScrollHandler {
  final UiContext uiContext;
  final WindowManager windowManager = new WindowManager();

  Window usagesWindow;
  PopupMenu popupMenu;
  EditorColorScheme theme;

  EditorUi(UiContext context) {
    uiContext = context;
    popupMenu = new PopupMenu(uiContext);
  }

  void setTheme(EditorColorScheme theme) {
    this.theme = theme;
    if (usagesWindow != null) usagesWindow.setTheme(theme.dialogItem);
    popupMenu.setTheme(theme.dialogItem, theme.popupMenuFont);
  }

  void dispose() {
    popupMenu.dispose();
    if (usagesWindow != null) {
      disposeUsagesWindow();
    }
    windowManager.dispose();
  }

  void paint() {
    windowManager.draw(uiContext.graphics);
    popupMenu.paint();
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    return windowManager.onMouseMove(event)
        || popupMenu.onMouseMove(event);
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return windowManager.onMouseClick(event, button, clickCount)
        || popupMenu.onMouseClick(event, button, clickCount);
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    var r = popupMenu.onMouseDown(event, button);
    if (r != null) return r;
    return windowManager.onMouseDown(event, button);
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    return windowManager.onMouseUp(event, button)
        || popupMenu.onMouseUp(event, button);
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
    FindUsagesView usagesView = new FindUsagesView(uiContext, () -> {
      uiContext.setFocus(editor);
      disposeUsagesWindow();
    });

    usagesView.setItems(actions);
    usagesView.setTheme(theme.dialogItem, theme.usagesFont);

    usagesWindow = new Window(uiContext);
    ScrollView scrollView = new ScrollView(usagesView, uiContext);
    scrollView.setScrollColor(theme.dialogItem.dialogScrollLine, theme.dialogItem.dialogScrollBg);
    usagesWindow.setContent(scrollView);
    usagesWindow.setTitle("Usages of " + elementName, theme.windowTitleFont, 4);
    usagesWindow.setTheme(theme.dialogItem);
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

  void displayNoUsagesPopup(V2i position, EditorComponent edit) {
    popupMenu.hide();
    popupMenu.display(position, noDefOrUsagesPop(), setEditFocus(edit));
  }

  Runnable setEditFocus(EditorComponent editor) {
    return () -> uiContext.setFocus(editor);
  }

  private Supplier<ToolbarItem[]> noDefOrUsagesPop() {
    return ArrayOp.supplier(
        new ToolbarItem(
            popupMenu::hide,
            "No definition or usages",
            theme.dialogItem.findUsagesColorsError)
    );
  }

  public void showContextMenu(MouseEvent event, EditorComponent editor, ThemeApi themeApi, Supplier<String[]> fonts) {
    if (!popupMenu.isVisible()) {
      popupMenu.display(event.position,
          new PopupMenuBuilder(
              editor,
              fonts,
              themeApi).build(event.position),
          setEditFocus(editor));
    }
  }

  @Override
  public boolean onScroll(MouseEvent event, float dX, float dY) {
    return windowManager.onScroll(event, dX, dY);
  }

  public interface ThemeApi {
    void toggleDarcula();
    void toggleLight();
    void toggleDark();

    default void setTheme(String theme) {
      switch (theme) {
        case "light" -> toggleLight();
        case "darcula" -> toggleDarcula();
        case "dark" -> toggleDark();
        default -> Debug.consoleInfo("unknown theme: " + theme);
      }
    }
  }

  class PopupMenuBuilder {

    final EditorComponent editor;
    final Supplier<String[]> fonts;
    final ThemeApi themeApi;

    PopupMenuBuilder(
        EditorComponent editor,
        Supplier<String[]> fonts,
        ThemeApi themeApi) {
      this.editor = editor;
      this.fonts = fonts;
      this.themeApi = themeApi;
    }

    Supplier<ToolbarItem[]> build(V2i eventPosition) {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();

      gotoItems(eventPosition, tbb);
      cutCopyPaste(tbb);
      if (1 < 0) tbb.addItem("old >", theme.dialogItem.toolbarItemColors, oldDev());
      tbb.addItem("Settings >", theme.dialogItem.toolbarItemColors, settingsItems());
      tbb.addItem("Development >", theme.dialogItem.toolbarItemColors, devItems());
      return tbb.supplier();
    }

    private void cutCopyPaste(ToolbarItemBuilder tbb) {
      if (!editor.readonly) {
        tbb.addItem("Cut", theme.dialogItem.toolbarItemColors, this::cutAction);
      }
      tbb.addItem("Copy", theme.dialogItem.toolbarItemColors, this::copyAction);

      if (!editor.readonly && window().isReadClipboardTextSupported()) {
        tbb.addItem("Paste", theme.dialogItem.toolbarItemColors, this::pasteAction);
      }
    }

    private org.sudu.experiments.Window window() {
      return editor.context.window;
    }

    private void pasteAction() {
      popupMenu.hide();
      window().readClipboardText(
          editor::handleInsert,
          onError("readClipboardText error: "));
    }

    private void cutAction() {
      popupMenu.hide();
      editor.onCopy(copyHandler(), true);
    }

    private void copyAction() {
      popupMenu.hide();
      editor.onCopy(copyHandler(), false);
    }

    private Consumer<String> copyHandler() {
      return text -> window().writeClipboardText(text,
          org.sudu.experiments.Const.emptyRunnable,
          onError("writeClipboardText error: "));
    }

    private Supplier<ToolbarItem[]> settingsItems() {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();
      tbb.addItem("Theme >", theme.dialogItem.toolbarItemColors, themes(), fireOnHover);
      tbb.addItem("Font size >", theme.dialogItem.toolbarItemColors, fontSize());
      tbb.addItem("Fonts >", theme.dialogItem.toolbarItemColors, fontSelect(), fireOnHover);
      return tbb.supplier();
    }

    private Supplier<ToolbarItem[]> devItems() {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();
      tbb.addItem("parser >", theme.dialogItem.toolbarItemColors, parser());
      tbb.addItem("open ...", theme.dialogItem.toolbarItemColors, this::showOpenFilePicker);
      return tbb.supplier();
    }

    void showOpenFilePicker() {
      popupMenu.hide();
      window().showOpenFilePicker(editor::openFile);
    }

    private void gotoItems(V2i eventPosition, ToolbarItemBuilder tbb) {
      Model model = editor.model();
      String language = model.language();
      String scheme = model.uriScheme();
      EditorRegistrations reg = editor.registrations;

      var declarationProvider = reg.findDeclarationProvider(language, scheme);

      if (declarationProvider != null) {
        tbb.addItem(
            "Go to Declaration",
            theme.dialogItem.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DECL));
      }

      var definitionProvider = reg.findDefinitionProvider(language, scheme);

      if (definitionProvider != null) {
        tbb.addItem(
            "Go to Definition",
            theme.dialogItem.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DEF));
      }

      var refProvider = reg.findReferenceProvider(language, scheme);

      if (refProvider != null) {
        tbb.addItem(
            "Go to References",
            theme.dialogItem.toolbarItemColors,
            () -> findUsages(eventPosition));
      }

      tbb.addItem(
          "Go to (local)",
          theme.dialogItem.toolbarItemColors,
          () -> findUsagesDefDecl(eventPosition, null));
    }

    private Supplier<ToolbarItem[]> parser() {
      return ArrayOp.supplier(
          ti("Int", theme.dialogItem.toolbarItemColors, editor::debugPrintDocumentIntervals),
          ti("Iter", theme.dialogItem.toolbarItemColors, editor::iterativeParsing),
          ti("VP", theme.dialogItem.toolbarItemColors, editor::parseViewport),
          ti("Rep", theme.dialogItem.toolbarItemColors, editor::parseFullFile));
    }

    private Supplier<ToolbarItem[]> oldDev() {
      return ArrayOp.supplier(
          ti("↓ move", theme.dialogItem.toolbarItemColors, editor::moveDown),
          ti("■ stop", theme.dialogItem.toolbarItemColors, editor::stopMove),
          ti("↑ move", theme.dialogItem.toolbarItemColors, editor::moveUp),
          ti("toggleContrast", theme.dialogItem.toolbarItemColors, editor::toggleContrast),
          ti("toggleXOffset", theme.dialogItem.toolbarItemColors, editor::toggleXOffset),
          ti("toggleTails", theme.dialogItem.toolbarItemColors, editor::toggleTails));
    }

    private Supplier<ToolbarItem[]> themes() {
      return ArrayOp.supplier(
          ti("Darcula", theme.dialogItem.toolbarItemColors, themeApi::toggleDarcula),
          ti("Dark", theme.dialogItem.toolbarItemColors, themeApi::toggleDark),
          ti("Light", theme.dialogItem.toolbarItemColors, themeApi::toggleLight)
      );
    }

    private Supplier<ToolbarItem[]> fontSize() {
      return ArrayOp.supplier(
          ti("↑ increase", theme.dialogItem.toolbarItemColors, editor::increaseFont),
          ti("↓ decrease", theme.dialogItem.toolbarItemColors, editor::decreaseFont));
    }

    private Supplier<ToolbarItem[]> fontSelect() {
      return () -> {
        String[] fonts = this.fonts.get();
        ToolbarItem[] items = new ToolbarItem[fonts.length];
        for (int i = 0; i < items.length; i++) {
          var font = fonts[i];
          Runnable runnable = () -> editor.changeFont(font);
          items[i] = new ToolbarItem(runnable, font, theme.dialogItem.toolbarItemColors);
        }
        return items;
      };
    }

    private void findUsages(V2i eventPosition) {
      String language = editor.model.language();
      String scheme = editor.model.uriScheme();
      ReferenceProvider.Provider provider = editor.registrations.findReferenceProvider(language, scheme);
      popupMenu.hide();
      editor.findUsages(eventPosition, provider);
    }

    private void findUsagesDefDecl(V2i eventPosition, DefDeclProvider.Type type) {
      popupMenu.hide();
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
