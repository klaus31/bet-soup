package beso.evaluation;

import beso.model.Bet;
import beso.model.Odds;
import beso.recommendation.BetFactory;
import static beso.evaluation.Outcome.FAIL;
import static beso.evaluation.Outcome.UNKNOWN;
import static beso.evaluation.Outcome.WIN;
import static beso.model.Bet.AMBIGUOUS;
import static beso.model.Bet.DRAW;
import static beso.model.Bet.TEAM_1_WIN;
import static beso.model.Bet.TEAM_2_WIN;

class OutcomeFactory {

  public static Outcome get(final BetFactory factory, final Odds odds) {
    if (!odds.getMatch().isFinished()) {
      return Outcome.UNKNOWN;
    }
    Bet bet = factory.getBet(odds);
    if (bet == null || bet == AMBIGUOUS) {
      return UNKNOWN;
    }
    if (bet == TEAM_1_WIN && odds.getMatch().getGoalsTeam1() > odds.getMatch().getGoalsTeam2()) {
      return WIN;
    } else if (bet == TEAM_2_WIN && odds.getMatch().getGoalsTeam2() > odds.getMatch().getGoalsTeam1()) {
      return WIN;
    } else if (bet == DRAW && odds.getMatch().getGoalsTeam2() == odds.getMatch().getGoalsTeam1()) {
      return WIN;
    } else {
      return FAIL;
    }
  }
}
