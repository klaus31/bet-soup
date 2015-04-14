package beso.evaluation;

import java.util.List;

import beso.model.Match;
import beso.stake.Profit;
import beso.stake.Stake;
import beso.stake.StakeStrategy;

public class StakeStrategyEvaluationPrognosis implements StakeStrategyEvaluation {

  final double totalStake;

  public StakeStrategyEvaluationPrognosis(final double totalStake) {
    this.totalStake = totalStake;
  }

  @Override
  public Profit getProfit(final StakeStrategy stakeStrategy, final List<Match> matches) {
    final Profit profit = new Profit();
    final List<Stake> stakes = stakeStrategy.getStakeRecommendation(matches, totalStake);
    for (Stake stake : stakes) {
      final Outcome outcome = OutcomeFactory.get(stake);
      switch (outcome) {
      case WIN:
        profit.plus(stake.getProfitChance());
        break;
      case LOOSE:
        profit.minus(stake.getValue());
        break;
      default:
        break;
      }
    }
    return profit;
  }
}
