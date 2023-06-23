package org.sudu.experiments.demo;

import org.sudu.experiments.SceneApi;

public class DemoEdit1 extends DemoEdit0 {
    public DemoEdit1(SceneApi api) {
        super(api);
        editor.model.language = Languages.JAVA;
        editor.setText(StartFile.getChars());
    }
}
