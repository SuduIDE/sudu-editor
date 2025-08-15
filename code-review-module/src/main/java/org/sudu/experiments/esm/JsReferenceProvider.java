package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JsReferenceProvider extends JSObject {

  abstract class Context implements JSObject {
    public abstract @JSProperty boolean getIncludeDeclaration();

    @JSBody(params = {"v"}, script = "return { includeDeclaration: v };")
    public static native Context create(boolean v);
  }

  //  type ProviderValue<T> = T | undefined | null;
  //  type ProviderResult<T> = ProviderValue<T> | Promise<ProviderValue<T>>;
  // returns ProviderResult<ILocation[]>

  JSObject provideReferences(
      JsITextModel model,
      JsPosition position,
      Context context,
      JsCancellationToken token
  );
}
