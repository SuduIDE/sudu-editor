public class TestResolve {

  public void intResolve() {
    int a = 1;
    var b = 1;
    var c = 1 + 1;
    var d = true ? 1 : 2;
    var e = d;
    var f = e - d;

    fun(1);
    fun(a);
    fun(b);
    fun(c);
    fun(d);
    fun(e);
    fun(f);
  }

  public void booleanResolve() {
    boolean a = true;
    var b = true;
    var c = true || false;
    var d = true ? true : false;
    var e = d;
    var f = 1 < 2;
    var g = (Integer) 1 instanceof Number;

    fun(true);
    fun(a);
    fun(b);
    fun(c);
    fun(d);
    fun(e);
    fun(f);
    fun(g);
  }

  public void stringResolve() {
    String a = "1";
    var b = "1";
    var c = "1" + "1";
    var d = true ? "1" : "2";
    var e = d;

    fun("1");
    fun(a);
    fun(b);
    fun(c);
    fun(d);
    fun(e);
  }

  public void classResolve() {
    RandomClass a = null;
    var b = new RandomClass();
    var c = new RandomClass("1");
    var d = true ? new RandomClass("1") : new RandomClass("2");
    var e = d;
    var f = (RandomClass) 1;

    fun(new RandomClass());
    fun(new RandomClass("1"));
    fun(a);
    fun(b);
    fun(c);
    fun(d);
    fun(e);
  }

  public void fun(int a) {}

  public void fun(boolean a) {}

  public void fun(String a) {}

  public void fun(RandomClass a) {}

}
