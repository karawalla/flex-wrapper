import org.sikuli.api.*;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.visual.ScreenPainter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Automator {
    private ScreenRegion currentScreen_;
    private Mouse mouse_;
    private Keyboard keyboard_;
    private ScreenPainter painter_;
    private Relative relative_;
    private String imagesRootFolder_;
    private String commonImagesFolder_;
    private String lastError_ = "OK";
    private double minScore_ = 0.8;


    public Automator(){
        mouse_ = new DesktopMouse();
        keyboard_ = new DesktopKeyboard();
        painter_ = new ScreenPainter();
        relative_ = new Relative();
        currentScreen_ = new DesktopScreenRegion();
    }



    public  ScreenRegion getNewScreen() {
        currentScreen_ = new DesktopScreenRegion();
        return currentScreen_;
    }

    public Target getTextPattern(String text){
        System.out.println("text = " + text);
        Target textTarget = new TextTarget(text);
        System.out.println("textTarget = " + textTarget);
        textTarget.setMinScore(0.7);
        return textTarget;
    }
    public  Target getExactPattern(String filePath) {
        try {
            Target retTarget = new ColorImageTarget(ImageIO.read(new File(filePath)));
            retTarget.setOrdering(Target.Ordering.TOP_DOWN);
            retTarget.setLimit(100);
            retTarget.setMinScore(getMinScore());
            return retTarget;
        } catch (IOException e) {
            System.out.println("FILE NOT FOUND = " + filePath);
        }
        return null;
    }

    private double getMinScore() {
        return minScore_;
    }

    public void setMinScore(double minScore){
        minScore_ = minScore;
    }

    public Target getShapePattern(String filePath) {
        try {

            Target retTarget = new StyledRectangleTarget(ImageIO.read(new File(filePath)));
            retTarget.setOrdering(Target.Ordering.TOP_DOWN);
            retTarget.setLimit(100);
            return retTarget;
        } catch (IOException e) {
            System.out.println("FILE NOT FOUND = " + filePath);
        }
        return null;
    }

    public  List<ScreenRegion> findAll(Target target) {
        System.out.println("target = " + target);
        List<ScreenRegion> retList = currentScreen_.findAll(target);
        System.out.println("retList.size() = " + retList.size());
        return retList;
    }

    public  ScreenRegion find(Target target) {
        ScreenRegion sr = currentScreen_.find(target);
        System.out.println("sr = " + sr);
        return sr;
    }

    public  ScreenRegion findRelative(String targetImgFile, String relation, String relativeImgFile ) {
        Target rqdTarget = getExactPattern(targetImgFile);
        Target relativeTarget = getExactPattern(relativeImgFile);

        ScreenRegion matchedRegion = findRelative(rqdTarget, relation, relativeTarget);
        if(matchedRegion == null){
            System.out.println("Could not find " + targetImgFile + " " + relation + " to " + relativeImgFile);
            return null;
        }

        return matchedRegion;
    }

    public  ScreenRegion findRelative(Target rqdTarget, String relation, Target relativeTarget ) {
        ScreenRegion relativeTargetScreenRegion = find(relativeTarget);
        if(relativeTargetScreenRegion == null){
            lastError_ = "Could not find Relative Image";
            System.out.println("lastError_ = " + lastError_);
            return null;
        }

        addBorder(relativeTargetScreenRegion, 5000);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int x = (int)relativeTargetScreenRegion.getBounds().getX();
        int y = (int)relativeTargetScreenRegion.getBounds().getY();
        int width = (int)relativeTargetScreenRegion.getBounds().getWidth();
        int height = (int)relativeTargetScreenRegion.getBounds().getHeight();

        if(relation.toLowerCase().equals("right"))
        {
            int x1,y1,w1,h1;
            x1 = x;
            y1 = y - 10;
            w1 = width;
            h1 = height + 10;
            relativeTargetScreenRegion.setBounds(new Rectangle(x1,y1,w1,h1));

            rqdTarget.setOrdering(Target.Ordering.LEFT_RIGHT);
            int numIters = currentScreen_.getScreen().getSize().width / 400;

            for(int i = 1; i <= numIters ; i++){
                ScreenRegion searchRegion = Relative.to(relativeTargetScreenRegion).right(i * 400).getScreenRegion();
                addBorder(searchRegion, 3000);
                ScreenRegion rqdScreenRegion = searchRegion.find(rqdTarget);
                if(rqdScreenRegion != null){
                    return rqdScreenRegion;
                }
            }
        }
        else if(relation.toLowerCase().equals("left"))
        {
            int x1,y1,w1,h1;
            x1 = x;
            y1 = y - 10;
            w1 = width;
            h1 = height + 10;
            relativeTargetScreenRegion.setBounds(new Rectangle(x1,y1,w1,h1));

            rqdTarget.setOrdering(Target.Ordering.RIGHT_LEFT);
            int numIters = currentScreen_.getScreen().getSize().width / 100;

            for(int i = 1; i <= numIters ; i++){
                ScreenRegion searchRegion = Relative.to(relativeTargetScreenRegion).left(100 * i).getScreenRegion();
                ScreenRegion rqdScreenRegion = searchRegion.find(rqdTarget);
                if(rqdScreenRegion != null){
                    return rqdScreenRegion;
                }
            }
        }
        else if(relation.toLowerCase().equals("above"))
        {
            int x1,y1,w1,h1;
            x1 = x - 10;
            y1 = y;
            w1 = width + 10;
            h1 = height;
            relativeTargetScreenRegion.setBounds(new Rectangle(x1,y1,w1,h1));

            rqdTarget.setOrdering(Target.Ordering.BOTTOM_UP);
            int numIters = currentScreen_.getScreen().getSize().height / 100;

            for(int i = 1; i <= numIters ; i++){
                ScreenRegion searchRegion = Relative.to(relativeTargetScreenRegion).above(100 * i).getScreenRegion();
                ScreenRegion rqdScreenRegion = searchRegion.find(rqdTarget);
                if(rqdScreenRegion != null){
                    return rqdScreenRegion;
                }
            }
        }
        else if(relation.toLowerCase().equals("below"))
        {
            int x1,y1,w1,h1;
            x1 = x - 10;
            y1 = y;
            w1 = width + 10;
            h1 = height;
            relativeTargetScreenRegion.setBounds(new Rectangle(x1,y1,w1,h1));

            rqdTarget.setOrdering(Target.Ordering.TOP_DOWN);

            int numIters = currentScreen_.getScreen().getSize().height / 100;

            for(int i = 1; i <= numIters ; i++){
                ScreenRegion searchRegion = Relative.to(relativeTargetScreenRegion).below(100 * i).getScreenRegion();
                ScreenRegion rqdScreenRegion = searchRegion.find(rqdTarget);
                if(rqdScreenRegion != null){
                    return rqdScreenRegion;
                }
            }
        }
        else if(relation.toLowerCase().equals("inside"))
        {
            rqdTarget.setOrdering(Target.Ordering.LEFT_RIGHT);
            ScreenRegion searchRegion = relativeTargetScreenRegion;
            ScreenRegion rqdScreenRegion = searchRegion.find(rqdTarget);
            if(rqdScreenRegion != null){
                return rqdScreenRegion;
            }
        }

        System.out.println("Could not find original target" + " " + relation + " to relative target");
        return null;
    }
    public  void showLabelOnScreen(String msgString) {
        painter_.label(currentScreen_,msgString, 3000);
    }

    public  void addBorder(ScreenRegion screenRegion, int displayTime) {
        try{
            System.out.println("screenRegion = " + screenRegion);
            painter_.box(screenRegion,displayTime);
        }
        catch (Exception e){

        }

    }

    public  void addLabel(ScreenLocation screenLocation, String msgString, int displayTime) {
        painter_.label(screenLocation, msgString, displayTime);
    }

    public Relative getRelativityHandler()
    {
        return relative_;
    }

    public void click(ScreenRegion screenRegion){
        mouse_.click(screenRegion.getCenter());
    }

    public void hover(ScreenRegion screenRegion){
        mouse_.drag(screenRegion.getCenter());
    }

    public void doubleClick(ScreenRegion screenRegion){
        mouse_.doubleClick(screenRegion.getCenter());
    }


    public void type(String text){
        keyboard_.type(text);
    }

    public void paste(String text){
        keyboard_.paste(text);
    }

    public void clickAndType(ScreenRegion sr, String text){
        mouse_.click(sr.getCenter());
        keyboard_.type(text);
    }


    //Settings
    public void setImagesRootFolder(String imagesRootFolder){
        imagesRootFolder_ = imagesRootFolder;
    }

    public void setCommonImagesFolder(String commonImagesFolder){
        commonImagesFolder_ = commonImagesFolder;
    }
}
