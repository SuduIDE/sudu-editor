How to embed the web version of the SuduEditor into any website.

The web version of the editor is an ESM module.

It is located in demo-edit-es-module/module folder and consist of 
 - description files: package.json, editor.d.ts
 - source files: src/editor.js, src/worker.js

Only two source files are required for editor to work: editor.js, worker.js

There are different variants of embedding
  - put src/editor.js and src/worker.js to your website
  - or load the sources from CDN

If you ready to add editor source files to you website then embedding is very easy:

Assume you have the following directory structure:

 - index.html - has a div with id `"editorDiv"` to place editor there
 - main.js
 - src\
   - editor.js
   - worker.js

To run editor in the div just do that in the main.js:

```javascript
const editorApi = await import("./src/editor.js");
const editor = await editorApi.newEditor({
    containerId: "editorDiv", workerUrl: "./src/worker.js"
});
editor.focus();
```
If you want to manipulate documents in the editor you can create one or many text models and put one of them to be editable in the editor:

```javascript
const initialText1 = "int main() { return 0; }";
let model1 = editorApi.newTextModel(initialText1, "cpp", "filename1.cpp");
const initialText2 = "void code() { }";
let model2 = editorApi.newTextModel(initialText2, "cpp", "filename2.cpp");
editor.setModel(model1);
```

At some time later you can change edited file by

```javascript
editor.setModel(model2);
```

You may find examples of embedding in
- demo-edit-es-module/module/samples/example.js
- demo-edit-es-module/module/samples/loadFromCDN.js

To load from CDN please see loadFromCDN.js example.

If you want to build the EDM editor module you can use 

`mvn package package -am -pl demo-edit-es-module -P release`

For detailed build instructions please see [README.md](README.md).
You may also find the full api description here: [demo-edit-es-module/module/editor.d.ts](demo-edit-es-module/module/editor.d.ts)


