

console.log("hi")

const c2_32 = 65536. * 0x10000;

const hi1 = 1000;
const lo1 = 12345678;

const dAddr = hi1 * c2_32 + lo1;

const hi2 = hi(dAddr);
const lo2 = lo(dAddr);

console.log("hi2 = " + (hi1 === hi2));
console.log("lo2 = " + (lo1 === lo2));

const a = Math.PI;

const b = a % 2;

console.log("a = ", a);
console.log("b = ", b);

function hi(addr) {
  return (addr / c2_32) | 0;
}

function lo(addr) {
  return (addr % c2_32) | 0;
}

const qqq = 4.294967296;

const DA = new Float64Array(1);
const bytes = new Uint8Array(DA.buffer);

DA[0] = qqq; console.log(bytes)
DA[0] = 4294967296; console.log("4294967296", bytes);
DA[0] = qqq * 1E9; console.log("4.294967296 * E9", bytes)


const c35_2 = c2_32 * 2 * 2 * 2 + c2_32 + 1;
console.log("c35_2", c35_2, "isInteger", Number.isInteger(c35_2));
console.log("c35_2", c35_2, "isSafeInteger", Number.isSafeInteger(c35_2));
console.log("c35_2", c35_2, "c35_2 | 0", (c35_2 | 0));
console.log("c2_32", c2_32, "c35_2 | 0", (c2_32 | 0));

DA[0] = c35_2; console.log("c35_2", c35_2, bytes);



