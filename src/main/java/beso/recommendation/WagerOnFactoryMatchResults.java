package beso.recommendation;

import java.util.List;

import beso.base.Beso;
import beso.pojo.Match;
import beso.pojo.Team;
import beso.pojo.WagerOn;
import beso.tools.BesoFormatter;
import static beso.pojo.WagerOn.TEAM_1_WIN;
import static beso.pojo.WagerOn.TEAM_2_WIN;
import static beso.recommendation.MatchResultExpectation.LOSE;
import static beso.recommendation.MatchResultExpectation.NOT_LOSE;
import static beso.recommendation.MatchResultExpectation.NOT_WON;
import static beso.recommendation.MatchResultExpectation.WON;
import static beso.tools.BesoFormatter.format;

public class WagerOnFactoryMatchResults implements WagerOnFactory {

  private final List<Match> matches;
  private final MatchResultExpectation matchResultExpectation;
  private final float percentRequired;
  private final Team team;

  public WagerOnFactoryMatchResults(final Team team, final List<Match> matches) {
    this(team, matches, 1, MatchResultExpectation.WON);
  }

  public WagerOnFactoryMatchResults(final Team team, final List<Match> matches, final float percentRequired, final MatchResultExpectation matchResultExpectation) {
    for (Match match : matches) {
      if (!team.played(match)) {
        Beso.exitWithError("team did not play all given matches");
      }
    }
    this.matches = matches;
    this.team = team;
    this.percentRequired = percentRequired;
    this.matchResultExpectation = matchResultExpectation;
  }

  public WagerOnFactoryMatchResults(final Team team, final List<Match> matches, final MatchResultExpectation matchResultExpectation) {
    this(team, matches, 1, matchResultExpectation);
  }

  public Team getTeam() {
    return team;
  }

  /**
   * recommend the given team if it won or not lose the given matches.
   * recommend the other team if the given team lose or not won the given matches.
   */
  @Override
  public WagerOn getWagerOn(final Match matchRequested) {
    if (!matchRequested.isWith(team)) {
      return null;
    }
    int matchesAsExpected = 0;
    for (Match match : matches) {
      if (matchResultExpectation == WON && team.won(match) || matchResultExpectation == LOSE && team.lose(match) || matchResultExpectation == NOT_WON && team.notWon(match) || matchResultExpectation == NOT_LOSE && team.notLose(match)) {
        matchesAsExpected++;
      }
    }
    if (1F * matchesAsExpected / matches.size() >= percentRequired) {
      if (team.is(matchRequested.getTeam1())) {
        return matchResultExpectation == LOSE || matchResultExpectation == NOT_WON ? TEAM_2_WIN : TEAM_1_WIN;
      } else {
        return matchResultExpectation == LOSE || matchResultExpectation == NOT_WON ? TEAM_1_WIN : TEAM_2_WIN;
      }
    } else {
      return null;
    }
  }

  @Override
  public String getWagerOnDescription() {
    String result = "";
    if (matchResultExpectation == NOT_WON || matchResultExpectation == LOSE) {
      result = "the opponent if " + team.getName() + " ";
    } else {
      result = team.getName() + " if they ";
    }
    return result + format(matchResultExpectation) + " " + BesoFormatter.formatPercent(percentRequired * 1D) + " of given " + matches.size() + " matches";
  }
}
