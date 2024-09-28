package ar.edu.utn.frc.tup.lciii.clients;

import ar.edu.utn.frc.tup.lciii.records.Match;
import ar.edu.utn.frc.tup.lciii.records.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RugbyClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RugbyClient rugbyClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTeamsByPool_Success() {
        char poolId = 'A';

        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<List<Team>> response = rugbyClient.getTeamsByPool(poolId);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetTeamsByPool_Fail() {
        char poolId = 'A';

        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException());

        try {
            rugbyClient.getTeamsByPool(poolId);
        } catch (RuntimeException e) {
            assertEquals(500, 500);
        }
    }

    @Test
    void testGetMatchesByPool_Success() {
        char poolId = 'A';

        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<List<Match>> response = rugbyClient.getMatchesByPool(poolId);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetMatchesByPool_Fail() {
        char poolId = 'A';

        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException());

        try {
            rugbyClient.getMatchesByPool(poolId);
        } catch (RuntimeException e) {
            assertEquals(500, 500);
        }
    }

    @Test
    void testGetAllTeams_Success() {
        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<List<Team>> response = rugbyClient.getAllTeams();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllTeams_Fail() {
        char poolId = 'A';

        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException());

        try {
            rugbyClient.getAllTeams();
        } catch (RuntimeException e) {
            assertEquals(500, 500);
        }
    }
}
