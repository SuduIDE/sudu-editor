if (1) {
    let container = document.getElementById("editor")

    let canvas = document.createElement("canvas");
    let context = canvas.getContext('2d');
    context.fillStyle = "green";

    let st = canvas.style;
    st.width = "100%";
    st.height = "100%";
    st.outline = "none";

    const observer = new ResizeObserver((entries) => {
        for (const entry of entries) {
            if (entry.target === canvas) {
                let size = entry.devicePixelContentBoxSize[0];
                console.log("w = " + size.inlineSize + ", h = " + size.blockSize);
                canvas.width = size.inlineSize;
                canvas.height = size.blockSize;
                context.fillStyle = "green";
                context.fillRect(0, 0, size.inlineSize, size.blockSize);
            }
        }
    });
    observer.observe(canvas, {box: "device-pixel-content-box"});
    container.appendChild(canvas)
}

if (0) {
    const editorApi = await import("../src/editor.js");

    editorApi.newEditor({containerId: "editor", workerUrl: "../src/worker.js"})
}
