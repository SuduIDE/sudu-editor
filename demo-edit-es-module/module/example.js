import { newEditor } from "./src/editor.js";

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

function connectEditor(editor) {

    editor.focus();
    editor.setText("");
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
