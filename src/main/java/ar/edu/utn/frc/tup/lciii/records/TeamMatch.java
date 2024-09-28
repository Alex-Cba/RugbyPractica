package ar.edu.utn.frc.tup.lciii.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamMatch(

        @JsonProperty("id") Long id,
        @JsonProperty("points") int points,
        @JsonProperty("tries") int tries,
        @JsonProperty("yellow_cards") int yellow_cards,
        @JsonProperty("red_cards") int red_cards
) {
}
