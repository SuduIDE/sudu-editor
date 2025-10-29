// java reflection of this file is located at
// demo-edit-es-module/src/main/java/org/sudu/experiments/EditJsApi.java

import {Channel, IDisposable} from "./common";

export {
  Channel, Message, setLogLevel, setLogOutput, newRemoteChannelTest, LogLevel, ChannelTestApi, IDisposable
} from './common';

export interface WorkerPool {
  getNumThreads(): number
}

export function newWorkerPool(workerUrl: string, numThreads: number): ITextModel

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

export enum _SemanticTokenType {
  Namespace = 'namespace', // For identifiers that declare or reference a namespace, module, or package.
  Class = 'class', // For identifiers that declare or reference a class type.
  Enum = 'enum', // For identifiers that declare or reference an enumeration type.
  Interface = 'interface', // For identifiers that declare or reference an interface type.
  Struct = 'struct', // For identifiers that declare or reference a struct type.
  TypeParameter = 'typeParameter', // For identifiers that declare or reference a type parameter.
  Type = 'type', // For identifiers that declare or reference a type that is not covered above.
  Parameter = 'parameter', // For identifiers that declare or reference a function or method parameters.
  Variable = 'variable', // For identifiers that declare or reference a local or global variable.
  Property = 'property', // For identifiers that declare or reference a member property, member field, or member variable.
  EnumMember = 'enumMember', // For identifiers that declare or reference an enumeration property, constant, or member.
  Decorator = 'decorator', // For identifiers that declare or reference decorators and annotations.
  Event = 'event', // For identifiers that declare an event property.
  Function = 'function', // For identifiers that declare a function.
  Method = 'method', // For identifiers that declare a member function or method.
  Macro = 'macro', // For identifiers that declare a macro.
  Label = 'label', // For identifiers that declare a label.
  Comment = 'comment', // For tokens that represent a comment.
  String = 'string', // For tokens that represent a string literal.
  Keyword = 'keyword', // For tokens that represent a language keyword.
  Number = 'number', // For tokens that represent a number literal.
  Regexp = 'regexp', // For tokens that represent a regular expression literal.
  Operator = 'operator', // For tokens that represent an operator.
}

export enum _SemanticTokenModifiers {
  Declaration = 'declaration', // For declarations of symbols.
  Definition = 'definition', // For definitions of symbols, for example, in header files.
  Readonly = 'readonly', // For readonly variables and member fields (constants).
  Static = 'static', // For class members (static members).
  Deprecated = 'deprecated', // For symbols that should no longer be used.
  Abstract = 'abstract', // For types and member functions that are abstract.
  Async = 'async', // For functions that are marked async.
  Modification = 'modification', // For variable references where the variable is assigned to.
  Documentation = 'documentation', // For occurrences of symbols in documentation.
  DefaultLibrary = 'defaultLibrary', // For symbols that are part of the standard library.
}

export type _SemanticToken = {
  line: number;
  startChar: number;
  length: number;
  legendIdx: number;
  text: string;
};

export type _SemanticTokenColorSettings = {
  foreground?: string
  background?: string
  italic?: boolean
  bold?: boolean
};

export type _SemanticTokenLegendItem = {
  tokenType: string;
  modifiers: _SemanticTokenModifiers[];
  color?: _SemanticTokenColorSettings;
};

export interface ITextModel extends IDisposable {
  language?: string
  uri?: Uri

  getOffsetAt(position: IPosition): number

  getPositionAt(offset: number): IPosition

  getText(): string

  setEditListener(listener: (m: ITextModel) => void): void

  setSemanticTokens(legend: _SemanticTokenLegendItem[], semanticTokens: _SemanticToken[]): void
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

export type DialogOption = {
  title: string
  isEnabled: boolean
}

export type DialogState = {
  options: DialogOption[]
}

export type DialogButton = {
  title: string
  isDefault?: boolean
  isEnabled: (state: DialogState) => boolean
}

export type DialogInput = {
  title: string
  text: string
  options: DialogOption[]
  buttons: DialogButton[]
}

export type DialogResult = {
  button: DialogButton
  options: DialogOption[]
}

export interface ExternalMessageBar {
  setStatusBarMessage(text: string | null): void
  setToolBarMessage(text: string | null): void
}

export interface ExternalDialogProvider {
  showModalDialog(input: DialogInput): Promise<DialogResult | null>
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

  setExternalDialogProvider(opener: ExternalDialogProvider | null): void

  setExternalMessageBar(statusBar: ExternalMessageBar): void

  setExternalContextMenuProvider(p: ContextMenuProvider): void;

  setNotificationsProvider(listener: NotificationsProvider): void;

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

  setRequestSemanticHighlight(listener: (isLeft: boolean) => void): void;

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

