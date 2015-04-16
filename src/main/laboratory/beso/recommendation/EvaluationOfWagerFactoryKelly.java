package beso.recommendation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.base.Beso;
import beso.base.BesoFormatter;
import beso.base.BesoTable;
import beso.dao.BesoDao;
import beso.evaluation.WagerEvaluation;
import beso.main.Launchable;
import beso.pojo.Budget;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Wager;
import static beso.base.BesoFormatter.format;
import static beso.base.BesoFormatter.formatEuro;

@Component
public class EvaluationOfWagerFactoryKelly implements Launchable {

  @Autowired
  private BesoTable table;

  @Override
  public void launch(final String... args) {
    final int countOfGames = 1000;
    final List<Quota> quotas = BesoDao.me().findQuotas(countOfGames);
    final Budget totalBudget = new Budget(100);
    final WagerFactoryFavorite wagerFactory = new WagerFactoryFavorite(quotas);
    final List<Wager> wagers = wagerFactory.getWagerRecommendation(Quota.getMatches(quotas), totalBudget);
    Beso.exitWithOkIf(wagers.isEmpty(), "no recommendations for wagers");
    table.addHeadline("Evaluation of the favorite wager factory".toUpperCase());
    table.addHeaderCols("quota team1", "quota draw", "quota team2", "wager on", "wager amount", "actual profit", "bet result");
    for (Wager wager : wagers) {
      table.add(wager.getQuota().getRateTeam1());
      table.add(wager.getQuota().getRateDraw());
      table.add(wager.getQuota().getRateTeam2());
      table.add(format(wager.getWagerOn()));
      table.add(formatEuro(wager.getValue()));
      table.add(format(wager.getActualProfit()));
      table.add(format(wager.getBetResult()));
    }
    table.print();
    table.clear();
    //    2nd table summary
    WagerEvaluation evaluation = new WagerEvaluation();
    Profit profit = evaluation.getActualProfit(wagers);
    table.addHeadline("summary".toUpperCase());
    table.setNoHeaderColumns(2);
    table.add("games checked", countOfGames);
    table.add("wagers made", wagers.size());
    table.add("budget", BesoFormatter.format(totalBudget));
    table.add("total profit", BesoFormatter.format(profit));
    table.print();
  }
}