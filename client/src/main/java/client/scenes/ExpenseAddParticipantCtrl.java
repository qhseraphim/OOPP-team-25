package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Person;
import commons.Tag;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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

    @Inject
    public ExpenseAddParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
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

        // tag and its style
        Tag tag = expense.getTag();
        tagLabel.setText(tag.getName());

        var oldFill = tagLabel.getBackground().getFills().getFirst();
        tagLabel.setBackground(new Background(
            new BackgroundFill(Color.web(tag.getColour().toHexString()), oldFill.getRadii(),
                oldFill.getInsets())));

        // calculate what colour the text should be depending on the background
        int red = tag.getColour().getRed();
        int green = tag.getColour().getGreen();
        int blue = tag.getColour().getBlue();
        if (red * 0.299 + green * 0.587 + blue * 0.114 > 186) {
            tagLabel.setTextFill(Color.web("#000000"));
        } else {
            tagLabel.setTextFill(Color.web("#ffffff"));
        }

        // Populate current participants
        for (Person participant : expense.getParticipants()) {
            addParticipantCardToFlowPane(currentParticipants, participant);
        }

        // Populate available participants
        for (Person participant : event.getPeople()) {
            if (!expense.getParticipants().contains(participant)) {
                addParticipantCardToFlowPane(currentParticipants, participant);
            }
        }
    }

    /**
     * Creates a new Participant card for the dynamically scaled FlowPane.
     *
     * @param participant The participant
     * @return An anchor pane
     */
    private void addParticipantCardToFlowPane(FlowPane flowPane, Person participant) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(475, 50);
        card.setStyle(
            "-fx-border-color: lightgrey; -fx-border-width: 2px; -fx-border-radius: 5px;");

        String participantRepresentation = participant.getFirstName().concat("#"
            + participant.getId());
        Label participantLabel = new Label(participantRepresentation);
        Font globalFont = new Font("System Bold", 24);
        participantLabel.setFont(globalFont);
        participantLabel.setLayoutX(12.5);
        participantLabel.setLayoutY(7.5);
        participantLabel.setOnMouseEntered(
            event -> participantLabel.setText("ID: " + participant.getId()));
        participantLabel.setOnMouseExited(
            event -> participantLabel.setText(participantRepresentation));

        card.getChildren().add(participantLabel);
        flowPane.getChildren().add(card);
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

}
