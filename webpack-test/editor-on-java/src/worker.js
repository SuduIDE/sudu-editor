console.log(`Hello, from worker thread`);

self.onmessage = function(message) {
    console.log(`Worker: ` + message.data);
}
