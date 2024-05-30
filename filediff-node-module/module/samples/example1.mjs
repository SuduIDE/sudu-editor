
import { moduleFactory } from "../src/module.mjs";

let module = await moduleFactory("../src/worker.mjs");

console.log("got module: ", module.constructor.name);
module.fib(5).then(
    msg => console.log("got foo result: " + msg)
);




