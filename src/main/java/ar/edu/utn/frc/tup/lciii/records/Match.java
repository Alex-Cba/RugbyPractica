package ar.edu.utn.frc.tup.lciii.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record Match(

        @JsonProperty("id") Long id,
        @JsonProperty("date") LocalDate date,
        @JsonProperty("teams") List<TeamMatch> teams,
        @JsonProperty("stadium") int worldRanking,
        @JsonProperty("pool") char pool
) {
}
