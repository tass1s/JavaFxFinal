package Services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import Exception.CountriesAPIException;
import Model.CountryInfo;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CountryAPIService {

    private static final String BASE_URL = "https://restcountries.com/v3.1/";

    // Ανακτά πληροφορίες για μία χώρα με βάση το όνομά της
    public List<CountryInfo> getCountryByName(String name) throws CountriesAPIException {
        try {
            // Κωδικοποίηση του ονόματος της χώρας για χρήση στο URL
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            return getAPIData("name/" + encodedName, CountryInfo[].class);
        } catch (IOException e) {
            throw new CountriesAPIException("Error encoding the country name.", e);
        }
    }

    // Ανακτά πληροφορίες για χώρες με βάση τη γλώσσα
    public List<CountryInfo> getCountriesByLanguage(String language) throws CountriesAPIException {
        try {
            String encodedName = URLEncoder.encode(language, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            return getAPIData("lang/" + encodedName, CountryInfo[].class);
        } catch (IOException e) {
            throw new CountriesAPIException("Error encoding the country name.", e);
        }
    }

    // Ανακτά πληροφορίες για χώρες με βάση το νόμισμα
    public List<CountryInfo> getCountriesByCurrency(String currency) throws CountriesAPIException {
        try {
            String encodedName = URLEncoder.encode(currency, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            return getAPIData("currency/" + encodedName, CountryInfo[].class);
        } catch (IOException e) {
            throw new CountriesAPIException("Error encoding the country name.", e);
        }
    }

    // Ανακτά πληροφορίες για χώρες με βάση την περιοχή
    public List<CountryInfo> getCountriesByRegion(String region) throws CountriesAPIException {
        try {
            String encodedRegion = URLEncoder.encode(region, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            return getAPIData("region/" + encodedRegion, CountryInfo[].class);
        } catch (IOException e) {
            throw new CountriesAPIException("Error encoding the country name.", e);
        }
    }

    // Ανακτά πληροφορίες για όλες τις χώρες
    public List<CountryInfo> getAllCountries() throws CountriesAPIException {
        return getAPIData("all", CountryInfo[].class);
    }

    // Γενική μέθοδος για την ανάκτηση δεδομένων από το API
    private <T> List<T> getAPIData(String path, Class<T[]> clazz) throws CountriesAPIException {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(BASE_URL + path);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new CountriesAPIException("Error occurred on API call: " + response.getStatusLine().getReasonPhrase());
            }

            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(entity.getContent(), clazz));
        } catch (IOException e) {
            throw new CountriesAPIException("Error requesting data from the CountryDB API.", e);
        }
    }
}
