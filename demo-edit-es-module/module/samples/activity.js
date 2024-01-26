const initialText = "activity {\n" +
    "  select{A1,B1, C1};\n" +
    "  A1;\n" +
    "  select{\n" +
    "    sequence {\n" +
    "      select{(A1)E1, F1};\n" +
    "      G1;\n" +
    "      H1;\n" +
    "      select{\n" +
    "        (E1) X1, \n" +
    "        Y1, \n" +
    "        (A1 and (B1 or C1)) Z1\n" +
    "      }\n" +
    "    };\n" +
    "    sequence {\n" +
    "      G1;\n" +
    "      select{M1, N1};\n" +
    "      H1;\n" +
    "      select{\n" +
    "       ({A1->A1} and M1) A1;\n" +
    "       B1;\n" +
    "       ({A1,G1} and N1) C1\n" +
    "      }\n" +
    "    }\n" +
    "  }  \n" +
    "}";

//CDN
// const path = "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.9-beta12"

//LOCAL
const path = "../src/";
const workerSrc = fetch(path + "worker.js").then(r => r.blob());
const editorApi = await import(path + "editor.js");
const workerUrl = URL.createObjectURL(await workerSrc);
const editor = await editorApi.newEditor({containerId: "editor", workerUrl: workerUrl});

let model = editorApi.newTextModel(initialText, "activity")
editor.setModel(model);

