package beso.pojo;

import beso.base.Beso;
import beso.evaluation.BetResultFactory;

public class Wager {

  final Budget budget;
  final Match match;
  final WagerOn wagerOn;

  public Wager(final Budget budget, final WagerOn wagerOn, final Match match) {
    Beso.exitWithErrorIf(budget == null, "wager must be positive");
    Beso.exitWithErrorIf(budget.getValue() < 0, "wager must be positive");
    this.budget = budget;
    this.wagerOn = wagerOn;
    this.match = match;
  }

  public Profit getActualProfit() {
    if (match.getRate(wagerOn) == null) {
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
    return new Profit((match.getRate(wagerOn) * budget.getValue()) - budget.getValue());
  }

  public Match getMatch() {
    return match;
  }

  public double getValue() {
    return budget.getValue();
  }

  public WagerOn getWagerOn() {
    return wagerOn;
  }
}
