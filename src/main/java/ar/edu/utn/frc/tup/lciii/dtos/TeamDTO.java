package ar.edu.utn.frc.tup.lciii.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {

    @JsonProperty("team_id")
    private Long Id;

    @JsonProperty("team_name")
    private String teamName;

    @JsonProperty("country")
    private String country;

    @JsonProperty("matches_played")
    private int matchesPlayed;

    @JsonProperty("wins")
    private int wins;

    @JsonProperty("draws")
    private int draws;

    @JsonProperty("losses")
    private int losses;

    @JsonProperty("points_for")
    private int points_for;

    @JsonProperty("points_against")
    private int points_against;

    @JsonProperty("points_differential")
    private int points_differential;

    @JsonProperty("tries_made")
    private int tries_made;

    @JsonProperty("bonus_points")
    private int bonus_points;

    @JsonProperty("points")
    private int points;

    @JsonProperty("total_yellow_cards")
    private int total_yellow_cards;

    @JsonProperty("total_red_cards")
    private int total_red_cards;
}
