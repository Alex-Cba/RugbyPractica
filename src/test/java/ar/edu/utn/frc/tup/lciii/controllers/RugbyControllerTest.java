package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.PoolResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.TeamDTO;
import ar.edu.utn.frc.tup.lciii.services.IRugbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RugbyControllerTest {

    @InjectMocks
    private RugbyController rugbyController;

    @Mock
    private IRugbyService rugbyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPools() {
        TeamDTO team1 = new TeamDTO(1L, "Team A", "Country A", 5, 3, 1, 1, 100, 80, 20, 15, 2, 12, 20, 1);
        TeamDTO team2 = new TeamDTO(2L, "Team B", "Country B", 5, 4, 0, 1, 110, 70, 40, 18, 1, 11, 20, 1);

        List<TeamDTO> teams = List.of(team1, team2);
        PoolResponseDTO expectedPool = new PoolResponseDTO('A', teams);

        when(rugbyService.GetAllPools()).thenReturn(List.of(expectedPool));

        ResponseEntity<List<PoolResponseDTO>> response = rugbyController.GetAllPools();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(expectedPool), response.getBody());
    }


    @Test
    void testGetPoolById() {
        char poolId = 'A';
        TeamDTO team1 = new TeamDTO(1L, "Team A", "Country A", 5, 3, 1, 1, 100, 80, 20, 15, 2, 12, 20, 1);
        List<TeamDTO> teams = List.of(team1);
        PoolResponseDTO expectedPool = new PoolResponseDTO(poolId, teams);

        when(rugbyService.GetPoolById(poolId)).thenReturn(expectedPool);

        ResponseEntity<PoolResponseDTO> response = rugbyController.GetPoolById(poolId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPool, response.getBody());
    }
}
