import {newTextModel, newCodeDiff} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

const editorArgs = {containerId: "editor", workerUrl: "./../" + workerUrl};

function main() {
    newCodeDiff(editorArgs).then(
        diff1 => {
            let model = newTextModel(initialTextJava, "java", "urlNew");
            diff1.setLeftModel(model)
            diff1.setReadonly(true)
            newCodeDiff(editorArgs).then(
                diff2 => {
                    diff2.setRightModel(model)
                    diff2.setReadonly(true)
                    document.getElementById("focusFirst").addEventListener('click', () => diff1.focus())
                    document.getElementById("focusSecond").addEventListener('click', () => diff2.focus())
                }
            )
        }
    )
}

document.addEventListener("DOMContentLoaded", main)
