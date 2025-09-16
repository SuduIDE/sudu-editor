const array1 = new Int8Array(100);

const array2 = new Int8Array(array1.buffer, 0, 16);
// const array2 = new Int8Array(array1, 0, 16);

array2[3] = 3;

if (array1[3] !== 3)
  throw new Error("test failed: array2[3] !== 3, array2[3]=" + array2[3]);

console.log("bufferTest: 3 ==", array1[3]);
