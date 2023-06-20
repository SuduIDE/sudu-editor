import { newEditor, newTextModel } from "./src/editor.js";

function main() {
    newEditor({containerId: "editor", workerUrl: "./src/worker.js"})
        .then(connectEditor, error => console.error(error));
}

// connector: '../connector/dist/suduMonaco'
const suduEditorConnector = {
    connect() {
        return {
            load(url) { return Promise.resolve(true) }
        }
    }
}

const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin";

function modelChanged(modelChanged) {
    console.log("modelChanged"
        + ": old = " + modelChanged.oldModelUrl
        + ", new = " + modelChanged.newModelUrl);
}

function connectEditor(editor) {
    editor.focus();

    let model = newTextModel(initialText, "language", "urlNew")

    editor.onDidChangeModel(modelChanged);
    editor.setModel(model);
    let p31 = model.getPositionAt(31);
    let p32 = model.getPositionAt(32);
    let p18 = model.getPositionAt(18);
    console.log("p31 = " + JSON.stringify(p31));
    console.log("p32 = " + JSON.stringify(p32));
    console.log("p18 = " + JSON.stringify(p18));
    console.log("Editor started");

    const input = document.getElementById("address");

    const connector = suduEditorConnector.connect(
        editor, {host: '$ENVOY_ENDPOINT'}
    );

    connector.handleInternalNavigation = true;
    connector.updateStatusBar = (statusBarText) => {
        const statusBar = document.getElementById('statusBar');
        if (statusBar) {
            statusBar.textContent = statusBarText;
        }
    };
    connector.onUriChanged = (uri) => {
        if (!uri) return
        const url = uri.toString()
        if (input.value !== url) {
            history.pushState(url, '')
            input.value = url
        }
    }

    onpopstate = async (event) => {
        const url = event.state
        if (url) navigate(url, false)
    };

    async function navigate(url, isNewState) {
        console.log('Loading: ' + url)
        if (isNewState) history.pushState(url, '')
        else input.value = url
        await connector.load(url)
        editor.focus()
    }

    input.onkeydown = (event) => {
        if (event.key === 'Enter')
            navigate(input.value, true)
    };

    let link = '$INIT_LINK';
    if(link.includes('INIT_LINK')) link = 'sudu://api.sudu.online/root/revision/path'
    navigate(link, false);
}

document.addEventListener("DOMContentLoaded", main)
