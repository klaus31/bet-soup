package beso.recommendation;

import java.util.ArrayList;
import java.util.List;

import beso.base.Beso;
import beso.evaluation.WagerOnFactoryEvaluation;
import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Wager;
import beso.pojo.WagerOn;

// http://de.wikipedia.org/wiki/Kelly-Formel
class WagerFactoryKelly implements WagerFactory {

  private final Double chance;
  private final WagerOnFactory factory;
  private boolean recommandNotToBetMatches = false;

  public WagerFactoryKelly(final List<Match> referenceMatches) {
    this(new WagerOnFactoryFavorite(), referenceMatches);
  }

  public WagerFactoryKelly(final WagerOnFactory factory, final List<Match> referenceMatches) {
    this.factory = factory;
    WagerOnFactoryEvaluation evaluation = new WagerOnFactoryEvaluation();
    // IDEA evaluate chance with reality
    chance = evaluation.getEvaluationResult(factory, referenceMatches).getSuccessRate();
    Beso.exitWithErrorIf(chance == null, "could not compute the chance to win with given reference quota");
  }

  public void doNotRecommandNotToBetMatches() {
    recommandNotToBetMatches = false;
  }

  @Override
  public List<Wager> getWagerRecommendation(final List<Match> matches, final Budget totalBudget) {
    final List<Wager> wagers = new ArrayList<>(matches.size());
    final List<WagerOn> bets = new ArrayList<>(matches.size());
    final List<Double> kellyFactors = new ArrayList<>(matches.size());
    final List<Match> matchesWithRecommendation = new ArrayList<>(matches.size());
    double kellyFactorsSum = 0;
    for (Match match : matches) {
      WagerOn bet = factory.getWagerOn(match);
      final Double profitRate = match.getRate(bet);
      if ((profitRate == null || profitRate < 1)) {
        if (recommandNotToBetMatches) {
          bets.add(bet);
          kellyFactors.add(0D);
          matchesWithRecommendation.add(match);
        }
      } else {
        bets.add(bet);
        matchesWithRecommendation.add(match);
        final double kellyFactor = (profitRate * chance - (1 - chance)) / profitRate;
        kellyFactors.add(kellyFactor);
        kellyFactorsSum += kellyFactor;
      }
    }
    for (int index = 0; index < kellyFactors.size(); index++) {
      final Double kellyFactor = kellyFactors.get(index);
      final double wagerValue = kellyFactor * totalBudget.getValue() / kellyFactorsSum;
      wagers.add(new Wager(new Budget(wagerValue), bets.get(index), matchesWithRecommendation.get(index)));
    }
    return wagers;
  }

  public void recommandNotToBetMatches() {
    recommandNotToBetMatches = true;
  }
}
