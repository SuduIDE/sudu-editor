package org.sudu.experiments;

import org.sudu.experiments.text.TextFormat;
import org.teavm.jso.browser.Performance;

import java.util.function.DoubleSupplier;

public class JsTime implements DoubleSupplier {
  double t0 = Performance.now() * 0.001;

  @Override
  public double getAsDouble() {
    double t1 = Performance.now() * 0.001;
    double dT = t1 - t0;
    t0 = t1;
    return dT;
  }

  @Override
  public String toString() {
    return TextFormat.toString3(getAsDouble());
  }
}
