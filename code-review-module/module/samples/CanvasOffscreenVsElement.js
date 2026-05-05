import { initControlPanel } from "./control-panel.js";

const controlPanel = initControlPanel(document.getElementById("editor"))

function testCreate() {
  const element = document.createElement("canvas");
  const offscreen = new OffscreenCanvas(300, 150);

  console.log("element", element, element.width, element.height);
  console.log("offscreen", offscreen, offscreen.width, offscreen.height);
}

const N = 10000;

const array = new Array(N);

function createElements() {
  for (let i = 0; i < N; i++) {
    array[i] = document.createElement("canvas");
  }
  console.log("created", N, "elements");
}

function createOffscreen() {
  for (let i = 0; i < N; i++) {
    array[i] = new OffscreenCanvas(300, 150);
  }
  console.log("created", N, "OffscreenCanvas");
}

console.log("ready");

const controls = {
  'elements': createElements,
  'offscreen': createOffscreen,
  '🔄️': () => window.location.reload()
}

Object.entries(controls).forEach(([icon, handler]) => controlPanel.add(icon, handler))
