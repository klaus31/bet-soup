package beso.evaluation;

import java.util.List;

import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Profit;
import beso.pojo.Wager;
import beso.recommendation.WagerFactory;

public class WagerEvaluation {

  final Budget totalBudget;

  public WagerEvaluation(final Budget totalBudget) {
    this.totalBudget = totalBudget;
  }

  public Profit getActualProfit(final List<Wager> wagers) {
    final Profit profit = new Profit();
    for (Wager wager : wagers) {
      profit.add(wager.getActualProfit());
    }
    return profit;
  }

  public Profit getActualProfit(final WagerFactory wagerFactory, final List<Match> matches) {
    return getActualProfit(wagerFactory.getWagerRecommendation(matches, totalBudget));
  }
}
