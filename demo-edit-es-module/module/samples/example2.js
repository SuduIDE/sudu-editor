



    let container = document.getElementById("editor")

    let canvas = document.createElement("canvas");
    let context = canvas.getContext('2d');

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
                const ratio = window.devicePixelRatio;
                canvas.style.width = ((1./32. + size.inlineSize) / ratio) + 'px';
                canvas.style.height = ((1./32. + size.blockSize) /ratio) + 'px';
                console.log("dpr = " + ratio)
                console.log("st.w = " + canvas.style.width);
                console.log("st.h = " + canvas.style.height);
                context.fillStyle = "green";
                context.fillRect(0, 0, size.inlineSize, size.blockSize);
            }
        }
    });
    observer.observe(canvas, {box: "device-pixel-content-box"});
    container.appendChild(canvas)
