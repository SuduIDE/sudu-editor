/*
 *  Copyright 2023 Alexey Andreev.
 *
 *  This code is granted by Alexey Andreev,
 *      the author of the TeaVM project teavm.org
 */
package org.sudu.experiments.json;

public abstract class JsonErrorReporter {
  public abstract void error(String message);
}
