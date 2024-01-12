package org.sudu.experiments.js;

import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.text.SplitText;
import org.teavm.jso.core.JSString;

public interface SplitJsText {
  static SplitInfo split(JSString t) {
    return SplitText.split(t.getLength(), i -> (char) t.charCodeAt(i));
  }
}
