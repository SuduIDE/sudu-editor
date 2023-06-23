package org.sudu.experiments.demo;

import org.sudu.experiments.SceneApi;

public class DemoEdit1 extends DemoEdit0 {
    public DemoEdit1(SceneApi api) {
        super(api);
        editor.setModel(new Model(StartFile.START_CODE_JAVA, Languages.JAVA));
    }
}
