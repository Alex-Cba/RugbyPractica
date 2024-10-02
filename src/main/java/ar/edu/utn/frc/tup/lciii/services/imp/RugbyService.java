package ar.edu.utn.frc.tup.lciii.services.imp;

import ar.edu.utn.frc.tup.lciii.clients.RugbyClient;
import ar.edu.utn.frc.tup.lciii.dtos.PoolResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.TeamDTO;
import ar.edu.utn.frc.tup.lciii.exceptions.NotFoundException;
import ar.edu.utn.frc.tup.lciii.records.Team;
import ar.edu.utn.frc.tup.lciii.records.TeamMatch;
import ar.edu.utn.frc.tup.lciii.services.IRugbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RugbyService implements IRugbyService {

    @Autowired
    private RugbyClient rugbyClient;


    @Override
    public List<PoolResponseDTO> GetAllPools() {
        List<PoolResponseDTO> response = new java.util.ArrayList<>(List.of());

        var checkTeams = rugbyClient.getAllTeams();

        if (checkTeams == null || checkTeams.getBody() == null || checkTeams.getBody().isEmpty()) {
            throw new NotFoundException("Teams not found");
        }

        var teams = checkTeams.getBody();

        var pools = teams.stream().map(Team::pool).distinct().toList();

        for (var poolId : pools) {
            var checkTeam = rugbyClient.getTeamsByPool(poolId);

            if (checkTeam == null || checkTeam.getBody() == null || checkTeam.getBody().isEmpty()) {
                throw new NotFoundException("Teams not found for pool: " + poolId);
            }

            var team = checkTeam.getBody();

            var checkMatches = rugbyClient.getMatchesByPool(poolId);

            if (checkMatches == null || checkMatches.getBody() == null || checkMatches.getBody().isEmpty()) {
                throw new NotFoundException("Matches not found for pool: " + poolId);
            }

            var matches = checkMatches.getBody();

            var tempResult = new PoolResponseDTO();
            tempResult.setId(poolId);

            var TeamsDTO = ConvertTeamsToDTO(team);
            tempResult.setTeams(TeamsDTO);

            for (var match : matches) {
                var team1 = match.teams().get(0);
                var team2 = match.teams().get(1);

                if (team1.points() == team2.points()) {
                    DrawMatch(tempResult, team1, team2);
                } else if (team1.points() > team2.points()) {
                    WinMatch(tempResult, team1, team2);
                } else {
                    WinMatch(tempResult, team2, team1);
                }
            }

            response.add(tempResult);
        }

        return response;
    }

    @Override
    public PoolResponseDTO GetPoolById(char poolId) {

        var checkTeams = rugbyClient.getTeamsByPool(poolId);

        if (checkTeams == null || checkTeams.getBody() == null || checkTeams.getBody().isEmpty()) {
            throw new NotFoundException("Teams not found for pool: " + poolId);
        }

        var teams = checkTeams.getBody();

        var checkMatches = rugbyClient.getMatchesByPool(poolId);

        if (checkMatches == null || checkMatches.getBody() == null || checkMatches.getBody().isEmpty()) {
            throw new NotFoundException("Matches not found for pool: " + poolId);
        }

        var matches = checkMatches.getBody();

        var response = new PoolResponseDTO();
        response.setId(poolId);

        var TeamsDTO = ConvertTeamsToDTO(teams);
        response.setTeams(TeamsDTO);

        for (var match : matches) {
            var team1 = match.teams().get(0);
            var team2 = match.teams().get(1);

            if (team1.points() == team2.points()) {
                DrawMatch(response, team1, team2);
            } else if (team1.points() > team2.points()) {
                WinMatch(response, team1, team2);
            } else {
                WinMatch(response, team2, team1);
            }
        }

        return response;
    }

    private void WinMatch(PoolResponseDTO response, TeamMatch team1, TeamMatch team2) {
        //Team 1 Win
        response.getTeams().stream()
                .filter(teamDTO -> teamDTO.getId().equals(team1.id()))
                .findFirst()
                .ifPresent(
                        teamDTO -> {
                            teamDTO.setWins(teamDTO.getWins() + 1);
                            teamDTO.setPoints_for(teamDTO.getPoints_for() + team1.points());
                            teamDTO.setPoints_against(teamDTO.getPoints_against() + team2.points());
                            teamDTO.setMatchesPlayed(teamDTO.getMatchesPlayed() + 1);
                            teamDTO.setTries_made(teamDTO.getTries_made() + team1.tries());
                            teamDTO.setTotal_yellow_cards(teamDTO.getTotal_yellow_cards() + team1.yellow_cards());
                            teamDTO.setTotal_red_cards(teamDTO.getTotal_red_cards() + team1.red_cards());
                            teamDTO.setPoints(teamDTO.getPoints() + 4);
                            teamDTO.setPoints_differential(teamDTO.getPoints_for() + (team1.points() - team2.points()));
                            teamDTO.setBonus_points(teamDTO.getBonus_points() + BonusPointsCalculator(team1, team2));
                        }
                );

        //Team 2 Lose
        response.getTeams().stream()
                .filter(teamDTO -> teamDTO.getId().equals(team2.id()))
                .findFirst()
                .ifPresent(
                        teamDTO -> {
                            teamDTO.setLosses(teamDTO.getLosses() + 1);
                            teamDTO.setPoints_for(teamDTO.getPoints_for() + team1.points());
                            teamDTO.setPoints_against(teamDTO.getPoints_against() + team2.points());
                            teamDTO.setMatchesPlayed(teamDTO.getMatchesPlayed() + 1);
                            teamDTO.setTries_made(teamDTO.getTries_made() + team2.tries());
                            teamDTO.setTotal_yellow_cards(teamDTO.getTotal_yellow_cards() + team2.yellow_cards());
                            teamDTO.setTotal_red_cards(teamDTO.getTotal_red_cards() + team2.red_cards());
                            teamDTO.setPoints_differential(teamDTO.getPoints_for() + (team2.points() - team1.points()));
                            teamDTO.setBonus_points(teamDTO.getBonus_points() + BonusPointsCalculator(team2, team1));
                        }
                );
    }

    private void DrawMatch(PoolResponseDTO response, TeamMatch team1, TeamMatch team2) {
        //Team 1
        response.getTeams().stream()
                .filter(teamDTO -> teamDTO.getId().equals(team1.id()))
                .findFirst()
                .ifPresent(
                    teamDTO -> {
                        teamDTO.setDraws(teamDTO.getDraws() + 1);
                        teamDTO.setPoints_for(teamDTO.getPoints_for() + team1.points());
                        teamDTO.setPoints_against(teamDTO.getPoints_against() + team2.points());
                        teamDTO.setMatchesPlayed(teamDTO.getMatchesPlayed() + 1);
                        teamDTO.setTries_made(teamDTO.getTries_made() + team1.tries());
                        teamDTO.setTotal_yellow_cards(teamDTO.getTotal_yellow_cards() + team1.yellow_cards());
                        teamDTO.setTotal_red_cards(teamDTO.getTotal_red_cards() + team1.red_cards());
                        teamDTO.setPoints(teamDTO.getPoints() + 2);
                        teamDTO.setPoints_differential(teamDTO.getPoints_for() + (team1.points() - team2.points()));
                        teamDTO.setBonus_points(teamDTO.getBonus_points() + BonusPointsCalculator(team1, team2));
                    }
                );

        //Team 2
        response.getTeams().stream()
                .filter(teamDTO -> teamDTO.getId().equals(team2.id()))
                .findFirst()
                .ifPresent(
                        teamDTO -> {
                            teamDTO.setDraws(teamDTO.getDraws() + 1);
                            teamDTO.setPoints_for(teamDTO.getPoints_for() + team2.points());
                            teamDTO.setPoints_against(teamDTO.getPoints_against() + team1.points());
                            teamDTO.setMatchesPlayed(teamDTO.getMatchesPlayed() + 1);
                            teamDTO.setTries_made(teamDTO.getTries_made() + team2.tries());
                            teamDTO.setTotal_yellow_cards(teamDTO.getTotal_yellow_cards() + team2.yellow_cards());
                            teamDTO.setTotal_red_cards(teamDTO.getTotal_red_cards() + team2.red_cards());
                            teamDTO.setPoints(teamDTO.getPoints() + 2);
                            teamDTO.setPoints_differential(teamDTO.getPoints_for() + (team2.points() - team1.points()));
                            teamDTO.setBonus_points(teamDTO.getBonus_points() + BonusPointsCalculator(team2, team1));
                        }
                );
    }

    private int BonusPointsCalculator(TeamMatch teamA, TeamMatch teamB) {
        int result = 0;
        if (teamA.tries() >= 4) {
            result += 1;
        }

        var subPoint = teamA.points() - teamB.points();
        if (subPoint <= 7 && subPoint >= 0) {
            result += 1;
        }

        return result;
    }

    private List<TeamDTO> ConvertTeamsToDTO(List<Team> teams) {
        List<TeamDTO> result = new java.util.ArrayList<>(List.of());

        for (var team : teams) {
            var teamDTO = new TeamDTO();
            teamDTO.setId(team.id());
            teamDTO.setTeamName(team.name());
            teamDTO.setCountry(team.country());
            result.add(teamDTO);
        }

        return result;
    }
}
