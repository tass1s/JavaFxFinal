package gr.unipi.CountriesUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.CountryApi;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Exception.CountriesAPIException;
import Model.CountryInfo;
import Model.countrydb.Name;

public class App extends Application {
    private CountryApi countryApi = new CountryApi();// Δημιουργία αντικειμένου για την κλήση API
    private LinkedList<String> searchHistory = new LinkedList<>();// Ιστορικό αναζητήσεων
    private Map<ActionType, List<String>> suggestionsMap = new HashMap<>();// Χάρτης για προτάσεις ανάλογα με τον τύπο ενέργειας
    private ListView<String> historyListView;// Εμφάνιση ιστορικού αναζητήσεων

    @Override
    public void start(Stage stage) throws CountriesAPIException {
    	
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: blue;");// Ρύθμιση χρώματος φόντου
        
        

        TextField queryInput = new TextField();
        queryInput.setPromptText("Enter your query here");// Κείμενο υπόδειξης στο πεδίο εισαγωγής
        
        StackPane stackPane = new StackPane();
        ImageView defaultIcon = new ImageView(new Image("file:C:/Users/User/Downloads/OIP.jpg"));
        defaultIcon.setPreserveRatio(true);
        defaultIcon.setFitWidth(400); // Adjust the size of the default icon as needed
        stackPane.getChildren().add(defaultIcon);


        TextArea textArea = new TextArea();
        textArea.setEditable(false); // Καθιστά την περιοχή κειμένου μη επεξεργάσιμη
        stackPane.getChildren().add(textArea);

        // Αρχικοποίηση των προτάσεων

        populateSuggestions();

        Button fetchAllButton = new Button("Fetch All Countries");
        fetchAllButton.setOnAction(e -> fetchAllCountries(textArea));// Ανάκτηση όλων των χωρών

        // Δημιουργία κουμπιών για διάφορες λειτουργίες ανάκτησης δεδομένων

        Button fetchByCurrencyButton = createActionButton("Fetch by Currency", queryInput, textArea, ActionType.CURRENCY);
        Button fetchByLanguageButton = createActionButton("Fetch by Language", queryInput, textArea, ActionType.LANGUAGE);
        Button fetchByRegionButton = createActionButton("Fetch by Region", queryInput, textArea, ActionType.REGION);
        Button fetchByNameButton = createActionButton("Fetch by Name", queryInput, textArea, ActionType.NAME);

        Button clearButton = new Button("Clear Results");
        clearButton.setOnAction(e -> textArea.clear());// Δράση για την εκκαθάριση των αποτελεσμάτω

        historyListView = new ListView<>();
        historyListView.setPrefHeight(150);
        historyListView.setVisible(false);  // Αρχικά το ιστορικό δεν είναι ορατό
        // κατά την επιλογή ενός στοιχείου από τη λίστα ιστορικού
        historyListView.setOnMouseClicked(event -> {
            String selectedSearch = historyListView.getSelectionModel().getSelectedItem();
            if (selectedSearch != null && !selectedSearch.isEmpty()) {
                queryInput.setText(selectedSearch);
                queryInput.setVisible(true); // Ensure the queryInput is visible if it was not
            }
        });

        Button historyButton = new Button("Show History");
        historyButton.setOnAction(e -> showHistory(historyListView)); //εμφάνιση του ιστορικού αναζητήσεων

        // VBox for sidebar layout with added padding to push elements a bit lower
        VBox sidebar = new VBox(10, queryInput, fetchAllButton, fetchByCurrencyButton, fetchByLanguageButton,
                fetchByRegionButton, fetchByNameButton, clearButton, historyButton, historyListView);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(20, 0, 0, 0)); // Top padding to lower the buttons

        root.setLeft(sidebar);
        root.setCenter(textArea);

        Scene scene = new Scene(root, 1200, 1000);
        stage.setTitle("Java Final Project App"); // Set window title
        stage.setScene(scene);
      
        		stage.getIcons().add(new Image("file:C:/Users/User/Downloads/OIP.jpg"));

        		// Εμφάνιση του παραθύρου
        		stage.show();
        		}

        		// Δημιουργία ενός κουμπιού με βάση τον τύπο δράσης
        		private Button createActionButton(String buttonText, TextField queryInput, TextArea textArea, ActionType actionType) {
        		    // Δημιουργία κουμπιού με το δοθέν κείμενο
        		    Button button = new Button(buttonText);
        		    button.setOnAction(e -> {
        		        // Κάνει ορατό και δίνει το focus στο πεδίο εισαγωγής
        		        queryInput.setVisible(true);
        		        queryInput.requestFocus();

        		        // Ανάκτηση και εμφάνιση προτάσεων με βάση τον τύπο δράσης
        		        List<String> suggestions = suggestionsMap.get(actionType);
        		        if (suggestions != null) {
        		            SuggestionProvider.createAutoCompletePopup(queryInput, suggestions);
        		        }

        		        // Ανάλυση της εισαγωγής όταν πατηθεί το Enter
        		        queryInput.setOnKeyPressed(event -> {
        		            if (event.getCode() == KeyCode.ENTER) {
        		                String query = queryInput.getText().trim();
        		                queryInput.clear();
        		                queryInput.setVisible(false);
        		                performActionBasedOnType(query, textArea, actionType);
        		                addToSearchHistory(query); // Προσθήκη της αναζήτησης στο ιστορικό
        		            }
        		        });
        		    });
        		    return button;
        		}

        		// Εκτέλεση δράσης με βάση τον τύπο και το ερώτημα
        		private void performActionBasedOnType(String query, TextArea textArea, ActionType actionType) {
        		    switch (actionType) {
        		    case CURRENCY:
        		        fetchCountriesByCurrency(query, textArea);
        		        break;
        		    case LANGUAGE:
        		        fetchCountriesByLanguage(query, textArea);
        		        break;
        		    case REGION:
        		        fetchCountriesByRegion(query, textArea);
        		        break;
        		    case NAME:
        		        fetchCountryByName(query, textArea);
        		        break;
        		    case ALL:
        		        fetchAllCountries(textArea);
        		        break;
        		    }
        		}

        		// Αρχικοποίηση του χάρτη με προτάσεις
        		private void populateSuggestions() throws CountriesAPIException {
        		    // Ανάκτηση και αποθήκευση προτάσεων για νομίσματα
        		    suggestionsMap.put(ActionType.CURRENCY,
        		            countryApi.getAllCountries().stream().filter(country -> country.getCurrencies() != null)
        		                    .flatMap(country -> country.getCurrencies().keySet().stream()).distinct().sorted()
        		                    .collect(Collectors.toList()));

        		    // Ανάκτηση και αποθήκευση προτάσεων για ονόματα χωρών
        		    suggestionsMap.put(ActionType.NAME,
        		            countryApi.getAllCountries().stream().map(CountryInfo::getName).map(Name::getCommon).sorted()
        		                    .collect(Collectors.toList()));

        		    // Ανάκτηση και αποθήκευση προτάσεων για περιοχές
        		    suggestionsMap.put(ActionType.REGION,
        		            countryApi.getAllCountries().stream().flatMap(country -> country.getContinents().stream()).distinct().sorted()
        		                    .collect(Collectors.toList()));
        		}

        		// Ανάκτηση και εμφάνιση όλων των χωρών
        		private void fetchAllCountries(TextArea textArea) {
        		    try {
        		        var countries = countryApi.getAllCountries();
        		        StringBuilder countriesInfo = new StringBuilder();
        		        for (CountryInfo country : countries) {
        		            countriesInfo.append(formatCountryInfo(country)).append("\n\n");
        		        }
        		        textArea.setText(countriesInfo.toString());
        		    } catch (CountriesAPIException e) {
        		        textArea.setText("Failed to fetch all countries: " + e.getMessage());
        	    }
        	}


        		// Μέθοδος για την ανάκτηση χωρών βάσει νομίσματος
        		private void fetchCountriesByCurrency(String currency, TextArea textArea) {
        		    try {
        		        // Ανάκτηση χωρών από το API
        		        var countries = countryApi.getCountriesByCurrency(currency);
        		        // Εάν δεν βρεθούν χώρες, εμφάνιση μηνύματος
        		        if (countries.isEmpty()) {
        		            textArea.setText("No countries found with the currency: " + currency);
        		            return;
        		        }
        		        // Διαφορετικά, σύνθεση και εμφάνιση των πληροφοριών των χωρών
        		        StringBuilder countriesInfo = new StringBuilder();
        		        for (CountryInfo country : countries) {
        		            countriesInfo.append(formatCountryInfo(country)).append("\n\n");
        		        }
        		        textArea.setText(countriesInfo.toString());
        		    } catch (CountriesAPIException e) {
        		        // Εμφάνιση μηνύματος σφάλματος σε περίπτωση αποτυχίας
        		        textArea.setText("Failed to fetch countries by currency: " + e.getMessage());
        		    }
        		}

        		// Μέθοδος για την ανάκτηση χωρών βάσει γλώσσας
        		private void fetchCountriesByLanguage(String language, TextArea textArea) {
        		    try {
        		        var countries = countryApi.getCountriesByLanguage(language);
        		        if (countries.isEmpty()) {
        		            textArea.setText("No countries found with the language: " + language);
        		            return;
        		        }
        		        StringBuilder countriesInfo = new StringBuilder();
        		        for (CountryInfo country : countries) {
        		            countriesInfo.append(formatCountryInfo(country)).append("\n\n");
        		        }
        		        textArea.setText(countriesInfo.toString());
        		    } catch (CountriesAPIException e) {
        		        textArea.setText("Failed to fetch countries by language: " + e.getMessage());
        		    }
        		}

        		// Μέθοδος για την ανάκτηση χωρών βάσει περιοχής
        		private void fetchCountriesByRegion(String region, TextArea textArea) {
        		    try {
        		        var countries = countryApi.getCountriesByRegion(region);
        		        if (countries.isEmpty()) {
        		            textArea.setText("No countries found in the region: " + region);
        		            return;
        		        }
        		        StringBuilder countriesInfo = new StringBuilder();
        		        for (CountryInfo country : countries) {
        		            countriesInfo.append(formatCountryInfo(country)).append("\n\n");
        		        }
        		        textArea.setText(countriesInfo.toString());
        		    } catch (CountriesAPIException e) {
        		        textArea.setText("Failed to fetch countries by region: " + e.getMessage());
        		    }
        		}

        		// Μέθοδος για την ανάκτηση μιας χώρας βάσει ονόματος
        		private void fetchCountryByName(String name, TextArea textArea) {
        		    try {
        		        var country = countryApi.getCountryByName(name);
        		        if (country != null) {
        		            textArea.setText(formatCountryInfo(country));
        		        } else {
        		            textArea.setText("No country found with the name: " + name);
        		        }
        		    } catch (CountriesAPIException e) {
        		        textArea.setText("Failed to fetch country by name: " + e.getMessage());
        		    }
        		}

        		// Μορφοποίηση και εμφάνιση πληροφοριών μιας χώρας
        		private String formatCountryInfo(CountryInfo country) {
        		    // Σύνθεση πληροφοριών νομίσματος, περιοχής, πρωτεύουσας και πληθυσμού
        		    String currencyInfo = country.getCurrencies() != null ?
        		        country.getCurrencies().entrySet().stream()
        		            .map(entry -> entry.getKey() + ": " + entry.getValue().getName() + " (Symbol: " + entry.getValue().getSymbol() + ")")
        		            .collect(Collectors.joining(", ")) : "N/A";

        		    String regionInfo = country.getContinents() != null ? String.join(", ", country.getContinents()) : "N/A";
        		    List<String> capitals = country.getCapitals();
        		    String capitalInfo = capitals != null && !capitals.isEmpty() ? String.join(", ", capitals) : "N/A";

        		    return "Country Information:\n" + 
        		        "Name: " + (country.getName() != null ? country.getName().getCommon() : "N/A") + "\n" + 
        		        "Currencies: " + currencyInfo + "\n" + 
        		        "Population: " + country.getPopulation() + "\n" + 
        		        "Region: " + regionInfo + "\n" + 
        		        "Capital: " + capitalInfo + "\n";
        		}

        		// Εμφάνιση του ιστορικού αναζητήσεων
        		private void showHistory(ListView<String> historyListView) {
        		    historyListView.getItems().clear();
        		    historyListView.getItems().addAll(searchHistory);
        		    historyListView.setVisible(true); // Κάνει ορατή τη λίστα του ιστορικού
        		}

        		// Προσθήκη ενός ερωτήματος στο ιστορικό αναζητήσεων
        		private void addToSearchHistory(String query) {
        		    searchHistory.addFirst(query); // Προσθήκη στην αρχή της λίστας
        		    if (searchHistory.size() > 5) {
        		        searchHistory.removeLast(); // Διαγραφή του παλαιότερου ερωτήματος αν υπερβεί τα 5 στοιχεία
        		    }
        		}

        		// Δηλώσεις τύπων ενεργειών για την κατηγοριοποίηση των αναζητήσεων
        		enum ActionType {
        		    CURRENCY, LANGUAGE, REGION, NAME, ALL
        		}

        		// Εκκίνηση της εφαρμογής
        		public static void main(String[] args) {
        		    launch(args);
        		}

}
