import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", {path: "WWWWWWWWWWWWWWWWWWWWWWWWWWW.java"})
    editor.setModel(model)

    editor.registerDefinitionProvider("java",
        {
            provideDefinition(model, position, token) {
                return [{
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber + 1,
                        startColumn: position.column + 5,
                        endLineNumber: position.lineNumber + 1,
                        endColumn: position.column + 10
                    }
                }, {
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber - 2,
                        startColumn: position.column,
                        endLineNumber: position.lineNumber -  2,
                        endColumn: position.column
                    }
                }, {
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber + 1,
                        startColumn: position.column + 5,
                        endLineNumber: position.lineNumber + 1,
                        endColumn: position.column + 10
                    }
                },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column + 1,
                            endLineNumber: position.lineNumber - 1,
                            endColumn: position.column + 2
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column,
                            endLineNumber: position.lineNumber -  2,
                            endColumn: position.column
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column + 1,
                            endLineNumber: position.lineNumber - 1,
                            endColumn: position.column + 2
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column,
                            endLineNumber: position.lineNumber -  2,
                            endColumn: position.column
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column + 1,
                            endLineNumber: position.lineNumber - 1,
                            endColumn: position.column + 2
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column,
                            endLineNumber: position.lineNumber -  2,
                            endColumn: position.column
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column + 1,
                            endLineNumber: position.lineNumber - 1,
                            endColumn: position.column + 2
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column,
                            endLineNumber: position.lineNumber -  2,
                            endColumn: position.column
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        },
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        },
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        },
                    } , {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column,
                            endLineNumber: position.lineNumber -  2,
                            endColumn: position.column
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column + 1,
                            endLineNumber: position.lineNumber - 1,
                            endColumn: position.column + 2
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber - 2,
                            startColumn: position.column,
                            endLineNumber: position.lineNumber -  2,
                            endColumn: position.column
                        }
                    }, {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        },
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        },
                    },
                    {
                        uri: model.uri,
                        range: {
                            startLineNumber: position.lineNumber + 1,
                            startColumn: position.column + 5,
                            endLineNumber: position.lineNumber + 1,
                            endColumn: position.column + 10
                        },
                    }

                ]
            }
        }
    );
}

function main() {
    newEditor({containerId: "editor", workerUrl: "./../" + workerUrl})
        .then(run, error => console.error(error));
}

function run(editor) {
    test1(editor);
}

document.addEventListener("DOMContentLoaded", main)
