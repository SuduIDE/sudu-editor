package org.sudu.experiments.webdemo;

//import org.sudu.experiments.math.XorShiftRandom;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SessionManager {
  final AtomicInteger sessionId = new AtomicInteger(0);
  final ConcurrentHashMap<String, WebSession> sessionMap = new ConcurrentHashMap<>();

  public String newSession(Consumer<WebSession> h) {
    int id = sessionId.getAndIncrement();
    String session;
    do {
      int salt = (int) System.nanoTime();
      session = rngString(id, salt, 10);
    } while (sessionMap.containsKey(session));
    WebSession value = new WebSession();
    sessionMap.put(session, value);
    h.accept(value);
    return session;
  }

  public WebSession getSession(String id) {
    return sessionMap.get(id);
  }

  static String rngString(int sessionId, int salt, int l) {
//    XorShiftRandom xr = new XorShiftRandom(sessionId + 1, salt);
    char[] r = new char[l];
    int chars = 'z' - 'a' + 1;
    for (int i = 0; i < l; i++) {
      int nextInt = (int) (Math.random() * (chars + chars + 10));
      int nChar = nextInt < chars ? 'a' + nextInt
          : nextInt < chars * 2 ? 'A' + nextInt - chars
          : nextInt + '0' - chars * 2;
      r[i] = (char) nChar;
    }
    return new String(r);
  }
}
