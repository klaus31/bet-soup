package beso.stake;

import beso.evaluation.Outcome;
import beso.evaluation.OutcomeFactory;
import beso.model.Bet;
import beso.model.Odds;

public class Stake {

  final Bet bet;
  final Odds odds;
  final double value;

  public Stake(final double value, final Bet bet, final Odds odds) {
    this.value = value;
    this.bet = bet;
    this.odds = odds;
  }

  public Bet getBet() {
    return bet;
  }

  public Odds getOdds() {
    return odds;
  }

  public Outcome getOutcome() {
    return OutcomeFactory.get(this);
  }

  public double getProfitChance() {
    if (odds.getRate(bet) == null) {
      return 0D;
    }
    if (getOutcome() == Outcome.WIN) {
      return (odds.getRate(bet) * value) - value;
    } else if (getOutcome() == Outcome.LOOSE) {
      return value * -1;
    } else {
      return 0D;
    }
  }

  public double getValue() {
    return value;
  }
}
