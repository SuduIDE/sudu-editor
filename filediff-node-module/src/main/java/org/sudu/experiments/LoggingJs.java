package org.sudu.experiments;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

@JSFunctor public interface LoggingJs extends JSObject {
  void log(int level, JSString message);

  int OFF = 0;
  int FATAL = 1;
  int ERROR = 2;
  int WARN = 3;
  int INFO = 4;
  int DEBUG = 5;
  int TRACE = 6;

  class Static {
    public static int level;
    public static LoggingJs logger;
  }

  @JSFunctor interface SetLogLevel extends JSObject {
    void f(int level);
  }

  @JSFunctor interface SetLogger extends JSObject {
    void f(LoggingJs logger);
  }

  class Setter {
    @JSBody(params = {"sll", "sl"}, script = "setLogLevel = sll; setLogOutput = sl")
    static native void setApi(SetLogLevel sll, SetLogger sl);

    public static void set() {
      setApi(
          level -> Static.level = level,
          logger -> {
            Static.logger = logger;
            logger.log(Static.level, JSString.valueOf("Current level " + Static.level));
          }
      );
    }
  }
}
