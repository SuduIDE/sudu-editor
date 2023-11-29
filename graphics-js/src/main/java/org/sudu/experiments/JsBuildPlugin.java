package org.sudu.experiments;

import org.sudu.experiments.js.JsHelper;
import org.teavm.backend.javascript.TeaVMJavaScriptHost;
import org.teavm.backend.javascript.codegen.SourceWriter;
import org.teavm.backend.javascript.rendering.RenderingManager;
import org.teavm.backend.javascript.spi.Injector;
import org.teavm.backend.javascript.spi.InjectorContext;
import org.teavm.jso.JSObject;
import org.teavm.model.MethodReference;
import org.teavm.vm.BuildTarget;
import org.teavm.vm.spi.RendererListener;
import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMPlugin;

import java.io.IOException;
import java.util.Properties;

//  JsBuildPlugin
//    invokes MainClass.main() for web worker code
//    generates JsHelper.directJavaToJs and JsHelper.directJsToJava
public class JsBuildPlugin implements TeaVMPlugin, RendererListener, Injector {

  private boolean invokeMain;
  private SourceWriter writer;

  @Override
  public void install(TeaVMHost host) {
    invokeMain = isTrue("invokeMain", host.getProperties());
    TeaVMJavaScriptHost jsHost = host.getExtension(TeaVMJavaScriptHost.class);
    if (jsHost == null) return;
    jsHost.add(this);
    addGenerators(jsHost);
  }

  private void addGenerators(TeaVMJavaScriptHost jsHost) {
    for (MethodReference ref : new MethodReference[]{
        new MethodReference(JsHelper.class, "directJavaToJs", Object.class, JSObject.class),
        new MethodReference(JsHelper.class, "directJsToJava", JSObject.class, Object.class)
    }) jsHost.add(ref, this);
  }

  @Override
  public void generate(InjectorContext context, MethodReference methodRef) {
    switch (methodRef.getName()) {
      case "directJavaToJs":
      case "directJsToJava":
        context.writeExpr(context.getArgument(0), context.getPrecedence());
        break;
    }
  }

  static boolean isTrue(String name, Properties properties) {
    return "true".equals(properties.getProperty(name));
  }

  @Override
  public void begin(RenderingManager context, BuildTarget buildTarget) throws IOException {
    writer = context.getWriter();
//    String hi = "// HELLO TeamVm from plugin";
//    writer.append(hi).newLine();
  }

  @Override
  public void complete() throws IOException {
    if (invokeMain) {
      writer.append("$rt_exports.main();").newLine();
    }
  }
}
