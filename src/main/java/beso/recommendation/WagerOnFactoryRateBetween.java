package beso.recommendation;

import beso.pojo.Quota;
import beso.pojo.WagerOn;

public class WagerOnFactoryRateBetween implements WagerOnFactory {

  private final double max;
  private final double min;

  public WagerOnFactoryRateBetween(final double min, final double max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public WagerOn getWagerOn(final Quota quota) {
    WagerOn result = null;
    if (quota.getRateTeam1() >= min && quota.getRateTeam1() <= max) {
      result = WagerOn.TEAM_1_WIN;
    }
    if (quota.getRateTeam2() >= min && quota.getRateTeam2() <= max) {
      if (result == null) {
        result = WagerOn.TEAM_2_WIN;
      } else {
        result = WagerOn.UNSURE;
      }
    }
    if (quota.getRateDraw() >= min && quota.getRateDraw() <= max) {
      if (result == null) {
        result = WagerOn.DRAW;
      } else {
        result = WagerOn.UNSURE;
      }
    }
    return result;
  }
}
