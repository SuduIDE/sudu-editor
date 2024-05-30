
import { Worker } from 'node:worker_threads';
import { moduleFactory } from "../src/module.mjs";

function runMyWorker() {
  const worker = new Worker("../src/worker.mjs");
  worker.on("message", m => {
    console.log("main: got a message: " + m)
  });
}

let module = await moduleFactory("../src/worker.mjs");

console.log("got module: ", module.constructor.name);
module.foo().then(
    msg => console.log("got foo result: " + msg)
);




