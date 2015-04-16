package beso.recommendation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.base.BesoFormatter;
import beso.base.BesoTable;
import beso.dao.BesoDao;
import beso.evaluation.WagerEvaluation;
import beso.evaluation.WagerOnFactoryEvaluation;
import beso.main.Launchable;
import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Wager;
import static beso.base.BesoFormatter.format;
import static beso.base.BesoFormatter.formatEuro;

@Component
public class EvaluationOfWagerOnFactoryRateBetween implements Launchable {

  private class ProfitPerWager extends ProfitTotal {

    private final Budget totalBudget;
    private final List<Wager> wagers;

    public ProfitPerWager(final Profit profit, final int factoryNumber, final List<Wager> wagers, final Budget totalBudget) {
      super(profit, factoryNumber);
      this.wagers = wagers;
      this.totalBudget = totalBudget;
    }

    public double getAmountPerWager() {
      return totalBudget.getValue() / wagers.size();
    }

    public double getProfitPerWager() {
      return profit.getValue() / wagers.size();
    }

    public boolean isWorseThan(final ProfitPerWager profitPerWager) {
      return this.getAmountPerWager() < profitPerWager.getAmountPerWager();
    }
  }
  private class ProfitTotal {

    private final int factoryNumber;
    protected final Profit profit;

    public ProfitTotal(final Profit profit, final int factoryNumber) {
      this.profit = profit;
      this.factoryNumber = factoryNumber;
    }

    public int getFactoryNumber() {
      return factoryNumber;
    }

    public boolean isWorseThan(final Profit profit) {
      return this.profit.getValue() < profit.getValue();
    }
  }

  @Autowired
  private BesoTable table;

  @Override
  public void launch(final String... args) {
    final List<Quota> quotas = BesoDao.me().findQuotas();
    final List<Match> matches = Quota.getMatches(quotas);
    final WagerOnFactoryEvaluation bfe = new WagerOnFactoryEvaluation();
    final Budget totalBudget = new Budget(100);
    double min = 1;
    final double CHECK_MIN_MAX = 1.5;
    double max = 1.1;
    final double CHECK_MAX_MAX = 1.7;
    ProfitTotal highestProfit = null;
    ProfitPerWager highestProfitPerWager = null;
    String output = "";
    int factoriesChecked = 0;
    // prepare table
    table.addHeadline("evaluation of wager on factory".toUpperCase());
    table.addHeadline("set " + format(totalBudget) + " in sum for recommanded wagers using the favorite wager factory");
    table.addHeaderCols("#", "quota min", "max", "wagers made", "won", "won %", "profit total", "ø set", "ø won");
    while (min < CHECK_MIN_MAX) {
      while (max < CHECK_MAX_MAX) {
        factoriesChecked++;
        final WagerOnFactoryRateBetween factory = new WagerOnFactoryRateBetween(min, max);
        final Double rate = bfe.rate(factory, quotas);
        if (rate == null) {
          table.add(format(min, "0.0"), format(max, "0.0"), 0, 0, "-", "-", "-", "-");
        } else {
          final WagerFactoryFavorite wagerFactory = new WagerFactoryFavorite(factory, quotas);
          final List<Wager> wagers = wagerFactory.getWagerRecommendation(matches, totalBudget);
          final WagerEvaluation wagerEvaluation = new WagerEvaluation();
          final Profit profit = wagerEvaluation.getActualProfit(wagers);
          final String percent = BesoFormatter.formatPercent(rate);
          final int wonCount = (int) (rate * wagers.size());
          final ProfitPerWager currentProfitPerWager = new ProfitPerWager(profit, factoriesChecked, wagers, totalBudget);
          table.add(factoriesChecked, format(min, "0.0"), format(max, "0.0"), wagers.size(), wonCount, percent, format(profit), formatEuro(currentProfitPerWager.getAmountPerWager()), formatEuro(currentProfitPerWager.getProfitPerWager()));
          if (highestProfit == null || highestProfit.isWorseThan(profit)) {
            highestProfit = new ProfitTotal(profit, factoriesChecked);
          }
          if (highestProfitPerWager == null || highestProfitPerWager.isWorseThan(currentProfitPerWager)) {
            highestProfitPerWager = new ProfitPerWager(profit, factoriesChecked, wagers, totalBudget);
          }
        }
        System.out.println(output);
        max += .1;
      }
      min += .1;
      max = min + .1;
    }
    table.print();
    // table summary
    table.clear();
    table.addHeadline("summary".toUpperCase());
    table.setNoHeaderColumns(2);
    table.add("FACTORIES CHECKED", factoriesChecked);
    table.add("QUOTAS CHECKED", quotas.size());
    table.add("BUDGET", BesoFormatter.format(totalBudget));
    table.add("BEST FACTORY TOTAL", "#" + highestProfit.getFactoryNumber());
    table.add("BEST FACTORY PRO WAGER", "#" + highestProfit.getFactoryNumber());
    table.print();
  }
}