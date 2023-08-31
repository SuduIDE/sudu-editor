package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.demo.ui.window.ScrollView;
import org.sudu.experiments.demo.ui.window.Window;
import org.sudu.experiments.demo.ui.window.WindowManager;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.demo.ui.ToolbarItemBuilder.ti;

class EditorUi implements MouseListener, InputListeners.ScrollHandler {
  final UiContext uiContext;
  UiFont titleFont = new UiFont(Fonts.SegoeUI, 16);
  WindowManager windowManager;
  Window usagesMenu;
  PopupMenu popupMenu;
  EditorColorScheme colors;

  EditorUi(UiContext context) {
    uiContext = context;
    windowManager = new WindowManager();


    popupMenu = new PopupMenu(uiContext);
    popupMenu.setFont(new UiFont(
        EditorConst.POPUP_MENU_FONT_NAME,
        EditorConst.POPUP_MENU_FONT_SIZE));
  }

  void setTheme(EditorColorScheme theme) {
    colors = theme;
    if (usagesMenu != null) usagesMenu.setTheme(theme.dialogItem);
    popupMenu.setTheme(theme.dialogItem);
  }

  void dispose() {
    popupMenu.dispose();
    if (usagesMenu != null) {
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
  public boolean onMouseDown(MouseEvent event, int button) {
    return windowManager.onMouseDown(event, button)
        || popupMenu.onMouseDown(event, button);
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
    if (usagesMenu != null) disposeUsagesWindow();
    FindUsagesView usagesView = new FindUsagesView(uiContext, () -> {
      uiContext.setFocus(editor);
      disposeUsagesWindow();
    });

    usagesView.setFont(new UiFont(
        EditorConst.FIND_USAGES_FONT_NAME,
        EditorConst.FIND_USAGES_FONT_SIZE
    ));
    usagesView.setItems(actions);
    usagesView.setTheme(colors.dialogItem);

    usagesMenu = new Window(uiContext);
    ScrollView scrollView = new ScrollView(usagesView, uiContext);
    scrollView.setScrollColor(colors.editor.scrollBarLine, colors.editor.scrollBarBg);
    usagesMenu.setContent(scrollView);
    usagesMenu.setTitle("Usages of " + elementName, titleFont, 4);
    usagesMenu.setTheme(colors.dialogItem);
    windowManager.addWindow(usagesMenu);
    int minY = usagesMenu.titleHeight() + uiContext.toPx(2);
    V2i limitedPosition = usagesView.setLimitedPosition(position, minY);
    V2i size = usagesView.calculateSize(limitedPosition);
    usagesMenu.setPosition(limitedPosition, size);

    uiContext.setFocus(usagesView);
  }

  private void disposeUsagesWindow() {
    windowManager.removeWindow(usagesMenu);
    usagesMenu.dispose();
    usagesMenu = null;
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
            colors.dialogItem.findUsagesColorsError)
    );
  }

  public void showContextMenu(MouseEvent event, EditorComponent editor, DemoEdit0 demoEdit0) {
    if (!popupMenu.isVisible()) {
      popupMenu.display(event.position,
          new PopupMenuBuilder(editor, demoEdit0).build(event.position),
          setEditFocus(editor));
    }
  }

  @Override
  public boolean onScroll(MouseEvent event, float dX, float dY) {
    return windowManager.onScroll(event, dX, dY);
  }

  class PopupMenuBuilder {

    final EditorComponent editor;
    final DemoEdit0 demoEdit0;

    PopupMenuBuilder(EditorComponent editor, DemoEdit0 demoEdit0) {
      this.editor = editor;
      this.demoEdit0 = demoEdit0;
    }

    Supplier<ToolbarItem[]> build(V2i eventPosition) {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();

      gotoItems(eventPosition, tbb);
      cutCopyPaste(tbb);
      if (1 < 0) tbb.addItem("old >", colors.dialogItem.toolbarItemColors, oldDev());
      tbb.addItem("Settings >", colors.dialogItem.toolbarItemColors, settingsItems());
      tbb.addItem("Development >", colors.dialogItem.toolbarItemColors, devItems());
      return tbb.supplier();
    }

    private void cutCopyPaste(ToolbarItemBuilder tbb) {
      if (!editor.readonly) {
        tbb.addItem("Cut", colors.dialogItem.toolbarItemColors, this::cutAction);
      }
      tbb.addItem("Copy", colors.dialogItem.toolbarItemColors, this::copyAction);

      if (!editor.readonly && window().isReadClipboardTextSupported()) {
        tbb.addItem("Paste", colors.dialogItem.toolbarItemColors, this::pasteAction);
      }
    }

    org.sudu.experiments.Window window() { return demoEdit0.uiContext.window; }

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
      tbb.addItem("Theme >", colors.dialogItem.toolbarItemColors, themes());
      tbb.addItem("Font size >", colors.dialogItem.toolbarItemColors, fontSize());
      tbb.addItem("Fonts >", colors.dialogItem.toolbarItemColors, fontSelect());
      return tbb.supplier();
    }

    private Supplier<ToolbarItem[]> devItems() {
      ToolbarItemBuilder tbb = new ToolbarItemBuilder();
      tbb.addItem("parser >", colors.dialogItem.toolbarItemColors, parser());
      tbb.addItem("open ...", colors.dialogItem.toolbarItemColors, this::showOpenFilePicker);
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
            colors.dialogItem.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DECL));
      }

      var definitionProvider = reg.findDefinitionProvider(language, scheme);

      if (definitionProvider != null) {
        tbb.addItem(
            "Go to Definition",
            colors.dialogItem.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DEF));
      }

      var refProvider = reg.findReferenceProvider(language, scheme);

      if (refProvider != null) {
        tbb.addItem(
            "Go to References",
            colors.dialogItem.toolbarItemColors,
            () -> findUsages(eventPosition));
      }

      tbb.addItem(
          "Go to (local)",
          colors.dialogItem.toolbarItemColors,
          () -> findUsagesDefDecl(eventPosition, null));
    }

    private Supplier<ToolbarItem[]> parser() {
      return ArrayOp.supplier(
          ti("Int", colors.dialogItem.toolbarItemColors, editor::debugPrintDocumentIntervals),
          ti("Iter", colors.dialogItem.toolbarItemColors, editor::iterativeParsing),
          ti("VP", colors.dialogItem.toolbarItemColors, editor::parseViewport),
          ti("Rep", colors.dialogItem.toolbarItemColors, editor::parseFullFile));
    }

    private Supplier<ToolbarItem[]> oldDev() {
      return ArrayOp.supplier(
          ti("↓ move", colors.dialogItem.toolbarItemColors, editor::moveDown),
          ti("■ stop", colors.dialogItem.toolbarItemColors, editor::stopMove),
          ti("↑ move", colors.dialogItem.toolbarItemColors, editor::moveUp),
          ti("toggleContrast", colors.dialogItem.toolbarItemColors, editor::toggleContrast),
          ti("toggleXOffset", colors.dialogItem.toolbarItemColors, editor::toggleXOffset),
          ti("toggleTails", colors.dialogItem.toolbarItemColors, editor::toggleTails));
    }

    private Supplier<ToolbarItem[]> themes() {
      return ArrayOp.supplier(
          ti("Dark", colors.dialogItem.toolbarItemColors, demoEdit0::toggleDarcula),
          ti("Light", colors.dialogItem.toolbarItemColors, demoEdit0::toggleLight)
      );
    }

    private Supplier<ToolbarItem[]> fontSize() {
      return ArrayOp.supplier(
          ti("↑ increase", colors.dialogItem.toolbarItemColors, editor::increaseFont),
          ti("↓ decrease", colors.dialogItem.toolbarItemColors, editor::decreaseFont));
    }

    private Supplier<ToolbarItem[]> fontSelect() {
      return () -> {
        String[] fonts = demoEdit0.menuFonts();
        ToolbarItem[] items = new ToolbarItem[fonts.length];
        for (int i = 0; i < items.length; i++) {
          var font = fonts[i];
          Runnable runnable = () -> editor.changeFont(font);
          items[i] = new ToolbarItem(runnable, font, colors.dialogItem.toolbarItemColors);
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
