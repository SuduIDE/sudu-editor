# Text Tokenization Pipeline

How a document goes from raw source text to styled/tokenized `CodeElement`s, and where the worker parser sits. This is the **baseline behavior today**: the document is 100% source code, with no inline disassembly yet. It is written down because adding inline disassembly (and unified diff) changes this pipeline.

Relevant code: `Model`, `Document`, `CodeLine`, `CodeElement`, `worker/parser/ParserUtils`, `worker/parser/ParseResult`, `worker/proxy/*Proxy`, `EditorComponent`.

## Terms

- **`Document`** - the model: an ordered array of `CodeLine`s plus parse-derived data (`tree`, `scopeGraph`, `usageToDef`, `defToUsages`).
- **`CodeLine`** - one line, made of styled `CodeElement`s.
- **`CodeElement`** - a text fragment (`s`) with a `color`/`type` (from `ParserConstants.TokenTypes`) and `style`.
- **`ParseResult`** - decoded worker output: `ints` (encoded tokenized document), `source` (word chars), optional `graphInts`/`graphChars` (scope graph), `language`, `version`.

## Steps

### 1. Source -> lines (un-tokenized)
`Model` creates a `Document` from a line array (`new Document(String[] text)` -> `CodeLine.makeLines`). Each line starts as **one default `CodeElement`** (`new CodeLine(text[i])` -> `new CodeElement(s)`); no semantics/colors yet. Line splitting uses `SplitText`/`SplitInfo`.

### 2. Send source to the worker
`Model.requestParseFile()` chooses a path based on size and `isDisableParser()`:

- **Full parse (small files / activity):** `sendFull` -> `FileProxy.asyncParseFullFile` (per-language job, e.g. `JavaProxy.PARSE_FULL_FILE_SCOPES`) -> `Model.onFileParsed`.
- **Large files:** `sendLexer` first (fast colorization) -> `onFileLexed`, then `sendStructure` (Java) -> `onFileStructureParsed`, then viewport parse.
- **Parser disabled:** lexer only (`sendLexer`), no full semantics.

The worker receives `document.getChars()` (the raw source characters), the language type, and `document.currentVersion`.

### 3. Worker parses
The language `*Proxy` runs the corresponding parser and returns an `Object[]`, decoded into a `ParseResult`. The `ints` payload is an encoded document: line count `N`, interval count `K`, usage/def count `L`, then per-line element data (`start`, `stop`, `type`, `style`) plus the word characters in `source`.

### 4. Apply result -> replace lines with tokenized ones
`ParserUtils.updateDocument(document, parseRes)` is the **"replace un-tokenized code with tokenized lines"** step:

- Reads `N`, resizes `document.lines`.
- For each line reads `CodeElement[]` (`readElements`) and replaces the line: `document.setLine(i, new CodeLine(elements), true)`.
- Builds `IntervalTree` (structure) and `usageToDef` / `defToUsages`; optionally `updateGraph` -> `scopeGraph`.
- `updateDocument(..., saveOldLines=true)` (structure parse) **skips** existing lines, preserving already-lexed content.
- `updateDocumentInterval(...)` replaces only a sub-range (viewport / incremental) and splices `left`/`right` elements from the existing boundary lines.

### 5. Post-parse
`Model.onResolved` runs `document.onResolve` and `computeUsages()`; usages/definition are looked up via the `Document` maps and highlighted. `EditorComponent`/`CodeLineRenderer` render the now-tokenized `CodeLine`s.

### 6. Edits and reparse
Edits produce a `CpxDiff`, bump `document.currentVersion`, and trigger a reparse (`requestParseFile`, `iterativeParsing`). Results whose `parseRes.version != document.currentVersion` are discarded. Incremental reparse uses `document.tree.getReparseNode()` to compute the changed interval.

### 7. Semantic tokens
`setSemanticTokens(pendingSemanticTokens)` merges external semantic tokens (`SemanticTokenInfo`) with parser colors.

## Invariants (today)

- The worker only ever sees **source characters** (`document.getChars()`), keyed by `document.currentVersion`.
- Parser line indices **are** document line indices: results replace `CodeLine`s in place, `1:1`.
- Structure, scope graph, usages, viewport ranges, and reparse intervals all share the **same line/char coordinate space** as the document (`Pos(line, char)`, character offsets).
- `Document` length / `getChars()` / `getFullLength` are assumed to be "source only" for diff, sync, copy, and parsing.

## Why inline disassembly strains this pipeline

If disassembly lines live in the same `Document` (as planned), then:

- `document.getChars()` would include disassembly text, so the parser would receive invalid source for its language.
- Parser line indices would no longer match document line indices (extra lines above shift everything).
- `Pos(line, char)`, the `IntervalTree`, `scopeGraph`, `usageToDef`/`defToUsages`, viewport ranges, and incremental reparse intervals would all be offset by the number of interleaved disassembly lines.
- `ParserUtils.updateDocument` would overwrite disassembly lines with parsed source lines.
- Diff/sync/copy would treat disassembly lines as editable source unless excluded.

These consequences are the basis of [inline-disasm-open-questions.md](inline-disasm-open-questions.md).
