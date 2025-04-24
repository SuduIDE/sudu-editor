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

  static CompactViewRange visible(int b, int e) { return new CompactViewRange(b, e, true); }
  static CompactViewRange invisible(int b, int e) { return new CompactViewRange(b, e, false); }

  static CompactViewRange[] t1() {
    return new CompactViewRange[]{
        invisible(0, 1),
        visible(1, 9),
        visible(9, 12),
        invisible(12, 19),
        visible(19, 32),
        invisible(32, 35),
        visible(35, 37),
        invisible(37, 46),
        visible(46, 58),
        invisible(58, 70),
        invisible(70, 76)
    };
  }

  @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
  static CompactViewRange[] makeDebugRemap(Document doc) {
    int length = doc.length();
    int rngMax = length / 5;
    XorShiftRandom r = new XorShiftRandom();
    ArrayList<CompactViewRange> ranges = new ArrayList<>();

    int pos = 0;

    while (pos < length) {
      int l = 1 + r.nextInt(rngMax - 1);
      boolean visible = r.nextBoolean();
      int endLine = Math.min(pos + l, length);
      ranges.add(new CompactViewRange(pos, endLine, visible));
      pos += l;
    }

    return ranges.toArray(new CompactViewRange[ranges.size()]);
  }
}
