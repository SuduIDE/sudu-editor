package org.sudu.experiments.esm;

import org.sudu.experiments.fonts.FontConfigJs;
import org.sudu.experiments.js.FontFace;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// export function loadFonts(codiconUrl: string): Promise<FontFace[]>;
@JSFunctor
interface LoadFonts extends JSObject {
  Promise<?> load(JSString codiconUrl);
}

public interface JsLoadFonts {
  class Setter {
    @JSBody(params = {"f"}, script = "loadFonts = f;")
    static native void set(LoadFonts f);
  }

  static void install() {
    Setter.set(JsLoadFonts::load);
  }

  static Promise<?> load(JSString codiconUrl) {
    var promise = FontFace.loadFonts(
        FontConfigJs.codiconFontConfig(codiconUrl.stringValue()));
    promise.then(
        FontFace::addToDocument,
        JsHelper::consoleError);
    return promise;
  }
}
