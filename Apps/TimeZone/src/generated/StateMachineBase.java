/**
 * This class contains generated code from the LWUIT resource editor, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://lwuit.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.sun.lwuit.*;
import com.sun.lwuit.util.*;
import com.sun.lwuit.plaf.*;
import com.sun.lwuit.events.*;

public abstract class StateMachineBase extends UIBuilder {
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars() {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.open(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            return showForm("Splash", null);
        } else {
            Form f = (Form)createContainer(resPath, "Splash");
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.open(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "Splash");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.sun.lwuit.Container findContainer4(Container root) {
        return (com.sun.lwuit.Container)findByName("Container4", root);
    }

    public com.sun.lwuit.Container findTitleArea(Container root) {
        return (com.sun.lwuit.Container)findByName("titleArea", root);
    }

    public com.sun.lwuit.Container findContainer3(Container root) {
        return (com.sun.lwuit.Container)findByName("Container3", root);
    }

    public com.sun.lwuit.Container findContainer2(Container root) {
        return (com.sun.lwuit.Container)findByName("Container2", root);
    }

    public com.sun.lwuit.Label findSliderTimeTab(Container root) {
        return (com.sun.lwuit.Label)findByName("sliderTimeTab", root);
    }

    public com.sun.lwuit.Container findContainer1(Container root) {
        return (com.sun.lwuit.Container)findByName("Container1", root);
    }

    public com.sun.lwuit.Container findContainer8(Container root) {
        return (com.sun.lwuit.Container)findByName("Container8", root);
    }

    public com.sun.lwuit.Button findRemoveModeButton(Container root) {
        return (com.sun.lwuit.Button)findByName("removeModeButton", root);
    }

    public com.sun.lwuit.Container findContainer7(Container root) {
        return (com.sun.lwuit.Container)findByName("Container7", root);
    }

    public com.sun.lwuit.Label findTickLabel(Container root) {
        return (com.sun.lwuit.Label)findByName("tickLabel", root);
    }

    public com.sun.lwuit.Container findContainer6(Container root) {
        return (com.sun.lwuit.Container)findByName("Container6", root);
    }

    public com.sun.lwuit.Label findTimeSliderPosition(Container root) {
        return (com.sun.lwuit.Label)findByName("timeSliderPosition", root);
    }

    public com.sun.lwuit.Container findContainer5(Container root) {
        return (com.sun.lwuit.Container)findByName("Container5", root);
    }

    public com.sun.lwuit.Button findExitButton(Container root) {
        return (com.sun.lwuit.Button)findByName("exitButton", root);
    }

    public com.sun.lwuit.Button findSettingsButton(Container root) {
        return (com.sun.lwuit.Button)findByName("settingsButton", root);
    }

    public com.sun.lwuit.Label findDate(Container root) {
        return (com.sun.lwuit.Label)findByName("date", root);
    }

    public com.sun.lwuit.Form findSplash(Container root) {
        return (com.sun.lwuit.Form)findByName("Splash", root);
    }

    public com.sun.lwuit.Container findFriendsRoot(Container root) {
        return (com.sun.lwuit.Container)findByName("friendsRoot", root);
    }

    public com.sun.lwuit.Label findFriendName(Container root) {
        return (com.sun.lwuit.Label)findByName("friendName", root);
    }

    public com.sun.lwuit.List findTimeSlider(Container root) {
        return (com.sun.lwuit.List)findByName("timeSlider", root);
    }

    public com.sun.lwuit.Label findDayOrNight(Container root) {
        return (com.sun.lwuit.Label)findByName("dayOrNight", root);
    }

    public com.sun.lwuit.Container findRenderer(Container root) {
        return (com.sun.lwuit.Container)findByName("Renderer", root);
    }

    public com.sun.lwuit.Button findButton(Container root) {
        return (com.sun.lwuit.Button)findByName("Button", root);
    }

    public com.sun.lwuit.Label findTitle(Container root) {
        return (com.sun.lwuit.Label)findByName("title", root);
    }

    public com.sun.lwuit.CheckBox findSelected(Container root) {
        return (com.sun.lwuit.CheckBox)findByName("selected", root);
    }

    public com.sun.lwuit.Dialog findAddZone(Container root) {
        return (com.sun.lwuit.Dialog)findByName("AddZone", root);
    }

    public com.sun.lwuit.Container findZoneRenderer(Container root) {
        return (com.sun.lwuit.Container)findByName("ZoneRenderer", root);
    }

    public com.sun.lwuit.Label findLabel1(Container root) {
        return (com.sun.lwuit.Label)findByName("Label1", root);
    }

    public com.sun.lwuit.Label findDescription(Container root) {
        return (com.sun.lwuit.Label)findByName("description", root);
    }

    public com.sun.lwuit.Label findLabel2(Container root) {
        return (com.sun.lwuit.Label)findByName("Label2", root);
    }

    public com.sun.lwuit.Form findMainUI(Container root) {
        return (com.sun.lwuit.Form)findByName("MainUI", root);
    }

    public com.sun.lwuit.Label findTimeOfDay(Container root) {
        return (com.sun.lwuit.Label)findByName("timeOfDay", root);
    }

    public com.sun.lwuit.Button findRemoveFriend(Container root) {
        return (com.sun.lwuit.Button)findByName("removeFriend", root);
    }

    public com.sun.lwuit.Label findDayOfWeek(Container root) {
        return (com.sun.lwuit.Label)findByName("dayOfWeek", root);
    }

    public com.sun.lwuit.Container findContainer(Container root) {
        return (com.sun.lwuit.Container)findByName("Container", root);
    }

    public com.sun.lwuit.RadioButton findWorldZone(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("worldZone", root);
    }

    public com.sun.lwuit.Label findIcon(Container root) {
        return (com.sun.lwuit.Label)findByName("icon", root);
    }

    public com.sun.lwuit.Dialog findSettings(Container root) {
        return (com.sun.lwuit.Dialog)findByName("Settings", root);
    }

    public com.sun.lwuit.Container findFriend(Container root) {
        return (com.sun.lwuit.Container)findByName("Friend", root);
    }

    public com.sun.lwuit.RadioButton findFriendZone(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("friendZone", root);
    }

    public com.sun.lwuit.List findAddZoneList(Container root) {
        return (com.sun.lwuit.List)findByName("addZoneList", root);
    }

    public com.sun.lwuit.Label findCurrentTimeAndDate(Container root) {
        return (com.sun.lwuit.Label)findByName("currentTimeAndDate", root);
    }

    public com.sun.lwuit.Label findTick(Container root) {
        return (com.sun.lwuit.Label)findByName("tick", root);
    }

    public com.sun.lwuit.Button findAddEntriesButton(Container root) {
        return (com.sun.lwuit.Button)findByName("addEntriesButton", root);
    }

    public com.sun.lwuit.Label findSubtitle(Container root) {
        return (com.sun.lwuit.Label)findByName("subtitle", root);
    }

    public com.sun.lwuit.Container findContainer41(Container root) {
        return (com.sun.lwuit.Container)findByName("Container41", root);
    }

    public com.sun.lwuit.Label findLabel(Container root) {
        return (com.sun.lwuit.Label)findByName("Label", root);
    }

    public com.sun.lwuit.CheckBox findCivilianTimeCheckbox(Container root) {
        return (com.sun.lwuit.CheckBox)findByName("civilianTimeCheckbox", root);
    }

    public static final int COMMAND_MainUISettings = 2;
    public static final int COMMAND_SettingsOK = 5;
    public static final int COMMAND_MainUIAdd = 3;
    public static final int COMMAND_AddZoneOK = 4;
    public static final int COMMAND_MainUIExit = 1;

    protected boolean onMainUISettings() {
        return false;
    }

    protected boolean onSettingsOK() {
        return false;
    }

    protected boolean onMainUIAdd() {
        return false;
    }

    protected boolean onAddZoneOK() {
        return false;
    }

    protected boolean onMainUIExit() {
        return false;
    }

    protected void processCommand(ActionEvent ev, Command cmd) {
        switch(cmd.getId()) {
            case COMMAND_MainUISettings:
                if(onMainUISettings()) {
                    ev.consume();
                }
                return;

            case COMMAND_SettingsOK:
                if(onSettingsOK()) {
                    ev.consume();
                }
                return;

            case COMMAND_MainUIAdd:
                if(onMainUIAdd()) {
                    ev.consume();
                }
                return;

            case COMMAND_AddZoneOK:
                if(onAddZoneOK()) {
                    ev.consume();
                }
                return;

            case COMMAND_MainUIExit:
                if(onMainUIExit()) {
                    ev.consume();
                }
                return;

        }
    }

    protected void exitForm(Form f) {
        if("ZoneRenderer".equals(f.getName())) {
            exitZoneRenderer(f);
            return;
        }

        if("Friend".equals(f.getName())) {
            exitFriend(f);
            return;
        }

        if("MainUI".equals(f.getName())) {
            exitMainUI(f);
            return;
        }

        if("Splash".equals(f.getName())) {
            exitSplash(f);
            return;
        }

        if("AddZone".equals(f.getName())) {
            exitAddZone(f);
            return;
        }

        if("Renderer".equals(f.getName())) {
            exitRenderer(f);
            return;
        }

        if("Settings".equals(f.getName())) {
            exitSettings(f);
            return;
        }

    }


    protected void exitZoneRenderer(Form f) {
    }


    protected void exitFriend(Form f) {
    }


    protected void exitMainUI(Form f) {
    }


    protected void exitSplash(Form f) {
    }


    protected void exitAddZone(Form f) {
    }


    protected void exitRenderer(Form f) {
    }


    protected void exitSettings(Form f) {
    }

    protected void beforeShow(Form f) {
        if("ZoneRenderer".equals(f.getName())) {
            beforeZoneRenderer(f);
            return;
        }

        if("Friend".equals(f.getName())) {
            beforeFriend(f);
            return;
        }

        if("MainUI".equals(f.getName())) {
            beforeMainUI(f);
            return;
        }

        if("Splash".equals(f.getName())) {
            beforeSplash(f);
            return;
        }

        if("AddZone".equals(f.getName())) {
            beforeAddZone(f);
            return;
        }

        if("Renderer".equals(f.getName())) {
            beforeRenderer(f);
            return;
        }

        if("Settings".equals(f.getName())) {
            beforeSettings(f);
            return;
        }

    }


    protected void beforeZoneRenderer(Form f) {
    }


    protected void beforeFriend(Form f) {
    }


    protected void beforeMainUI(Form f) {
    }


    protected void beforeSplash(Form f) {
    }


    protected void beforeAddZone(Form f) {
    }


    protected void beforeRenderer(Form f) {
    }


    protected void beforeSettings(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        if("ZoneRenderer".equals(c.getName())) {
            beforeContainerZoneRenderer(c);
            return;
        }

        if("Friend".equals(c.getName())) {
            beforeContainerFriend(c);
            return;
        }

        if("MainUI".equals(c.getName())) {
            beforeContainerMainUI(c);
            return;
        }

        if("Splash".equals(c.getName())) {
            beforeContainerSplash(c);
            return;
        }

        if("AddZone".equals(c.getName())) {
            beforeContainerAddZone(c);
            return;
        }

        if("Renderer".equals(c.getName())) {
            beforeContainerRenderer(c);
            return;
        }

        if("Settings".equals(c.getName())) {
            beforeContainerSettings(c);
            return;
        }

    }


    protected void beforeContainerZoneRenderer(Container c) {
    }


    protected void beforeContainerFriend(Container c) {
    }


    protected void beforeContainerMainUI(Container c) {
    }


    protected void beforeContainerSplash(Container c) {
    }


    protected void beforeContainerAddZone(Container c) {
    }


    protected void beforeContainerRenderer(Container c) {
    }


    protected void beforeContainerSettings(Container c) {
    }

    protected void postShow(Form f) {
        if("ZoneRenderer".equals(f.getName())) {
            postZoneRenderer(f);
            return;
        }

        if("Friend".equals(f.getName())) {
            postFriend(f);
            return;
        }

        if("MainUI".equals(f.getName())) {
            postMainUI(f);
            return;
        }

        if("Splash".equals(f.getName())) {
            postSplash(f);
            return;
        }

        if("AddZone".equals(f.getName())) {
            postAddZone(f);
            return;
        }

        if("Renderer".equals(f.getName())) {
            postRenderer(f);
            return;
        }

        if("Settings".equals(f.getName())) {
            postSettings(f);
            return;
        }

    }


    protected void postZoneRenderer(Form f) {
    }


    protected void postFriend(Form f) {
    }


    protected void postMainUI(Form f) {
    }


    protected void postSplash(Form f) {
    }


    protected void postAddZone(Form f) {
    }


    protected void postRenderer(Form f) {
    }


    protected void postSettings(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("ZoneRenderer".equals(c.getName())) {
            postContainerZoneRenderer(c);
            return;
        }

        if("Friend".equals(c.getName())) {
            postContainerFriend(c);
            return;
        }

        if("MainUI".equals(c.getName())) {
            postContainerMainUI(c);
            return;
        }

        if("Splash".equals(c.getName())) {
            postContainerSplash(c);
            return;
        }

        if("AddZone".equals(c.getName())) {
            postContainerAddZone(c);
            return;
        }

        if("Renderer".equals(c.getName())) {
            postContainerRenderer(c);
            return;
        }

        if("Settings".equals(c.getName())) {
            postContainerSettings(c);
            return;
        }

    }


    protected void postContainerZoneRenderer(Container c) {
    }


    protected void postContainerFriend(Container c) {
    }


    protected void postContainerMainUI(Container c) {
    }


    protected void postContainerSplash(Container c) {
    }


    protected void postContainerAddZone(Container c) {
    }


    protected void postContainerRenderer(Container c) {
    }


    protected void postContainerSettings(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("ZoneRenderer".equals(rootName)) {
            onCreateZoneRenderer();
            return;
        }

        if("Friend".equals(rootName)) {
            onCreateFriend();
            return;
        }

        if("MainUI".equals(rootName)) {
            onCreateMainUI();
            return;
        }

        if("Splash".equals(rootName)) {
            onCreateSplash();
            return;
        }

        if("AddZone".equals(rootName)) {
            onCreateAddZone();
            return;
        }

        if("Renderer".equals(rootName)) {
            onCreateRenderer();
            return;
        }

        if("Settings".equals(rootName)) {
            onCreateSettings();
            return;
        }

    }


    protected void onCreateZoneRenderer() {
    }


    protected void onCreateFriend() {
    }


    protected void onCreateMainUI() {
    }


    protected void onCreateSplash() {
    }


    protected void onCreateAddZone() {
    }


    protected void onCreateRenderer() {
    }


    protected void onCreateSettings() {
    }

    protected boolean setListModel(List cmp) {
        String listName = cmp.getName();
        if("timeSlider".equals(listName)) {
            return initListModelTimeSlider(cmp);
        }
        if("addZoneList".equals(listName)) {
            return initListModelAddZoneList(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelTimeSlider(List cmp) {
        return false;
    }

    protected boolean initListModelAddZoneList(List cmp) {
        return false;
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        if(rootContainerName == null) return;
        if(rootContainerName.equals("ZoneRenderer")) {
            if("selected".equals(c.getName())) {
                onZoneRenderer_SelectedAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Friend")) {
            if("removeFriend".equals(c.getName())) {
                onFriend_RemoveFriendAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("MainUI")) {
            if("timeSlider".equals(c.getName())) {
                onMainUI_TimeSliderAction(c, event);
                return;
            }
            if("addEntriesButton".equals(c.getName())) {
                onMainUI_AddEntriesButtonAction(c, event);
                return;
            }
            if("removeModeButton".equals(c.getName())) {
                onMainUI_RemoveModeButtonAction(c, event);
                return;
            }
            if("settingsButton".equals(c.getName())) {
                onMainUI_SettingsButtonAction(c, event);
                return;
            }
            if("exitButton".equals(c.getName())) {
                onMainUI_ExitButtonAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("AddZone")) {
            if("worldZone".equals(c.getName())) {
                onAddZone_WorldZoneAction(c, event);
                return;
            }
            if("friendZone".equals(c.getName())) {
                onAddZone_FriendZoneAction(c, event);
                return;
            }
            if("selected".equals(c.getName())) {
                onAddZone_SelectedAction(c, event);
                return;
            }
            if("addZoneList".equals(c.getName())) {
                onAddZone_AddZoneListAction(c, event);
                return;
            }
            if("Button".equals(c.getName())) {
                onAddZone_ButtonAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Settings")) {
            if("Button".equals(c.getName())) {
                onSettings_ButtonAction(c, event);
                return;
            }
            if("civilianTimeCheckbox".equals(c.getName())) {
                onSettings_CivilianTimeCheckboxAction(c, event);
                return;
            }
        }
    }

      protected void onZoneRenderer_SelectedAction(Component c, ActionEvent event) {
      }

      protected void onFriend_RemoveFriendAction(Component c, ActionEvent event) {
      }

      protected void onMainUI_TimeSliderAction(Component c, ActionEvent event) {
      }

      protected void onMainUI_AddEntriesButtonAction(Component c, ActionEvent event) {
      }

      protected void onMainUI_RemoveModeButtonAction(Component c, ActionEvent event) {
      }

      protected void onMainUI_SettingsButtonAction(Component c, ActionEvent event) {
      }

      protected void onMainUI_ExitButtonAction(Component c, ActionEvent event) {
      }

      protected void onAddZone_WorldZoneAction(Component c, ActionEvent event) {
      }

      protected void onAddZone_FriendZoneAction(Component c, ActionEvent event) {
      }

      protected void onAddZone_SelectedAction(Component c, ActionEvent event) {
      }

      protected void onAddZone_AddZoneListAction(Component c, ActionEvent event) {
      }

      protected void onAddZone_ButtonAction(Component c, ActionEvent event) {
      }

      protected void onSettings_ButtonAction(Component c, ActionEvent event) {
      }

      protected void onSettings_CivilianTimeCheckboxAction(Component c, ActionEvent event) {
      }

}
