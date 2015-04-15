package beso.laboratory.main.stdout;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.model.Odds;
import beso.recommendation.BetFactory;
import beso.recommendation.BetFactoryRateBetween;
import beso.stake.Stake;
import beso.stake.StakeStrategyKelly;

public class EvaluationStakeStrategyKelly {

  public static void main(final String... args) {
    final EvaluationStakeStrategyKelly main = new EvaluationStakeStrategyKelly();
    main.start();
  }

  private void start() {
    List<Odds> odds = BesoDao.me().findOdds(10);
    final double totalStake = 100;
    final BetFactory factory = new BetFactoryRateBetween(1, 1.5);
    final StakeStrategyKelly stakeStrategy = new StakeStrategyKelly(factory, odds);
    stakeStrategy.doNotRecommandNotToBetMatches();
    final List<Stake> stakes = stakeStrategy.getStakeRecommendation(Odds.getMatches(odds), totalStake);
    for (Stake stake : stakes) {
      System.out.println(BesoFormatter.formatVerbose(stake));
    }
  }
}