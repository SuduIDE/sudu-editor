package org.sudu.experiments.win32;

import org.sudu.experiments.FontDesk;
import org.sudu.experiments.FontLoader;
import org.sudu.experiments.Fonts;

import java.io.IOException;

public class D2dFactoryTest {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();
    Win32.coInitialize();

    D2dFactory f = D2dFactory.create(FontLoader.JetBrainsMono.regular());

    FontDesk font1 = f.getFont(Fonts.JetBrainsMono, 20, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
    FontDesk font2 = f.getFont(Fonts.JetBrainsMono, 20, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);

    FontDesk segoeUI = f.getFont(Fonts.SegoeUI, 20, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
    FontDesk consolas = f.getFont(Fonts.Consolas, 20, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);

    f.dispose();

  }

}
