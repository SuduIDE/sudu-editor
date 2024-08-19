package org.sudu.experiments.protocol;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.ui.fs.RemoteFileTreeNode;
import org.teavm.jso.JSObject;

import static org.sudu.experiments.protocol.JsCast.*;

public class FrontendState {

  int firstVisibleNode;
  int leftSelectedNode;
  int rightSelectedNode;
  FrontendMessage viewState;
  BackendMessage lastBackendData;

  public static JsArray<JSObject> serialize(
      int firstVisibleNode,
      int leftSelectedNode,
      int rightSelectedNode,
      RemoteFileTreeNode leftRoot,
      RemoteFileTreeNode rightRoot,
      RemoteFolderDiffModel modelRoot,
      String searchQuery
  ) {
    int p = 0;
    JsArray<JSObject> result = JsArray.create();
    result.set(p++, jsInts(firstVisibleNode, leftSelectedNode, rightSelectedNode));

    JsArray<JSObject> serializedFrontend = FrontendMessage.serialize(leftRoot, rightRoot, modelRoot, searchQuery);
    JsArray<JSObject> serializedBackend = BackendMessage.serializeFullModel(modelRoot, leftRoot.name(), rightRoot.name());

    result.set(p++, jsInts(serializedFrontend.getLength(), serializedBackend.getLength()));

    for (int i = 0; i < serializedFrontend.getLength(); i++)
      result.set(p++, serializedFrontend.get(i));
    for (int i = 0; i < serializedBackend.getLength(); i++)
      result.set(p++, serializedBackend.get(i));

    return result;
  }

  public static FrontendState deserialize(JsArray<JSObject> jsArray) {
    int p = 0;
    int[] ints = ints(jsArray, p++);

    int[] sizes = ints(jsArray, p++);
    JsArray<JSObject> serializedFrontend = jsArray.slice(p, p + sizes[0]);
    p += sizes[0];
    JsArray<JSObject> serializedBackend = jsArray.slice(p, p + sizes[1]);

    FrontendState state = new FrontendState();
    state.firstVisibleNode = ints[0];
    state.leftSelectedNode = ints[1];
    state.rightSelectedNode = ints[2];
    state.viewState = FrontendMessage.deserialize(serializedFrontend);
    state.lastBackendData = BackendMessage.deserialize(serializedBackend);
    return state;
  }

  @Override
  public String toString() {
    return "{" +
        "\"firstVisibleNode\":" + firstVisibleNode +
        ", \"leftSelectedNode\":" + leftSelectedNode +
        ", \"rightSelectedNode\":" + rightSelectedNode +
        ", \"viewState\":" + viewState +
        ", \"lastBackendData\":" + lastBackendData +
        "}";
  }
}
