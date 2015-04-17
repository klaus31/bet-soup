package beso.laboratory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.evaluation.WagerEvaluation;
import beso.evaluation.WagerOnEvaluationResult;
import beso.evaluation.WagerOnFactoryEvaluation;
import beso.main.Launchable;
import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Wager;
import beso.recommendation.WagerFactory;
import beso.recommendation.WagerFactoryFixedBudgetPerWager;
import beso.recommendation.WagerOnFactoryRateBetween;
import beso.tools.BesoFormatter;
import beso.tools.BesoTable;
import static beso.tools.BesoFormatter.format;
import static beso.tools.BesoFormatter.formatEuro;

@Primary
@Component
public class OptionsExplorerFactoryRateBetween implements Launchable {

  private class ProfitAvg extends ProfitTotal {

    private final List<Wager> wagers;

    public ProfitAvg(final Profit profit, final int factoryNumber, final List<Wager> wagers) {
      super(profit, factoryNumber);
      this.wagers = wagers;
    }

    public double getProfitPerWager() {
      return profit.getValue() / wagers.size();
    }

    public boolean isWorseThan(final ProfitAvg profitPerWager) {
      return this.getProfitPerWager() < profitPerWager.getProfitPerWager();
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
  public void launch() {
    System.out.println("Please wait …");
    final List<Quota> quotas = BesoDao.me().findQuotas();
    final List<Match> matches = Quota.getMatches(quotas);
    final WagerOnFactoryEvaluation bfe = new WagerOnFactoryEvaluation();
    final Budget budgetPerWager = new Budget(1);
    final double CHECK_MIN_MAX = 1.5;
    final double CHECK_MAX_MAX = 2;
    double min = 1;
    double max = 1.1;
    ProfitTotal bestProfitTotal = null;
    ProfitAvg bestProfitAvg = null;
    int factoriesChecked = 0;
    // prepare table
    table.addHeadline("exploring options".toUpperCase());
    table.addHeadline("set " + format(budgetPerWager) + " for each wager using 'quota between min and max' and compare actual made profits");
    table.addHeaderCols("#", "quota min", "max", "wagers made", "won", "won %", "€ won total", "€ won ø");
    final int factoriesToCheck = (int) (((CHECK_MIN_MAX - min) / .1) * ((CHECK_MAX_MAX - max) / .1));
    while (min < CHECK_MIN_MAX) {
      while (max < CHECK_MAX_MAX) {
        System.out.println("… " + (factoriesToCheck - factoriesChecked) + " factories left");
        factoriesChecked++;
        final WagerOnFactoryRateBetween wagerOnFactory = new WagerOnFactoryRateBetween(min, max);
        final WagerOnEvaluationResult wagerOnEvaluationResult = bfe.getEvaluationResult(wagerOnFactory, quotas);
        if (wagerOnEvaluationResult.getCountWagersMade() == 0) {
          table.add(format(min, "0.0"), format(max, "0.0"), 0, 0, "-", "-", "-", "-");
        } else {
          final WagerFactory wagerFactory = new WagerFactoryFixedBudgetPerWager(wagerOnFactory);
          final List<Wager> wagers = wagerFactory.getWagerRecommendation(matches, budgetPerWager);
          final WagerEvaluation wagerEvaluation = new WagerEvaluation();
          final Profit currentProfit = wagerEvaluation.getActualProfit(wagers);
          final ProfitAvg currentProfitPerWager = new ProfitAvg(currentProfit, factoriesChecked, wagers);
          table.add(factoriesChecked, format(min, "0.0"), format(max, "0.0"));
          table.add(wagers.size(), wagerOnEvaluationResult.getCountWon(), BesoFormatter.formatPercent(wagerOnEvaluationResult.getSuccessRate()));
          table.add(format(currentProfit), formatEuro(currentProfitPerWager.getProfitPerWager()));
          if (bestProfitTotal == null || bestProfitTotal.isWorseThan(currentProfit)) {
            bestProfitTotal = new ProfitTotal(currentProfit, factoriesChecked);
          }
          if (bestProfitAvg == null || bestProfitAvg.isWorseThan(currentProfitPerWager)) {
            bestProfitAvg = new ProfitAvg(currentProfit, factoriesChecked, wagers);
          }
        }
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
    table.add("BUDGET", BesoFormatter.format(budgetPerWager));
    table.add("BEST FACTORY ALL WAGERS", "#" + bestProfitTotal.getFactoryNumber());
    table.add("BEST FACTORY PRO WAGER", "#" + bestProfitAvg.getFactoryNumber());
    table.print();
  }
}