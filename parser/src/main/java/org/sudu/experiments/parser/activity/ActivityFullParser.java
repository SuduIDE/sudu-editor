package org.sudu.experiments.parser.activity;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.walker.ActivityWalker;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;

import java.util.Collections;
import java.util.List;

public class ActivityFullParser extends BaseFullParser {
    @Override
    protected Lexer initLexer(CharStream stream) {
        return new ActivityLexer(stream);
    }

    public int[] parse(char[] source) {
        initLexer(source);

        ActivityParser parser = new ActivityParser(tokenStream);
        parser.setErrorHandler(new ErrorHighlightingStrategy());

        var program = parser.program();
        var walker = new ActivityWalker(tokenTypes, tokenStyles, usageToDefinition);
        var parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(walker, program);

        for (var token : allTokens) {
            if (token.getType() == ActivityLexer.ERROR) {
                tokenTypes[token.getTokenIndex()] = ParserConstants.TokenTypes.ERROR;
            }
        }

        return getInts(defaultIntervalNode());
    }

    @Override
    protected SplitRules initSplitRules() {
        return new SplitRules() {
            @Override
            public List<TokenSplitRule> getRules() {
                return Collections.emptyList();
            }
        };
    }

    @Override
    protected boolean tokenFilter(Token token) {
        int type = token.getType();
        return type != ActivityLexer.EOF && type != ActivityLexer.NEW_LINE;
    }
}
