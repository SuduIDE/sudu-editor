package org.sudu.experiments.utils;

import org.sudu.experiments.esm.JsLanguageFilter;
import org.sudu.experiments.demo.LanguageSelector;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsArrayReader;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public abstract class LanguageSelectorUtils {

  public static LanguageSelector[] toSelectors(JSObject obj) {
    if (JsArray.isArray(obj)) {
      JsArrayReader<JSObject> jsArr = obj.cast();
      int jsArrLength = jsArr.getLength();
      LanguageSelector[] res = new LanguageSelector[jsArrLength];
      for (int i = 0; i < jsArrLength; i++) {
        res[i] = languageSelector(jsArr.get(i));
      }
      return res;
    }
    return new LanguageSelector[]{ languageSelector(obj) };
  }

  private static LanguageSelector languageSelector(JSObject obj) {
    boolean isString = JSString.isInstance(obj);
    String l = isString ? obj.<JSString>cast().stringValue()
        : obj.<JsLanguageFilter>cast().getLanguageOrNull();
    String s = isString ? null : obj.<JsLanguageFilter>cast().getSchemeOrNull();
    return new LanguageSelector(l, s);
  }
}
