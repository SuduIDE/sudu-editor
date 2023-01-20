package org.sudu.experiments;

import org.teavm.backend.javascript.TeaVMJavaScriptHost;
import org.teavm.backend.javascript.codegen.SourceWriter;
import org.teavm.backend.javascript.rendering.RenderingManager;
import org.teavm.vm.BuildTarget;
import org.teavm.vm.spi.RendererListener;
import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMPlugin;

import java.io.IOException;
import java.util.Properties;

// JsWorkerBuildPlugin invokes MainClass.main() for web worker code
public class JsWorkerBuildPlugin implements TeaVMPlugin, RendererListener {

  private boolean worker;
  private SourceWriter writer;

  @Override
  public void install(TeaVMHost host) {
    worker = isTrue("worker", host.getProperties());
    TeaVMJavaScriptHost jsHost = host.getExtension(TeaVMJavaScriptHost.class);
    if (jsHost != null) jsHost.add(this);
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
    if (worker) {
      writer.append("main();").newLine();
    }
  }
}
