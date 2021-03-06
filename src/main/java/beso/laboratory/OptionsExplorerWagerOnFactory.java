package beso.laboratory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import beso.evaluation.WagerEvaluation;
import beso.evaluation.WagerOnEvaluationResult;
import beso.evaluation.WagerOnFactoryEvaluation;
import beso.main.Launchable;
import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Profit;
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
  @Autowired
  private BesoAsciiArtTable table;
  final private WagerEvaluation wagerEvaluation = new WagerEvaluation();

  private void fillTable(final OptionsExplorerWagerOnSubject oewoSubject) {
    factoriesChecked++;
    final WagerOnFactory wagerOnFactory = oewoSubject.getWagerOnFactory();
    final List<Match> matches = oewoSubject.getMatchesToEvaluate();
    final WagerOnEvaluationResult wagerOnEvaluationResult = bfe.getEvaluationResult(wagerOnFactory, matches);
    if (wagerOnEvaluationResult.getCountWagersMade() == 0) {
      table.add(factoriesChecked, wagerOnFactory.getWagerOnDescription(), 0, 0, "-", "-", "-");
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

  protected abstract List<OptionsExplorerWagerOnSubject> getSubjects();

  @Override
  public void launch() {
    // prepare table
    table.addHeadline("exploring options".toUpperCase());
    table.addHeadline(getDoc());
    table.addHeaderCols("#", "factory", "wagers made", "won", "won %", "€ won total", "€ won ø");
    final List<OptionsExplorerWagerOnSubject> oewoSubjects = getSubjects();
    for (OptionsExplorerWagerOnSubject oewoSubject : oewoSubjects) {
      fillTable(oewoSubject);
    }
    table.print();
    // table summary
    table.clear();
    table.addHeadline("summary".toUpperCase());
    table.setNoHeaderColumns(2);
    table.add("FACTORIES CHECKED", factoriesChecked);
    table.add("BUDGET", BesoFormatter.format(budgetPerWager));
    table.add("BEST FACTORY ALL WAGERS", "#" + (bestProfitTotal == null ? "-" : bestProfitTotal.getFactoryNumber()));
    table.add("BEST FACTORY PRO WAGER", "#" + (bestProfitAvg == null ? "-" : bestProfitAvg.getFactoryNumber()));
    table.print();
  }
}