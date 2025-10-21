const initStyle = () => {
    const styleId = 'control-panel-style'
    if (document.getElementById(styleId)) return
    const style = document.createElement('style')
    style.id = styleId
    style.innerHTML = `
    .control-panel {
        position: absolute;
        display: flex;
        right: 25px;
        top: 10px;
        border-radius: 3px;
        background-color: rgba(83, 83, 83, 0.6);
        z-index: 100000;
    }

    .control-panel>button:first-child {
        border-radius: 3px 0px 0px 3px;
    }

    .control-panel>button:last-child {
        border-radius: 0px 3px 3px 0px;
    }

    .control-panel>button {
        padding: 5px;
        background-color: inherit;
        padding-bottom: 6px;
        border: 0;
        flex: content;
    }


    .control-panel>button:hover {
        box-shadow: inset 0 0 0 10px rgba(83, 83, 83, 0.9);
    }
    `
    document.head.appendChild(style)
}

export const initControlPanel = (parentElement = document.body) => {
    initStyle()
    const root = document.createElement('div')
    root.classList.add('control-panel')

    parentElement.style.position = 'relative'
    parentElement.append(root)

    const api = {
        /**
         *
         * @param {string} icon
         * @param {(event: PointerEvent)=> void | Promise<void>} handler
         * @returns {api}
         */
        add: (icon, handler) => {
            const element = document.createElement('button')

            element.onclick = (ev) => {
                handler(ev)
            }

            element.innerText = icon

            root.appendChild(element)
            return api
        }
    }
    return api
}