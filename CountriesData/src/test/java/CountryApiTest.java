import org.junit.Before;
import org.junit.Test;
import Services.CountryAPIService;
import test.CountryApi;
import Model.CountryInfo;
import Exception.CountriesAPIException;
import java.util.List;

public class CountryApiTest {

    private CountryApi countryApi;

    @Before
    public void setUp() {
        countryApi = new CountryApi();
    }

    @Test
    public void testGetCountryByName() {
        try {
            CountryInfo country = countryApi.getCountryByName("Greece");
            System.out.println(country);
        } catch (CountriesAPIException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetAllCountries() {
        try {
            List<CountryInfo> countries = countryApi.getAllCountries();
            System.out.println("All Countries: " + countries);
        } catch (CountriesAPIException e) {
            e.printStackTrace();        }
    }

    @Test
    public void testGetCountriesByLanguage() {
        final String language = "Greek";
        try {
            List<CountryInfo> countries = countryApi.getCountriesByLanguage(language);
            System.out.println("Countries by Language (" + language + "): " + countries);
        } catch (CountriesAPIException e) {
            e.printStackTrace();        }
    }

    @Test
    public void testGetCountriesByCurrency() {
        final String currency = "EUR";
        try {
            List<CountryInfo> countries = countryApi.getCountriesByCurrency(currency);
            System.out.println("Countries by Currency (" + currency + "): " + countries);
        } catch (CountriesAPIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCountriesByRegion() {
        final String region = "Europe";
        try {
            List<CountryInfo> countries = countryApi.getCountriesByRegion(region);
            System.out.println("Countries by Region (" + region + "): " + countries);
        } catch (CountriesAPIException e) {
            e.printStackTrace();
        }
    }

   
}
