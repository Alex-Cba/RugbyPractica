package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.clients.RugbyClient;
import ar.edu.utn.frc.tup.lciii.dtos.PoolResponseDTO;
import ar.edu.utn.frc.tup.lciii.exceptions.NotFoundException;
import ar.edu.utn.frc.tup.lciii.records.Match;
import ar.edu.utn.frc.tup.lciii.records.Team;
import ar.edu.utn.frc.tup.lciii.records.TeamMatch;
import ar.edu.utn.frc.tup.lciii.services.imp.RugbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RugbyServiceTest {

    @Mock
    private RugbyClient rugbyClient;

    @InjectMocks
    private RugbyService rugbyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPools_Success() {
        List<Team> teams = List.of(new Team(1L, "Team A", "Country A", 1, 'A'),
                new Team(2L, "Team B", "Country B", 2, 'A'));
        when(rugbyClient.getAllTeams()).thenReturn(ResponseEntity.ok(teams));

        List<Match> matches = List.of(new Match(1L, null, List.of(new TeamMatch(1L, 10, 2, 0, 0),
                new TeamMatch(2L, 5, 1, 0, 0)), 0, 'A'));
        when(rugbyClient.getMatchesByPool('A')).thenReturn(ResponseEntity.ok(matches));

        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(teams));

        List<PoolResponseDTO> result = rugbyService.GetAllPools();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals('A', result.get(0).getId());
        assertEquals(2, result.get(0).getTeams().size());
    }

    @Test
    public void testGetAllPools_TeamsNotFound() {
        when(rugbyClient.getAllTeams()).thenReturn(ResponseEntity.ok(new ArrayList<>()));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rugbyService.GetAllPools());
        assertEquals("Teams not found", exception.getMessage());
    }

    @Test
    public void testGetPoolById_Success() {
        List<Team> teams = List.of(new Team(1L, "Team A", "Country A", 1, 'A'));
        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(teams));

        List<Match> matches = List.of(new Match(1L, null, List.of(new TeamMatch(1L, 10, 2, 0, 0),
                new TeamMatch(1L, 5, 1, 0, 0)), 0, 'A'));
        when(rugbyClient.getMatchesByPool('A')).thenReturn(ResponseEntity.ok(matches));

        PoolResponseDTO result = rugbyService.GetPoolById('A');

        assertNotNull(result);
        assertEquals('A', result.getId());
        assertEquals(1, result.getTeams().size());
    }

    @Test
    public void testGetPoolById_Success_Draw() {
        List<Team> teams = List.of(new Team(1L, "Team A", "Country A", 1, 'A'),
                            new Team(2L, "Team B", "Country B", 2, 'A'));

        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(teams));

        List<Match> matches = List.of(new Match(1L, null, List.of(new TeamMatch(1L, 10, 2, 0, 0),
                new TeamMatch(2L, 10, 1, 0, 0)),
                0, 'A'));
        when(rugbyClient.getMatchesByPool('A')).thenReturn(ResponseEntity.ok(matches));

        PoolResponseDTO result = rugbyService.GetPoolById('A');

        assertNotNull(result);
        assertEquals('A', result.getId());
        assertEquals(2, result.getTeams().size());
        assertEquals(1, result.getTeams().get(0).getDraws());
        assertEquals(1, result.getTeams().get(1).getDraws());
    }

    @Test
    public void testGetPoolById_Success_Team1Win() {
        List<Team> teams = List.of(new Team(1L, "Team A", "Country A", 1, 'A'),
                new Team(2L, "Team B", "Country B", 2, 'A'));

        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(teams));

        List<Match> matches = List.of(new Match(1L, null, List.of(new TeamMatch(1L, 10, 2, 0, 0),
                new TeamMatch(2L, 5, 1, 0, 0)),
                0, 'A'));
        when(rugbyClient.getMatchesByPool('A')).thenReturn(ResponseEntity.ok(matches));

        PoolResponseDTO result = rugbyService.GetPoolById('A');

        assertNotNull(result);
        assertEquals('A', result.getId());
        assertEquals(2, result.getTeams().size());
        assertEquals(1, result.getTeams().get(0).getWins());
        assertEquals(0, result.getTeams().get(0).getLosses());
        assertEquals(1, result.getTeams().get(1).getLosses());
        assertEquals(0, result.getTeams().get(1).getWins());
    }

    @Test
    public void testGetPoolById_Success_Team1Lose() {
        List<Team> teams = List.of(new Team(1L, "Team A", "Country A", 1, 'A'),
                new Team(2L, "Team B", "Country B", 2, 'A'));

        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(teams));

        List<Match> matches = List.of(new Match(1L, null, List.of(new TeamMatch(1L, 7, 2, 0, 0),
                new TeamMatch(2L, 10, 1, 0, 0)),
                0, 'A'));
        when(rugbyClient.getMatchesByPool('A')).thenReturn(ResponseEntity.ok(matches));

        PoolResponseDTO result = rugbyService.GetPoolById('A');

        assertNotNull(result);
        assertEquals('A', result.getId());
        assertEquals(2, result.getTeams().size());
        assertEquals(1, result.getTeams().get(0).getLosses());
        assertEquals(0, result.getTeams().get(0).getWins());
        assertEquals(1, result.getTeams().get(1).getWins());
        assertEquals(0, result.getTeams().get(1).getLosses());
    }

    @Test
    public void testGetPoolById_TeamsNotFound() {
        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(new ArrayList<>()));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rugbyService.GetPoolById('A'));
        assertEquals("Teams not found for pool: A", exception.getMessage());
    }

    @Test
    public void testGetPoolById_MatchesNotFound() {
        List<Team> teams = List.of(new Team(1L, "Team A", "Country A", 1, 'A'));
        when(rugbyClient.getTeamsByPool('A')).thenReturn(ResponseEntity.ok(teams));
        when(rugbyClient.getMatchesByPool('A')).thenReturn(ResponseEntity.ok(new ArrayList<>()));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> rugbyService.GetPoolById('A'));
        assertEquals("Matches not found for pool: A", exception.getMessage());
    }
}
