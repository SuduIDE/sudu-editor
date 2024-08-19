package org.sudu.experiments.protocol;

import java.util.Arrays;
import java.util.Objects;

public class FrontendTreeNode {

  public String name;
  public FrontendTreeNode[] children;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FrontendTreeNode node = (FrontendTreeNode) o;
    return Objects.equals(name, node.name) && Objects.deepEquals(children, node.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, Arrays.hashCode(children));
  }

  @Override
  public String toString() {
    return
        "{\"name\":\"" + name + '\"' +
        ", \"children\":" + Arrays.toString(children) + "}";
  }
}
