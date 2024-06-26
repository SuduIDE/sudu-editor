package org.sudu.experiments.merge;

public interface MergeRangeTypes {

  int DEFAULT             = 0;

  int LEFT_INSERT         = 1;
  int RIGHT_INSERT        = 2;

  int LEFT_DELETE         = 3;
  int RIGHT_DELETE        = 4;

  int LEFT_EDITED         = 5;
  int RIGHT_EDITED        = 6;

  int CONFLICTING         = 7;
  int LEFT_RIGHT_EDITED   = 8;
}
