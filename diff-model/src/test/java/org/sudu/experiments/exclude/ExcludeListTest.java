package org.sudu.experiments.exclude;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExcludeListTest {

  @Test
  public void dirMatchTest1() {
    ExcludeList excludeList = new ExcludeList("""
        dir
        """);
    assertExclude(excludeList, "dir", true);
    assertExclude(excludeList, "dir", false);

    assertExclude(excludeList, "parent/dir", true);
    assertExclude(excludeList, "parent/dir", false);
  }

  @Test
  public void dirMatchTest2() {
    ExcludeList excludeList = new ExcludeList("""
        dir/
        """);
    assertExclude(excludeList, "dir", true);
    assertExclude(excludeList, "parent/dir", true);

    assertNotExclude(excludeList, "dir", false);
    assertNotExclude(excludeList, "parent/dir", false);
  }

  @Test
  public void dirMatchTest3() {
    ExcludeList excludeList = new ExcludeList("""
        /dir
        """);
    assertExclude(excludeList, "dir", true);
    assertNotExclude(excludeList, "parent/dir", true);

    assertExclude(excludeList, "dir", false);
    assertNotExclude(excludeList, "parent/dir", false);
  }

  @Test
  public void dirMatchTest4() {
    ExcludeList excludeList = new ExcludeList("""
        /dir/
        """);
    assertExclude(excludeList, "dir", true);
    assertNotExclude(excludeList, "parent/dir", true);

    assertNotExclude(excludeList, "dir", false);
    assertNotExclude(excludeList, "parent/dir", false);
  }

  @Test
  public void subDirMatchTest1() {
    ExcludeList excludeList = new ExcludeList("""
        parent/dir
        """);
    assertExclude(excludeList, "parent/dir", true);
    assertExclude(excludeList, "parent/dir", false);

    assertNotExclude(excludeList, "root/parent/dir", true);
    assertNotExclude(excludeList, "root/parent/dir", false);
  }

  @Test
  public void subDirMatchTest2() {
    ExcludeList excludeList = new ExcludeList("""
        parent/dir/
        """);
    assertExclude(excludeList, "parent/dir", true);

    assertNotExclude(excludeList, "parent/dir", false);
    assertNotExclude(excludeList, "root/parent/dir", true);
    assertNotExclude(excludeList, "root/parent/dir", false);
  }

  @Test
  public void fileMatchTest1() {
    ExcludeList excludeList = new ExcludeList("""
        dir/*
        !dir/main.*
        """);
    assertExclude(excludeList, "dir/a.txt", false);
    assertExclude(excludeList, "dir/b.java", false);
    assertExclude(excludeList, "dir/c.js", false);

    assertNotExclude(excludeList, "parent/dir/a.txt", false);
    assertNotExclude(excludeList, "parent/dir/b.java", false);
    assertNotExclude(excludeList, "parent/dir/c.js", false);

    assertNotExclude(excludeList, "dir/main.txt", false);
    assertNotExclude(excludeList, "dir/main.java", false);
    assertNotExclude(excludeList, "dir/main.js", false);

    assertNotExclude(excludeList, "dir", true);
  }

  @Test
  public void wildcardTest1() {
    ExcludeList excludeList = new ExcludeList("""
        *.?s
        !main.js
        !main.ts
        """);
    assertExclude(excludeList, "source.js", false);
    assertExclude(excludeList, "source.ts", false);
    assertExclude(excludeList, "main/source.js", false);
    assertExclude(excludeList, "main/source.ts", false);

    assertNotExclude(excludeList, "source.jjs", false);
    assertNotExclude(excludeList, "source.s", false);
    assertNotExclude(excludeList, "main/source.jjs", false);
    assertNotExclude(excludeList, "main/source.s", false);

    assertNotExclude(excludeList, "main.js", false);
    assertNotExclude(excludeList, "main.ts", false);
    assertNotExclude(excludeList, "src/main.js", false);
    assertNotExclude(excludeList, "src/main.ts", false);
  }

  @Test
  public void wildcardTest2() {
    ExcludeList excludeList = new ExcludeList("""
        *.[jt]s
        !main.js
        !main.ts
        """);
    assertExclude(excludeList, "source.js", false);
    assertExclude(excludeList, "source.ts", false);
    assertExclude(excludeList, "main/source.js", false);
    assertExclude(excludeList, "main/source.ts", false);

    assertNotExclude(excludeList, "source.jjs", false);
    assertNotExclude(excludeList, "source.s", false);
    assertNotExclude(excludeList, "main/source.jjs", false);
    assertNotExclude(excludeList, "main/source.s", false);

    assertNotExclude(excludeList, "main.js", false);
    assertNotExclude(excludeList, "main.ts", false);
    assertNotExclude(excludeList, "src/main.js", false);
    assertNotExclude(excludeList, "src/main.ts", false);
  }

  @Test
  public void wildcardTest3() {
    ExcludeList excludeList = new ExcludeList("""
        *.[j-t]s
        !*.os
        """);
    assertExclude(excludeList, "source.js", false);
    assertExclude(excludeList, "source.ts", false);
    assertExclude(excludeList, "source.ls", false);
    assertExclude(excludeList, "main/source.js", false);
    assertExclude(excludeList, "main/source.ts", false);
    assertExclude(excludeList, "main/source.ls", false);

    assertNotExclude(excludeList, "source.jjs", false);
    assertNotExclude(excludeList, "source.s", false);
    assertNotExclude(excludeList, "source.as", false);
    assertNotExclude(excludeList, "source.zs", false);
    assertNotExclude(excludeList, "main/source.jjs", false);
    assertNotExclude(excludeList, "main/source.s", false);
    assertNotExclude(excludeList, "main/source.as", false);
    assertNotExclude(excludeList, "main/source.zs", false);

    assertNotExclude(excludeList, "main.os", false);
    assertNotExclude(excludeList, "src/main.os", false);
  }

  @Test
  public void testExcept1() {
    ExcludeList excludeList = new ExcludeList("""
        /*
        !/foo
        /foo/*
        !/foo/bar
        """);
    assertExclude(excludeList, "foo/test", true);
    assertExclude(excludeList, "foo/main", true);

    assertNotExclude(excludeList, "foo", true);
    assertNotExclude(excludeList, "foo", false);
    assertNotExclude(excludeList, "foo/bar", true);
    assertNotExclude(excludeList, "foo/bar", false);
    assertNotExclude(excludeList, "foo/bar/test", true);
    assertNotExclude(excludeList, "foo/bar/test", false);
  }

  @Test
  public void leadingPathWildcardTest1() {
    ExcludeList excludeList = new ExcludeList("""
        **/foo
        """);
    assertExclude(excludeList, "foo", true);
    assertExclude(excludeList, "foo", false);
    assertExclude(excludeList, "a/foo", true);
    assertExclude(excludeList, "a/foo", false);
    assertExclude(excludeList, "a/b/foo", true);
    assertExclude(excludeList, "a/b/foo", false);
    assertExclude(excludeList, "a/b/c/foo", true);
    assertExclude(excludeList, "a/b/c/foo", false);
  }

  @Test
  public void leadingPathWildcardTest2() {
    ExcludeList excludeList = new ExcludeList("""
        **/foo/bar
        """);
    assertExclude(excludeList, "foo/bar", true);
    assertExclude(excludeList, "foo/bar", false);
    assertExclude(excludeList, "a/foo/bar", true);
    assertExclude(excludeList, "a/foo/bar", false);
    assertExclude(excludeList, "a/b/foo/bar", true);
    assertExclude(excludeList, "a/b/foo/bar", false);
    assertExclude(excludeList, "a/b/c/foo/bar", true);
    assertExclude(excludeList, "a/b/c/foo/bar", false);

    assertNotExclude(excludeList, "foo/a/bar", true);
    assertNotExclude(excludeList, "foo/a/bar", false);
    assertNotExclude(excludeList, "foo", true);
    assertNotExclude(excludeList, "foo", false);
    assertNotExclude(excludeList, "bar", true);
    assertNotExclude(excludeList, "bar", false);
  }

  @Test
  public void trailingPathWildcardTest1() {
    ExcludeList excludeList = new ExcludeList("""
        foo/**
        """);
    assertExclude(excludeList, "foo/bar", true);
    assertExclude(excludeList, "foo/bar", false);
    assertExclude(excludeList, "foo/bar/test", true);
    assertExclude(excludeList, "foo/bar/test", false);

    assertNotExclude(excludeList, "foo", true);
    assertNotExclude(excludeList, "foo", false);
  }

  @Test
  public void trailingPathWildcardTest2() {
    ExcludeList excludeList = new ExcludeList("""
        foo/bar/**
        """);
    assertExclude(excludeList, "foo/bar/test", true);
    assertExclude(excludeList, "foo/bar/test", false);

    assertNotExclude(excludeList, "foo", true);
    assertNotExclude(excludeList, "foo", false);
    assertNotExclude(excludeList, "foo/bar", true);
    assertNotExclude(excludeList, "foo/bar", false);
  }

  @Test
  public void middlePathWildcardTest1() {
    ExcludeList excludeList = new ExcludeList("""
        a/**/b
        """);
    assertExclude(excludeList, "a/b", true);
    assertExclude(excludeList, "a/b", false);
    assertExclude(excludeList, "a/c/b", true);
    assertExclude(excludeList, "a/c/b", false);
    assertExclude(excludeList, "a/c/d/b", true);
    assertExclude(excludeList, "a/c/d/b", false);

    assertNotExclude(excludeList, "parent/a/b", true);
    assertNotExclude(excludeList, "parent/a/b", false);
    assertNotExclude(excludeList, "parent/a/c/b", true);
    assertNotExclude(excludeList, "parent/a/c/b", false);
  }

  @Test
  public void middlePathWildcardTest2() {
    ExcludeList excludeList = new ExcludeList("""
        /src/**/main/*.py
        """);
    assertExclude(excludeList, "src/main/main.py", false);
    assertExclude(excludeList, "src/dir1/main/main.py", false);
    assertExclude(excludeList, "src/dir1/dir2/main/main.py", false);

    assertNotExclude(excludeList, "main.py", false);
    assertNotExclude(excludeList, "main/main.py", false);
  }

  @Test
  public void pathWildcardTest1() {
    ExcludeList excludeList = new ExcludeList("""
        **/src/**/main/**
        """);
    assertExclude(excludeList, "src/main/main.py", false);
    assertExclude(excludeList, "project/src/main/main.py", false);
    assertExclude(excludeList, "src/module/main/main.py", false);
    assertExclude(excludeList, "src/main/java/main.py", false);
    assertExclude(excludeList, "project/src/module/main/main.py", false);
    assertExclude(excludeList, "project/src/main/java/main.py", false);
    assertExclude(excludeList, "project/src/main/java/main.py", false);
    assertExclude(excludeList, "src/module/main/java/main.py", false);
    assertExclude(excludeList, "project/src/main/main.py", false);
    assertExclude(excludeList, "src/module/main/java/main.py", false);
    assertExclude(excludeList, "project/src/module/main/java/main.py", false);

    assertNotExclude(excludeList, "scr/main", true);
  }

  private void assertExclude(ExcludeList excludeList, String path, boolean isDir) {
    Assertions.assertTrue(excludeList.isExcluded(path, isDir));
  }

  private void assertNotExclude(ExcludeList excludeList, String path, boolean isDir) {
    Assertions.assertFalse(excludeList.isExcluded(path, isDir));
  }
}
