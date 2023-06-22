package org.sudu.experiments.utils;

import org.sudu.experiments.demo.DocumentHighlight;
import org.sudu.experiments.demo.Location;
import org.sudu.experiments.esm.*;
import org.teavm.jso.core.JSArray;

public abstract class ProviderUtils {

  public static DocumentHighlight[] toHighlights(JSArray<JsDocumentHighlight> jsArr) {
    DocumentHighlight[] result = new DocumentHighlight[jsArr.getLength()];
    for (int i = 0; i < result.length; i++) {
      var jsHl = jsArr.get(i);
      var hl = new DocumentHighlight();
      result[i] = hl;
      if (jsHl.hasKind()) hl.kind = jsHl.getKind();
      jsHl.getRange().toRange(hl.range);
    }
    return result;
  }

  public static Location[] toLocations(JSArray<JsLocation> jsArr) {
    Location[] result = new Location[jsArr.getLength()];
    for (int i = 0; i < result.length; i++) {
      JsLocation loc = jsArr.get(i);
      JsUri uri = loc.getUri();
      var l = new Location(uri.toJava());
      loc.getRange().toRange(l.range);
      result[i] = l;
    }
    return result;
  }
}
