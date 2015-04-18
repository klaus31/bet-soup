package beso.evaluation;

import beso.pojo.BetResult;
import beso.pojo.Match;
import beso.pojo.Wager;
import beso.pojo.WagerOn;
import beso.recommendation.WagerOnFactory;
import static beso.pojo.BetResult.LOSE;
import static beso.pojo.BetResult.UNKNOWN;
import static beso.pojo.BetResult.WIN;
import static beso.pojo.WagerOn.DRAW;
import static beso.pojo.WagerOn.TEAM_1_WIN;
import static beso.pojo.WagerOn.TEAM_2_WIN;
import static beso.pojo.WagerOn.UNSURE;

public class BetResultFactory {

  public static BetResult get(final Wager wager) {
    return get(wager.getWagerOn(), wager.getMatch());
  }

  public static BetResult get(final WagerOn wagerOn, final Match match) {
    if (!match.isFinished()) {
      return UNKNOWN;
    }
    if (wagerOn == null || wagerOn == UNSURE) {
      return UNKNOWN;
    }
    if (wagerOn == TEAM_1_WIN && match.getGoalsTeam1() > match.getGoalsTeam2()) {
      return WIN;
    } else if (wagerOn == TEAM_2_WIN && match.getGoalsTeam2() > match.getGoalsTeam1()) {
      return WIN;
    } else if (wagerOn == DRAW && match.getGoalsTeam2() == match.getGoalsTeam1()) {
      return WIN;
    } else {
      return LOSE;
    }
  }

  public static BetResult get(final WagerOnFactory factory, final Match match) {
    return get(factory.getWagerOn(match), match);
  }
}
