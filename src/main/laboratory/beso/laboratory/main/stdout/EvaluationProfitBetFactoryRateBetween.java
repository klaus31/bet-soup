package beso.laboratory.main.stdout;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.evaluation.StakeStrategyEvaluation;
import beso.evaluation.StakeStrategyEvaluationPrognosis;
import beso.model.Match;
import beso.model.Odds;
import beso.recommendation.BetFactory;
import beso.recommendation.BetFactoryRateBetween;
import beso.stake.Profit;
import beso.stake.StakeStrategy;
import beso.stake.StakeStrategyKelly;

public class EvaluationProfitBetFactoryRateBetween {

  public static void main(final String... args) {
    final EvaluationProfitBetFactoryRateBetween main = new EvaluationProfitBetFactoryRateBetween();
    main.start();
  }

  private void start() {
    final List<Match> matches = BesoDao.me().findMatchesFinished();
    final List<Odds> odds = BesoDao.me().findOdds();
    final double totalStake = 100; // TODO ich setze auf alle spiele der letzten 5 Jahre 100 euro?!?!
    final StakeStrategyEvaluation sse = new StakeStrategyEvaluationPrognosis(totalStake);
    double min = 1;
    double max = 1.5;
    while (max < 2.5) {
      final BetFactory factory = new BetFactoryRateBetween(min, max);
      final StakeStrategy stakeStrategy = new StakeStrategyKelly(factory, odds);
      final Profit profit = sse.getProfit(stakeStrategy, matches);
      final String output = String.format("%s %s –→ %s", BesoFormatter.format(min, "0.0"), BesoFormatter.format(max, "0.0"), BesoFormatter.format(profit));
      System.out.println(output);
      max += .1;
    }
  }
}