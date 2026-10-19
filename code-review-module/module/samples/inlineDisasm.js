import * as editorApi from "../src/codereview.js";

/**
 * Inline-disassembly sample with two tokenization modes (toggle top-right).
 *
 * `newTextModelFromLines` builds a *mapped* document from an array of
 * `ILineDescriptor`:
 *
 *   { lineNumber: N, tokens: [{ text: "..." }] }   -> a source line with ordinal N
 *   { tokens: [{ text: "..." }, ...] }             -> an attached (disassembly) line
 *
 * A descriptor's `tokens` are the run segments the line is made of. Concatenated
 * they form the line text, so a plain line is just one run. Each run may carry a
 * `tokenType`; a run without one is plain default text.
 *
 * The same tokenized runs are rendered two different ways:
 *
 *   "custom"  — `tokenType` is a free-form name from this document's *token
 *               dictionary* (the last argument of `newTextModelFromLines`),
 *               with explicit colors. Nothing global knows these names.
 *
 *   "builtin" — `tokenType` is a predefined `_SemanticTokenType` name
 *               (`keyword`, `number`, `operator`, ...). No dictionary is passed,
 *               so the editor's theme palette colors them and they follow
 *               theme changes for free.
 *
 * The resulting document is always read-only, for every line. Editing keys,
 * paste, cut and undo are all blocked; the model is meant to display, not edit.
 */

const program = [
  {
    src: "int sum(int n) {",
    asm: ["sum(int):", "    push   rbp", "    mov    rbp, rsp", "    mov    DWORD PTR [rbp-0x4], edi"],
  },
  {
    src: "    int s = 0;",
    asm: ["    mov    DWORD PTR [rbp-0x8], 0x0"],
  },
  {
    src: "    for (int i = 0; i < n; i++) {",
    asm: ["    mov    DWORD PTR [rbp-0xc], 0x0", ".L2:", "    mov    eax, DWORD PTR [rbp-0xc]", "    cmp    eax, DWORD PTR [rbp-0x4]", "    jge    .L3"],
  },
  {
    src: "        s += i;",
    asm: ["    mov    eax, DWORD PTR [rbp-0xc]", "    add    DWORD PTR [rbp-0x8], eax"],
  },
  {
    src: "    }",
    asm: ["    add    DWORD PTR [rbp-0xc], 0x1", "    jmp    .L2"],
  },
  {
    src: "    return s;",
    asm: [".L3:", "    mov    eax, DWORD PTR [rbp-0x8]"],
  },
  {
    src: "}",
    asm: ["    pop    rbp", "    ret"],
  },
];

/**
 * The tokenizer classifies each run into one of these semantic *kinds*.
 * How a kind is spelled in `tokenType` depends on the mode (see the maps below).
 */
const KIND = {
  instruction: "instruction",
  register: "register",
  number: "number",
  operator: "operator",
  keyword: "keyword",
};

/** Mode 1: free-form names, resolved by this document's dictionary. */
const customTypes = {
  instruction: "instruction",
  register: "register",
  number: "numericConstant",
  operator: "asmOperator",
  keyword: "asmKeyword",
};

/** Per-document token dictionary: free-form name -> explicit color settings. */
const customDictionary = {
  instruction:     { foreground: "#c586c0" },
  register:        { foreground: "#9cdcfe" },
  numericConstant: { foreground: "#b5cea8" },
  asmOperator:     { foreground: "#d4d4d4" },
  asmKeyword:      { foreground: "#569cd6" },
};

/** Mode 2: predefined `_SemanticTokenType` names, colored by the editor theme. */
const builtinTypes = {
  instruction: "keyword",
  register: "property",
  number: "number",
  operator: "operator",
  keyword: "type",
};

const ASM_INSTRUCTIONS = new Set([
  "push", "pop", "mov", "movzx", "movsx", "add", "sub", "imul", "idiv",
  "cmp", "test", "and", "or", "xor", "lea", "call", "ret", "nop", "leave",
  "jmp", "je", "jne", "jl", "jle", "jg", "jge", "cdqe", "sete", "setne",
]);

const ASM_REGISTERS = new Set([
  "rax", "rbx", "rcx", "rdx", "rsi", "rdi", "rbp", "rsp", "rip",
  "eax", "ebx", "ecx", "edx", "esi", "edi", "ebp", "esp", "eip",
  "ax", "bx", "cx", "dx", "al", "bl", "cl", "dl", "ah", "bh", "ch", "dh",
  "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15",
]);

const ASM_SIZE_KEYWORDS = new Set([
  "BYTE", "WORD", "DWORD", "QWORD", "PTR", "SHORT", "LONG",
]);

/**
 * Splits one assembly line into run segments covering the whole text.
 * Known mnemonics, registers and size keywords get a kind; everything else
 * (whitespace, punctuation, labels) stays plain.
 */
function tokenizeAsm(line) {
  const runs = [];
  const re = /(\s+)|(0[xX][0-9a-fA-F]+|\d+)|([A-Za-z_.$][A-Za-z0-9_.$]*)|([+\-*/])|(.)/g;
  let m;
  while ((m = re.exec(line)) !== null) {
    const text = m[0];
    let kind;
    if (m[2]) kind = KIND.number;
    else if (m[3]) {
      const word = m[3];
      if (ASM_INSTRUCTIONS.has(word)) kind = KIND.instruction;
      else if (ASM_REGISTERS.has(word)) kind = KIND.register;
      else if (ASM_SIZE_KEYWORDS.has(word)) kind = KIND.keyword;
    } else if (m[4]) kind = KIND.operator;
    runs.push(kind ? { text, kind } : { text });
  }
  return runs;
}

/**
 * Flattens the program into the descriptor array the API expects, spelling each
 * token kind with the type map of the chosen mode.
 */
function buildLines(program, typeFor) {
  const lines = [];
  program.forEach((entry, index) => {
    lines.push({ lineNumber: index + 1, tokens: [{ text: entry.src }] });
    for (const asm of entry.asm) {
      lines.push({
        tokens: tokenizeAsm(asm).map(run =>
          run.kind ? { text: run.text, tokenType: typeFor[run.kind] } : { text: run.text }
        ),
      });
    }
  });
  return lines;
}

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const editor = editorApi.newEditor({
  containerId: "editor",
  workers: threadPool,
});

const toggle = document.getElementById("tokenMode");

const MODE_LABELS = {
  custom: "tokens: custom dictionary",
  builtin: "tokens: built-in _SemanticTokenType",
};

let mode = "custom";

function applyMode() {
  const builtin = mode === "builtin";
  const lines = buildLines(program, builtin ? builtinTypes : customTypes);
  // "builtin" passes no dictionary: every run then resolves through the
  // predefined semantic types and the editor theme palette.
  const model = editorApi.newTextModelFromLines(
    threadPool, lines, "cpp", { path: "sample.c" },
    builtin ? undefined : customDictionary
  );
  editor.setModel(model);
  editor.focus();
  toggle.textContent = MODE_LABELS[mode];

  // The document is interleaved, so it has more lines than the source it maps.
  console.log(
    `inline disasm [${mode}]: descriptors=${lines.length}` +
      ` sourceLines=${program.length}` +
      ` documentLines=${editor.getNumLines()}`
  );
}

toggle.addEventListener("click", () => {
  mode = mode === "custom" ? "builtin" : "custom";
  applyMode();
});

applyMode();
