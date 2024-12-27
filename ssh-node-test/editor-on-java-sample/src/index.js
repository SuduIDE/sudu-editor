// import { newEditor } from "editor-on-java";

// let editor = newEditor("arg")

// console.log(`Hello, ${editor}`);
console.log(`Hello`);

import { Worker } from 'worker_threads';

let worker = new Worker("./worker.js");

worker.on('message', function(message) {
    console.log("message from worker: ", message);
    let f = message === 'finish';
    console.log("message === 'finish'", f);
    if (f) worker.terminate();
});

worker.postMessage("hello worker");

const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));






