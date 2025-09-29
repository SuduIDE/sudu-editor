package org.sudu.experiments.protocol;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.diff.BinDataCache;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class RemoteDataSource implements BinDataCache.DataSource {

  public Channel channel;
  public boolean left;

  public DoubleConsumer onSizeGot;
  public Result onFetched;

  public RemoteDataSource(Channel channel, boolean left, DoubleConsumer onSizeGot, Result onFetched) {
    this.channel = channel;
    this.left = left;
    this.onSizeGot = onSizeGot;
    this.onFetched = onFetched;
    channel.setOnMessage(this::onMessage);
  }

  private void onMessage(JsArray<JSObject> m) {
    int type = JsCast.ints(m.pop())[0];
    LoggingJs.info("RemoteDataSource.onMessage got " + type);
    switch (type) {
      case FileDiffChannelUpdater.FETCH -> onFetched(m);
      case FileDiffChannelUpdater.FETCH_SIZE -> onSizeFetched(m);
    }
  }

  @Override
  public void fetchSize(DoubleConsumer result, Consumer<String> onError) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(FileDiffChannelUpdater.FETCH_SIZE_ARRAY);
    channel.sendMessage(jsArray);
  }

  private void onSizeFetched(JsArray<JSObject> jsArray) {
    LoggingJs.info("RemoteDataSource.onSizeFetched: left" + left);
  }

  private void onFetched(JsArray<JSObject> jsArray) {
    LoggingJs.info("RemoteDataSource.onFetched: left" + left);
  }

  @Override
  public void fetch(double address, int chinkSize, Result handler) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0, chinkSize));
    jsArray.set(1, JsCast.jsNumbers(address));
    jsArray.push(FileDiffChannelUpdater.FETCH_ARRAY);
    channel.sendMessage(jsArray);
  }
}
