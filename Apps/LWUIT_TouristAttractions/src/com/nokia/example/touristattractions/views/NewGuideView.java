/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.views;

import com.nokia.example.touristattractions.lists.NewGuideList;
import com.nokia.example.touristattractions.lists.NewGuideListRenderer;
import com.nokia.example.touristattractions.main.TouristMidlet;
import com.nokia.example.touristattractions.models.Guide;
import com.nokia.example.touristattractions.network.NewGuidesOperation;
import com.nokia.example.touristattractions.util.Util;
import com.nokia.example.touristattractions.util.Visual;
import com.nokia.example.touristattractions.views.components.LoadingComponent;
import com.nokia.mid.payment.IAPClientPaymentListener;
import com.nokia.mid.payment.IAPClientPaymentManager;
import com.nokia.mid.payment.IAPClientProductData;
import com.nokia.mid.payment.IAPClientUserAndDeviceData;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * View to display a list of purchasable guides.
 */
public final class NewGuideView
        extends View
        implements IAPClientPaymentListener {

    private Form loadingForm;
    private LoadingComponent loadingComp; // loading component
    private static final String NEW_GUIDES_URL =
            "http://fn-iap-repo.cloudapp.net/api/touristattractions"; // back end url
    private static final String GUIDE_URL_PREFIX = NEW_GUIDES_URL + "/";
    private NewGuideList newGuideList; // list of new guides to purchase
    private volatile String account = null;
    private Button loginButton;
    /* Loading indicators */
    private volatile boolean loadingAccount = false;
    private volatile boolean loadingGuides = false;
    private IAPClientPaymentManager manager = null; // IAP helper class
    private volatile Guide newGuide = null; // guide being purchased
    private Hashtable waitingProductData;
    // Indicate if pointer has been dragged after press event
    private boolean dragStarted = false;

    public NewGuideView(TouristMidlet midlet) {
        super(midlet);
        setTitle("Buy guide");
        setLayout(new BorderLayout());
        getStyle().setBgColor(Visual.BACKGROUND_COLOR);

        loadingForm = new Form("Buy guide");
        loadingForm.setLayout(new BorderLayout());
        loadingComp = new LoadingComponent(this);
        loadingForm.addComponent(BorderLayout.CENTER, loadingComp);

        /* See the flow of In-App Purchasing at:
         * http://www.developer.nokia.com/Resources/Library/Java/#!developers-guides/in-app-purchase.html */
        try {
            manager = IAPClientPaymentManager.getIAPClientPaymentManager();
            IAPClientPaymentManager.setIAPClientPaymentListener(this);
        } catch (Exception e) {
            Util.showAlert(e.getClass().getName(), "Could not initialize In-App Purchase manager");
        }

        newGuideList = new NewGuideList(this);
        addComponent(BorderLayout.CENTER, newGuideList);

        loginButton = new Button("Login to see past purchases");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                loadAccountAndGuides();
            }
        });
        addCommands();

        revalidate();
    }

    private void addCommands() {
        Command backCommand = new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                NewGuideView.this.midlet.showGuideView();
            }
        };
        setBackCommand(backCommand);
        addCommand(backCommand);
        loadingForm.setBackCommand(backCommand);
        loadingForm.addCommand(backCommand);
    }

    /**
     * Shows this view and loads IAP account and guides.
     */
    public void show() {
        super.show();

        if (newGuideList.getListModel().getSize() == 0) {
            loadGuides();
        }

        if (account == null && !this.contains(loginButton)) {
            addComponent(BorderLayout.NORTH, loginButton);
        } else if (account != null && this.contains(loginButton)) {
            removeComponent(loginButton);
        }
    }

    /**
     * Shows/hides loading indicator.
     *
     * @param show True to show the indicator, false to hide
     */
    public void showLoading(boolean show) {
        if (show && Display.getInstance().getCurrent() != loadingForm && (loadingAccount || loadingGuides)) {
            loadingForm.show();
            loadingComp.startSpin();
            revalidate();
        } else if (!show) {
            loadingComp.stopSpin();
            this.show();
            revalidate();
        }
    }


    /* some custom handling added for a list item highlight after tapping */
    public void pointerPressed(int x, int y) {
        ((NewGuideListRenderer) newGuideList.getRenderer()).setReleased(false);
        super.pointerPressed(x, y);
    }

    public void pointerDragged(int x, int y) {
        dragStarted = true;
        super.pointerDragged(x, y);
    }

    public void pointerReleased(int x, int y) {
        if (!dragStarted && !getMenuBar().contains(x, y)) {
            ((NewGuideListRenderer) newGuideList.getRenderer()).setReleased(true);
        }
        super.pointerReleased(x, y);
        dragStarted = false;
    }

    public final void productDataReceived(int status,
            IAPClientProductData productData) {
        newGuide.setCity(productData.getShortDescription());
        newGuide.setPrice(productData.getPrice());
        newGuide.setUrl(GUIDE_URL_PREFIX + newGuide.getId());
        manager.purchaseProduct(newGuide.getId(),
                IAPClientPaymentManager.FORCED_AUTOMATIC_RESTORATION);
    }

    /**
     * @see IAPClientPaymentListener#purchaseCompleted(int, java.lang.String)
     */
    public final void purchaseCompleted(int status, String purchaseTicket) {
        switch (status) {
            case OK:
                guidePurchased(purchaseTicket);
                break;
            case RESTORABLE:
                guidePurchased(purchaseTicket == null ? "" : purchaseTicket);
                break;
            default:
                newGuide = null;
                Util.showAlert("Purchase failure", getPaymentError(status));
                break;
        }
    }

    /**
     * @see IAPClientPaymentListener#restorationCompleted(int, java.lang.String)
     */
    public final void restorationCompleted(int status, String purchaseTicket) {
        switch (status) {
            case OK:
                guidePurchased(purchaseTicket);
                break;
            default:
                newGuide = null;
                Util.showAlert("Restoration failure", getPaymentError(status));
                break;
        }
    }

    /**
     * Called when guide is purchased. Adds the new guide to the guide list and
     * saves it to RMS.
     *
     * @param purchaseTicket
     */
    public void guidePurchased(String purchaseTicket) {
        if (newGuide == null) {
            return;
        }

        newGuide.setPurchaseTicket(purchaseTicket);
        newGuideList.getListModel().removeItem(newGuide);
        midlet.getGuideView().getGuideList().getListModel().addItem(newGuide);
        midlet.showAttractionView(newGuide);
        midlet.saveNewGuides();
        newGuide = null;
        midlet.getAttractionView().showLoading(true);
    }

    /**
     * @param code
     * @return Error string according to the code
     */
    public final String getPaymentError(int code) {
        switch (code) {
            case IAPClientPaymentListener.NO_PMT_METHODS:
                return "No payment methods are set up in your Nokia account. "
                        + "Please enable some of the methods to be able to "
                        + "proceed.";
            case IAPClientPaymentListener.OVI_SIGN_IN_FAILED:
                return "Cannot sign in to your Nokia Account. Please check "
                        + "your credentials.";
            case IAPClientPaymentListener.RESTORATION_DEVICE_LMT_EXCEEDED:
                return "Unfortunately the number of restorations allowed on "
                        + "the device has exceeded limit.";
            case IAPClientPaymentListener.RESTORATION_LMT_EXCEEDED:
                return "Unfortunately the number of restorations allowed for "
                        + "this item has exceeded limit.";
            case IAPClientPaymentListener.RESTORATION_FAILED:
            case IAPClientPaymentListener.RESTORATION_NOT_ALLOWED:
                return "It seems you haven't purchased the guide yet, and "
                        + "therefore restoration is not"
                        + "possible. Please, use the purchasing options.";
            case IAPClientPaymentListener.SMS_PMT_FAILED:
                return "Sending SMS has failed in operator payment.";
            default:
                return "There was a problem with Nokia Services. Please try "
                        + "again later. If the problem persists, please "
                        + "contact the application vendor.";
        }
    }

    /**
     * Purchases a guide.
     *
     * @param guide Guide to be purchased.
     */
    public void purchase(Guide guide) {
        int status = manager.purchaseProduct(guide.getId(),
                IAPClientPaymentManager.FORCED_AUTOMATIC_RESTORATION);
        if (status != IAPClientPaymentManager.SUCCESS) {
            guide = null;
            Util.showAlert("Purchase failure", getPaymentError(status));
        }
    }

    /**
     * Handles clicked product whether it has to be purchased or restored.
     *
     * @param guide Clicked guide
     */
    public void handleClickedProduct(Guide guide) {
        newGuide = guide;
        
        // If the user is not logged in, show login dialog, refresh products
        // and then restore or buy
        if (account == null) {
            loadAccountAndGuides();
        } // User is logged in, restore or buy
        else if (newGuide.isRestorable()) {
            // Restore guide from the backend
            guidePurchased("");
        } else {
            // Purchase guide using Nokia Store
            purchase(newGuide);
        }

    }

    /**
     * @see IAPClientPaymentListener#userAndDeviceDataReceived(int,
     * com.nokia.mid.payment.IAPClientUserAndDeviceData)
     */
    public final void userAndDeviceDataReceived(int status,
            IAPClientUserAndDeviceData ud) {
        loadingAccount = false;
        if (status == OK) {
            account = ud.getAccount();
            loadGuides();
        } else {
            Util.showAlert("Authorization failure", getPaymentError(status));
            showLoading(false);
        }
    }

    /**
     * @see IAPClientPaymentListener#productDataListReceived(int,
     * com.nokia.mid.payment.IAPClientProductData[])
     */
    public final void productDataListReceived(int status,
            IAPClientProductData[] productDataList) {
        if (status == OK) {
            if (productDataList.length == 0) {
                Util.showAlert("No products", "There are no products available.");
                midlet.showGuideView();
            }

            for (int i = 0, size = productDataList.length; i < size; i++) {
                IAPClientProductData productData = productDataList[i];

                if (productData.getProductId() != null) {
                    Guide guideProduct = (Guide) waitingProductData.remove(
                            productData.getProductId());
                    String title = productData.getTitle();

                    if (title == null) {
                        title = productData.getShortDescription();
                    }

                    if (title == null) {
                        title = "unknown";
                    }

                    String price = productData.getPrice();
                    if (price == null) {
                        price = "unknown";
                    }

                    if (guideProduct != null) {
                        guideProduct.setCity(title);
                        guideProduct.setPrice(price);
                        newGuideList.addItem(guideProduct);
                        repaint();
                    }
                    
                    // A guide was clicked before logging in and refreshing 
                    // the product list. Proceed with the purchase / restore.
                    if (newGuide != null && guideProduct.getId().equals(
                        newGuide.getId())) {
                        newGuide = null;
                        loadingGuides = false;
                        handleClickedProduct(guideProduct);
                        return;
                    }
                }
            }
        } else {
            Util.showAlert("Loading failure", "Loading new guides failed: "
                    + getPaymentError(status));
            midlet.showGuideView();
        }
        waitingProductData = null;
        loadingGuides = false;
        showLoading(false);
        repaint();
    }

    public final void restorableProductsReceived(int status,
            IAPClientProductData[] productDataList) {
    }

    private Vector getOwnedGuides() {
        return midlet.getGuideView().getGuideList().getListModel().getItems();
    }

    private void loadGuides() {
        if (loadingGuides) {
            return;
        }

        loadingGuides = true;        
        showLoading(true);
        newGuideList.getListModel().removeAll();

        new NewGuidesOperation(new NewGuidesOperation.Listener() {
            public void guidesReceived(Vector newGuides) {
                if (newGuides == null) {
                    Util.showAlert("Network failure",
                            "Connecting network failed.");
                    midlet.showGuideView();
                    loadingGuides = false;
                } else {
                    // Populate guide list
                    boolean emptyList = true;
                    waitingProductData = new Hashtable();

                    for (int i = 0, length = newGuides.size(); i < length; i++) {
                        Guide guide = (Guide) newGuides.elementAt(i);
                        Vector ownedGuides = getOwnedGuides();
                        if (!ownedGuides.contains(guide)) {
                            emptyList = false;
                            waitingProductData.put(guide.getId(), guide);
                            guide.setUrl(GUIDE_URL_PREFIX + guide.getId());
                            guide.setAccount(account);
                        }
                    }

                    if (emptyList) {
                        Util.showAlert("No products",
                                "There are no more products available.");
                        loadingGuides = false;
                        midlet.showGuideView();                        
                    } else {
                        loadProductData();
                    }

                }
            }
        }, NEW_GUIDES_URL, account).start();
    }

    private void loadProductData() {
        if (waitingProductData.isEmpty()) {
            waitingProductData = null;
            loadingGuides = false;
            return;
        }

        String[] productIds = new String[waitingProductData.size()];
        Enumeration e = waitingProductData.keys();

        for (int i = 0; e.hasMoreElements(); i++) {
            productIds[i] = ((String) e.nextElement());
        }

        int status = manager.getProductData(productIds);
        if (status != IAPClientPaymentManager.SUCCESS) {
            waitingProductData = null;
            Util.showAlert("Metadata failure", getPaymentError(status));
            midlet.showGuideView();
            loadingGuides = false;
        }
    }

    private void loadAccountAndGuides() {
        if (loadingAccount) {
            return;
        }

        loadingAccount = true;
        showLoading(true);

        // For restoring guides user's account is needed
        int status = manager.getUserAndDeviceId(
                IAPClientPaymentManager.DEFAULT_AUTHENTICATION);

        if (status != IAPClientPaymentManager.SUCCESS) {
            Util.showAlert("Authorization failure", getPaymentError(status));
            newGuide = null;
            loadingAccount = false;
        }

    }
}
