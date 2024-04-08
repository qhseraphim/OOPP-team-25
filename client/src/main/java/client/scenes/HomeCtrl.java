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

import static client.utils.PaneCreator.createEventItem;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import java.net.URL;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Home screen.
 */
public class HomeCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ResourceBundle resources;

    @FXML
    private ComboBox<String> dropDown;

    @FXML
    private VBox eventList;
    private List<Event> events;

    @FXML
    private TextField eventCodeTextField;

    @FXML
    private Label serverStatus;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        /*
        Makes options for the dropdown menu
        TODO should set a new server in the config file and ask to reboot
        */
        ObservableList<String> options = FXCollections.observableArrayList(
            "Server 1",
            "Server 2",
            "Server 3"
        );
        dropDown.setValue("Server 1");
        dropDown.setItems(options);
    }

    /**
     * Gets called on showHome to request data.
     * It reads all stored event codes from config files and iterates through them.
     */
    public void refetch() {
        events = new ArrayList<>();

        /*
            Will check if the event exists and add it, if it does not exist but the connection is
            valid it means that should be removed from the config
        */
        for (String code : Main.configManager.getCodes()) {
            try {
                Event event = server.getEventByCode(code);
                if (event != null) {
                    events.add(event);
                } else {
                    Main.configManager.removeCode(code);
                }
            } catch (Exception e) {
                System.err.println("Couldn't get event from server: " + e);
            }
        }

        populate();
    }

    /**
     * Populate the screen.
     */
    public void populate() {
        if (events == null) {
            return;
        }
        eventList.getChildren()
            .setAll(
                events.stream().map(e -> createEventItem(e, this::handleClickEvent)).toList());
    }

    private void handleClickEvent(Event event) {
        mainCtrl.showEventOverview(event, false);
    }

    /**
     * Testing function for language switch.
     */
    public void testing() {
        if (mainCtrl.getCurrentLanguage().equals("en")) {
            mainCtrl.changeLanguage("lt");
        } else {
            mainCtrl.changeLanguage("en");
        }
    }

    /**
     * Logic for the "language" button on home.
     */
    public void clickLanguage() {
        System.out.println("Pressed language");
        testing();
    }

    /**
     * Logic for the "currency" button on home.
     */
    public void clickCurrency() {
        System.out.println("Pressed currency.");
    }

    /**
     * Logic for the home title.
     */
    public void clickHome() {
        System.out.println("Pressed home.");
        mainCtrl.showHome();
    }

    /**
     * Adds a new event to the config and home screen based on input field.
     */
    public void joinEvent() {
        String code = "";
        //checks if there is something in the input field
        if (!eventCodeTextField.getText().isEmpty()) {
            code = eventCodeTextField.getText();
            eventCodeTextField.clear();
        } else {
            eventCodeTextField.setPromptText("Please enter a code!");
            return;
        }

        //checks if there is an event with the given code
        try {
            if (server.getEventByCode(code) == null) {
                eventCodeTextField.setPromptText("Event not found");
                return;
            }
        } catch (ServerException e) {
            System.err.println("Couldn't get event from server: " + e);
            eventCodeTextField.setPromptText("Server error");
            return;
        }

        Main.configManager.addCode(code);
        eventCodeTextField.setPromptText("Event added");
        refetch();
    }

    public void createEvent() {
        mainCtrl.showEventCreationPopup();
    }

    @FXML
    private void clickAdminView(ActionEvent actionEvent) {
        mainCtrl.showAdminCredentialsPopup();
    }

    /**
     * Changes the label according to server status.
     */
    public void updateStatus(boolean statusOk) {
        System.out.println("updating to " + statusOk);
        if (statusOk) {
            serverStatus.setStyle("-fx-background-color: #93c47d;");
            serverStatus.setText(resources.getString("home.connected"));
        } else {
            serverStatus.setStyle("-fx-background-color: #e06666;");
            serverStatus.setText(resources.getString("home.disconnected"));
        }
    }
}