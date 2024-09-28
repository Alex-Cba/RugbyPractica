package ar.edu.utn.frc.tup.lciii.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Team(

        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("country") String country,
        @JsonProperty("world_ranking") int worldRanking,
        @JsonProperty("pool") char pool
) {
}
