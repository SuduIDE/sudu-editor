// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.PopupMenu;
import org.sudu.experiments.demo.ui.ToolbarItem;
import org.sudu.experiments.demo.ui.ToolbarItemBuilder;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.demo.ui.ToolbarItemBuilder.ti;

public class DemoEdit0 extends Scene0 {

  WglGraphics g;
  final SetCursor setCursor;

  final PopupMenu popupMenu;
  FontDesk toolBarFont;

  EditorComponent editor;
  V2i editorPos = new V2i();

  public DemoEdit0(SceneApi api) {
    super(api, false);
    this.g = api.graphics;
//    clearColor.set(Color.Cvt.gray(0));
    this.setCursor = SetCursor.wrap(api.window);
    popupMenu = new PopupMenu(g);

    editor = new EditorComponent(api);
    api.input.addListener(new EditInput());
  }

  public Document document() {
    return editor.model.document;
  }

  public EditorComponent editor() {
    return editor;
  }

  @Override
  public void dispose() {
    popupMenu.dispose();
    editor.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    return editor.update(timestamp);
  }

  @Override
  public void paint() {
    super.paint();
    editor.paint();
    popupMenu.paint();
  }

  protected Supplier<ToolbarItem[]> popupMenuContent(V2i eventPosition) {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();

    gotoItems(eventPosition, tbb);
    cutCopyPaste(tbb);
    if (1 < 0) tbb.addItem("old >", editor.colors.dialogItemColor.toolbarItemColors, oldDev());
    tbb.addItem("Settings >", editor.colors.dialogItemColor.toolbarItemColors, settingsItems());
    tbb.addItem("Development >", editor.colors.dialogItemColor.toolbarItemColors, devItems());
    return tbb.supplier();
  }

  private void cutCopyPaste(ToolbarItemBuilder tbb) {
    if (!editor().readonly) {
      tbb.addItem("Cut", editor.colors.dialogItemColor.toolbarItemColors, this::cutAction);
    }
    tbb.addItem("Copy", editor.colors.dialogItemColor.toolbarItemColors, this::copyAction);

    if (!editor().readonly && api.window.isReadClipboardTextSupported()) {
      tbb.addItem("Paste", editor.colors.dialogItemColor.toolbarItemColors, this::pasteAction);
    }
  }

  private Supplier<ToolbarItem[]> settingsItems() {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();
    tbb.addItem("Theme >", editor.colors.dialogItemColor.toolbarItemColors, themes());
    tbb.addItem("Font size >", editor.colors.dialogItemColor.toolbarItemColors, fontSize());
    tbb.addItem("Fonts >", editor.colors.dialogItemColor.toolbarItemColors, fontSelect());
    return tbb.supplier();
  }

  private Supplier<ToolbarItem[]> devItems() {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();
    tbb.addItem("parser >", editor.colors.dialogItemColor.toolbarItemColors, parser());
    tbb.addItem("open ...", editor.colors.dialogItemColor.toolbarItemColors, this::showOpenFilePicker);
    return tbb.supplier();
  }

  private void gotoItems(V2i eventPosition, ToolbarItemBuilder tbb) {
      Model model = editor().model();
      String language = model.language();
      String scheme = model.uriScheme();
      EditorRegistrations reg = editor().registrations;

      var declarationProvider = reg.findDeclarationProvider(language, scheme);

      if (declarationProvider != null) {
        tbb.addItem(
            "Go to Declaration",
            editor.colors.dialogItemColor.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DECL));
      }

      var definitionProvider = reg.findDefinitionProvider(language, scheme);

      if (definitionProvider != null) {
        tbb.addItem(
            "Go to Definition",
            editor.colors.dialogItemColor.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DEF));
      }

      var refProvider = reg.findReferenceProvider(language, scheme);

      if (refProvider != null) {
        tbb.addItem(
            "Go to References",
            editor.colors.dialogItemColor.toolbarItemColors,
            () -> findUsages(eventPosition));
      }

      tbb.addItem(
          "Go to (local)",
          editor.colors.dialogItemColor.toolbarItemColors,
          () -> findUsagesDefDecl(eventPosition, null));
  }

  private Supplier<ToolbarItem[]> parser() {
    return ArrayOp.supplier(
        ti("Int", editor.colors.dialogItemColor.toolbarItemColors, editor::debugPrintDocumentIntervals),
        ti("Iter", editor.colors.dialogItemColor.toolbarItemColors, editor::iterativeParsing),
        ti("VP", editor.colors.dialogItemColor.toolbarItemColors, editor::parseViewport),
        ti("Rep", editor.colors.dialogItemColor.toolbarItemColors, editor::parseFullFile));
  }

  private Supplier<ToolbarItem[]> oldDev() {
    return ArrayOp.supplier(
        ti("↓ move", editor.colors.dialogItemColor.toolbarItemColors, editor::moveDown),
        ti("■ stop", editor.colors.dialogItemColor.toolbarItemColors, editor::stopMove),
        ti("↑ move", editor.colors.dialogItemColor.toolbarItemColors, editor::moveUp),
        ti("toggleContrast", editor.colors.dialogItemColor.toolbarItemColors, editor::toggleContrast),
        ti("toggleXOffset", editor.colors.dialogItemColor.toolbarItemColors, editor::toggleXOffset),
        ti("toggleTails", editor.colors.dialogItemColor.toolbarItemColors, editor::toggleTails));
  }

  private Supplier<ToolbarItem[]> themes() {
    popupMenu.hide();
    return ArrayOp.supplier(
        ti("Dark", editor.colors.dialogItemColor.toolbarItemColors, editor::toggleDark),
        ti("Light", editor.colors.dialogItemColor.toolbarItemColors, editor::toggleLight)
    );
  }

  private Supplier<ToolbarItem[]> fontSize() {
    return ArrayOp.supplier(
        ti("↑ increase", editor.colors.dialogItemColor.toolbarItemColors, editor::increaseFont),
        ti("↓ decrease", editor.colors.dialogItemColor.toolbarItemColors, editor::decreaseFont));
  }

  private Supplier<ToolbarItem[]> fontSelect() {
    return () -> {
      String[] fonts = menuFonts();
      ToolbarItem[] items = new ToolbarItem[fonts.length];
      for (int i = 0; i < items.length; i++) {
        var font = fonts[i];
        items[i] = new ToolbarItem(() -> setFont(font), font, Colors.rngToolButton());
      }
      return items;
    };
  }

  protected String[] menuFonts() { return Fonts.editorFonts(false); }

  private void setFont(String font) {
    editor.changeFont(font, editor.getFontVirtualSize());
  }

  private void pasteAction() {
    popupMenu.hide();
    api.window.readClipboardText(
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
    return text -> api.window.writeClipboardText(text,
        org.sudu.experiments.Const.emptyRunnable,
        onError("writeClipboardText error: "));
  }

  private void findUsages(V2i eventPosition) {
    String language = editor().model.language();
    String scheme = editor().model.uriScheme();
    ReferenceProvider.Provider provider =  editor().registrations.findReferenceProvider(language, scheme);
    popupMenu.hide();
    editor.findUsages(eventPosition, provider);
  }

  private void findUsagesDefDecl(V2i eventPosition, DefDeclProvider.Type type) {
    popupMenu.hide();
    Model model = editor().model();
    String language = model.language();
    String scheme = model.uriScheme();
    EditorRegistrations reg = editor().registrations;
    DefDeclProvider.Provider provider = type != null ? switch (type) {
      case DEF -> reg.findDefinitionProvider(language, scheme);
      case DECL -> reg.findDeclarationProvider(language, scheme);
    } : null;
    editor.findUsages(eventPosition, provider);
  }

  static Consumer<Throwable> onError(String s) {
    return throwable -> Debug.consoleInfo(s + throwable.getMessage());
  }

  void showOpenFilePicker() {
    api.window.showOpenFilePicker(editor::openFile);
  }

  @Override
  public void onResize(V2i newSize, double newDpr) {
    size.set(newSize);
    editor.setPos(editorPos, size, newDpr);

    if (dpr != newDpr) {
      dpr = newDpr;
      int toolbarFontSize = Numbers.iRnd(EditorConst.POPUP_MENU_FONT_SIZE * newDpr);
      toolBarFont = g.fontDesk(EditorConst.POPUP_MENU_FONT_NAME, toolbarFontSize);
      popupMenu.setTheme(toolBarFont, Colors.toolbarBg);
    }
    popupMenu.onResize(newSize, newDpr);
  }

  class EditInput implements InputListener {

    @Override
    public void onFocus() {
      editor.onFocusGain();
    }

    @Override
    public void onBlur() {
      popupMenu.hide();
      editor.onFocusLost();
    }

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      return editor.onMouseWheel(event, dX, dY);
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      return popupMenu.onMousePress(event.position, button, press, clickCount)
          || editor.onMousePress(event, button, press, clickCount);
    }

    public boolean onContextMenu(MouseEvent event) {
      if (!popupMenu.isVisible()) {
        popupMenu.display(event.position, popupMenuContent(event.position), editor::onFocusGain, editor.colors);
        editor.onFocusLost();
      }
      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return popupMenu.onMouseMove(event.position, setCursor)
          || editor.onMouseMove(event, setCursor);
    }

    @Override
    public boolean onKey(KeyEvent event) {
      return handleKey(event) || editor.onKey(event);
    }

    private boolean handleKey(KeyEvent event) {
      if (!event.isPressed) return false;

      if (event.keyCode == KeyCode.ESC) {
        if (popupMenu.isVisible()) {
          popupMenu.hide();
          return true;
        }
      }

      if (event.ctrl && event.keyCode == KeyCode.O) {
        if (event.shift) {
          api.window.showDirectoryPicker(
              s -> Debug.consoleInfo("showDirectoryPicker -> " + s));
        } else {
          showOpenFilePicker();
        }
        return true;
      }
      return false;
    }

    @Override
    public boolean onCopy(Consumer<String> setText, boolean isCut) {
      return editor.onCopy(setText, isCut);
    }

    @Override
    public Consumer<String> onPastePlainText() {
      return editor::handleInsert;
    }
  }
}
