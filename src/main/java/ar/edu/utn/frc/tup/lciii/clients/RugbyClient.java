package ar.edu.utn.frc.tup.lciii.clients;

import ar.edu.utn.frc.tup.lciii.records.Match;
import ar.edu.utn.frc.tup.lciii.records.Team;
import lombok.Data;
import org.apache.catalina.authenticator.SavedRequest;
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
}
