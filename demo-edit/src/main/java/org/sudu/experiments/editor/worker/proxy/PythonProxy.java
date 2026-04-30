package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.base.FirstLinesIntLexer;
import org.sudu.experiments.parser.common.base.IntParser;
import org.sudu.experiments.parser.python.parser.PythonFirstLinesLexer;
import org.sudu.experiments.parser.python.parser.PythonIntervalParser;
import org.sudu.experiments.parser.python.parser.PythonLightParser;

public class PythonProxy extends BaseProxy {

  public PythonProxy() {
    super(FileProxy.PYTHON_FILE, Languages.PYTHON);
  }

  @Override
  public FirstLinesIntLexer getFirstLinesLexer() {
    return new PythonFirstLinesLexer();
  }

  public static final String PARSE_FULL_FILE = "PythonProxy.parseFullFile";

  @Override
  public IntParser getFullParser() {
    return new PythonLightParser();
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new PythonIntervalParser();
  }
}
