package beso.pojo;

import beso.base.Beso;
import beso.evaluation.BetResultFactory;

public class Wager {

  final Budget budget;
  final Quota quota;
  final WagerOn wagerOn;

  public Wager(final Budget budget, final WagerOn wagerOn, final Quota quota) {
    Beso.exitWithErrorIf(budget == null, "wager must be positive");
    Beso.exitWithErrorIf(budget.getValue() < 0, "wager must be positive");
    this.budget = budget;
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
      return new Profit(budget.getValue() * -1);
    } else {
      return new Profit(0D);
    }
  }

  public BetResult getBetResult() {
    return BetResultFactory.get(this);
  }

  public Profit getPossibleProfit() {
    return new Profit((quota.getRate(wagerOn) * budget.getValue()) - budget.getValue());
  }

  public Quota getQuota() {
    return quota;
  }

  public double getValue() {
    return budget.getValue();
  }

  public WagerOn getWagerOn() {
    return wagerOn;
  }
}
