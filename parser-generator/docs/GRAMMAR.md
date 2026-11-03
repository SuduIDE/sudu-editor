# Grammar conventions

Style guide for `.g4` files under [`src/main/resources/grammar/`](src/main/resources/grammar/).

These are **target** conventions. Grammars should follow this file.

This project does not use stock ANTLR. Generation uses the local TeaVM-compatible Tool (`antlr.version` in the root POM, currently the `antlr4-teavm-compatible` fork). Do not add a dependency on another ANTLR distribution.

## Layout and generation

- Put each language in its own folder: `grammar/<lang>/`.
- Lexer-only languages are fine. Not every language has a parser (Python, AscendC, EcmaScript today).
- Combined grammars (`grammar Foo`) and split grammars (`lexer grammar` / `parser grammar`) are both allowed.
- Grammar name should match the file name (`JsonLexer.g4` → `lexer grammar JsonLexer`).
- Generate only with [`GenerateParsers`](../src/main/java/GenerateParsers.java). It walks `grammar/`, and for each folder writes Java under `parser/src/main/java/org/sudu/experiments/parser/<lang>/gen/` with package `org.sudu.experiments.parser.<lang>.gen`. Nested folders insert `gen` after the first segment (`grammar/java/st` → `.../java/gen/st`).
- Do not check in `parser-generator/gen/`. Generated sources belong in the `parser` module.
- If the grammar uses `options { superClass = ...; }`, the hand-written base class lives in that same generated Java package (`PythonLexerBase`, `LightEcmaScriptLexerBase`).

## Lexer

### Channels and skip

- Never use `-> skip`. Tokens that should not reach the parser go to the hidden channel.
- Only the default channel and `HIDDEN` are allowed. Do not declare extra channels.

### Required rules

Every lexer grammar must define both of these:

```antlr
NEW_LINE: ('\r\n' | '\r' | '\n') -> channel(HIDDEN);
ERROR:    .                      -> channel(HIDDEN);
```

- There is **exactly one** `NEW_LINE` rule.
- `NEW_LINE` always uses `-> channel(HIDDEN)`.
- Wrap alternatives in parentheses so the channel command applies to the whole rule (ANTLR requires the `->` command on a single outermost alt).
- The default pattern is `'\r\n' | '\r' | '\n'`. Languages that treat Unicode line separators as line terminators (EcmaScript) may also include `'\u2028'` and `'\u2029'` in `NEW_LINE`.
- `ERROR` is the last lexer rule in the default mode (the catch-all). Place it after comments, whitespace, and `NEW_LINE`.
- `ERROR` always uses `-> channel(HIDDEN)`, including helper lexers.

If the lexer uses modes, keep a single `NEW_LINE` and a single `ERROR` in the default mode. Other modes must not declare another `NEW_LINE` or `ERROR`; leftover characters can be mapped with `-> type(ERROR), channel(HIDDEN)`.

### Naming

| Kind            | Style                      | Examples                                               |
|-----------------|----------------------------|--------------------------------------------------------|
| Lexer tokens    | `SNAKE_CASE`, all capitals | `NEW_LINE`, `LINE_COMMENT`, `OPEN_BRACE`, `IDENTIFIER` |
| Lexer fragments | `PascalCase`               | `HexDigit`, `EscapeSequence`, `DecimalIntegerLiteral`  |
| Parser rules    | `camelCase`                | `compilationUnit`, `json`, `primaryExpression`         |

## Parser

- A parser is optional.
- A split parser grammar must set the lexer vocabulary:

```antlr
parser grammar JsonParser;

options {
    tokenVocab = JsonLexer;
}
```

- Combined grammars do not use `tokenVocab`.
- Parser rule names are `camelCase`, starting with a lowercase letter.
- Literals in parser rules are allowed (`'{'`, `';'`). Token names are also allowed.
