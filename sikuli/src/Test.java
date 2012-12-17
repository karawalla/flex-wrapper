import com.google.common.collect.Lists;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.TextTarget;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static String rootFolder = "c:\\dev\\images";
    static Automator automator = new Automator();
    private static String imgName = "";
    private static List<String> commands = new ArrayList<String>();

    public static void main(String[] args) throws InterruptedException {
        while (true){
            Thread.sleep(1000);
            System.out.println("waiting  for code");
            readScript();
            executeCommands();
        }
    }

    private static void executeCommands() {
        for (String command : commands) {
            String[] aLineParts = command.split("<~~~>");
            String action = aLineParts[0];
            imgName = aLineParts[1];
            runCommand();
        }
        commands.clear();
    }

    private static void runCommand() {
        try {
            Target requiredTarget = automator.getShapePattern(rootFolder + "\\" + imgName + ".png");
            List<ScreenRegion> foundRegions = automator.findAll(requiredTarget);
            int count = 1;
            for (ScreenRegion foundRegion : foundRegions) {
                automator.addLabel(Relative.to(foundRegion).above(20).getScreenRegion(), imgName + "-" + count++, 5000);
            }


        }
        catch(Exception e){

        }
    }

    private static boolean readScript() {
        try {
            FileReader scriptReader = new FileReader(getScriptFileName());
            BufferedReader bf = new BufferedReader(scriptReader);

            String aLine;
            int numberOfLines = 0;
            while((aLine = bf.readLine()) != null)
            {
                commands.add(aLine);
            }

            scriptReader.close();
            bf.close();
            File f = new File(getScriptFileName());
            f.delete();
            f.createNewFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }

    private static String getScriptFileName() {
        return "C:\\dev\\java\\flexcode\\script.txt";
    }


}
