package beso.evaluation;

import beso.pojo.BetResult;
import beso.pojo.Quota;
import beso.pojo.Wager;
import beso.pojo.WagerOn;
import beso.recommendation.WagerOnFactory;
import static beso.pojo.BetResult.LOOSE;
import static beso.pojo.BetResult.UNKNOWN;
import static beso.pojo.BetResult.WIN;
import static beso.pojo.WagerOn.DRAW;
import static beso.pojo.WagerOn.TEAM_1_WIN;
import static beso.pojo.WagerOn.TEAM_2_WIN;
import static beso.pojo.WagerOn.UNSURE;

public class BetResultFactory {

  public static BetResult get(final WagerOn wagerOn, final Quota quota) {
    if (!quota.getMatch().isFinished()) {
      return BetResult.UNKNOWN;
    }
    if (wagerOn == null || wagerOn == UNSURE) {
      return UNKNOWN;
    }
    if (wagerOn == TEAM_1_WIN && quota.getMatch().getGoalsTeam1() > quota.getMatch().getGoalsTeam2()) {
      return WIN;
    } else if (wagerOn == TEAM_2_WIN && quota.getMatch().getGoalsTeam2() > quota.getMatch().getGoalsTeam1()) {
      return WIN;
    } else if (wagerOn == DRAW && quota.getMatch().getGoalsTeam2() == quota.getMatch().getGoalsTeam1()) {
      return WIN;
    } else {
      return LOOSE;
    }
  }

  public static BetResult get(final WagerOnFactory factory, final Quota quota) {
    return get(factory.getWagerOn(quota), quota);
  }

  public static BetResult get(final Wager wager) {
    return get(wager.getWagerOn(), wager.getQuota());
  }
}
