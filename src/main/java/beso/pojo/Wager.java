package beso.pojo;

import beso.base.Beso;
import beso.evaluation.BetResultFactory;

public class Wager {

  final WagerOn wagerOn;
  final Quota quota;
  final double value;

  public Wager(final double value, final WagerOn wagerOn, final Quota quota) {
    Beso.exitWithErrorIf(value < 0, "wager must be positive");
    this.value = value;
    this.wagerOn = wagerOn;
    this.quota = quota;
  }

  public Profit getActualProfit() {
    if (quota.getRate(wagerOn) == null) {
      return new Profit(0D);
    }
    if (getBetResult() == BetResult.WIN) {
      return getPossibleProfit();
    } else if (getBetResult() == BetResult.LOSE) {
      return new Profit(value * -1);
    } else {
      return new Profit(0D);
    }
  }

  public WagerOn getWagerOn() {
    return wagerOn;
  }

  public Quota getQuota() {
    return quota;
  }

  public BetResult getBetResult() {
    return BetResultFactory.get(this);
  }

  public Profit getPossibleProfit() {
    return new Profit((quota.getRate(wagerOn) * value) - value);
  }

  public double getValue() {
    return value;
  }
}
