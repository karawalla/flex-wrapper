import com.google.common.collect.Lists;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.TextTarget;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;

import java.awt.*;
import java.util.List;













public class Test {
    public static void main(String[] args) {
        Automator automator = new Automator();
      //  Target textTarget = automator.getTextPattern("See Advanced");
        List<String> strings = Lists.newArrayList();
        strings.add("Computer description");
        strings.add("Workgroup");
        //strings.add("Computer Name");
        strings.add("Advanced");
        strings.add("Hardware");
        strings.add("System Protection");
        strings.add("Remote");
        strings.add("Network ID");
        strings.add("Change");
        strings.add("OK");
        strings.add("Cancel");
        strings.add("Apply");

        for (String s : strings){

            ScreenRegion r = automator.find(new TextTarget(s));
            automator.click(r);

        }



    }
}
