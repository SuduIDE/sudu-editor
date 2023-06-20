import { newEditor } from "sudu-editor-tmp";

function addPreText(divId, text) {
    let pre = document.createElement("pre");
    pre.innerHTML = text;
    let style = pre.style;
    style.setProperty("padding-left", "10px");
    style.setProperty("padding-right", "10px");
    document.getElementById(divId).appendChild(pre);
}

function main() {
    for (const x of ["editor1", "editor2"]) {
        newEditor({containerId: x, workerUrl: "worker.js"}).then(
            editor => {
                if (x === "editor1") {
                    editor.focus();
                    editor.setText("");
                } else {
                    editor.setTheme("light")
                }
                console.log("Editor " + x + " started")
            },
            error => addPreText(x, error)
        )
    }
}

document.addEventListener("DOMContentLoaded", main)
