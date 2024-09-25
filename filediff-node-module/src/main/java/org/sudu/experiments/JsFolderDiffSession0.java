package org.sudu.experiments;

import org.sudu.experiments.js.Promise;
import org.sudu.experiments.update.DiffModelChannelUpdater;
import org.teavm.jso.JSObject;

public class JsFolderDiffSession0 implements JsFolderDiffSession {
  final DiffModelChannelUpdater updater;

  JsFolderDiffSession0(DiffModelChannelUpdater updater) {
    this.updater = updater;
  }

  @Override
  public Promise<JSObject> shutdown() {
    return Promise.create((ok, fail) ->
        updater.shutdown(() -> ok.f(null))
    );
  }
}
