package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSBody;

public class JsNative {
  @JSBody(
      params = {"title", "isDefault", "isEnabled"},
      script = "return {title:title, isDefault:isDefault, isEnabled:isEnabled};"
  )
  public static native JsDialogButton createButton(
      String title,
      boolean isDefault,
      JsDialogButton.IsEnabled isEnabled
  );

  @JSBody(
      params = {"title", "isDefault"},
      script =
          "return {title:title, isDefault:isDefault, " +
              "isEnabled: state => {return true;} };"
  )
  public static native JsDialogButton createButton(
      String title,
      boolean isDefault
  );

  @JSBody(
      params = {"title", "isDefault"},
      script = "return {title:title, isDefault:isDefault};"
  )
  public static native JsDialogOption createOption(
      String title,
      boolean isDefault
  );

  @JSBody(
      params = "options",
      script = "return {options:options};"
  )
  public static native JsDialogState createState(
      JsArray<JsDialogOption> options
  );

  @JSBody(
      params = {"title", "text", "options", "buttons"},
      script = "return {title:title, text:text, options:options, buttons:buttons};"
  )
  public static native JsDialogInput createInput(
      String title,
      String text,
      JsArray<JsDialogOption> options,
      JsArray<JsDialogButton> buttons
  );
}
