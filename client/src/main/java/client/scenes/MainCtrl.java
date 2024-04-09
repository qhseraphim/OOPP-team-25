/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.scenes;

import client.Main;
import client.MyFXML;
import client.utils.CsPair;
import client.utils.WebSocketClient;
import commons.Event;
import commons.Expense;
import commons.Person;
import java.io.File;
import java.util.Locale;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This is the main controller, which holds references to all controllers and scenes.
 */
public class MainCtrl {
    /*
    How to insert a new page: (you can use home as reference)
    Make sure you created a controller and your Page.fxml links:"client.scenes.PageCtrl"
    TODO
    */
    private Stage primaryStage;
    private Stage popup;
    private MyFXML fxml;
    private String savedAdminPassword;
    private CsPair<HomeCtrl> homePair;
    private CsPair<AdminCredentialsCtrl> adminCredentialsPair;
    private CsPair<ExpenseOverviewCtrl> expenseOverviewPair;
    private CsPair<EventOverviewCtrl> eventOverviewPair;
    private CsPair<ManageExpenseCtrl> manageExpensePair;
    private CsPair<ExpenseAddParticipantCtrl> expenseAddParticipantPair;
    private CsPair<AddParticipantCtrl> addParticipantPair;
    private CsPair<ManageParticipantsCtrl> manageParticipantsPair;
    private CsPair<EditParticipantCtrl> editParticipantPair;
    private CsPair<DeleteParticipantConfirmationCtrl> deleteParticipantConfirmationPair;
    private CsPair<AdminOverviewCtrl> adminOverviewPair;
    private CsPair<DeleteEventConfirmationCtrl> deleteEventConfirmationPair;
    private CsPair<CreateEventCtrl> createEventPair;
    private CsPair<LanguageSelectCtrl> languageSelectPair;
    private CsPair<ManageExpenseListCtrl> manageExpenseListCtrlPair;
    private Initializable currentCtrl;
    // private Pair<ExpenseCardCtrl, Parent> expenseCard;
    private WebSocketClient websocketClient;

    /**
     * Main controller initialization.
     *
     * @param primaryStage the primary stage
     * @param fxml         MyFXML class
     */
    public void initialize(Stage primaryStage, MyFXML fxml) {
        this.primaryStage = primaryStage;
        this.fxml = fxml;
        loadAllPairs();

        showHome();
        primaryStage.setResizable(false);
        primaryStage.show();

        websocketClient = new WebSocketClient(Main.configManager.getWsServer(),
            () -> Platform.runLater(this::updateAll));
    }

    private void loadAllPairs() {
        homePair = fxml.load(HomeCtrl.class, "client", "scenes", "Home.fxml");
        homePair.scene.getStylesheets().add("/client/css/global.css");

        languageSelectPair =
            fxml.load(LanguageSelectCtrl.class,
                "client", "scenes", "LanguageSelection.fxml");
        languageSelectPair.scene.getStylesheets().add("/client/css/globals.css");

        adminCredentialsPair =
            fxml.load(AdminCredentialsCtrl.class,
                "client", "scenes", "AdminCredentials.fxml");
        adminCredentialsPair.scene.getStylesheets().add("/client/css/global.css");

        expenseOverviewPair = fxml.load(ExpenseOverviewCtrl.class, "client", "scenes",
            "ExpenseOverview.fxml");
        expenseOverviewPair.scene.getStylesheets().add("/client/css/global.css");

        eventOverviewPair = fxml.load(EventOverviewCtrl.class,
            "client", "scenes", "EventOverview.fxml");
        eventOverviewPair.scene.getStylesheets().add("/client/css/global.css");

        manageExpensePair = fxml.load(ManageExpenseCtrl.class, "client", "scenes",
            "ManageExpense.fxml");
        manageExpensePair.scene.getStylesheets().add("/client/css/global.css");

        expenseAddParticipantPair = fxml.load(ExpenseAddParticipantCtrl.class, "client",
            "scenes", "ExpenseAddParticipant.fxml");
        expenseAddParticipantPair.scene.getStylesheets().add("/client/css/global.css");

        addParticipantPair = fxml.load(AddParticipantCtrl.class,
            "client", "scenes", "AddParticipant.fxml");
        addParticipantPair.scene.getStylesheets().add("/client/css/global.css");

        manageParticipantsPair = fxml.load(ManageParticipantsCtrl.class,
            "client", "scenes", "ManageParticipants.fxml");
        manageParticipantsPair.scene.getStylesheets().add("/client/css/global.css");

        editParticipantPair = fxml.load(EditParticipantCtrl.class,
            "client", "scenes", "EditParticipant.fxml");
        editParticipantPair.scene.getStylesheets().add("/client/css/global.css");

        deleteParticipantConfirmationPair = fxml.load(DeleteParticipantConfirmationCtrl.class,
            "client", "scenes", "DeleteParticipantConfirmation.fxml");
        deleteParticipantConfirmationPair.scene.getStylesheets().add("/client/css/global.css");

        adminOverviewPair = fxml.load(AdminOverviewCtrl.class,
            "client", "scenes", "AdminOverview.fxml");
        adminOverviewPair.scene.getStylesheets().add("/client/css/global.css");

        deleteEventConfirmationPair = fxml.load(DeleteEventConfirmationCtrl.class,
            "client", "scenes", "DeleteEventConfirmation.fxml");
        deleteEventConfirmationPair.scene.getStylesheets().add("/client/css/global.css");

        createEventPair = fxml.load(CreateEventCtrl.class, "client", "scenes", "CreateEvent.fxml");
        createEventPair.scene.getStylesheets().add("/client/css/global.css");

        manageExpenseListCtrlPair = fxml.load(ManageExpenseListCtrl.class,
                "client", "scenes", "ManageExpenseList.fxml");
        manageExpenseListCtrlPair.scene.getStylesheets().add("/client/css/global.css");
    }

    /**
     * Updates all screens with data from the database.
     */
    private void updateAll() {
        homePair.ctrl.refetch();
        expenseOverviewPair.ctrl.refetch();
        eventOverviewPair.ctrl.refetch();
        manageExpensePair.ctrl.refetch();
        manageParticipantsPair.ctrl.refetch();
        editParticipantPair.ctrl.refetch();
        adminOverviewPair.ctrl.refetch();
        manageExpenseListCtrlPair.ctrl.refetch();
    }

    /**
     * Sets primary stage to the Home scene.
     */
    public void showHome() {
        primaryStage.setTitle(fxml.getBundle().getString("home.title"));
        homePair.ctrl.refetch();
        primaryStage.setScene(homePair.scene);
        currentCtrl = homePair.ctrl;
    }

    /**
     * Changes the current language and refreshes everything.
     *
     * @param language a new language (example: "en", "lt")
     */
    public void changeLanguage(String language) {
        Locale locale = Locale.of(language);
        fxml.changeLocale(locale);
        loadAllPairs();
        updateCurrentScreen();
    }

    public String getCurrentLanguage() {
        return fxml.getCurrentLocale().toLanguageTag();
    }

    /**
     * Sets the scene to the screen it was showing before.
     * This is called after changing the language
     */
    private void updateCurrentScreen() {
        switch (currentCtrl) {
            case HomeCtrl homeCtrl -> showHome();

            case ExpenseOverviewCtrl expenseOverviewCtrl ->
                showExpenseOverview(expenseOverviewCtrl.getExpense(),
                    expenseOverviewCtrl.getEvent());

            case EventOverviewCtrl eventOverviewCtrl ->
                showEventOverview(eventOverviewCtrl.getEvent(),
                    eventOverviewCtrl.getGoBackToAdmin());

            case AdminOverviewCtrl adminOverviewCtrl -> showAdminOverview();

            case ManageParticipantsCtrl manageParticipantsCtrl ->
                showManageParticipantsScreen(manageParticipantsCtrl.getEvent());

            default -> {
                System.err.println("Tried to switch language from an unknown screen");
                showHome();
            }
        }

        showLanguageSelectPopup();
    }

    /**
     * Creates an admin credential popup (dialog window) that blocks other windows.
     * Should never be called twice before closing one of the popups.
     */
    public void showAdminCredentialsPopup() {
        // If the user already entered the password, it should directly open the admin overview
        if (adminCredentialsPair.ctrl.savedPasswordIsCorrect()) {
            showAdminOverview();
            return;
        }

        popup = new Stage();
        // Set it to block other windows (you can only click on this popup)
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(fxml.getBundle().getString("admin-credentials.title"));
        popup.setScene(adminCredentialsPair.scene);
        // Making it not resizable also sets it to the size specified in the .fxml file
        // This was the only way I found that fixed that problem
        // (Except the .setMaximized(true), which makes the window flash when it appears)
        // Also, this might be a linux issue only
        popup.setResizable(false);
        popup.show();
    }

    /**
     * Creates an language selection popup (dialog window) that blocks other windows.
     * Should never be called twice before closing one of the popups.
     */
    public void showLanguageSelectPopup() {
        if (popup == null) {
            popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(primaryStage);
            System.out.println("something");
            popup.setResizable(false);
        }

        popup.setTitle(fxml.getBundle().getString("language-select.title"));
        popup.setScene(languageSelectPair.scene);
        popup.show();
    }

    /**
     * Closes the primary popup.
     * Should never be called if the primary popup window is not shown.
     */
    public void closePopup() {
        popup.close();
        popup = null;
    }

    /**
     * Shows the admin view.
     * This method should only be called after entering the correct admin password.
     */
    public void showAdminOverview() {
        primaryStage.setScene(adminOverviewPair.scene);
        primaryStage.setTitle(fxml.getBundle().getString("admin-overview.title"));
        adminOverviewPair.ctrl.refetch();
        currentCtrl = adminOverviewPair.ctrl;
    }

    /**
     * Opens a file chooser dialog which creates a file.
     *
     * @param initialFileName the suggested file name of the file
     * @return the created file. Can be null
     */
    public File createSaveFile(String initialFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(initialFileName);
        return fileChooser.showSaveDialog(primaryStage);
    }

    /**
     * Opens a file chooser dialog which opens a file.
     *
     * @return the opened file. Can be null
     */
    public File openSavedFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }

    /**
     * Show the AddParticipant popup.
     *
     * @param callback function which will be called if the person is added successfully
     */
    public void showAddParticipantPopup(Consumer<Person> callback) {
        popup = new Stage();
        popup.setTitle(fxml.getBundle().getString("add-participant.title"));
        popup.setScene(addParticipantPair.scene);
        addParticipantPair.ctrl.setCallback(callback);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setResizable(false);
        popup.show();
    }


    /**
     * Show the ManageParticipants screen.
     */
    public void showManageParticipantsScreen(Event event) {
        primaryStage.setTitle(fxml.getBundle().getString("manage-participants.title"));
        primaryStage.setScene(manageParticipantsPair.scene);
        manageParticipantsPair.ctrl.update(event);
        currentCtrl = manageParticipantsPair.ctrl;
    }

    /**
     * Show the EditParticipant popup.
     */
    public void showEditParticipantPopup(Person person) {
        popup = new Stage();
        popup.setTitle(fxml.getBundle().getString("edit-participant.title"));
        popup.setScene(editParticipantPair.scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        editParticipantPair.ctrl.update(person);
        popup.show();
        popup.setResizable(false);
    }

    /**
     * Show the DeleteParticipantConfirmation popup.
     */
    public void showDeleteParticipantConfirmationPopup(Runnable callback) {
        popup = new Stage();
        popup.setTitle(fxml.getBundle().getString("delete-participant-confirmation.title"));
        popup.setScene(deleteParticipantConfirmationPair.scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        deleteParticipantConfirmationPair.ctrl.setCallback(callback);
        popup.show();
        popup.setResizable(false);
    }

    /**
     * Show the DeleteEventConfirmation popup.
     *
     * @param deleteCallback the function to be called if the user confirms their action
     */
    public void showDeleteEventConfirmationPopup(Runnable deleteCallback) {
        popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(fxml.getBundle().getString("delete-event-confirmation.title"));
        popup.setScene(deleteEventConfirmationPair.scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        popup.show();
        popup.setResizable(false);
        deleteEventConfirmationPair.ctrl.setCallback(deleteCallback);
    }

    //add step 4 here.

    // public Pair<ExpenseCardCtrl, Parent> getExpenseCard() {
    //     return expenseCard;
    // }

    /**
     * Sets primary stage to the Event overview scene.
     *
     * @param fromAdmin whether the user is coming to this screen from the admin overview
     */
    public void showEventOverview(Event event, boolean fromAdmin) {
        primaryStage.setTitle(fxml.getBundle().getString("event-overview.title"));
        eventOverviewPair.ctrl.update(event);
        if (fromAdmin) {
            eventOverviewPair.ctrl.setGoBackToAdmin(true);
        }
        primaryStage.setScene(eventOverviewPair.scene);
        currentCtrl = eventOverviewPair.ctrl;
    }

    public String getSavedAdminPassword() {
        return savedAdminPassword;
    }

    public void setSavedAdminPassword(String savedAdminPassword) {
        this.savedAdminPassword = savedAdminPassword;
    }

    /**
     * Shows the expense overview.
     *
     * @param expense the expense to show
     * @param event   which event the expense belongs to
     */
    public void showExpenseOverview(Expense expense, Event event) {
        primaryStage.setTitle(fxml.getBundle().getString("expense-overview.title"));
        expenseOverviewPair.ctrl.update(expense, event);
        primaryStage.setScene(expenseOverviewPair.scene);
        currentCtrl = expenseOverviewPair.ctrl;
    }

    /**
     * Show the manage expense popup.
     *
     * @param expense the expense to manage
     * @param event   which event the expense belongs to
     */
    public void showManageExpensePopup(Expense expense, Event event) {
        popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(fxml.getBundle().getString("manage-expense.title"));
        popup.setScene(manageExpensePair.scene);
        popup.setResizable(false);
        manageExpensePair.ctrl.update(expense, event);
        popup.show();
        manageExpensePair.ctrl.defaultStatus();
    }

    /**
     * Show the manage expense popup.
     *
     * @param expense the expense to manage
     * @param event   which event the expense belongs to
     */
    public void showExpenseAddParticipantPopup(Expense expense, Event event) {
        popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(fxml.getBundle().getString("expense-add-participant.title"));
        popup.setScene(expenseAddParticipantPair.scene);
        popup.setResizable(false);
        expenseAddParticipantPair.ctrl.update(expense, event);
        popup.show();
        expenseAddParticipantPair.ctrl.defaultStatus();
    }

    /**
     * Show the Event creation popup.
     */
    // TODO: check if this is correct
    public void showEventCreationPopup() {
        popup = new Stage();
        popup.setTitle(fxml.getBundle().getString("create-event.title"));
        popup.setScene(createEventPair.scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setResizable(false);
        popup.initOwner(primaryStage);
        popup.show();
    }

    /**
     * Show the ManageExpenseListCtrl screen.
     */
    public void showManageExpenseListScreen(Event event) {
        primaryStage.setTitle(fxml.getBundle().getString("manage-expense-list.title"));
        primaryStage.setScene(manageExpenseListCtrlPair.scene);
        manageExpenseListCtrlPair.ctrl.update(event);
        currentCtrl = manageExpenseListCtrlPair.ctrl;
    }

}