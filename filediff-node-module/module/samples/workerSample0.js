const { Worker, isMainThread } = require('node:worker_threads');

if (isMainThread) {
  // This re-loads the current file inside a Worker instance.
  console.log("isMainThread = " + isMainThread);  // Prints 'false'.

  new Worker(__filename);
} else {
  console.log('Inside Worker!');
  console.log("isMainThread = " + isMainThread);  // Prints 'false'.
}
