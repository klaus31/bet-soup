package beso.recommendation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import beso.base.BesoFormatter;
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

public class EvaluationOfWagerOnFactoryRateBetween implements Launchable {

  private class ProfitPerWager extends ProfitTotal {

    private final Budget totalBudget;
    private final List<Wager> wagers;

    public ProfitPerWager(final Profit profit, final WagerOnFactoryRateBetween factory, final String output, final List<Wager> wagers, final Budget totalBudget) {
      super(profit, factory, output);
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

    private final WagerOnFactoryRateBetween factory;
    private final String output;
    protected final Profit profit;

    public ProfitTotal(final Profit profit, final WagerOnFactoryRateBetween factory, final String output) {
      this.profit = profit;
      this.factory = factory;
      this.output = output;
    }

    public String getOutput() {
      return output;
    }

    public boolean isWorseThan(final Profit profit) {
      return this.profit.getValue() < profit.getValue();
    }

    @Override
    public String toString() {
      return "made " + BesoFormatter.format(profit) + " with min " + factory.getMin() + " and max " + factory.getMax();
    }
  }

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
    while (min < CHECK_MIN_MAX) {
      while (max < CHECK_MAX_MAX) {
        factoriesChecked++;
        final WagerOnFactoryRateBetween factory = new WagerOnFactoryRateBetween(min, max);
        final Double rate = bfe.rate(factory, quotas);
        if (rate == null) {
          output = String.format("quota between %s and %s –→ no bet results", BesoFormatter.format(min, "0.0"), BesoFormatter.format(max, "0.0"));
        } else {
          final WagerFactoryFavorite wagerFactory = new WagerFactoryFavorite(factory, quotas);
          final List<Wager> wagers = wagerFactory.getWagerRecommendation(matches, totalBudget);
          final WagerEvaluation wagerEvaluation = new WagerEvaluation();
          final Profit profit = wagerEvaluation.getActualProfit(wagers);
          final String percent = BesoFormatter.formatPercent(rate);
          final int wonCount = (int) (rate * wagers.size());
          final ProfitPerWager currentProfitPerWager = new ProfitPerWager(profit, factory, output, wagers, totalBudget);
          final String format = "quota between %s and %s –→ set %s in %s wagers –→ %s won (%s) –→ profit: %s (ø set %s and won %s)";
          output = String.format(format, format(min, "0.0"), format(max, "0.0"), format(totalBudget), wagers.size(), wonCount, percent, format(profit), formatEuro(currentProfitPerWager.getAmountPerWager()), formatEuro(currentProfitPerWager.getProfitPerWager()));
          if (highestProfit == null || highestProfit.isWorseThan(profit)) {
            highestProfit = new ProfitTotal(profit, factory, output);
          }
          if (highestProfitPerWager == null || highestProfitPerWager.isWorseThan(currentProfitPerWager)) {
            highestProfitPerWager = new ProfitPerWager(profit, factory, output, wagers, totalBudget);
          }
        }
        System.out.println(output);
        max += .1;
      }
      min += .1;
      max = min + .1;
    }
    String line = StringUtils.repeat('–', output.length());
    System.out.println(line);
    System.out.println("FACTORIES CHECKED:              " + factoriesChecked);
    System.out.println("QUOTAS CHECKED:                 " + quotas.size());
    System.out.println("BUDGET:                         " + BesoFormatter.format(totalBudget));
    System.out.println("BEST FACTORY TOTAL:             " + highestProfit.toString());
    System.out.println("BEST FACTORY TOTAL DETAILS:     " + highestProfit.getOutput());
    System.out.println("BEST FACTORY PRO WAGER:         " + highestProfit.toString());
    System.out.println("BEST FACTORY PRO WAGER DETAILS: " + highestProfit.getOutput());
  }
}