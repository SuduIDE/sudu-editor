package org.sudu.experiments.text;

import org.sudu.experiments.SplitInfo;
import org.teavm.jso.core.JSString;

public interface SplitJsText {
  static SplitInfo split(JSString t) {
    return SplitText.split(t.getLength(), new SplitText.Source() {
      char charAt(int index) {
        return (char) t.charCodeAt(index);
      }
    });
  }
}
