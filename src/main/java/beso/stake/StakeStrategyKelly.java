package beso.stake;

import java.util.ArrayList;
import java.util.List;

import beso.evaluation.BetFactoryEvaluation;
import beso.evaluation.BetFactoryEvaluationPrognosis;
import beso.model.Bet;
import beso.model.Match;
import beso.model.Odds;
import beso.recommendation.BetFactory;

// http://de.wikipedia.org/wiki/Kelly-Formel
public class StakeStrategyKelly implements StakeStrategy {

  private final double chance;
  private final BetFactory factory;

  public StakeStrategyKelly(final BetFactory factory, final List<Odds> referenceOdds) {
    this.factory = factory;
    BetFactoryEvaluation evaluation = new BetFactoryEvaluationPrognosis();
    // IDEA evaluate chance with reality
    chance = evaluation.rate(factory, referenceOdds);
  }

  @Override
  public List<Stake> getStakeRecommendation(final List<Match> matches, final double totalStake) {
    final List<Stake> stakes = new ArrayList<>(matches.size());
    final List<Bet> bets = new ArrayList<>(matches.size());
    final List<Double> kellyFactors = new ArrayList<>(matches.size());
    double kellyFactorsSum = 0;
    for (Match match : matches) {
      final Odds odds = match.getOdds();
      Bet bet = factory.getBet(odds);
      bets.add(bet);
      final Double profitRate = odds.getRate(bet);
      if (profitRate == null || profitRate < 1) {
        kellyFactors.add(0D);
      } else {
        final double kellyFactor = (profitRate * chance - (1 - chance)) / profitRate;
        kellyFactors.add(kellyFactor);
        kellyFactorsSum += kellyFactor;
      }
    }
    for (int index = 0; index < kellyFactors.size(); index++) {
      final Double kellyFactor = kellyFactors.get(index);
      final double stakeValue = kellyFactor * totalStake / kellyFactorsSum;
      stakes.add(new Stake(stakeValue, bets.get(index), matches.get(index).getOdds()));
    }
    return stakes;
  }
}
