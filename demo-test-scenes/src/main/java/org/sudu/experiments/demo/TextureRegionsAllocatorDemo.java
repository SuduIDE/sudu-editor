package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.ui.RegionTexture;
import org.sudu.experiments.demo.ui.RegionTextureAllocator;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.RngHelper;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.ArrayList;

public class TextureRegionsAllocatorDemo extends Scene0 implements MouseListener {

  private final UiContext uiContext;
  private final RegionTexture regionTexture;
  private final ArrayList<TextRect> tItemsList = new ArrayList<>();
  private final ArrayList<String> itemsName = new ArrayList<>();
  private final FontDesk font;
  private final int ELEMENTS_COUNT = 100;
  private final int MIN_TEXT_LENGTH = 1;
  private final int MAX_TEXT_LENGTH = 20;
  private final double COVERAGE_PERCENT = 0.7;
  private final Color textureBgColor = new Color("#e3c8ab");
  private GL.Texture texture;
  private V2i textureSize;
  private boolean isTesting;
  XorShiftRandom r = new XorShiftRandom();
  private int usedCoverage = 0;

  public TextureRegionsAllocatorDemo(SceneApi api) {
    super(api);

    uiContext = new UiContext(api);
    uiContext.dprListeners.add(this::open);
    regionTexture = new RegionTexture();

    api.input.onMouse.add(this);
    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onKeyPress.add(uiContext::onKeyPress);

    font = api.graphics.fontDesk("Consolas", 35);
    clearColor.set(new Color("#39322b"));

  }

  private boolean onKeyPress(KeyEvent keyEvent) {
    if (keyEvent.keyCode == KeyCode.SPACE) {
      isTesting = !isTesting;
      return true;
    }
    if (keyEvent.keyCode == KeyCode.ENTER) {
      createItems(1);
      return true;
    }
    if (keyEvent.keyCode == KeyCode.BACKSPACE) {
      removeItems(1);
      return true;
    }
    return false;
  }

  private void removeItems(int i) {
    for (int j = 0; j < i; j++) {
      if (tItemsList.size() > 0) {
        int indexToRemove = (int) (Math.random() * (tItemsList.size() - 1));
        TextRect textRect = tItemsList.remove(indexToRemove);
        itemsName.remove(indexToRemove);
        usedCoverage -= textRect.size.x * textRect.size.y;
        regionTexture.free(textRect.textureRegion);
      }
    }
  }

  private String getRandomText(int minLength, int maxLength) {
    return RngHelper.rngString(r, minLength + (int) (Math.random() * (maxLength - minLength)));
  }

  private void addItem(Canvas mCanvas) {
    String text = getRandomText(MIN_TEXT_LENGTH, MAX_TEXT_LENGTH);
    TextRect textRect = new TextRect();
    regionTexture.setContext(mCanvas, font, font.lineHeight());
    textRect.textureRegion.set(regionTexture.alloc(text, RegionTextureAllocator.measuringF(mCanvas)));
    Color.Cvt.fromHSV(Math.random(), 1, 1, textRect.bgColor).setW(0.5f);
    textRect.pos.set((int) textRect.textureRegion.x, (int) textRect.textureRegion.y);
    textRect.size.set((int) textRect.textureRegion.z, (int) textRect.textureRegion.w);
    itemsName.add(text);
    tItemsList.add(textRect);
    usedCoverage += textRect.size.x * textRect.size.y;
  }

  private void createItems(int n) {
    Canvas mCanvas = uiContext.mCanvas();
    mCanvas.setFont(font);
    for (int i = 0; i < n; i++) {
      addItem(mCanvas);
    }
    textureSize = regionTexture.getTextureSize();
  }

  private void open(float oldDpr, float newDpr) {
    if (oldDpr == 0) {
      openWindow(new V2i());
    }
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    super.onResize(newSize, dpr);
    uiContext.onResize(newSize, dpr);
  }

  @Override
  public void paint() {
    super.paint();
    WglGraphics graphics = api.graphics;
    graphics.enableBlend(true);
    render();
    graphics.enableBlend(false);
  }

  private void render() {
    WglGraphics g = uiContext.graphics;
    if (itemsName.size() == 0) return;
    renderTexture(g);
    for (int i = 0; i < itemsName.size(); i++) {
      TextRect textRect = tItemsList.get(i);
      textRect.setColor(new Color(0));
      textRect.drawText(g, texture, 0, 0, 0);
    }
  }

  private void renderTexture(WglGraphics g) {
    Canvas canvas = g.createCanvas(textureSize.x, textureSize.y);
    canvas.setFont(font);
    float baseline = font.fAscent;

    g.drawRect(0, 0, textureSize, textureBgColor);

    for (int i = 0; i < itemsName.size(); i++) {
      TextRect textRect = tItemsList.get(i);
      canvas.drawText(itemsName.get(i),
          textRect.textureRegion.x,
          textRect.textureRegion.y + baseline);
    }

    if (texture == null || texture.width() != textureSize.x || texture.height() != textureSize.y) {
      texture = Disposable.assign(texture, g.createTexture());
    }
    texture.setContent(canvas);
    canvas.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    if (isTesting) {
      balanceItems();
    }
    return isTesting;
  }

  private void balanceItems() {
    int textureArea = Math.min(textureSize.x * textureSize.y, RegionTextureAllocator.MAX_TEXTURE_SIZE * 1080);
    double coveragePercent = (double) usedCoverage / textureArea;
    if (coveragePercent > COVERAGE_PERCENT) {
      removeItems(r.nextInt(5));
    } else {
      createItems(r.nextInt(5));
    }
  }

  private void openWindow(V2i position) {
    createItems(ELEMENTS_COUNT);
  }

}
