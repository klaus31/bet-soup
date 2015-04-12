package beso.recommendation;

import beso.model.Bet;
import beso.model.Odds;

public class BetFactoryRateBetween implements BetFactory {

  private final double max;
  private final double min;

  public BetFactoryRateBetween(final double min, final double max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public Bet getBet(final Odds odds) {
    Bet result = null;
    if (odds.getRateTeam1() >= min && odds.getRateTeam1() <= max) {
      result = Bet.TEAM_1_WIN;
    }
    if (odds.getRateTeam2() >= min && odds.getRateTeam2() <= max) {
      if (result == null) {
        result = Bet.TEAM_2_WIN;
      } else {
        result = Bet.AMBIGUOUS;
      }
    }
    if (odds.getRateDraw() >= min && odds.getRateDraw() <= max) {
      if (result == null) {
        result = Bet.DRAW;
      } else {
        result = Bet.AMBIGUOUS;
      }
    }
    return result;
  }
}
