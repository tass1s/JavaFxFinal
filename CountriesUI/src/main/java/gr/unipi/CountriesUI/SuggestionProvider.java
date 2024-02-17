package gr.unipi.CountriesUI;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;
import java.util.stream.Collectors;

public class SuggestionProvider {
    // Δημιουργία αυτόματου popup προτάσεων για ένα TextField
    public static void createAutoCompletePopup(TextField textField, List<String> suggestions) {
        Popup popup = new Popup(); // Δημιουργία νέου Popup
        ListView<String> listView = new ListView<>(); // Δημιουργία λίστας για τις προτάσεις
        listView.setItems(FXCollections.observableList(suggestions)); // Ορισμός στοιχείων της λίστας με τις προτάσεις

        // Ρύθμιση της εμφάνισης κάθε στοιχείου της λίστας μέσω StringConverter
        listView.setCellFactory(TextFieldListCell.forListView(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object; // Εμφάνιση του αντικειμένου ως string
            }

            @Override
            public String fromString(String string) {
                return string; // Επιστροφή του string
            }
        }));

        // Ενέργεια κατά την επιλογή στοιχείου από τη λίστα
        listView.setOnMouseClicked(event -> {
            textField.setText(listView.getSelectionModel().getSelectedItem()); // Ορισμός του κειμένου του TextField
            popup.hide(); // Κλείσιμο του popup
        });

        // Ενέργεια κατά την αλλαγή κειμένου στο TextField
        listView.setOnKeyReleased(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                String input = textField.getText(); // Ανάκτηση του κειμένου που έχει εισαχθεί
                // Φιλτράρισμα των προτάσεων με βάση το κείμενο που έχει εισαχθεί
                listView.setItems(FXCollections.observableList(suggestions.stream()
                        .filter(s -> s.toLowerCase().startsWith(input.toLowerCase())).collect(Collectors.toList())));
                popup.show(textField.getScene().getWindow()); // Εμφάνιση του popup
            }
        });

        popup.getContent().add(listView); // Προσθήκη της λίστας στο popup
        popup.setAutoHide(true); // Ρύθμιση του popup να κλείνει αυτόματα

        Callback<ListView<String>, ListCell<String>> forListView = TextFieldListCell.forListView();
        listView.setCellFactory(forListView); // Ρύθμιση του factory για την εμφάνιση των στοιχείων της λίστας
        listView.setPrefWidth(textField.getWidth()); // Ορισμός του προτεινόμενου πλάτους της λίστας

        // Εμφάνιση του popup κάτω από το TextField
        popup.show(textField.getScene().getWindow(), textField.localToScreen(textField.getBoundsInLocal()).getMinX(),
                textField.localToScreen(textField.getBoundsInLocal()).getMaxY());
    }
}
