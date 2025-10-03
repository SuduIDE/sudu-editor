// java reflection of this file is located at
// code-review-module/src/main/java/org/sudu/experiments/CodeReview_d_ts.java

import {IDisposable} from "./common";

export * from './common';

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

interface Uri {
  scheme?: string,
  authority?: string,
  path?: string
}

interface IEvent<T> {
  /**
   * Registers disposable event callback
   *
   * When event is fired, a new value V will be delivered to all listeners that were registered at the moment when the value V processing started.
   * It means that if listener A disposes listener B during processing of value V, listener B will still receive value V.
   * @param listener callback to be called when event is fired
   */
  (listener: (e: T) => any): IDisposable;
}

/**
 * An event describing that an editor has had its model reset (i.e. `editor.setModel()`).
 */
export interface IModelChangedEvent {
  /**
   * The `uri` of the previous model or null.
   */
  readonly oldModelUrl: Uri | null;
  /**
   * The `uri` of the new model or null.
   */
  readonly newModelUrl: Uri | null;
}

interface TextDocumentContentChangeEvent {
  // todo: replicate VSCode API event
}

export interface ITextModel extends IDisposable {
  language?: string
  uri?: Uri

  getOffsetAt(position: IPosition): number

  getPositionAt(offset: number): IPosition

  getText(): string

  setEditListener(listener: (m: ITextModel) => void): void

  setText(newText: string, fireEvent: boolean): void

  // todo: pass through changes from VSCode event
  // applyChanges(changes: TextDocumentContentChangeEvent, undoOrRedoOrUndefined, fireEvent: boolean): void
}

type ProviderValue<T> = T | undefined | null;
type ProviderResult<T> = ProviderValue<T> | Promise<ProviderValue<T>>;

// Monaco like IPosition line and column starts with 1:
//      1st line has number 1,
//      first char column is 1, 1st glyph is between column 1 and column 2
interface IPosition {
  column: number,
  lineNumber: number
}

// numeration starts from 1
interface IRange {
  endColumn: number,
  endLineNumber: number,
  startColumn: number,
  startLineNumber: number
}

interface ICancellationToken {
  isCancellationRequested: boolean
}

interface ILocation {
  range: IRange,
  uri: Uri
}

export const enum DocumentHighlightKind {
  Text = 0,
  Read = 1,
  Write = 2
}

interface IDocumentHighlight {
  range: IRange,
  kind?: DocumentHighlightKind
}

interface IDefinitionProvider {
  provideDefinition(
    model: ITextModel,
    position: IPosition,
    token: ICancellationToken
  ): ProviderResult<ILocation[]>
}

interface IDeclarationProvider {
  provideDeclaration(
    model: ITextModel,
    position: IPosition,
    token: ICancellationToken
  ): ProviderResult<ILocation[]>
}

interface IReferenceProvider {
  provideReferences(
    model: ITextModel,
    position: IPosition,
    context: { includeDeclaration: boolean },
    token: ICancellationToken
  ): ProviderResult<ILocation[]>
}

interface IDocumentHighlightProvider {
  provideDocumentHighlights(
    model: ITextModel,
    position: IPosition,
    token: ICancellationToken
  ): ProviderResult<IDocumentHighlight[]>
}

type SelectionOrPosition = IRange | IPosition;

interface ICodeEditorOpener {
  openCodeEditor(
    source: IEditorView,
    resource: Uri,
    selectionOrPosition?: SelectionOrPosition
  ): boolean | Promise<boolean>;
}

interface ILanguageFilter {
  language?: string
  scheme?: string
}

type LanguageSelector = string | ILanguageFilter | Array<string | ILanguageFilter>;

// function apply(filter:LanguageSelector, model: ITextModel) : boolean {
//    if (Array.isArray(filter)) foreach...
//    else return apply(filter)
// }

// function apply(filter:LanguageFilter, model: ITextModel) : boolean {
//     if (filter.language && model.language && filter.language !== model.language) return false;
//     if (filter.scheme && model.uri.scheme && filter.scheme !== model.uri.scheme) return false;
//     return true;
// }

export const enum ThemeColor {
  // tree view
  TreeViewBackground = 0,
  TreeViewForeground = 1,
  SelectedItemBackground = 2,
  SelectedItemForeground = 3,
  HoveredItemBackground = 4,
  InactiveSelectionBackground = 5,

  AddedResourceForeground = 6,
  DeletedResourceForeground = 7,
  ModifiedResourceForeground = 8,
  IgnoredResourceForeground = 22,

  // window title
  PanelHeaderBackground = 9,
  PanelHeaderForeground = 10,

  // editor
  EditorBackground = 11,
  EditorForeground = 12,
  CurrentLineBorder = 13,
  CurrentLineBackground = 14,

  // editor diff
  DeletedRegionBackground = 15,
  DeletedTextBackground = 16,
  InsertedRegionBackground = 17,
  InsertedTextBackground = 18,
  LineNumberForeground = 19,
  ActiveLineNumberForeground = 20,
  LineNumberActiveForeground = 21
}

export type BaseTheme = 'dark' | 'light' | 'darcula';

export type Font = {
  size: number,
  family: string,
  weight: number
}

type WithFonts = {
  uiFont: Font,
  editorFont: Font
}

export type Theme = {
  [color in ThemeColor]?: string;
} & {
  baseTheme: BaseTheme;
} & WithFonts | BaseTheme;

export interface HasTheme {
  setTheme(theme: Theme): void
}

export interface Focusable {
  focus(): void
}

export interface ExternalMessageBar {
  setStatusBarMessage(text: string | null): void
  setToolBarMessage(text: string | null): void
}

// see org.sudu.experiments.esm.JsContextMenuProvider
export const enum ContextMenuActions {
  cut = "Cut",
  copy = "Copy",
  paste = "Paste",
  alignWith = "AlignWith",
  removeAlignment = "RemoveAlignment",
}

export interface ContextMenuProvider {
  showContextMenu(actions: ContextMenuActions[]): void;
}

export interface NotificationsProvider {
    error(message: string): void;
    warn(message: string): void;
    info(message: string): void;
}

export interface View {
  disconnectFromDom(): void

  reconnectToDom(containerId?: string): void

  getController(): ViewController;

  onControllerUpdate: IEvent<ViewController>

  setExternalMessageBar(statusBar: ExternalMessageBar): void

  setExternalContextMenuProvider(p: ContextMenuProvider): void;

  setNotificationsProvider(provider: NotificationsProvider): void

  executeMenuAction(action: ContextMenuActions): void;
}

export interface TwoPanelDiff {
  setReadonly(leftReadonly: boolean, rightReadonly: boolean): void
}

export interface IEditorView extends View, HasTheme, Focusable {
  setText(text: string): void,

  getText(): string,

  setPosition(position: IPosition): void

  getPosition(): IPosition

  getModel(): ITextModel

  registerDefinitionProvider(languageSelector: LanguageSelector, provider: IDefinitionProvider): IDisposable

  registerDeclarationProvider(languageSelector: LanguageSelector, provider: IDeclarationProvider): IDisposable

  registerReferenceProvider(languageSelector: LanguageSelector, provider: IReferenceProvider): IDisposable

  registerDocumentHighlightProvider(languageSelector: LanguageSelector, provider: IDocumentHighlightProvider): IDisposable

  registerEditorOpener(opener: ICodeEditorOpener): IDisposable

  revealLineInCenter(line: number): void

  revealLine(line: number): void

  revealPosition(position: IPosition): void

  getController(): EditorViewController;

  onControllerUpdate: IEvent<EditorViewController>

  setReadonly(flag: boolean): void
}

export interface EditorView extends IEditorView, IDisposable {
  setModel(model: ITextModel): void

  onDidChangeModel: IEvent<IModelChangedEvent>
}

export interface IFileDiffView extends View, HasTheme, Focusable, TwoPanelDiff {
  getLeftModel(): ITextModel

  getRightModel(): ITextModel

  getController(): FileDiffViewController;

  onControllerUpdate: IEvent<FileDiffViewController>
}

export interface CodeReviewView extends IFileDiffView, IDisposable {
  setModel(modelL: ITextModel, modelR: ITextModel): void
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

export interface ViewController {
  getViewType(): 'folderDiff' | 'fileDiff' | 'editor'

  canNavigateUp(): boolean

  navigateUp(): void

  canNavigateDown(): boolean

  navigateDown(): void

  refresh(): void
}

export const enum DiffType {
  Same = 0, Added = 1, Deleted = 2, Modified = 3
}

export interface FolderDiffViewController extends ViewController {
  getViewType(): 'folderDiff'

  getSelection(): FolderDiffSelection | undefined

  getDiffFilter(): DiffType[]

  applyDiffFilter(filters: DiffType[]): void
}

export interface FileDiffViewController extends ViewController {
  setCompactView(compact: boolean): void;

  getViewType(): 'fileDiff'
}

export interface EditorViewController extends ViewController {
  getViewType(): 'editor'

  getSelection(): undefined
}

export function newTextModel(text: string, language?: string, uri?: Uri): ITextModel

export function newEditor(args: EditArgs): EditorView

export function newCodeReview(args: EditArgs): CodeReviewView
