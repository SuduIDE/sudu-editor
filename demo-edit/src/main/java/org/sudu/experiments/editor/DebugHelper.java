package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.parser.common.Pos;

import java.util.ArrayList;

public class DebugHelper {
  static void dumpFontsSize(WglGraphics g) {
    dumpFontSize(Fonts.CourierNew, g);
    dumpFontSize(Fonts.SegoeUI, g);
    dumpFontSize(Fonts.Helvetica, g);
    dumpFontSize(Fonts.Verdana, g);
  }

  static void dumpFontSize(String font, WglGraphics g) {
    for (int i = 5; i < 32; i++) {
      FontDesk fontDesk = g.fontDesk(font, i);
      Debug.consoleInfo("[" + i + "] ascent = ", fontDesk.fAscent);
      Debug.consoleInfo("[" + i + "] descent = ", fontDesk.fDescent);
      Debug.consoleInfo("[" + i + "] WWidth = ", fontDesk.WWidth);
      Debug.consoleInfo("[" + i + "] spaceWidth = ", fontDesk.spaceWidth);
    }
  }


  static void dumpProvider(DefDeclProvider.Provider provider, Model m, Pos pos) {
    provider.provide(m, pos.line, pos.pos,
        DebugHelper::dumpResult, Debug::consoleInfo);
  }

  private static void dumpResult(Location[] results) {
    System.out.println("results[" + results.length + "]:");
    for (int i = 0; i < results.length; i++) {
      Location r = results[i];
      System.out.println("[" + i + "]: uri = " + r.uri +
          ", " + r.range);
    }
  }

  static void dumpReferenceProvider(ReferenceProvider.Provider p, Model m, Pos pos) {
    p.provideReferences(m, pos.line, pos.pos, true,
        DebugHelper::dumpResult, Debug::consoleInfo);
  }

  static Runnable[] remapActions() {
    return new Runnable[] { () -> {} };
  }

  @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
  static CompactViewRange[] makeDebugRemap(Document doc) {
    int length = doc.length();
    int rngMax = length / 7;
    XorShiftRandom r = new XorShiftRandom();
    ArrayList<CompactViewRange> ranges = new ArrayList<>();

    int pos = 0;

    while (pos < length) {
      int l = r.nextInt(rngMax);
      boolean visible = r.nextBoolean();
      ranges.add(new CompactViewRange(pos, pos + l, visible));
      pos += l;
    }

    return ranges.toArray(new CompactViewRange[ranges.size()]);
  }
}
