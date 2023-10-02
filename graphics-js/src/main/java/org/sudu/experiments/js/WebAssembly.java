package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBuffer;

public class WebAssembly {

  public interface Memory extends JSObject {
    @JSProperty("buffer") ArrayBuffer buffer();

    int grow(int numPages64k);

    default JsMemoryAccess memoryAccess() { return new JsMemoryAccess(buffer()); }
  }

  public interface MemoryContainer extends JSObject {
    @JSProperty("memory") Memory memory();
  }

  @JSBody(
      params = {"requestPromise", "importObject"},
      script = "return WebAssembly.instantiateStreaming(requestPromise, importObject);"
  )
  public static native <Exports extends MemoryContainer>
  Promise<ModuleWithInstance<Exports>> instantiateStreaming(Promise<JSObject> requestPromise, JSObject importObject);

  @JSBody(
      params = {"buffer", "importObject"},
      script = "return WebAssembly.instantiate(buffer, importObject);"
  )
  public static native <Exports extends MemoryContainer>
  Promise<ModuleWithInstance<Exports>> instantiate(ArrayBuffer buffer, JSObject importObject);

  @JSBody(
      params = {"module", "importObject"},
      script = "return WebAssembly.instantiate(module, importObject);"
  )
  public static native <Exports extends MemoryContainer>
  Promise<ModuleWithInstance<Exports>> instantiate(Module module, JSObject importObject);

  public interface Module extends JSObject {
    interface NameKind extends JSObject {
      @JSProperty("name") JSString name();
      @JSProperty("kind") JSString kind();
    }
    interface Export extends NameKind {}

    interface Import extends NameKind {
      @JSProperty("module") JSString module();
    }

    @JSProperty("exports") JsArrayReader<Export> exports();
    @JSProperty("imports") JsArrayReader<Import> imports();
  }

  public interface Instance<Exports extends JSObject> extends JSObject {
    @JSProperty("exports") Exports exports();
  }

  public interface ModuleWithInstance<Exports extends JSObject> extends JSObject {
    @JSProperty("module")   Module module();
    @JSProperty("instance") Instance<Exports> instance();
  }
}
