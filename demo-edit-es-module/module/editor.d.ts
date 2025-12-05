// java reflection of this file is located at
// demo-edit-es-module/src/main/java/org/sudu/experiments/EditJsApi.java

import {
  Uri, IEvent, ITextModel,
  View, HasTheme, Theme, Focusable, TwoPanelDiff,
  ViewController, EditorViewController, FileDiffViewController,
  IEditorView, EditorView,
  DiffType
} from '@sudu-ide/types/frontend';

import { IDisposable } from '@sudu-ide/types';
import { Channel } from './common';

export { setLogLevel, setLogOutput, LogLevel } from '@sudu-ide/types';

export { Channel, Message, newRemoteChannelTest, ChannelTestApi } from './common';

export interface EditArgs {
  containerId: string

  // default value for workerUrl is "worker.js"
  workerUrl?: string | URL

  theme?: Theme

  readonly?: boolean

  disableParser?: boolean

  // number of worker threads for parsing and resolve
  // default: 2
  numThreads?: number

  codiconUrl?: string
}

export interface IFileDiffView extends View, HasTheme, Focusable, TwoPanelDiff {
  getLeftModel(): ITextModel

  getRightModel(): ITextModel

  getController(): FileDiffViewController;

  onControllerUpdate: IEvent<FileDiffViewController>
}

export interface IBinaryDiffView extends View, HasTheme, Focusable, TwoPanelDiff {}

export interface FileDiffView extends IFileDiffView, IDisposable {
  setModel(modelL: ITextModel, modelR: ITextModel): void
}

export interface IFolderDiffView extends View, HasTheme, TwoPanelDiff, Focusable {
  isReady(): boolean

  onReadyChanged: IEvent<boolean>

  getController(): FolderDiffViewController | FileDiffViewController | EditorViewController;

  onControllerUpdate: IEvent<FolderDiffViewController | FileDiffViewController | EditorViewController>
}

export interface FolderDiffView extends IFolderDiffView, IDisposable {
}

export interface FolderDiffSelection {
  // relativePath does not include root folder.
  // For root folder relativePath is empty
  relativePath: string
  isLeft: boolean
  isFolder: boolean

  // whether selected element exists only in current diff side
  isOrphan: boolean
}

export interface FolderDiffViewController extends ViewController {
  getViewType(): 'folderDiff'

  getSelection(): FolderDiffSelection | undefined

  getDiffFilter(): DiffType[]

  applyDiffFilter(filters: DiffType[]): void
}

export interface DiffExternalFileOpener {
  // All paths are absolute
  openFileDiff(leftPath: string, rightPath: string): void

  openEditor(path: string, isLeft: boolean): void
}

export interface RemoteFolderDiffView extends IFolderDiffView, IDisposable {
  getState(): any

  applyState(state: any): void

  setExternalFileOpener(opener: DiffExternalFileOpener | null): void
}

export interface RemoteFileDiffView extends IFileDiffView, IDisposable {
  getState(): any

  applyState(state: any): void
}

export interface RemoteBinaryDiffView extends IBinaryDiffView, IDisposable {
  getState(): any

  applyState(state: any): void
}

export interface RemoteEditorView extends IEditorView, IDisposable {
  getState(): any

  applyState(state: any): void
}

export function newTextModel(text: string, language?: string, uri?: Uri): ITextModel

export function newEditor(args: EditArgs): Promise<EditorView>

export function newFileDiff(args: EditArgs): Promise<FileDiffView>

export function newFolderDiff(args: EditArgs): Promise<FolderDiffView>

export function newRemoteFolderDiff(args: EditArgs, channel: Channel): Promise<RemoteFolderDiffView>

export function newRemoteFileDiff(args: EditArgs, channel: Channel): Promise<RemoteFileDiffView>

export function newRemoteBinaryDiff(args: EditArgs, channel: Channel): Promise<RemoteBinaryDiffView>

export function newRemoteEditor(args: EditArgs, channel: Channel): Promise<RemoteEditorView>

