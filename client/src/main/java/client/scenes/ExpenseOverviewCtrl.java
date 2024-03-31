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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;

/**
 * Controller class for the Expense Overview FXML UI.
 */
public class ExpenseOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private Button backButton;
    @FXML
    private Label expenseNameLabel;
    @FXML
    private Label expenseAmountLabel;
    @FXML
    private Button addParticipantButton;
    @FXML
    private Button manageButton;
    @FXML
    private Label participantCountLabel;
    @FXML
    private FlowPane participantsFlowPane;
    private Expense expense;
    private Event event;
    private ResourceBundle resources;

    @Inject
    public ExpenseOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Populates the UI with appropriate data from the expense object.
     */
    public void populate() {
        if (expense == null || event == null) {
            return;
        }
        // Initialize UI with expense data
        expenseNameLabel.setText(expense.getDescription());
        expenseAmountLabel.setText("€ " + expense.getPaid().toString());
        participantCountLabel.setText(Integer.toString(expense.getParticipants().size() + 1));

        // Create tag
        expenseNameLabel.setGraphic(PaneCreator.createTagItem(expense.getTag()));

        // Populate participants
        participantsFlowPane.getChildren().setAll();
        participantsFlowPane.getChildren().add(createRecipientCard(expense.getReceiver()));
        System.out.println("Created a recipient card instead of a normal participant card");
        for (Person participant : expense.getParticipants()) {
            participantsFlowPane.getChildren().add(createParticipantCard(participant));
            System.out.println("Created a regular participant card");
        }
        participantsFlowPane.requestLayout();

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
        participantLabel.setFont(globalFont);
        participantLabel.setLayoutX(12.5);
        participantLabel.setLayoutY(7.5);

        card.getChildren().add(participantLabel);
        return card;
    }


    /**
     * Creates a new Participant card for the dynamically scaled FlowPane.
     *
     * @param participant The participant
     * @return An anchor pane
     */
    private AnchorPane createParticipantCard(Person participant) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(475, 50);
        card.setStyle(
            "-fx-border-color: lightgrey; -fx-border-width: 2px; -fx-border-radius: 5px;");

        String participantRepresentation =
            participant.getFirstName() + " " + participant.getLastName();
        Label participantLabel = new Label(participantRepresentation);
        Font globalFont = new Font("System Bold", 24);
        participantLabel.setFont(globalFont);
        participantLabel.setLayoutX(12.5);
        participantLabel.setLayoutY(7.5);

        card.getChildren().add(participantLabel);
        return card;
    }


    @FXML
    private void onAddParticipantClicked() {
        mainCtrl.showExpenseAddParticipantPopup(expense, event);
    }

    @FXML
    private void onManageClicked() {
        mainCtrl.showManageExpensePopup(expense, event);
    }

    @FXML
    private void handleExit() {
        mainCtrl.showEventOverview(event, false);
    }

    /**
     * Updates the loaded expanse and event.
     */
    public void refetch() {
        if (this.expense == null || this.event == null) {
            return;
        }

        this.expense = server.getExpenseById(expense.getId());
        this.event = server.getEventById(event.getId());
        populate();
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