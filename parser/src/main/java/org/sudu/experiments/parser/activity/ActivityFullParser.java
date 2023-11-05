package org.sudu.experiments.parser.activity;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ErrorHighlightingStrategy;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.Node;
import org.sudu.experiments.parser.activity.graph.stat.Activity;
import org.sudu.experiments.parser.activity.walker.ActivityWalker;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.SplitRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityFullParser extends BaseFullParser {

    public Activity activity;
    public Dag2Part dag2;

    @Override
    protected Lexer initLexer(CharStream stream) {
        return new ActivityLexer(stream);
    }

    public List<Object> parse(char[] source) {
        initLexer(source);

        ActivityParser parser = new ActivityParser(tokenStream);
        parser.setErrorHandler(new ErrorHighlightingStrategy());

        var program = parser.activity();
        var walker = new ActivityWalker(tokenTypes, tokenStyles, usageToDefinition);
        var parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(walker, program);

        activity = walker.getActivity();
        System.out.println("READ new ACTIVITY:>>\r\n"+ activity);
        dag2 = activity.toDag2();
        String mermaid2 = dag2.input.printRecDag2(null);

        for (var token : allTokens) {
            if (token.getType() == ActivityLexer.ERROR) {
                tokenTypes[token.getTokenIndex()] = ParserConstants.TokenTypes.ERROR;
            }
        }

        var ret = new ArrayList<>();
        ret.add(getInts(defaultIntervalNode()));
        String mermaid1 = walker.getActivity().toDag1();
        ret.add(mermaid1);
        ret.add(mermaid2);
        return ret;
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
