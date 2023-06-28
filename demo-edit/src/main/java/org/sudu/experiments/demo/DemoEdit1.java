package org.sudu.experiments.demo;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.fonts.Fonts;

public class DemoEdit1 extends DemoEdit0 {
    public DemoEdit1(SceneApi api) {
        super(api);
        editor.setModel(new Model(StartFile.START_CODE_JAVA, Languages.JAVA));
    }

    @Override
    protected String[] menuFonts() {
        return Fonts.editorFonts(true);
    }
}
