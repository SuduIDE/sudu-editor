// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
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

  FontDesk toolBarFont;

  EditorComponent editor;
  V2i editorPos = new V2i();

  public DemoEdit0(SceneApi api) {
    super(api, false);
    this.g = api.graphics;
//    clearColor.set(Color.Cvt.gray(0));
    this.setCursor = SetCursor.wrap(api.window);

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
  }

  protected Supplier<ToolbarItem[]> popupMenuContent(V2i eventPosition) {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();

    gotoItems(eventPosition, tbb);
    cutCopyPaste(tbb);
    if (1 < 0) tbb.addItem("old >", editor.colors.dialogItemColors.toolbarItemColors, oldDev());
    tbb.addItem("Settings >", editor.colors.dialogItemColors.toolbarItemColors, settingsItems());
    tbb.addItem("Development >", editor.colors.dialogItemColors.toolbarItemColors, devItems());
    return tbb.supplier();
  }

  private void cutCopyPaste(ToolbarItemBuilder tbb) {
    if (!editor().readonly) {
      tbb.addItem("Cut", editor.colors.dialogItemColors.toolbarItemColors, this::cutAction);
    }
    tbb.addItem("Copy", editor.colors.dialogItemColors.toolbarItemColors, this::copyAction);

    if (!editor().readonly && api.window.isReadClipboardTextSupported()) {
      tbb.addItem("Paste", editor.colors.dialogItemColors.toolbarItemColors, this::pasteAction);
    }
  }

  private Supplier<ToolbarItem[]> settingsItems() {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();
    tbb.addItem("Theme >", editor.colors.dialogItemColors.toolbarItemColors, themes());
    tbb.addItem("Font size >", editor.colors.dialogItemColors.toolbarItemColors, fontSize());
    tbb.addItem("Fonts >", editor.colors.dialogItemColors.toolbarItemColors, fontSelect());
    return tbb.supplier();
  }

  private Supplier<ToolbarItem[]> devItems() {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();
    tbb.addItem("parser >", editor.colors.dialogItemColors.toolbarItemColors, parser());
    tbb.addItem("open ...", editor.colors.dialogItemColors.toolbarItemColors, this::showOpenFilePicker);
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
            editor.colors.dialogItemColors.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DECL));
      }

      var definitionProvider = reg.findDefinitionProvider(language, scheme);

      if (definitionProvider != null) {
        tbb.addItem(
            "Go to Definition",
            editor.colors.dialogItemColors.toolbarItemColors,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DEF));
      }

      var refProvider = reg.findReferenceProvider(language, scheme);

      if (refProvider != null) {
        tbb.addItem(
            "Go to References",
            editor.colors.dialogItemColors.toolbarItemColors,
            () -> findUsages(eventPosition));
      }

      tbb.addItem(
          "Go to (local)",
          editor.colors.dialogItemColors.toolbarItemColors,
          () -> findUsagesDefDecl(eventPosition, null));
  }

  private Supplier<ToolbarItem[]> parser() {
    return ArrayOp.supplier(
        ti("Int", editor.colors.dialogItemColors.toolbarItemColors, editor::debugPrintDocumentIntervals),
        ti("Iter", editor.colors.dialogItemColors.toolbarItemColors, editor::iterativeParsing),
        ti("VP", editor.colors.dialogItemColors.toolbarItemColors, editor::parseViewport),
        ti("Rep", editor.colors.dialogItemColors.toolbarItemColors, editor::parseFullFile));
  }

  private Supplier<ToolbarItem[]> oldDev() {
    return ArrayOp.supplier(
        ti("↓ move", editor.colors.dialogItemColors.toolbarItemColors, editor::moveDown),
        ti("■ stop", editor.colors.dialogItemColors.toolbarItemColors, editor::stopMove),
        ti("↑ move", editor.colors.dialogItemColors.toolbarItemColors, editor::moveUp),
        ti("toggleContrast", editor.colors.dialogItemColors.toolbarItemColors, editor::toggleContrast),
        ti("toggleXOffset", editor.colors.dialogItemColors.toolbarItemColors, editor::toggleXOffset),
        ti("toggleTails", editor.colors.dialogItemColors.toolbarItemColors, editor::toggleTails));
  }

  private Supplier<ToolbarItem[]> themes() {
    return ArrayOp.supplier(
        ti("Dark", editor.colors.dialogItemColors.toolbarItemColors, editor::toggleDark),
        ti("Light", editor.colors.dialogItemColors.toolbarItemColors, editor::toggleLight)
    );
  }

  private Supplier<ToolbarItem[]> fontSize() {
    return ArrayOp.supplier(
        ti("↑ increase", editor.colors.dialogItemColors.toolbarItemColors, editor::increaseFont),
        ti("↓ decrease", editor.colors.dialogItemColors.toolbarItemColors, editor::decreaseFont));
  }

  private Supplier<ToolbarItem[]> fontSelect() {
    return () -> {
      String[] fonts = menuFonts();
      ToolbarItem[] items = new ToolbarItem[fonts.length];
      for (int i = 0; i < items.length; i++) {
        var font = fonts[i];
        items[i] = new ToolbarItem(() -> setFont(font), font, editor.colors.dialogItemColors.toolbarItemColors);
      }
      return items;
    };
  }

  protected String[] menuFonts() { return Fonts.editorFonts(false); }

  private void setFont(String font) {
    editor.changeFont(font, editor.getFontVirtualSize());
  }

  private void pasteAction() {
    editor.popupMenu.hide();
    api.window.readClipboardText(
        editor::handleInsert,
        onError("readClipboardText error: "));
  }

  private void cutAction() {
    editor.popupMenu.hide();
    editor.onCopy(copyHandler(), true);
  }

  private void copyAction() {
    editor.popupMenu.hide();
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
    editor.popupMenu.hide();
    editor.findUsages(eventPosition, provider);
  }

  private void findUsagesDefDecl(V2i eventPosition, DefDeclProvider.Type type) {
    editor.popupMenu.hide();
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
      editor.popupMenu.setFont(toolBarFont);
    }
  }

  class EditInput implements InputListener {

    @Override
    public void onFocus() {
      editor.onFocusGain();
    }

    @Override
    public void onBlur() {
      editor.popupMenu.hide();
      editor.onFocusLost();
    }

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      return editor.onMouseWheel(event, dX, dY);
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      return editor.onMousePress(event, button, press, clickCount);
    }

    public boolean onContextMenu(MouseEvent event) {
      if (!editor.popupMenu.isVisible()) {
        editor.popupMenu.display(event.position, popupMenuContent(event.position), editor::onFocusGain);
        editor.onFocusLost();
      }
      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return editor.onMouseMove(event, setCursor);
    }

    @Override
    public boolean onKey(KeyEvent event) {
      return handleKey(event) || editor.onKey(event);
    }

    private boolean handleKey(KeyEvent event) {
      if (!event.isPressed) return false;

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
