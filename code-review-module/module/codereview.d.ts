// java reflection of this file is located at
// code-review-module/src/main/java/org/sudu/experiments/CodeReview_d_ts.java

import { IDisposable } from '@sudu-ide/types';

import {
  Uri, IEvent, ITextModel,
  View, HasTheme, Theme, Focusable, TwoPanelDiff,
  EditorView, FileDiffViewController
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

export function newCodeReview(args: EditArgs): CodeReviewView
