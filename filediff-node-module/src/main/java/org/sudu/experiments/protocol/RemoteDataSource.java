package org.sudu.experiments.protocol;

import org.sudu.experiments.Channel;
import org.sudu.experiments.diff.BinDataCache;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class RemoteDataSource implements BinDataCache.DataSource {

  public Channel channel;
  public boolean left;

  public DoubleConsumer sizeHandler;
  public Consumer<String> sizeError;
  public Result fetchHandler;

  public RemoteDataSource(Channel channel, boolean left) {
    this.channel = channel;
    this.left = left;
  }

  @Override
  public void fetchSize(DoubleConsumer result, Consumer<String> onError) {
    if (sizeHandler == null) sizeHandler = result;
    if (sizeError == null) sizeError = onError;
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(FileDiffChannelUpdater.FETCH_SIZE_ARRAY);
    channel.sendMessage(jsArray);
  }

  @Override
  public void fetch(double address, int chinkSize, Result handler) {
    if (fetchHandler == null) fetchHandler = handler;
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0, chinkSize));
    jsArray.set(1, JsCast.jsNumbers(address));
    jsArray.push(FileDiffChannelUpdater.FETCH_ARRAY);
    channel.sendMessage(jsArray);
  }
}
