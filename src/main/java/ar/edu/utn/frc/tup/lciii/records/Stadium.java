package ar.edu.utn.frc.tup.lciii.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Stadium(
        @JsonProperty("id") Long id,
        @JsonProperty("name") int name
) {
}
