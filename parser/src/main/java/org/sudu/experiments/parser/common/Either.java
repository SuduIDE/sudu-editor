package org.sudu.experiments.parser.common;

import java.util.function.Function;

public class Either<L, R> {

  public L left;
  public R right;

  public Either(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public boolean isLeft() {
    return left != null;
  }

  public boolean isRight() {
    return right != null;
  }

  public <LL, RR> Either<LL, RR> map(
      Function<L, LL> leftFun,
      Function<R, RR> rightFun
  ) {
    LL leftRes = isLeft() ? leftFun.apply(left) : null;
    RR rightRes = isRight() ? rightFun.apply(right) : null;
    return new Either<>(leftRes, rightRes);
  }

  public static <L, R> Either<L, R> left(L left) {
    return new Either<>(left, null);
  }

  public static <L, R> Either<L, R> right(R right) {
    return new Either<>(null, right);
  }

}
