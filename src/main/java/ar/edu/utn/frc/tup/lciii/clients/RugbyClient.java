package ar.edu.utn.frc.tup.lciii.clients;

import ar.edu.utn.frc.tup.lciii.records.Match;
import ar.edu.utn.frc.tup.lciii.records.Team;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Data
@Service
public class RugbyClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${URL_REST_TEMPLATE}")
    private String url;

    public RugbyClient () {
        if (url == null || url.isEmpty()) {
            url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023";
        }
    }

    @CircuitBreaker(name = "rugbyClient", fallbackMethod = "fallbackGetTeamsByPool")
    public ResponseEntity<List<Team>> getTeamsByPool(char poolId) {
        try {
            var response = restTemplate.exchange(url + "/teams?pool=" + poolId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Team>>() {
                    });
            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CircuitBreaker(name = "rugbyClient", fallbackMethod = "fallbackGetMatchesByPool")
    public ResponseEntity<List<Match>> getMatchesByPool(char poolId) {
        try {
            var response = restTemplate.exchange(url + "/matches?pool=" + poolId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Match>>() {
                    });
            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CircuitBreaker(name = "rugbyClient", fallbackMethod = "fallbackGetAllTeams")
    public ResponseEntity<List<Team>> getAllTeams() {
        try {
            var response = restTemplate.exchange(url + "/teams",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Team>>() {
                    });
            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Team>> fallbackGetTeamsByPool(char poolId, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(List.of());
    }

    public ResponseEntity<List<Match>> fallbackGetMatchesByPool(char poolId, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(List.of());
    }

    public ResponseEntity<List<Team>> fallbackGetAllTeams(Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(List.of());
    }
}
