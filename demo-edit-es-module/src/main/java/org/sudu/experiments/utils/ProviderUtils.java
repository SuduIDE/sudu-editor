package org.sudu.experiments.utils;

import org.sudu.experiments.demo.DocumentHighlight;
import org.sudu.experiments.demo.Location;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.JsArrayReader;

public abstract class ProviderUtils {

  public static DocumentHighlight[] toHighlights(JsArrayReader<JsDocumentHighlight> jsArr) {
    DocumentHighlight[] result = new DocumentHighlight[jsArr.getLength()];
    for (int i = 0; i < result.length; i++) {
      JsDocumentHighlight jsHighlight = jsArr.get(i);
      DocumentHighlight highlight = new DocumentHighlight();
      if (jsHighlight.hasKind()) highlight.kind = jsHighlight.getKind();
      jsHighlight.getRange().toJava(highlight.range);
      result[i] = highlight;
    }
    return result;
  }

  public static Location[] toLocations(JsArrayReader<JsLocation> jsArr) {
    Location[] result = new Location[jsArr.getLength()];
    for (int i = 0; i < result.length; i++) {
      JsLocation jsLocation = jsArr.get(i);
      Location location = new Location(jsLocation.getUri().toJava());
      jsLocation.getRange().toJava(location.range);
      result[i] = location;
    }
    return result;
  }
}
