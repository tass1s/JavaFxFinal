package Exception;

// Κλάση εξαίρεσης που χρησιμοποιείται για σφάλματα σχετικά με το API των χωρών
public class CountriesAPIException extends Exception {

    // Κατασκευαστής χωρίς παραμέτρους
    public CountriesAPIException() {
        super(); // Καλεί τον κατασκευαστή της υπερκλάσης Exception
    }

    // Κατασκευαστής που δέχεται ένα μήνυμα σφάλματος
    public CountriesAPIException(String message) {
        super(message); // Καλεί τον κατασκευαστή της υπερκλάσης Exception με ένα συγκεκριμένο μήνυμα
    }

    // Κατασκευαστής που δέχεται ένα μήνυμα σφάλματος και μια αιτία
    public CountriesAPIException(String message, Throwable cause) {
        super(message, cause); // Καλεί τον κατασκευαστή της υπερκλάσης Exception με ένα μήνυμα και μια αιτία
    }

    // Κατασκευαστής που δέχεται μόνο μια αιτία
    public CountriesAPIException(Throwable cause) {
        super(cause); // Καλεί τον κατασκευαστή της υπερκλάσης Exception με μια αιτία
    }

    // Κατασκευαστής που επιτρέπει τον πλήρη έλεγχο της εξαίρεσης, της αιτίας, της καταστολής και της στοιβάζουσας ιχνηλάτησης
    protected CountriesAPIException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Καλεί τον αντίστοιχο κατασκευαστή της υπερκλάσης Exception
    }

}
