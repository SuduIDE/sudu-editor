import { newCodeDiff, newTextModel } from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function main() {
  newCodeDiff({containerId: "editor", workerUrl: "./../" + workerUrl, readonly: true})
        .then(run, error => console.error(error));
}

function run(diff) {
  diff.setLeftModel(newTextModel(initialTextJava, "java"));
  diff.setRightModel(newTextModel(initialTextJava, "java"));
  let disconnect = () => {
    console.log("disconnectFromDom ...");
    diff.disconnectFromDom();
  };
  let reconnect = () => {
    console.log("reconnectToDom ...");
    diff.reconnectToDom();
  };
  document.getElementById("remove")
      .addEventListener('click', disconnect);
  document.getElementById("add")
      .addEventListener('click', reconnect);
}

document.addEventListener("DOMContentLoaded", main)
