package ar.edu.utn.frc.tup.lciii.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolResponseDTO {

    @JsonProperty("pool_id")
    private char Id;

    @JsonProperty("teams")
    private List<TeamDTO> teams;
}
