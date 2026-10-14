import * as editorApi from "../src/codereview.js";

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const langOptions = editorApi.newLangOptions();
langOptions.disableAll();
langOptions.enable("ascend-c");

const editor = editorApi.newEditor({
    containerId: "editor",
    workers: threadPool,
    disableParser: true,
    langOptions
});

const initialText = `
#ifndef COMMON_INC_EXTERNAL_ACLNN_KERNELS_SLICE_H
#define COMMON_INC_EXTERNAL_ACLNN_KERNELS_SLICE_H

#include "opdev/op_def.h"

namespace l0op {

const aclTensor* Slice(
    const aclTensor* x, const aclTensor* y, const aclTensor* offset, const aclTensor* size, aclOpExecutor* executor);

const aclTensor* Slice(
    const aclTensor* x, const aclIntArray* offsets, const aclIntArray* size, aclOpExecutor* executor);
} // namespace l0op
`

const model = editorApi.newTextModel(threadPool, initialText, "ascend-c", "url");
editor.setModel(model);
editor.focus();