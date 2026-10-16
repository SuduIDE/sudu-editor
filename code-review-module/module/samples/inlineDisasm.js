import * as editorApi from "../src/codereview.js";

/**
 * Inline-disassembly sample.
 *
 * `newTextModelFromLines` builds a *mapped* document from an array of
 * `ILineDescriptor`:
 *
 *   { lineNumber: N, tokens: [{ text: "..." }] }   -> a source line with ordinal N
 *   { tokens: [{ text: "..." }, ...] }             -> an attached (disassembly) line
 *
 * A descriptor's `tokens` are the run segments the line is made of. Concatenated
 * they form the line text, so a plain line is just one run. Each run may carry a
 * `tokenType`, a free-form name resolved through the per-document
 * *token dictionary* passed as the last argument of `newTextModelFromLines`.
 * A run without a `tokenType` is plain default text.
 *
 * The resulting document is always read-only, for every line. Editing keys,
 * paste, cut and undo are all blocked; the model is meant to display, not edit.
 *
 * Below, a small C function is interleaved with the (illustrative) instructions
 * a compiler might emit for each source line, the way an inline-disassembly view
 * renders it. The assembly is tokenized and colorized with the dictionary below.
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
 * Per-document token dictionary: token type name -> color settings.
 * These names are local to this document; nothing global knows about them.
 */
const tokenDictionary = {
  instruction:     { foreground: "#c586c0" },
  register:        { foreground: "#9cdcfe" },
  numericConstant: { foreground: "#b5cea8" },
  asmOperator:     { foreground: "#d4d4d4" },
  asmKeyword:      { foreground: "#569cd6" },
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
 * Known mnemonics, registers and size keywords get a token type; everything
 * else (whitespace, punctuation, labels) stays plain.
 */
function tokenizeAsm(line) {
  const runs = [];
  const re = /(\s+)|(0[xX][0-9a-fA-F]+|\d+)|([A-Za-z_.$][A-Za-z0-9_.$]*)|([+\-*/])|(.)/g;
  let m;
  while ((m = re.exec(line)) !== null) {
    const text = m[0];
    let tokenType;
    if (m[2]) tokenType = "numericConstant";
    else if (m[3]) {
      const word = m[3];
      if (ASM_INSTRUCTIONS.has(word)) tokenType = "instruction";
      else if (ASM_REGISTERS.has(word)) tokenType = "register";
      else if (ASM_SIZE_KEYWORDS.has(word)) tokenType = "asmKeyword";
    } else if (m[4]) tokenType = "asmOperator";
    runs.push(tokenType ? { text, tokenType } : { text });
  }
  return runs;
}

/** Flattens the program into the descriptor array the API expects. */
function buildLines(program) {
  const lines = [];
  program.forEach((entry, index) => {
    lines.push({ lineNumber: index + 1, tokens: [{ text: entry.src }] });
    for (const asm of entry.asm) lines.push({ tokens: tokenizeAsm(asm) });
  });
  return lines;
}

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const editor = editorApi.newEditor({
  containerId: "editor",
  workers: threadPool,
});

const lines = buildLines(program);
const model = editorApi.newTextModelFromLines(
  threadPool, lines, "cpp", { path: "sample.c" }, tokenDictionary
);
editor.setModel(model);
editor.focus();

// The document is interleaved, so it has more lines than the source it maps.
console.log(
  `inline disasm: descriptors=${lines.length}` +
    ` sourceLines=${program.length}` +
    ` documentLines=${editor.getNumLines()}`
);
