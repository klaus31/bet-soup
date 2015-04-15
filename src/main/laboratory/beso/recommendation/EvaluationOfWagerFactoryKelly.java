package beso.recommendation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import beso.base.Beso;
import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.evaluation.WagerEvaluation;
import beso.main.Launchable;
import beso.pojo.Budget;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Wager;

public class EvaluationOfWagerFactoryKelly implements Launchable {

  @Override
  public void launch(final String... args) {
    final int countOfGames = 1000;
    final List<Quota> quotas = BesoDao.me().findQuotas(countOfGames);
    final Budget totalBudget = new Budget(100);
    final WagerFactoryFavorite wagerFactory = new WagerFactoryFavorite(quotas);
    final List<Wager> wagers = wagerFactory.getWagerRecommendation(Quota.getMatches(quotas), totalBudget);
    Beso.exitWithOkIf(wagers.isEmpty(), "no recommendations for wagers");
    for (Wager wager : wagers) {
      System.out.println(BesoFormatter.formatVerbose(wager));
    }
    String line = StringUtils.repeat('–', BesoFormatter.formatVerbose(wagers.get(0)).length());
    System.out.println(line);
    WagerEvaluation evaluation = new WagerEvaluation();
    Profit profit = evaluation.getActualProfit(wagers);
    System.out.println("GAMES CHECKED:   " + countOfGames);
    System.out.println("WAGERS MADE:     " + wagers.size());
    System.out.println("BUDGET:          " + BesoFormatter.format(totalBudget));
    System.out.println("TOTAL PROFIT:    " + BesoFormatter.format(profit));
  }
}