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
  setModel(modelL: ITextModel, modelR: ITextModel): void
}

export function newTextModel(text: string, language?: string, uri?: Uri): ITextModel

export function newEditor(args: EditArgs): EditorView

export function newCodeReview(args: EditArgs): CodeReviewView
