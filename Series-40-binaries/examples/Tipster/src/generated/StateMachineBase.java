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

    public com.sun.lwuit.Label findTotal(Container root) {
        return (com.sun.lwuit.Label)findByName("total", root);
    }

    public com.sun.lwuit.Container findContainer4(Container root) {
        return (com.sun.lwuit.Container)findByName("Container4", root);
    }

    public com.sun.lwuit.Container findContainer3(Container root) {
        return (com.sun.lwuit.Container)findByName("Container3", root);
    }

    public com.sun.lwuit.Container findContainer2(Container root) {
        return (com.sun.lwuit.Container)findByName("Container2", root);
    }

    public com.sun.lwuit.Container findContainer1(Container root) {
        return (com.sun.lwuit.Container)findByName("Container1", root);
    }

    public com.sun.lwuit.TextField findBillTotalField(Container root) {
        return (com.sun.lwuit.TextField)findByName("billTotalField", root);
    }

    public com.sun.lwuit.Container findContainer6(Container root) {
        return (com.sun.lwuit.Container)findByName("Container6", root);
    }

    public com.sun.lwuit.Container findContainer5(Container root) {
        return (com.sun.lwuit.Container)findByName("Container5", root);
    }

    public com.sun.lwuit.Container findTypeRendererSelected(Container root) {
        return (com.sun.lwuit.Container)findByName("TypeRendererSelected", root);
    }

    public com.sun.lwuit.Button findButton2(Container root) {
        return (com.sun.lwuit.Button)findByName("Button2", root);
    }

    public com.sun.lwuit.Label findUnsel(Container root) {
        return (com.sun.lwuit.Label)findByName("Unsel", root);
    }

    public com.sun.lwuit.Form findSplash(Container root) {
        return (com.sun.lwuit.Form)findByName("Splash", root);
    }

    public com.sun.lwuit.Label findLowQuality(Container root) {
        return (com.sun.lwuit.Label)findByName("lowQuality", root);
    }

    public com.sun.lwuit.Label findAmount(Container root) {
        return (com.sun.lwuit.Label)findByName("amount", root);
    }

    public com.sun.lwuit.Button findButton(Container root) {
        return (com.sun.lwuit.Button)findByName("Button", root);
    }

    public com.sun.lwuit.Label findLabel1(Container root) {
        return (com.sun.lwuit.Label)findByName("Label1", root);
    }

    public com.sun.lwuit.Label findLabel3(Container root) {
        return (com.sun.lwuit.Label)findByName("Label3", root);
    }

    public com.sun.lwuit.Label findLabel2(Container root) {
        return (com.sun.lwuit.Label)findByName("Label2", root);
    }

    public com.sun.lwuit.Label findTipTotal(Container root) {
        return (com.sun.lwuit.Label)findByName("tipTotal", root);
    }

    public com.sun.lwuit.Form findCurrency(Container root) {
        return (com.sun.lwuit.Form)findByName("Currency", root);
    }

    public com.sun.lwuit.Container findHistoryContainer(Container root) {
        return (com.sun.lwuit.Container)findByName("historyContainer", root);
    }

    public com.sun.lwuit.RadioButton findGbp(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("gbp", root);
    }

    public com.sun.lwuit.RadioButton findInr(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("inr", root);
    }

    public com.sun.lwuit.Label findLabel6(Container root) {
        return (com.sun.lwuit.Label)findByName("Label6", root);
    }

    public com.sun.lwuit.TextField findWorkEffortField(Container root) {
        return (com.sun.lwuit.TextField)findByName("workEffortField", root);
    }

    public com.sun.lwuit.Container findContainer10(Container root) {
        return (com.sun.lwuit.Container)findByName("Container10", root);
    }

    public com.sun.lwuit.Container findHistoryMonth(Container root) {
        return (com.sun.lwuit.Container)findByName("HistoryMonth", root);
    }

    public com.sun.lwuit.Label findServiceType(Container root) {
        return (com.sun.lwuit.Label)findByName("serviceType", root);
    }

    public com.sun.lwuit.Container findBubbleParent(Container root) {
        return (com.sun.lwuit.Container)findByName("bubbleParent", root);
    }

    public com.sun.lwuit.Label findWorkScope(Container root) {
        return (com.sun.lwuit.Label)findByName("workScope", root);
    }

    public com.sun.lwuit.RadioButton findChf(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("chf", root);
    }

    public com.sun.lwuit.Slider findQualitySlider(Container root) {
        return (com.sun.lwuit.Slider)findByName("qualitySlider", root);
    }

    public com.sun.lwuit.Container findCurrencyContainer(Container root) {
        return (com.sun.lwuit.Container)findByName("currencyContainer", root);
    }

    public com.sun.lwuit.Button findButton1(Container root) {
        return (com.sun.lwuit.Button)findByName("Button1", root);
    }

    public com.sun.lwuit.Label findSeparator(Container root) {
        return (com.sun.lwuit.Label)findByName("separator", root);
    }

    public com.sun.lwuit.Label findTitleLabel(Container root) {
        return (com.sun.lwuit.Label)findByName("titleLabel", root);
    }

    public com.sun.lwuit.Container findContainer43(Container root) {
        return (com.sun.lwuit.Container)findByName("Container43", root);
    }

    public com.sun.lwuit.Container findContainer44(Container root) {
        return (com.sun.lwuit.Container)findByName("Container44", root);
    }

    public com.sun.lwuit.Container findContainer45(Container root) {
        return (com.sun.lwuit.Container)findByName("Container45", root);
    }

    public com.sun.lwuit.Container findBubbleContainer(Container root) {
        return (com.sun.lwuit.Container)findByName("bubbleContainer", root);
    }

    public com.sun.lwuit.RadioButton findUsd(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("usd", root);
    }

    public com.sun.lwuit.Label findDate(Container root) {
        return (com.sun.lwuit.Label)findByName("date", root);
    }

    public com.sun.lwuit.Container findTypeRenderer(Container root) {
        return (com.sun.lwuit.Container)findByName("TypeRenderer", root);
    }

    public com.sun.lwuit.Form findMain(Container root) {
        return (com.sun.lwuit.Form)findByName("Main", root);
    }

    public com.sun.lwuit.RadioButton findEur(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("eur", root);
    }

    public com.sun.lwuit.Form findHistory(Container root) {
        return (com.sun.lwuit.Form)findByName("History", root);
    }

    public com.sun.lwuit.Container findEntries(Container root) {
        return (com.sun.lwuit.Container)findByName("entries", root);
    }

    public com.sun.lwuit.Container findHistoryEntry(Container root) {
        return (com.sun.lwuit.Container)findByName("HistoryEntry", root);
    }

    public com.sun.lwuit.Container findContainer(Container root) {
        return (com.sun.lwuit.Container)findByName("Container", root);
    }

    public com.sun.lwuit.List findCarosel(Container root) {
        return (com.sun.lwuit.List)findByName("carosel", root);
    }

    public com.sun.lwuit.RadioButton findJpy(Container root) {
        return (com.sun.lwuit.RadioButton)findByName("jpy", root);
    }

    public com.sun.lwuit.Label findSel(Container root) {
        return (com.sun.lwuit.Label)findByName("Sel", root);
    }

    public com.sun.lwuit.Label findName(Container root) {
        return (com.sun.lwuit.Label)findByName("Name", root);
    }

    public com.sun.lwuit.Label findHighQuality(Container root) {
        return (com.sun.lwuit.Label)findByName("highQuality", root);
    }

    public com.sun.lwuit.Container findContainer42(Container root) {
        return (com.sun.lwuit.Container)findByName("Container42", root);
    }

    public com.sun.lwuit.Container findContainer41(Container root) {
        return (com.sun.lwuit.Container)findByName("Container41", root);
    }

    public com.sun.lwuit.Label findLabel(Container root) {
        return (com.sun.lwuit.Label)findByName("Label", root);
    }

    public com.sun.lwuit.TextField findTotalField(Container root) {
        return (com.sun.lwuit.TextField)findByName("totalField", root);
    }

    public static final int COMMAND_CurrencyCalculator = 3;
    public static final int COMMAND_HistoryCalculator = 4;
    public static final int COMMAND_MainExit = 1;
    public static final int COMMAND_MainTipHistory = 6;
    public static final int COMMAND_MainCurrency = 2;
    public static final int COMMAND_CurrencyTipHistory = 5;

    protected boolean onCurrencyCalculator() {
        return false;
    }

    protected boolean onHistoryCalculator() {
        return false;
    }

    protected boolean onMainExit() {
        return false;
    }

    protected boolean onMainTipHistory() {
        return false;
    }

    protected boolean onMainCurrency() {
        return false;
    }

    protected boolean onCurrencyTipHistory() {
        return false;
    }

    protected void processCommand(ActionEvent ev, Command cmd) {
        switch(cmd.getId()) {
            case COMMAND_CurrencyCalculator:
                if(onCurrencyCalculator()) {
                    ev.consume();
                }
                return;

            case COMMAND_HistoryCalculator:
                if(onHistoryCalculator()) {
                    ev.consume();
                }
                return;

            case COMMAND_MainExit:
                if(onMainExit()) {
                    ev.consume();
                }
                return;

            case COMMAND_MainTipHistory:
                if(onMainTipHistory()) {
                    ev.consume();
                }
                return;

            case COMMAND_MainCurrency:
                if(onMainCurrency()) {
                    ev.consume();
                }
                return;

            case COMMAND_CurrencyTipHistory:
                if(onCurrencyTipHistory()) {
                    ev.consume();
                }
                return;

        }
    }

    protected void exitForm(Form f) {
        if("History".equals(f.getName())) {
            exitHistory(f);
            return;
        }

        if("Splash".equals(f.getName())) {
            exitSplash(f);
            return;
        }

        if("TypeRendererSelected".equals(f.getName())) {
            exitTypeRendererSelected(f);
            return;
        }

        if("Currency".equals(f.getName())) {
            exitCurrency(f);
            return;
        }

        if("Main".equals(f.getName())) {
            exitMain(f);
            return;
        }

        if("HistoryMonth".equals(f.getName())) {
            exitHistoryMonth(f);
            return;
        }

        if("TypeRenderer".equals(f.getName())) {
            exitTypeRenderer(f);
            return;
        }

        if("HistoryEntry".equals(f.getName())) {
            exitHistoryEntry(f);
            return;
        }

    }


    protected void exitHistory(Form f) {
    }


    protected void exitSplash(Form f) {
    }


    protected void exitTypeRendererSelected(Form f) {
    }


    protected void exitCurrency(Form f) {
    }


    protected void exitMain(Form f) {
    }


    protected void exitHistoryMonth(Form f) {
    }


    protected void exitTypeRenderer(Form f) {
    }


    protected void exitHistoryEntry(Form f) {
    }

    protected void beforeShow(Form f) {
        if("History".equals(f.getName())) {
            beforeHistory(f);
            return;
        }

        if("Splash".equals(f.getName())) {
            beforeSplash(f);
            return;
        }

        if("TypeRendererSelected".equals(f.getName())) {
            beforeTypeRendererSelected(f);
            return;
        }

        if("Currency".equals(f.getName())) {
            beforeCurrency(f);
            return;
        }

        if("Main".equals(f.getName())) {
            beforeMain(f);
            return;
        }

        if("HistoryMonth".equals(f.getName())) {
            beforeHistoryMonth(f);
            return;
        }

        if("TypeRenderer".equals(f.getName())) {
            beforeTypeRenderer(f);
            return;
        }

        if("HistoryEntry".equals(f.getName())) {
            beforeHistoryEntry(f);
            return;
        }

    }


    protected void beforeHistory(Form f) {
    }


    protected void beforeSplash(Form f) {
    }


    protected void beforeTypeRendererSelected(Form f) {
    }


    protected void beforeCurrency(Form f) {
    }


    protected void beforeMain(Form f) {
    }


    protected void beforeHistoryMonth(Form f) {
    }


    protected void beforeTypeRenderer(Form f) {
    }


    protected void beforeHistoryEntry(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        if("History".equals(c.getName())) {
            beforeContainerHistory(c);
            return;
        }

        if("Splash".equals(c.getName())) {
            beforeContainerSplash(c);
            return;
        }

        if("TypeRendererSelected".equals(c.getName())) {
            beforeContainerTypeRendererSelected(c);
            return;
        }

        if("Currency".equals(c.getName())) {
            beforeContainerCurrency(c);
            return;
        }

        if("Main".equals(c.getName())) {
            beforeContainerMain(c);
            return;
        }

        if("HistoryMonth".equals(c.getName())) {
            beforeContainerHistoryMonth(c);
            return;
        }

        if("TypeRenderer".equals(c.getName())) {
            beforeContainerTypeRenderer(c);
            return;
        }

        if("HistoryEntry".equals(c.getName())) {
            beforeContainerHistoryEntry(c);
            return;
        }

    }


    protected void beforeContainerHistory(Container c) {
    }


    protected void beforeContainerSplash(Container c) {
    }


    protected void beforeContainerTypeRendererSelected(Container c) {
    }


    protected void beforeContainerCurrency(Container c) {
    }


    protected void beforeContainerMain(Container c) {
    }


    protected void beforeContainerHistoryMonth(Container c) {
    }


    protected void beforeContainerTypeRenderer(Container c) {
    }


    protected void beforeContainerHistoryEntry(Container c) {
    }

    protected void postShow(Form f) {
        if("History".equals(f.getName())) {
            postHistory(f);
            return;
        }

        if("Splash".equals(f.getName())) {
            postSplash(f);
            return;
        }

        if("TypeRendererSelected".equals(f.getName())) {
            postTypeRendererSelected(f);
            return;
        }

        if("Currency".equals(f.getName())) {
            postCurrency(f);
            return;
        }

        if("Main".equals(f.getName())) {
            postMain(f);
            return;
        }

        if("HistoryMonth".equals(f.getName())) {
            postHistoryMonth(f);
            return;
        }

        if("TypeRenderer".equals(f.getName())) {
            postTypeRenderer(f);
            return;
        }

        if("HistoryEntry".equals(f.getName())) {
            postHistoryEntry(f);
            return;
        }

    }


    protected void postHistory(Form f) {
    }


    protected void postSplash(Form f) {
    }


    protected void postTypeRendererSelected(Form f) {
    }


    protected void postCurrency(Form f) {
    }


    protected void postMain(Form f) {
    }


    protected void postHistoryMonth(Form f) {
    }


    protected void postTypeRenderer(Form f) {
    }


    protected void postHistoryEntry(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("History".equals(c.getName())) {
            postContainerHistory(c);
            return;
        }

        if("Splash".equals(c.getName())) {
            postContainerSplash(c);
            return;
        }

        if("TypeRendererSelected".equals(c.getName())) {
            postContainerTypeRendererSelected(c);
            return;
        }

        if("Currency".equals(c.getName())) {
            postContainerCurrency(c);
            return;
        }

        if("Main".equals(c.getName())) {
            postContainerMain(c);
            return;
        }

        if("HistoryMonth".equals(c.getName())) {
            postContainerHistoryMonth(c);
            return;
        }

        if("TypeRenderer".equals(c.getName())) {
            postContainerTypeRenderer(c);
            return;
        }

        if("HistoryEntry".equals(c.getName())) {
            postContainerHistoryEntry(c);
            return;
        }

    }


    protected void postContainerHistory(Container c) {
    }


    protected void postContainerSplash(Container c) {
    }


    protected void postContainerTypeRendererSelected(Container c) {
    }


    protected void postContainerCurrency(Container c) {
    }


    protected void postContainerMain(Container c) {
    }


    protected void postContainerHistoryMonth(Container c) {
    }


    protected void postContainerTypeRenderer(Container c) {
    }


    protected void postContainerHistoryEntry(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("History".equals(rootName)) {
            onCreateHistory();
            return;
        }

        if("Splash".equals(rootName)) {
            onCreateSplash();
            return;
        }

        if("TypeRendererSelected".equals(rootName)) {
            onCreateTypeRendererSelected();
            return;
        }

        if("Currency".equals(rootName)) {
            onCreateCurrency();
            return;
        }

        if("Main".equals(rootName)) {
            onCreateMain();
            return;
        }

        if("HistoryMonth".equals(rootName)) {
            onCreateHistoryMonth();
            return;
        }

        if("TypeRenderer".equals(rootName)) {
            onCreateTypeRenderer();
            return;
        }

        if("HistoryEntry".equals(rootName)) {
            onCreateHistoryEntry();
            return;
        }

    }


    protected void onCreateHistory() {
    }


    protected void onCreateSplash() {
    }


    protected void onCreateTypeRendererSelected() {
    }


    protected void onCreateCurrency() {
    }


    protected void onCreateMain() {
    }


    protected void onCreateHistoryMonth() {
    }


    protected void onCreateTypeRenderer() {
    }


    protected void onCreateHistoryEntry() {
    }

    protected boolean setListModel(List cmp) {
        String listName = cmp.getName();
        if("carosel".equals(listName)) {
            return initListModelCarosel(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelCarosel(List cmp) {
        return false;
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        if(rootContainerName == null) return;
        if(rootContainerName.equals("History")) {
            if("Button".equals(c.getName())) {
                onHistory_ButtonAction(c, event);
                return;
            }
            if("Button1".equals(c.getName())) {
                onHistory_Button1Action(c, event);
                return;
            }
            if("Button2".equals(c.getName())) {
                onHistory_Button2Action(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Splash")) {
            if("carosel".equals(c.getName())) {
                onSplash_CaroselAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Currency")) {
            if("Button".equals(c.getName())) {
                onCurrency_ButtonAction(c, event);
                return;
            }
            if("Button1".equals(c.getName())) {
                onCurrency_Button1Action(c, event);
                return;
            }
            if("Button2".equals(c.getName())) {
                onCurrency_Button2Action(c, event);
                return;
            }
            if("usd".equals(c.getName())) {
                onCurrency_UsdAction(c, event);
                return;
            }
            if("eur".equals(c.getName())) {
                onCurrency_EurAction(c, event);
                return;
            }
            if("chf".equals(c.getName())) {
                onCurrency_ChfAction(c, event);
                return;
            }
            if("jpy".equals(c.getName())) {
                onCurrency_JpyAction(c, event);
                return;
            }
            if("inr".equals(c.getName())) {
                onCurrency_InrAction(c, event);
                return;
            }
            if("gbp".equals(c.getName())) {
                onCurrency_GbpAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Main")) {
            if("Button".equals(c.getName())) {
                onMain_ButtonAction(c, event);
                return;
            }
            if("Button1".equals(c.getName())) {
                onMain_Button1Action(c, event);
                return;
            }
            if("Button2".equals(c.getName())) {
                onMain_Button2Action(c, event);
                return;
            }
            if("billTotalField".equals(c.getName())) {
                onMain_BillTotalFieldAction(c, event);
                return;
            }
            if("workEffortField".equals(c.getName())) {
                onMain_WorkEffortFieldAction(c, event);
                return;
            }
            if("totalField".equals(c.getName())) {
                onMain_TotalFieldAction(c, event);
                return;
            }
            if("carosel".equals(c.getName())) {
                onMain_CaroselAction(c, event);
                return;
            }
        }
    }

      protected void onHistory_ButtonAction(Component c, ActionEvent event) {
      }

      protected void onHistory_Button1Action(Component c, ActionEvent event) {
      }

      protected void onHistory_Button2Action(Component c, ActionEvent event) {
      }

      protected void onSplash_CaroselAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_ButtonAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_Button1Action(Component c, ActionEvent event) {
      }

      protected void onCurrency_Button2Action(Component c, ActionEvent event) {
      }

      protected void onCurrency_UsdAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_EurAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_ChfAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_JpyAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_InrAction(Component c, ActionEvent event) {
      }

      protected void onCurrency_GbpAction(Component c, ActionEvent event) {
      }

      protected void onMain_ButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_Button1Action(Component c, ActionEvent event) {
      }

      protected void onMain_Button2Action(Component c, ActionEvent event) {
      }

      protected void onMain_BillTotalFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_WorkEffortFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_TotalFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_CaroselAction(Component c, ActionEvent event) {
      }

}
