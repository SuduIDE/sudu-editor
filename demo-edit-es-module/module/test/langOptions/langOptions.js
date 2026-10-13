import { newLangOptions, newEditor } from "../../src/editor.js";

const srcLocation = "../../src/";

const langOptions = newLangOptions();
langOptions.disableAll();
langOptions.enable("ascend-c");

const editArgs = {
    containerId: "editor",
    workerUrl: srcLocation + "worker.js",
    numThreads: 2,
    langOptions: langOptions
}

const editor = await newEditor(editArgs)
editor.focus();