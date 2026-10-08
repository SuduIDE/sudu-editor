package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.ascendc.parser.AscendCFirstLinesLexer;
import org.sudu.experiments.parser.ascendc.parser.AscendCIntervalParser;
import org.sudu.experiments.parser.ascendc.parser.AscendCLightParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.base.FirstLinesIntLexer;
import org.sudu.experiments.parser.common.base.IntParser;

public class AscendCProxy extends BaseProxy {

  public AscendCProxy() {
    super(FileProxy.ASCEND_C_FILE, Languages.ASCEND_C);
  }

  @Override
  public FirstLinesIntLexer getFirstLinesLexer() {
    return new AscendCFirstLinesLexer();
  }

  public static final String PARSE_FULL_FILE = "AscendCProxy.parseFullFile";

  @Override
  public IntParser getFullParser() {
    return new AscendCLightParser();
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new AscendCIntervalParser();
  }
}
