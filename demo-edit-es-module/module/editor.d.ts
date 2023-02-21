// java reflection of this file is located at
// demo-edit-es-module/src/main/java/org/sudu/experiments/EditJsApi.java

interface EditArgs {
    containerId: string

    // default value for workerUrl is "worker.js"
    workerUrl?: string
}

interface EditApi {
    focus() : void
    saySomething() : string
    setText(text: string)
}

interface EditView extends EditApi {
    dispose() : void
}

export function newEditor(args: EditArgs) : Promise<EditView>