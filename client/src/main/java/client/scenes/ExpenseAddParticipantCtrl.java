package client.scenes;

import client.utils.PaneCreator;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Person;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Controller class for adding a participant to an expense.
 *
 */
public class ExpenseAddParticipantCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Label expenseNameLabel;

    @FXML
    private FlowPane availableParticipants;

    @FXML
    private FlowPane currentParticipants;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Label tagLabel;
    private ResourceBundle resources;
    private Expense expense;
    private Event event;

    @Inject
    public ExpenseAddParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * populates the UI with appropiate data from the expense object.
     */
    public void populate() {
        Event event = server.getEvents().getFirst();
        this.expense = event.getExpenses().getFirst();

        // Initialize UI with expense data
        expenseNameLabel.setText(expense.getDescription());

        // Create tag
        expenseNameLabel.setGraphic(PaneCreator.createTagItem(expense.getTag()));


        currentParticipants.getChildren().setAll();
        availableParticipants.getChildren().setAll();

        currentParticipants.getChildren().add(createRecipientCard(expense.getReceiver()));

        // Populate available participants
        for (Person participant : event.getPeople()) {
            if (!expense.getParticipants().contains(participant)
                &&
                !participant.equals(expense.getReceiver())) {
                addParticipantCardToAvailableParticipantFlowPane(participant);
            }
        }

        // Populate current participants
        for (Person participant : expense.getParticipants()) {
            addParticipantCardToCurrentParticipantFlowPane(participant);
        }




    }

    /**
     * Createse a new Participant card for the dynamically scaled FlowPane, and allows
     * switching to other flowpane.
     *
     * @param participant a participant
     * @return An anchor pane associated to the new card.
     */
    private void addParticipantCardToCurrentParticipantFlowPane(Person participant) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(475, 50);
        card.setStyle(
            "-fx-border-color: lightgrey; -fx-border-width: 2px; -fx-border-radius: 5px;");

        String participantRepresentation = participant.getFirstName() + " "
            + participant.getLastName();
        Label participantLabel = new Label(participantRepresentation);
        Font globalFont = new Font("System Bold", 24);
        participantLabel.setFont(globalFont);
        participantLabel.setLayoutX(12.5);
        participantLabel.setLayoutY(7.5);
        participantLabel.setMaxWidth(401);

        participantLabel.setOnMousePressed(event -> {
                //push to available participant pane and remove from expense
                this.expense.getParticipants().remove(participant);
                currentParticipants.getChildren().remove(card);
                addParticipantCardToAvailableParticipantFlowPane(participant);

                currentParticipants.requestLayout();
                server.updateExpense(this.expense);
                mainCtrl.updateAll();
            }
        );

        card.getChildren().add(participantLabel);
        currentParticipants.getChildren().add(card);
    }

    /**
     * Creates a new Participant card for the dynamically scaled FlowPane.
     *
     * @param participant The participant
     * @return An anchor pane
     */
    private AnchorPane createRecipientCard(Person participant) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(475, 50);
        card.setStyle(
            "-fx-border-color: lightgrey; -fx-border-width: 2px; -fx-border-radius: 5px;");

        String participantRepresentation =
            participant.getFirstName() + " " + participant.getLastName();
        System.out.println(participant.getId());
        System.out.println(expense.getReceiver().getId());
        participantRepresentation = participantRepresentation.concat(" (Recipient)");
        Label participantLabel = new Label(participantRepresentation);
        Font globalFont = new Font("System Bold", 24);
        participantLabel.setTextFill(Color.valueOf("#636363"));
        participantLabel.setFont(globalFont);
        participantLabel.setLayoutX(12.5);
        participantLabel.setMaxWidth(401);

        participantLabel.setLayoutY(7.5);

        ImageView lockedImage = new ImageView(new Image("client/icons/locked.png"));
        lockedImage.setLayoutX(426);
        lockedImage.setLayoutY(13);
        lockedImage.setFitHeight(24);
        lockedImage.setFitWidth(24);
        card.getChildren().add(lockedImage);

        card.getChildren().add(participantLabel);
        return card;
    }

    /**
     * Creates a new Participant card for the dynamically scaled FlowPane,
     * and allows switching to other flowpane.
     *
     * @param participant participant
     * @return An anchor pane associated to the new card.
     */
    private void addParticipantCardToAvailableParticipantFlowPane(Person participant) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(475, 50);
        card.setStyle(
            "-fx-border-color: lightgrey; -fx-border-width: 2px; -fx-border-radius: 5px;");

        String participantRepresentation = participant.getFirstName() + " "
            +
            participant.getLastName();
        Label participantLabel = new Label(participantRepresentation);
        Font globalFont = new Font("System Bold", 24);
        participantLabel.setFont(globalFont);
        participantLabel.setLayoutX(12.5);
        participantLabel.setLayoutY(7.5);
        participantLabel.setMaxWidth(401);
        participantLabel.setOnMousePressed(event -> {
                //push to current participant pane and remove from available and add to expense
                this.expense.getParticipants().add(participant);
                availableParticipants.getChildren().remove(card);
                addParticipantCardToCurrentParticipantFlowPane(participant);


                availableParticipants.requestLayout();
                currentParticipants.requestLayout();

                server.updateExpense(this.expense);
                mainCtrl.updateAll();
            }
        );

        card.getChildren().add(participantLabel);
        availableParticipants.getChildren().add(card);
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }


    /**
     * Set what the screen should show.
     *
     * @param expense the expense
     * @param event   the event
     */
    public void update(Expense expense, Event event) {
        this.expense = expense;
        this.event = event;
        populate();
    }
}