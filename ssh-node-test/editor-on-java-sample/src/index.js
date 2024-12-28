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

worker.postMessage({
        cmd: "readDir",
        ssh: {
            host: '172.29.85.42',
            port: 22,
            username: 'kirill',
            password: 'gbpltw'

            //  privateKey: readFileSync('/path/to/my/key')
        }
    }
);

const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));






