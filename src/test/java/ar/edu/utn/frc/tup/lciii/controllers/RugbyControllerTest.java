package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.PoolResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.TeamDTO;
import ar.edu.utn.frc.tup.lciii.exceptions.NotFoundException;
import ar.edu.utn.frc.tup.lciii.services.IRugbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RugbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRugbyService rugbyService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllPoolsNotFoundException() throws Exception {
        given(rugbyService.GetAllPools()).willThrow(new NotFoundException("Teams not found"));

        mockMvc.perform(get("/rwc/2023/pools"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Teams not found"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void testGetAllPools() throws Exception {
        TeamDTO team1 = new TeamDTO(1L, "Team A", "Country A", 5, 3, 1, 1, 100, 80, 20, 15, 2, 12, 20, 1);
        TeamDTO team2 = new TeamDTO(2L, "Team B", "Country B", 5, 4, 0, 1, 110, 70, 40, 18, 1, 11, 20, 1);

        List<TeamDTO> teams = List.of(team1, team2);
        PoolResponseDTO expectedPool = new PoolResponseDTO('A', teams);

        when(rugbyService.GetAllPools()).thenReturn(List.of(expectedPool));

        mockMvc.perform(get("/rwc/2023/pools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pool_id").value("A"))
                .andExpect(jsonPath("$[0].teams.length()").value(2))
                .andExpect(jsonPath("$[0].teams[0].team_name").value("Team A"))
                .andExpect(jsonPath("$[0].teams[1].team_name").value("Team B"));
    }

    @Test
    void testGetPoolByIdNotFoundException() throws Exception {
        given(rugbyService.GetPoolById('Z')).willThrow(new NotFoundException("Teams not found for pool: Z"));

        mockMvc.perform(get("/rwc/2023/pools/{pool_id}", 'Z'))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Teams not found for pool: Z"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void testGetPoolById() throws Exception {
        char poolId = 'A';
        TeamDTO team1 = new TeamDTO(1L, "Team A", "Country A", 5, 3, 1, 1, 100, 80, 20, 15, 2, 12, 20, 1);
        List<TeamDTO> teams = List.of(team1);
        PoolResponseDTO expectedPool = new PoolResponseDTO(poolId, teams);

        when(rugbyService.GetPoolById(poolId)).thenReturn(expectedPool);

        mockMvc.perform(get("/rwc/2023/pools/{pool_id}", poolId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pool_id").value("A"))
                .andExpect(jsonPath("$.teams.length()").value(1))
                .andExpect(jsonPath("$.teams[0].team_name").value("Team A"));
    }
}
