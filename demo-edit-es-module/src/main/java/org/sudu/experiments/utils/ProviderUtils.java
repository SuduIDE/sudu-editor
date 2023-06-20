package org.sudu.experiments.utils;

import org.sudu.experiments.demo.Location;
import org.sudu.experiments.esm.JsLocation;
import org.sudu.experiments.esm.JsRange;
import org.sudu.experiments.esm.JsUri;
import org.teavm.jso.core.JSArray;

public abstract class ProviderUtils {

  public static Location[] toLocations(JSArray<JsLocation> jsArr) {
    Location[] result = new Location[jsArr.getLength()];
    for (int i = 0; i < result.length; i++) {
      JsLocation loc = jsArr.get(i);
      JsUri uri = loc.getUri();
      JsRange range = loc.getRange();
      result[i] = new Location(
          uri.toJava(),
          range.getEndColumn(),
          range.getEndLineNumber(),
          range.getStartColumn(),
          range.getStartLineNumber()
      );
    }
    return result;
  }
}
