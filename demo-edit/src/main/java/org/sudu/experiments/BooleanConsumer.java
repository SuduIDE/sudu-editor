package org.sudu.experiments;

// The purpose of using this instead of Consumer<T>
// is possibility to create an array of such objects,
// While generic array creation not allowed

public interface BooleanConsumer {
  void accept(boolean value);
}
