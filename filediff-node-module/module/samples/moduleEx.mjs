
// typeof self !== 'undefined' ? self : this

console.log("typeof global = " + typeof global)
console.log("global = " + global)

function moduleFunctionExp(arg) {
  console.log("typeof this = ", typeof this);
  console.log("this = ", this);
  console.log("this.moduleFunction = ", this.moduleFunction);
  console.log(arg)
}

export const moduleFunction = moduleFunctionExp;

