// import {newRemoteChannelTest} from "../../editor";

const srcLocation = "../../src/";
const editorApi = await import(srcLocation + "editor.js");

function channel() {
    return {
        onMessageHandler : null,
        sendMessage: function(m) {
            console.log("sendMessage: ", m);
            this.onMessageHandler(m);
        },
        set onMessage(handler) {
            this.onMessageHandler = handler;
            console.log("onMessageHandler <- ", handler); }
    };
}

const channelTest = await editorApi.newRemoteChannelTest(channel());

channelTest.foo();
