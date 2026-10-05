// java reflection of this file is located at
// code-review-module/src/main/java/org/sudu/experiments/CodeReview_d_ts.java

import { IDisposable } from '@sudu-ide/types';

import {
  Uri, IEvent, ITextModel,
  View, HasTheme, Theme, Focusable, TwoPanelDiff,
  IEditorView, FileDiffViewController
} from '@sudu-ide/types/frontend';

export * from '@sudu-ide/types';

export interface WorkerPool {
  getNumThreads(): number
}

export function newWorkerPool(workerUrl: string, numThreads: number): Promise<WorkerPool>;

export function loadFonts(codiconUrl: string): Promise<FontFace[]>;

export interface GlDebugApi {
  textureUsage(): string;
  loseContext(): void;
}

export let glDebugApi : GlDebugApi;

export interface EditArgs {
  containerId: string

  workers: WorkerPool

  theme?: Theme

  readonly?: boolean

  disableParser?: boolean
}

interface TextDocumentContentChangeEvent {
  // todo: replicate VSCode API event
}

interface IDiffSizeChangeCallback {
  (
    numLines: number,
    lineHeight: number,
    cssLineHeight: number
  ): void
}

export interface IFileDiffView extends View, HasTheme, Focusable, TwoPanelDiff {
  getLeftModel(): ITextModel

  getRightModel(): ITextModel

  getController(): FileDiffViewController;

  onControllerUpdate: IEvent<FileDiffViewController>

  setDiffSizeListener(cb: IDiffSizeChangeCallback): void
}

export interface CodeReviewView extends IFileDiffView, IDisposable {
  setModel(model: ITextDiffModel): void
}

export function newTextModel(
  workers: WorkerPool,
  text: string, language?: string, uri?: Uri
): ITextModel

export interface LinesInfo {
  linesAdded : number;
  linesRemoved : number;
  linesModified : number;
}

export interface ApplyChangeInfo {
  oldFrom: number;
  oldTo: number;
  newFrom: number;
  newTo: number;
  isAccepted: boolean;
}

export interface ITextDiffModel {
  getLeftModel(): ITextModel;
  getRightModel(): ITextModel;

  getLinesInfo(): Promise<LinesInfo>;

  setApplyRejectListener(listener: (info: ApplyChangeInfo) => void): void;
  enableSyncEdit(flag: boolean): void;
}

export function newDiffModel(
    workers: WorkerPool,
    text1: string, text2: string,
    uri1?: Uri, uri2?: Uri,
    language?: string,
): ITextDiffModel

export function newEditor(args: EditArgs): EditorView

/**
 * Observer of the editor vertical scroll.
 *
 * The editor renders into a canvas and does its own virtual scrolling, so it
 * has no DOM scrollbar to listen to: this callback is the only way to observe
 * vertical scroll. It fires on mouse wheel, scrollbar drag, keyboard paging,
 * caret reveal and {@link EditorView.setVScrollPos}.
 *
 * The reported position, and every other size reported by the editor below,
 * is in *device pixels*: divide by {@link EditorView.getDevicePixelRatio} to
 * get CSS pixels, or multiply a CSS pixel value coming from the DOM to convert
 * it back into device pixels.
 */
export interface EditorScrollListener {
  (vScrollPos: number): void
}

export interface EditorView extends IEditorView, IDisposable {
  setModel(model: ITextModel): void

  /**
   * Registers a vertical scroll observer, replacing any previous one.
   * Pass `null` to remove it.
   */
  setVScrollListener(listener: EditorScrollListener | null): void

  /**
   * Registers an observer notified with the caret's line whenever the caret
   * moves to a different line: arrow keys, Home/End, Page Up/Down, mouse click
   * and drag, goto definition/declaration and {@link setPosition}.
   *
   * Column-only moves within a single line are not reported. The line is
   * 1-based, matching {@link IEditorView.getPosition}.
   *
   * Pass `null` to remove the observer.
   */
  setCaretListener(listener: ((lineNumber: number) => void) | null): void

  /** Current vertical scroll position, in device pixels. */
  getVScrollPos(): number

  /**
   * Sets the vertical scroll position, in device pixels. The value is clamped
   * to `[0, getMaxVScrollPos()]` and, when it actually changes, notifies the
   * listener installed by {@link setVScrollListener} and repaints the canvas.
   */
  setVScrollPos(vScrollPos: number): void

  /** Height of a single line, in device pixels. */
  getLineHeight(): number

  /** Number of whole lines that fit into the editor viewport. */
  getViewportRows(): number

  /** Document line count. */
  getNumLines(): number

  /** Largest value accepted by {@link setVScrollPos}, in device pixels. */
  getMaxVScrollPos(): number

  /** Editor height, in device pixels. */
  getEditorHeight(): number

  /** `window.devicePixelRatio`, to convert device pixels to CSS pixels. */
  getDevicePixelRatio(): number

  /**
   * Font size the editor rasterizes text at, in **device** pixels, like every
   * other pixel value this API reports. Divide by {@link getDevicePixelRatio}
   * to get the CSS pixels a DOM `font-size` needs.
   *
   * This is the size the editor's font was created with, so it is not rounded
   * and dividing it back by the device pixel ratio returns the exact CSS pixel
   * size, including on fractional ratios such as 1.25.
   *
   * `0` before the editor has resolved its font.
   */
  getFontSize(): number

  /**
   * Font family the editor renders text with, e.g. `"Consolas"`.
   *
   * `null` before the editor has resolved its font.
   */
  getFontFamily(): string | null
}

export function newCodeReview(args: EditArgs): CodeReviewView
