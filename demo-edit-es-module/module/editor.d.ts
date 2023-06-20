// java reflection of this file is located at
// demo-edit-es-module/src/main/java/org/sudu/experiments/EditJsApi.java

interface EditArgs {
    containerId: string

    // default value for workerUrl is "worker.js"
    workerUrl?: string

    // themes: "dark", "light"
    theme?: string
}

interface Uri {
    scheme?: string,
    authority?: string,
    path?: string
}

interface IDisposable {
    dispose(): void
}

interface IEvent<T> {
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

interface ITextModel extends IDisposable {
    language?: string
    uri?: Uri
    getOffsetAt(position: IPosition): number
    getPositionAt(offset: number): IPosition
}

type ProviderValue<T> = T | undefined | null;
type ProviderResult<T> = ProviderValue<T> | Promise<ProviderValue<T>>;

interface IPosition {
    column: number,
    lineNumber: number
}

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

export enum DocumentHighlightKind {
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
        source: ICodeEditor,
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

interface ICodeEditor {
    focus(): void,

    setText(text: string): void,

    getText(): string,

    setFontFamily(fontFamily: string): void,

    setFontSize(fontSize: number): void,

    setTheme(theme: string): void,

    setModel(model: ITextModel): void,

    setPosition(selectionOrPosition: SelectionOrPosition): void

    getModel(): ITextModel,

    registerDefinitionProvider(languageSelector: LanguageSelector, provider: IDefinitionProvider): IDisposable,

    registerReferenceProvider(languageSelector: LanguageSelector, provider: IReferenceProvider): IDisposable,

    registerDocumentHighlightProvider(languageSelector: LanguageSelector, provider: IDocumentHighlightProvider): IDisposable,

    registerEditorOpener(opener: ICodeEditorOpener): IDisposable

    revealLineInCenter(line: number): void

    revealLine(line: number): void

    onDidChangeModel: IEvent<IModelChangedEvent>
}


interface EditView extends ICodeEditor, IDisposable {}

export function newEditor(args: EditArgs): Promise<EditView>

export function newTextModel(text: string, language?: string, uri?: Uri): ITextModel
