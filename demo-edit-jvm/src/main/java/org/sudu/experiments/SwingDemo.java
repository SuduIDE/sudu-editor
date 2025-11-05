package org.sudu.experiments;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SwingDemo extends JFrame {
  public static void main(String[] args) {
    new SwingDemo();
  }

  public SwingDemo() throws HeadlessException {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D graphics = image.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect(0, 0, 301, 301);
    graphics.setColor(Color.GREEN);
    graphics.drawLine(0, 0, 300, 300);
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
    customComponent.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.err.println(e);
        Color clickColor = Color.RED;
        if (e.getButton() == MouseEvent.BUTTON2) clickColor = Color.ORANGE;
        if (e.getButton() == MouseEvent.BUTTON3) clickColor = Color.PINK;
        if (e.isAltDown()) clickColor = Color.WHITE;
        if (e.isControlDown()) clickColor = Color.BLUE;
        if (e.isShiftDown()) clickColor = Color.CYAN;
        graphics.setPaint(new RadialGradientPaint(e.getX(), e.getY(), 40, new float[]{0, 1}, new Color[]{clickColor, new Color(0, 0, 0, 0)}));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        customComponent.repaint();
      }
    });
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
}
