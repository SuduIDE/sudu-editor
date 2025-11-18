package org.sudu.experiments;

import org.sudu.experiments.angle.AngleGL;
import org.sudu.experiments.angle.AngleOffscreen;
import org.sudu.experiments.angle.NoWindow;
import org.sudu.experiments.editor.Editor1;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.nativelib.AngleDll;
import org.sudu.experiments.nativelib.SuduDll;
import org.sudu.experiments.win32.Win32Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SwingDemo extends JFrame {

  AngleOffscreen angleOffscreen;
  Win32Graphics glGraphics;
  NoWindow noWindow;
  InputListeners input;
  V2i size;
  Scene scene;
  int frameNo;

  BufferedImage image;
  Graphics2D graphics;

  public static void main(String[] args) {
    AngleDll.require();
    SuduDll.require();
    new SwingDemo();
  }

  Graphics2D graphics() {
    if (graphics == null)
      graphics = image.createGraphics();
    return graphics;
  }

  public SwingDemo() throws HeadlessException {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    size = new V2i(1024, 800);
    if (initAngle(size)) return;

    if (!angleOffscreen.makeCurrentContext()) {
      System.err.println("angleOffscreen.makeCurrentContext() returned false");
      return;
    }

    glGraphics = angleOffscreen.getGraphics();
    noWindow = new NoWindow();
    input = new InputListeners(noWindow::repaint);
    var api = new SceneApi(glGraphics, input, noWindow);

    scene = new Editor1(api);
    image = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
    renderToImage(image);

    JComponent customComponent = new JComponent() {
      @Override
      public Dimension getMinimumSize() {
        return super.getPreferredSize();
      }

      @Override
      public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
      }

      @Override
      protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
      }
    };
    getContentPane().add(customComponent);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    MouseAdapter mouseAdapter = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        handleMouseEvent(e);
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        handleMouseEvent(e);
      }

      private void handleMouseEvent(MouseEvent e) {
        System.err.println(e);
        Color clickColor = Color.RED;
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) clickColor = Color.RED;
        if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) != 0) clickColor = Color.GREEN;
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) clickColor = Color.BLUE;
        if ((e.getModifiersEx() & MouseEvent.ALT_DOWN_MASK) != 0) clickColor = Color.CYAN;
        if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0) clickColor = Color.MAGENTA;
        if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0) clickColor = Color.WHITE;
        graphics().setPaint(new RadialGradientPaint(e.getX(), e.getY(), 40, new float[]{0, 1}, new Color[]{clickColor, new Color(0, 0, 0, 0)}));
        graphics().fillRect(0, 0, image.getWidth(), image.getHeight());
        customComponent.repaint();
      }
    };
    customComponent.addMouseListener(mouseAdapter);
    customComponent.addMouseMotionListener(mouseAdapter);
    customComponent.setFocusable(true);
    customComponent.setOpaque(true);
    customComponent.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        System.err.println(e);
      }
    });
    setVisible(true);
  }

  @Override
  public void dispose() {
    //do custom stuff
    if (angleOffscreen != null) {
      angleOffscreen.dispose();
      angleOffscreen = null;
    }
    if (graphics != null) {
      graphics.dispose();
      graphics = null;
    }
    super.dispose();
  }

  void renderToImage(BufferedImage image) {
    renderByAngle(image);
    // renderByGraphics(image);
  }

  void renderByAngle(BufferedImage image) {
    if (!angleOffscreen.makeCurrent()) {
      System.err.println(AngleOffscreen.e("make current error: "));
      return;
    }
    glGraphics.setViewPortAndClientRect(size.x, size.y);
    scene.onResize(size, 1.f);
    scene.paint();
    frameNo++;

    if (false) {
      GL.ImageData glImage = new GL.ImageData(size.x, size.y);
      AngleGL.readPixels(0, 0, size.x, size.y,
          GLApi.Context.RGBA, GLApi.Context.UNSIGNED_BYTE, glImage.data);
      saveByteImage(glImage);
    }

    var data = image.getRaster().getDataBuffer();
    if (data instanceof DataBufferInt dataBufferInt) {
      int[] bits = dataBufferInt.getData();
      if (bits.length == size.x * size.y) {
        AngleGL.readPixels(0, 0, size.x, size.y,
            GLApi.Context.RGBA, GLApi.Context.UNSIGNED_BYTE, bits);
        BufferedImageCvt.glPixelsToBufferedImage(bits, size.x, size.y);
        saveIntImage(bits);

      } else {
        System.err.println("dataBufferInt.getData().length != size.x * size.y");
      }
    }
  }

  private void saveIntImage(int[] bits) {
    try {
      Path path = Path.of("renderToIntImage" + frameNo + ".bmp");
      Files.write(path,
          BmpWriter.toBmp(size.x, size.y, bits, false, false));
      System.out.println("saved to " + path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private void saveByteImage(GL.ImageData glImage) {
    try {
      Path path = Path.of("renderToByteImage" + frameNo + ".bmp");
      Files.write(path, BmpWriter.toBmp(glImage, true));
      System.out.println("saved to " + path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private boolean initAngle(V2i size) {
    if (angleOffscreen == null) {
      angleOffscreen = new AngleOffscreen(true);
      var error = angleOffscreen.initialize();
      if (error != null) {
        System.err.println(error);
        return true;
      }
      error = angleOffscreen.createSurface(size.x, size.y);
      if (error != null) {
        System.err.println(error);
        return true;
      }
    }
    return false;
  }
}
