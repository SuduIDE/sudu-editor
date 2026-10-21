# Work Summary Report


|                    |                                                                  |
| ------------------ | ---------------------------------------------------------------- |
| **Period**         | 2022-10-03 – 2026-09                                             |
| **Repo**           | [SuduIDE/sudu-editor](https://github.com/SuduIDE/sudu-editor)    |
| **Product target** | Portable editor + file/folder compare stack (Web + Desktop); consumed by Huawei Compare |


---

## 1. Executive summary

**Result:** sudu-editor is a **portable code editor and compare stack** written in **Java/Kotlin**, running identically on **Web (TeaVM → ESM)** and **Desktop (JVM + Angle/OpenGL ES)**. Beyond a general-purpose editor, it ships a production-shaped **file / folder / binary / remote (SSH) compare** surface, packaged for embedding as `@sudu-ide/editor`, `@sudu-ide/code-review`, `@sudu-ide/types`, and a Node **DiffEngine** (`filediff-node-module`).

**Performance result (folder scan):** vs a **Node** baseline on the same trees, the Java DiffEngine path is about **2.5×–3.7× faster** on representative projects; GraalVM native image keeps **much lower peak private memory** on extreme scans (see §2.2). These are **measured FS-scan benchmarks** in-repo (`performance.md`), not a single “vs Monaco UI” headline.

**Why it exists:** Investigate performance limits of a **single-codebase** portable editor (vs Monaco / VS Code–class hosts) and deliver an **embeddable compare/diff product** that host apps can drive over typed JS APIs and a Node FS/SSH engine.

**Status vs Huawei Compare:**

> **Consumed as the compare engine/UI.** Huawei Compare is the host product; sudu-editor supplies the embeddable editor/diff/code-review packages and Node DiffEngine. A separate Huawei Compare work-summary (integration versions, host wiring, product UX) is deferred until that repo/path is available — this report covers the **sudu-editor** lifetime deliverable.

**Who drove the outcome (by result, not commit count):**


| Outcome                                                                 | Primary owners                          |
| ----------------------------------------------------------------------- | --------------------------------------- |
| Core portable engine, graphics/WebGL, TeaVM path, product packaging     | Kirill Prazdnikov                       |
| Diff model, sync edit, folder/file compare UX evolution                 | Pavel Pertsev                           |
| Folder-diff host APIs, theme/selection APIs, npm packaging/CI           | Anatoly Nikitin                         |
| Early side-by-side diff chrome (middle line, scroll sync, line HL)      | Vladislav Kasimov, Sergey Loktev        |
| Smaller / later touches                                                 | Kirill Aleshin, Mikhail Protasov, others |


---

## 2. Overall results

### 2.1 Deliverables


| Artifact                 | Version          | What it is                                                                 |
| ------------------------ | ---------------- | -------------------------------------------------------------------------- |
| `@sudu-ide/editor`       | **0.0.128**      | Embeddable ESM editor + file/folder/remote/binary diff views               |
| `@sudu-ide/code-review`  | **0.0.128**      | Code-review oriented file-diff UI (WorkerPool, accept/reject merge model)  |
| `@sudu-ide/types`        | **0.0.128**      | Shared TypeScript contracts (models, themes, controllers)                  |
| `filediff-node-module`   | **0.0.1-beta**   | Node DiffEngine: folder/file sessions, exclude lists, encoding, SSH        |
| Web demos                | —                | `demo-edit-js`, `ai-demo-js`, webpack sample                               |
| Desktop demo             | —                | `demo-edit-jvm` (Angle / OpenGL ES)                                        |
| Release tags             | through **v0.0.128** | Continuous tag cadence; no formal CHANGELOG in-repo                     |


### 2.2 Performance results

Relative to a **Node** folder-scan baseline (`performance.md`; antivirus off, dry-run warm-up, GraalVM JDK 22):


| Workload                                      | Result                                                                 |
| --------------------------------------------- | ---------------------------------------------------------------------- |
| Mid-size tree (~12k files)                    | Java ~**2.5×** faster than Node                                        |
| Large tree (llvm-project scale)               | Java ~**3.7×** faster than Node                                        |
| Extreme scan (Chromium-scale, scan ×2)        | Graal native peak private memory **~400–600 MB** vs Java ~2.15 GB; Node ~1.1–1.3 GB but ~2× slower wall time |


These figures characterize **diff-engine / FS scan** cost, which dominates large folder compares. UI frame-time vs Monaco is out of scope for this table unless separately measured.

### 2.3 Capabilities that matter for host products (incl. Huawei Compare)

- **Embeddable editor:** `newEditor` / `newTextModel` ESM API (`embedding.md`).
- **Two-panel file & folder diff:** filters, refresh, root change, orphan copy/remove, exclude lists, next/prev navigation.
- **Compact view:** collapse unchanged regions; code-map / navigation in compact mode.
- **Sync points & merge UX:** draw/update on edit; accept/reject-oriented merge controls (code-review package).
- **Binary diff:** `BinaryDiffView` and remote binary path.
- **Remote / SSH compare:** DiffEngine + message `Channel` → `newRemoteFolderDiff` / `newRemoteFileDiff` / `newRemoteBinaryDiff` / `newRemoteEditor`.
- **Language / highlighting:** optional parsers; semantic highlighting API; encoding / code-page detection (incl. GBK).
- **Shared WorkerPool** for code-review and multi-view hosts.

### 2.4 Comparison to typical host-editor stacks


|                               | Monaco / VS Code–style host          | sudu-editor stack                                      |
| ----------------------------- | ------------------------------------ | ------------------------------------------------------ |
| **Codebase**                  | Large TS/C++ editor frameworks       | **Single Java/Kotlin core** → TeaVM web + JVM desktop  |
| **Rendering**                 | DOM / browser canvas stacks          | Custom **WebGL / Angle** graphics                      |
| **Folder/file compare**       | Host-specific extensions / SCM UI    | First-class **FolderDiff / FileDiff / BinaryDiff**     |
| **Remote FS**                 | Host FS / remote providers           | **DiffEngine** + SSH inputs + Channel-backed remote views |
| **Embedding**                 | Monaco AMD/ESM in webviews           | `@sudu-ide/editor` / `@sudu-ide/code-review` ESM        |
| **Performance note (scan)**   | Host FS tooling                      | Java DiffEngine **~2.5–3.7×** vs Node on cited trees   |


### 2.5 Project timeline


| Phase                    | When                | What landed                                                                 |
| ------------------------ | ------------------- | --------------------------------------------------------------------------- |
| Genesis                  | Oct 2022 – mid 2023 | Initial editor, graphics, web console; TeaVM web path                       |
| Diff chrome foundation   | mid–late 2023       | Side-by-side diff UI (middle line, scroll sync, line HL, line numbers)      |
| Folder compare product   | early–mid 2024      | FolderDiff model/windows, DiffEngine Node bindings, remote folder diff WIP  |
| Host API & packaging     | 2024 – early 2025   | JS folder-diff APIs, themes/selection, `@sudu-ide/editor` rename, CI/npm    |
| Compact / remote / SSH   | 2025                | Compact view, SSH roots & reconnect, large-file FS, exclude lists           |
| Code review & binary     | mid–late 2025       | MergeButtons redesign, `@sudu-ide/code-review` NPM, binary/remote binary    |
| Sync edit & polish       | late 2025 – 2026    | Sync edit + DiffModel refactors, semantic highlighting, packaging/CI (OIDC, Node 20); tag **v0.0.128** |


---

## 3. Technical implementation (brief)

```
Host app (browser / Electron / Node)
  ├─ @sudu-ide/editor | @sudu-ide/code-review   (TeaVM UI + workers)
  └─ filediff DiffEngine (Node) ──SSH/FS──► Channel ──► remote* views
```

- **Core:** `demo-edit` (editor + diff UI), `diff-model` (algorithms/merge), `parser*` (optional lex/parse), `graphics*` (WebGL/Angle).
- **Public surfaces:** `editor.d.ts` (`newEditor`, `newFileDiff`, `newFolderDiff`, `newRemote*`), `codereview.d.ts` (`newCodeReview`, WorkerPool), `diff.d.ts` (`createDiffEngine`, `startFolderDiff` / `startFileDiff`, SSH inputs).
- **Build:** local TeaVM fork + TeaVM-compatible ANTLR; Maven multi-module; npm publish from CI (incl. Huawei Cloud OBS webapp upload).

---

## 4. Individual contributions

Alias-merged commit totals (all refs, period above). **Commits ≠ credit**; use §1 outcome map as the primary attribution.


| Person            | Commits | Active                  | Focus                                                              |
| ----------------- | ------- | ----------------------- | ------------------------------------------------------------------ |
| Kirill Prazdnikov | 949     | 2022-10-03 – 2026-09-07 | Core engine, graphics, TeaVM, packaging, compare infrastructure    |
| Pavel Pertsev     | 542     | 2022-11-16 – 2026-08-03 | Diff model, sync edit, folder/file compare UX                      |
| Anatoly Nikitin   | 102     | 2023-06-16 – 2026-04-21 | Host APIs, folder-diff UX/API, themes, npm/CI packaging            |
| Vladislav Kasimov | 42      | 2023-05-24 – 2023-09-26 | Early side-by-side diff chrome                                     |
| Sergey Loktev     | 26      | 2023-06-01 – 2023-10-02 | Diff shaders / line-number rendering                               |
| Mikhail Protasov  | 15      | 2023-06-30 – 2025-06-02 | Supporting fixes / features                                        |
| Kirill Aleshin    | 8       | 2025-08-12 – 2025-10-21 | Later incremental contributions                                    |


### 4.1 Kirill Prazdnikov — core engine, graphics, and packaging

- Founded and drove the **portable editor architecture**: Java/Kotlin core, custom graphics, TeaVM web delivery, JVM desktop demos
- Owned **product packaging** of embeddable modules (`@sudu-ide/editor`, code-review, types) and ongoing release tagging through **v0.0.128**
- Built and evolved **compare infrastructure** (workers, DiffEngine Node path, FS jobs, SSH/large-file support, compact view / code-map threads)
- Hardened **WebGL runtime** concerns (including single-context UI work and Angle/desktop graphics)

### 4.2 Pavel Pertsev — diff model and compare UX

- Led long-running **DiffModel / sync-edit** work: apply/undo, empty-diff edge cases, model/controller refactors
- Drove **folder and file compare** behavior end-to-end (trees, remote folder diff, merge-button / accept-reject oriented flows)
- Iterated **UX polish** for scrolling, animations, and two-panel interaction that hosts rely on

### 4.3 Anatoly Nikitin — host APIs and delivery

- Shaped **folder-diff host APIs** used by embedding apps: selection events, filters/controllers, font-size, external dialogs/status bar hooks
- Improved **TreeView / folder-diff UX** (selection, hover, themes, keyboard navigation)
- Renamed/published the embeddable package as **`@sudu-ide/editor`**; later **npm CI** work (trusted publishing via OIDC, Node 20)

### 4.4 Vladislav Kasimov & Sergey Loktev — early diff chrome

- Established early **side-by-side diff presentation**: middle line, gap rendering, scroll synchronization, line highlighting
- Optimized **line-number / diff-range drawing** (including shader and allocator-based approaches)

### 4.5 Others

- **Mikhail Protasov**, **Kirill Aleshin**, and additional contributors shipped supporting fixes and smaller features across the lifetime.

---

## 5. Results

- Portable **Web + Desktop** editor from a single Java/Kotlin codebase (TeaVM + JVM)
- Production-shaped **folder / file / binary / remote (SSH)** compare stack with compact view, sync/merge, and code-review packaging
- Publishable NPM surface: **`@sudu-ide/editor` / `code-review` / `types` at 0.0.128**, plus Node DiffEngine
- Folder-scan evidence: Java DiffEngine **~2.5–3.7× vs Node**; Graal native **lower peak memory** on extreme trees
- Continuous release tags through **v0.0.128**; embed docs in `embedding.md`
- **Huawei Compare** consumes this stack as host product (detailed Compare report deferred)

---

## 6. References

- [SuduIDE/sudu-editor](https://github.com/SuduIDE/sudu-editor): `README.md`, `embedding.md`, `performance.md`
- Packages: `demo-edit-es-module/module/` (`@sudu-ide/editor`), `code-review-module/module/` (`@sudu-ide/code-review`), `common-ts-types/` (`@sudu-ide/types`), `filediff-node-module/module/`
- APIs: `editor.d.ts`, `codereview.d.ts`, `diff.d.ts`
- CI: `.github/workflows/build.yml` (build, npm publish, Huawei Cloud OBS webapp upload)
- Huawei Compare work summary: *to be added when the Compare source path/URL is provided*
