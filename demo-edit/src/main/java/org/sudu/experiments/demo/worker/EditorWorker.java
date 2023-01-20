package org.sudu.experiments.demo.worker;

import org.sudu.experiments.demo.worker.WorkerTest.TestJobs;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.worker.ArrayView;

import java.util.ArrayList;

public class EditorWorker {
  public static Object[] execute(String method, Object[] a) {
    System.out.println("EditorWorker: method = " + method);
    ArrayList<Object> result = new ArrayList<>();

    switch (method) {
      case TestJobs.withString -> TestJobs.withString(string(a, 0), result);
      case TestJobs.withChars -> TestJobs.withChars(array(a, 0).chars(), result);
      case TestJobs.withBytes -> TestJobs.withBytes(array(a, 0).bytes(), result);
      case TestJobs.withInts -> TestJobs.withInts(array(a, 0).ints(), result);
    }
    return ArrayOp.cloneArrayList(result, new Object[result.size()]);
  }

  static ArrayView array(Object[] args, int index) {
    return (ArrayView) args[index];
  }

  static String string(Object[] args, int index) {
    return (String) args[index];
  }
}
