package beso.laboratory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import beso.recommendation.WagerOnFactory;
import beso.tools.BesoAsciiArtTable;
import beso.tools.BesoFormatter;
import static beso.tools.BesoFormatter.format;
import static beso.tools.BesoFormatter.formatEuro;

public abstract class OptionsExplorerWagerOnFactory implements Launchable {

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

  private ProfitAvg bestProfitAvg = null;
  private ProfitTotal bestProfitTotal = null;
  final private WagerOnFactoryEvaluation bfe = new WagerOnFactoryEvaluation();
  private final Budget budgetPerWager = new Budget(1);
  private int factoriesChecked = 0;
  private int factoriesToCheck;
  private final List<Match> matches;
  private final List<Quota> quotas;
  @Autowired
  private BesoAsciiArtTable table;
  final private WagerEvaluation wagerEvaluation = new WagerEvaluation();

  public OptionsExplorerWagerOnFactory() {
    // FIXME this is nonsense - not all factories may want to check all quotes
    // FIXME why exactly differentiate between quotes and matches?!
    System.out.println("Please wait …"); // TODO is called twice in subclasses. no singleton?!
    quotas = BesoDao.me().findQuotas();
    matches = Quota.getMatches(quotas);
  }

  private void fillTable(final WagerOnFactory wagerOnFactory) {
    System.out.println("… " + (factoriesToCheck - factoriesChecked) + " factories left");
    factoriesChecked++;
    final WagerOnEvaluationResult wagerOnEvaluationResult = bfe.getEvaluationResult(wagerOnFactory, quotas);
    if (wagerOnEvaluationResult.getCountWagersMade() == 0) {
      table.add(wagerOnFactory.getWagerOnDescription(), 0, 0, "-", "-", "-", "-");
    } else {
      final WagerFactory wagerFactory = new WagerFactoryFixedBudgetPerWager(wagerOnFactory);
      final List<Wager> wagers = wagerFactory.getWagerRecommendation(matches, budgetPerWager);
      final Profit currentProfit = wagerEvaluation.getActualProfit(wagers);
      final ProfitAvg currentProfitPerWager = new ProfitAvg(currentProfit, factoriesChecked, wagers);
      table.add(factoriesChecked, wagerOnFactory.getWagerOnDescription());
      table.add(wagers.size(), wagerOnEvaluationResult.getCountWon(), BesoFormatter.formatPercent(wagerOnEvaluationResult.getSuccessRate()));
      table.add(format(currentProfit), formatEuro(currentProfitPerWager.getProfitPerWager()));
      if (bestProfitTotal == null || bestProfitTotal.isWorseThan(currentProfit)) {
        bestProfitTotal = new ProfitTotal(currentProfit, factoriesChecked);
      }
      if (bestProfitAvg == null || bestProfitAvg.isWorseThan(currentProfitPerWager)) {
        bestProfitAvg = new ProfitAvg(currentProfit, factoriesChecked, wagers);
      }
    }
  }

  protected abstract List<WagerOnFactory> getFactories();

  @Override
  public void launch() {
    // prepare table
    table.addHeadline("exploring options".toUpperCase());
    table.addHeadline(getDoc());
    table.addHeaderCols("#", "factory", "wagers made", "won", "won %", "€ won total", "€ won ø");
    List<WagerOnFactory> factories = getFactories();
    factoriesToCheck = factories.size();
    for (WagerOnFactory factory : factories) {
      fillTable(factory);
    }
    table.print();
    // table summary
    table.clear();
    table.addHeadline("summary".toUpperCase());
    table.setNoHeaderColumns(2);
    table.add("FACTORIES CHECKED", factoriesChecked);
    table.add("QUOTAS CHECKED", quotas.size());
    table.add("BUDGET", BesoFormatter.format(budgetPerWager));
    table.add("BEST FACTORY ALL WAGERS", "#" + (bestProfitTotal == null ? "-" : bestProfitTotal.getFactoryNumber()));
    table.add("BEST FACTORY PRO WAGER", "#" + (bestProfitAvg == null ? "-" : bestProfitAvg.getFactoryNumber()));
    table.print();
  }
}