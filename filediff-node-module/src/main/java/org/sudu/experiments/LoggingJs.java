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

  static void log(int level, String message) {
    Static.logger.log(level, JSString.valueOf(message));
  }

  static void info(String message) {
    Static.logger.log(INFO, JSString.valueOf(message));
  }

  static void info(JSString message) {
    Static.logger.log(INFO, message);
  }

  static void trace(String message) {
    Static.logger.log(TRACE, JSString.valueOf(message));
  }

  static void trace(JSString message) {
    Static.logger.log(TRACE, message);
  }

  static void error(String message) {
    Static.logger.log(ERROR, JSString.valueOf(message));
  }

  static void error(JSString message) {
    Static.logger.log(ERROR, message);
  }

  static void debug(String message) {
    Static.logger.log(DEBUG, JSString.valueOf(message));
  }

  static void debug(JSString message) {
    Static.logger.log(DEBUG, message);
  }

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
            logger.log(INFO, JSString.valueOf("Current level " + Static.level));
          }
      );
    }
  }
}
