import * as editorApi from "../src/codereview.js";

/**
 * Inline-disassembly sample.
 *
 * `newTextModelWithInline` builds a *mapped* document: an array of line
 * descriptors where each descriptor either
 *
 *   { lineNumber: N, text: "..." }  -> a source line carrying ordinal N, or
 *   { text: "..." }                 -> an attached line (disassembly), unnumbered.
 *
 * The resulting document is always read-only, for every line. Editing keys,
 * paste, cut and undo are all blocked; the model is meant to display, not edit.
 *
 * Below, a small C function is interleaved with the (illustrative) instructions
 * a compiler might emit for each source line, the way an inline-disassembly view
 * renders it.
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

/** Flattens the program into the descriptor array the API expects. */
function buildLines(program) {
  const lines = [];
  program.forEach((entry, index) => {
    lines.push({ lineNumber: index + 1, text: entry.src });
    for (const asm of entry.asm) lines.push({ text: asm });
  });
  return lines;
}

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const editor = editorApi.newEditor({
  containerId: "editor",
  workers: threadPool,
});

const lines = buildLines(program);
const model = editorApi.newTextModelWithInline(
  threadPool, lines, "cpp", { path: "sample.c" }
);
editor.setModel(model);
editor.focus();

// The document is interleaved, so it has more lines than the source it maps.
console.log(
  `inline disasm: descriptors=${lines.length}` +
    ` sourceLines=${program.length}` +
    ` documentLines=${editor.getNumLines()}`
);
