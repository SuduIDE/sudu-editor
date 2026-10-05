import * as editorApi from "../src/codereview.js";

/**
 * Editor on the left, a plain DOM table of per-line metrics on the right,
 * synchronized on the vertical axis in both directions.
 *
 * The editor renders into a canvas and does its own virtual scrolling, so it
 * has no DOM scrollbar to hook. The sync relies on the vertical scroll API of
 * this module (`setVScrollListener` / `getVScrollPos` / `setVScrollPos`), whose
 * values are in *device* pixels, while the DOM works in CSS pixels, so
 * everything crossing that boundary is divided by the device pixel ratio.
 */

const SAMPLE = [
  "// Per-line metrics table, synchronized with the editor vertical scroll.",
  "// Every table row corresponds to exactly one editor line.",
  "",
  "class Metrics {",
  "  constructor(workers) {",
  "    this.workers = workers;",
  "    this.samples = new Map();",
  "  }",
  "",
  "  // Collects one record per source line.",
  "  collect(text) {",
  "    const records = [];",
  "    const lines = text.split('\\n');",
  "    for (let i = 0; i < lines.length; i++) {",
  "      const line = lines[i];",
  "      if (line.trim() === '') continue;",
  "      records.push({",
  "        line: i,",
  "        length: line.length,",
  "        indent: countIndent(line),",
  "        score: scoreLine(line),",
  "      });",
  "    }",
  "    return records;",
  "  }",
  "",
  "  async summarize(records) {",
  "    let total = 0;",
  "    for (const record of records) {",
  "      if (record.score > 10) {",
  "        total += record.score;",
  "        this.samples.set(record.line, record);",
  "      }",
  "    }",
  "    return total;",
  "  }",
  "}",
  "",
  "function countIndent(line) {",
  "  let n = 0;",
  "  for (const ch of line) {",
  "    if (ch === ' ') n += 1;",
  "    else if (ch === '\\t') n += 4;",
  "    else break;",
  "  }",
  "  return n;",
  "}",
  "",
  "const BRANCH = /\\b(if|else|for|while|case|catch)\\b/g;",
  "",
  "function scoreLine(line) {",
  "  const trimmed = line.trim();",
  "  if (trimmed === '' || trimmed.startsWith('//')) return 0;",
  "  const operators = (trimmed.match(/[+\\-*/%=!<>|&?]+/g) || []).length;",
  "  const branches = (trimmed.match(BRANCH) || []).length;",
  "  const nesting = countIndent(line) / 2;",
  "  return 1 + operators + branches * 2 + Math.floor(nesting);",
  "}",
  "",
  "/* Scroll the table and the editor follows; scroll the editor and the table",
  "   follows. Clicking a row jumps the editor to that line, and the row under",
  "   the caret stays highlighted. */",
].join("\n");

const IDENTIFIER = /[A-Za-z_$][\w$]*/g;
const OPERATOR = /[+\-*/%=!<>|&?]+/g;
const BRANCH_KEYWORD = /\b(?:if|else|for|while|case|catch)\b/g;

/** Column widths in percent, shared by the header and the body table. */
const COLUMNS = [8, 7, 8, 7, 7, 8, 11];

/** Derives one metrics record per source line. */
function analyze(text) {
  return text.split("\n").map((line, index) => {
    const trimmed = line.trim();

    let indent = 0;
    for (const ch of line) {
      if (ch === " ") indent += 1;
      else if (ch === "\t") indent += 4;
      else break;
    }

    const kind =
      trimmed === ""
        ? "blank"
        : trimmed.startsWith("//") ||
          trimmed.startsWith("/*") ||
          trimmed.startsWith("*")
          ? "comment"
          : "code";

    const symbols = trimmed === "" ? 0 : (trimmed.match(IDENTIFIER) || []).length;
    const ops = trimmed === "" ? 0 : (trimmed.match(OPERATOR) || []).length;
    const branches = trimmed === "" ? 0 : (trimmed.match(BRANCH_KEYWORD) || []).length;

    // crude per-line complexity: operators, 2 per branch, plus half the indent
    const score = kind === "code" ? 1 + ops + branches * 2 + Math.floor(indent / 2) : 0;

    return {
      line: index + 1,
      length: line.length,
      indent,
      symbols,
      ops,
      score,
      kind,
      preview: line.replace(/\t/g, "    "),
    };
  });
}

// ---------------------------------------------------------------------------
// bootstrap
// ---------------------------------------------------------------------------

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const splitRoot = document.getElementById("split-root");
const panes = document.getElementById("panes");
const tableHead = document.getElementById("table-head");
const tableContent = document.querySelector("#table-scroll table");
const tableScroll = document.getElementById("table-scroll");
const tableBody = document.getElementById("metrics-body");
const followButton = document.getElementById("toggle-follow");

const editor = editorApi.newEditor({
  containerId: "editor-pane",
  workers: threadPool,
});

const model = editorApi.newTextModel(threadPool, SAMPLE, "js", { path: "Sample.js" });
editor.setModel(model);
editor.focus();

// ---------------------------------------------------------------------------
// geometry
// ---------------------------------------------------------------------------

/**
 * Everything the editor hands us is in *device* pixels; the DOM only understands
 * CSS pixels, so anything used for a DOM value is divided by `dpr`. All fields
 * stay 0 until the editor has laid itself out.
 */
const geom = {
  dpr: 0,             // window.devicePixelRatio, unitless (e.g. 2), not a length
  lineHeight: 0,      // DEVICE px: editor height of one line
  editorHeight: 0,    // DEVICE px: editor viewport height
  maxVScrollPos: 0,   // DEVICE px: largest value setVScrollPos accepts; 0 if the
                      // document already fits, i.e. there is nothing to scroll
  numLines: 0,        // unitless line count, no pixel unit applies
  rowHeight: 0,       // CSS px: lineHeight / dpr; height of one table row, and
                      // what makes table rows line up with editor lines
};

/**
 * Reads the metrics the table needs. Returns false while the editor has not
 * produced them yet - it needs a layout pass and resolved font metrics - in
 * which case there is nothing meaningful to lay out either.
 */
function readGeometry() {
  geom.dpr = editor.getDevicePixelRatio();
  geom.lineHeight = editor.getLineHeight();
  geom.editorHeight = editor.getEditorHeight();

  if (!(geom.dpr > 0) || !(geom.lineHeight > 0) || !(geom.editorHeight > 0)) {
    return false;
  }

  geom.rowHeight = geom.lineHeight / geom.dpr;
  geom.numLines = editor.getNumLines();
  geom.maxVScrollPos = editor.getMaxVScrollPos();

  // a row has to be tall enough for its text, so cap the font at a fraction of
  // the row and pin the line height to the row height
  const fontSize = Math.max(Math.min(geom.rowHeight * 0.8, 14), 8);
  for (const table of [tableHead, tableContent]) {
    table.style.fontSize = `${fontSize}px`;
    table.style.lineHeight = `${geom.rowHeight}px`;
  }
  tableHead.style.height = `${geom.rowHeight}px`;

  return true;
}

/**
 * Filler rows, so the table scrolls exactly as far as the editor does.
 *
 * The editor can be scrolled slightly past the last line, because its virtual
 * height covers that many rows plus EditorConst.BLANK_LINES extra ones. Rather
 * than repeating that constant here, recover the whole virtual row count from
 * the scroll range the editor reports, and take the remainder over the real
 * lines:
 *
 *   totalRows = (getMaxVScrollPos() + getEditorHeight()) / getLineHeight()
 *              = numLines + BLANK_LINES
 *
 * Deriving it keeps the two scrollbars from silently drifting apart if the Java
 * side ever changes the constant.
 */
function trailingRowCount() {
  // a max scroll of 0 means the document already fits, so there is no trailing
  // range to mirror and the table must stay unscrollable too
  if (geom.maxVScrollPos <= 0) return 0;

  const totalRows = Math.round(
    (geom.maxVScrollPos + geom.editorHeight) / geom.lineHeight
  );
  return Math.max(0, totalRows - geom.numLines);
}

// ---------------------------------------------------------------------------
// table rendering
// ---------------------------------------------------------------------------

function renderColumns() {
  const used = COLUMNS.reduce((a, b) => a + b, 0);
  const widths = [...COLUMNS, 100 - used];

  for (const id of ["head-cols", "body-cols"]) {
    document.getElementById(id).replaceChildren(
      ...widths.map((w) => {
        const col = document.createElement("col");
        col.style.width = `${w}%`;
        return col;
      })
    );
  }
}

function renderRows(records) {
  const fragment = document.createDocumentFragment();

  for (const r of records) {
    const tr = document.createElement("tr");
    tr.className = `kind-${r.kind}`;
    // assignment coerces to a string on its own; dataset is DOMString-typed
    tr.dataset.line = r.line;
    tr.style.height = `${geom.rowHeight}px`;

    const cells = [
      ["num", r.line],
      ["num", r.length],
      ["num", r.indent],
      ["num", r.symbols],
      ["num", r.ops],
      ["num", r.score],
      ["kind", r.kind],
      ["preview", r.preview || " "],
    ];

    for (const [className, value] of cells) {
      const td = document.createElement("td");
      td.className = className;
      td.textContent = String(value);
      tr.appendChild(td);
    }

    fragment.appendChild(tr);
  }

  for (let i = 0; i < trailingRowCount(); i++) {
    const tr = document.createElement("tr");
    tr.className = "filler";
    tr.style.height = `${geom.rowHeight}px`;
    const td = document.createElement("td");
    td.colSpan = COLUMNS.length + 1;
    td.textContent = "~";
    tr.appendChild(td);
    fragment.appendChild(tr);
  }

  tableBody.replaceChildren(fragment);
}

function recompute() {
  if (!readGeometry()) return;
  renderRows(analyze(editor.getText()));
  updateCurrentRow();
}

// ---------------------------------------------------------------------------
// caret highlight
// ---------------------------------------------------------------------------

let currentRow = null;

/**
 * Reads the caret line and applies it.
 *
 * Only needed where the editor has not told us the line itself: after a
 * re-render, and when the caret line could not be clamped. Caret movement
 * arrives through `setCaretListener` instead, which is why there is no polling
 * here - a poll cannot be both instant and cheap.
 */
function updateCurrentRow() {
  const pos = editor.getPosition();
  const line = pos && pos.lineNumber > 0 ? pos.lineNumber : null;
  if (!line) return clearHighlight();
  highlightRow(line);
}

/**
 * Moves the caret highlight to the given row, if it is not already there.
 * The editor reports the caret line, which is exactly the `data-line` value
 * the rows carry, so no position lookup is needed.
 */
function highlightRow(lineNumber) {
  const row = tableBody.querySelector(`tr[data-line="${lineNumber}"]`);
  if (!row || row === currentRow) return;
  if (currentRow) currentRow.classList.remove("current");
  row.classList.add("current");
  currentRow = row;
}

/** Drops the highlight, for when the caret line has no row. */
function clearHighlight() {
  if (!currentRow) return;
  currentRow.classList.remove("current");
  currentRow = null;
}

// ---------------------------------------------------------------------------
// scroll sync
// ---------------------------------------------------------------------------

/**
 * The scroll position this page last pushed into the editor, so the resulting
 * notification can be recognised as an echo instead of a user scroll.
 *
 * `setVScrollPos` notifies synchronously and only when the clamped position
 * actually changes, so the marker is cleared right after the call and can
 * never be left stale.
 */
let pendingEcho = -1;

let followEditor = true;

function onEditorScroll(vScrollPosDevicePx) {
  const target = vScrollPosDevicePx / geom.dpr;
  if (Math.abs(tableScroll.scrollTop - target) >= 0.5) {
    tableScroll.scrollTop = target;
  }
}

function wireScrollSync() {
  editor.setVScrollListener((vScrollPos) => {
    if (vScrollPos === pendingEcho) return; // our own scroll coming back
    if (followEditor) onEditorScroll(vScrollPos);
  });

  // Caret movement, synchronous with the editor's own handling of the key or
  // mouse event, so the highlight keeps up with the caret exactly.
  editor.setCaretListener(highlightRow);

  tableScroll.addEventListener("scroll", () => {
    const devicePos = Math.round(tableScroll.scrollTop * geom.dpr);
    pendingEcho = devicePos;
    editor.setVScrollPos(devicePos);
    pendingEcho = -1;
  });

  // Clicking a row only moves the caret; it must not scroll. The editor keeps
  // the caret visible by itself, scrolling the minimum needed, so the line stays
  // where it was instead of jumping to the middle of the viewport.
  //
  // `setPosition` also moves the caret, so setCaretListener already highlights
  // the row that was clicked - no explicit update needed here.
  tableBody.addEventListener("click", (event) => {
    const row = event.target.closest("tr[data-line]");
    if (!row) return;

    editor.setPosition({ lineNumber: Number(row.dataset.line), column: 1 });
    editor.focus();
  });

  followButton.addEventListener("click", () => {
    followEditor = !followEditor;
    followButton.textContent = `follow editor: ${followEditor ? "on" : "off"}`;
  });

  document.getElementById("recompute").addEventListener("click", recompute);
  model.setEditListener(recompute);

  window.addEventListener("resize", () => {
    const previousRowHeight = geom.rowHeight;
    if (!readGeometry()) return;
    if (geom.rowHeight !== previousRowHeight) recompute();
    if (followEditor) onEditorScroll(editor.getVScrollPos());
  });
}

// ---------------------------------------------------------------------------
// draggable splitter
// ---------------------------------------------------------------------------

const splitter = document.getElementById("splitter");
let dragging = false;

// One custom property drives the editor width in both the header row and the
// pane row, so the column labels stay aligned with the table columns.
splitter.addEventListener("pointerdown", (event) => {
  dragging = true;
  splitter.setPointerCapture(event.pointerId);
});

splitter.addEventListener("pointermove", (event) => {
  if (!dragging) return;
  const rect = panes.getBoundingClientRect();
  const ratio = (event.clientX - rect.left) / rect.width;
  const clamped = Math.min(Math.max(ratio, 0.15), 0.85);
  splitRoot.style.setProperty("--editor-width", `${clamped * 100}%`);
});

splitter.addEventListener("pointerup", (event) => {
  dragging = false;
  splitter.releasePointerCapture(event.pointerId);
  readGeometry();
});

// ---------------------------------------------------------------------------
// start
// ---------------------------------------------------------------------------

/**
 * Draws the table and wires the sync, as soon as the editor reports usable
 * metrics. Returns false while they are still unavailable.
 */
function start() {
  if (!readGeometry()) return false;

  renderColumns();
  renderRows(analyze(editor.getText()));
  wireScrollSync();
  updateCurrentRow();

  console.log(
    `editor+table: rows=${tableBody.querySelectorAll("tr[data-line]").length}` +
      ` rowHeight=${geom.rowHeight}css dpr=${geom.dpr} numLines=${geom.numLines}` +
      ` filler=${trailingRowCount()}`
  );
  return true;
}

// exposed so the sample can be driven and inspected from the console
Object.assign(window, { editor, geom });

// The editor needs a layout pass and its font metrics before it can report a
// line height, so wait for real values rather than inventing one.
if (!start()) {
  const waitForMetrics = () => {
    if (!start()) requestAnimationFrame(waitForMetrics);
  };
  requestAnimationFrame(waitForMetrics);
}