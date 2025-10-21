export const initControlPanel = (codeReview, compactView = true) => {
    const controller = codeReview.getController()
    const uid = (Math.random() * 10000) | 0

    const controlPanelId = `control-panel-${uid}`
    const buttons = {
        next: 'next',
        prev: 'prev',
        toggle: 'toggle'
    }

    const buttonText = {
        next: '🔽',
        prev: '🔼',
        toggle: '↕️'
    }

    const style = document.createElement('style')
    style.innerHTML = `
        #${controlPanelId} {
        position: fixed;
        display: flex;
        right: 25px;
        top: 10px;
        border-radius: 3px;
        background-color: rgba(83, 83, 83, 0.6);
        z-index: 100000;
    }

    #${controlPanelId}>button:first-child {
        border-radius: 3px 0px 0px 3px;
    }

    #${controlPanelId}>button:last-child {
        border-radius: 0px 3px 3px 0px;
    }

    #${controlPanelId}>button {
        padding: 5px;
        background-color: inherit;
        padding-bottom: 6px;
        border: 0;
        flex: content;
    }


    #${controlPanelId}>button:hover {
        box-shadow: inset 0 0 0 10px rgba(83, 83, 83, 0.9);
    }
    `
    document.head.appendChild(style)
    const root = document.createElement('div')
    root.id = controlPanelId

    Object.values(buttons).forEach((name) => {
        const element = document.createElement('button')
        element.id = name
        element.innerText = buttonText[name]
        root.appendChild(element)
    })

    document.body.append(root)


    const nextButton = document.getElementById(buttons.next)
    const prevButton = document.getElementById(buttons.prev)
    const toggleViewButton = document.getElementById(buttons.toggle)
    controller.setCompactView(compactView)

    if (nextButton) {
        nextButton.onclick = (ev) => {
            ev.preventDefault()
            const canNavigateDown = controller.canNavigateDown();
            console.log('canNavigateDown =', canNavigateDown)
            canNavigateDown && controller.navigateDown()
            codeReview.focus();
        }
    }

    if (prevButton) {
        prevButton.onclick = (ev) => {
            ev.preventDefault()
            const canNavigateUp = controller.canNavigateUp();
            console.log('canNavigateUp =', canNavigateUp)
            canNavigateUp && controller.navigateUp()
            codeReview.focus();
        }
    }

    if (toggleViewButton) {
        toggleViewButton.onclick = (ev) => {
            ev.preventDefault()
            compactView = !compactView
            console.log('compactView =', compactView)
            controller.setCompactView(compactView)
            codeReview.focus();
        }
    }

}