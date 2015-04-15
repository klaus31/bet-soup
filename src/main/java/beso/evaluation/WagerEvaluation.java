package beso.evaluation;

import java.util.List;

import beso.pojo.Profit;
import beso.pojo.Wager;

public class WagerEvaluation {

  public Profit getActualProfit(final List<Wager> wagers) {
    final Profit profit = new Profit();
    for (Wager wager : wagers) {
      profit.add(wager.getActualProfit());
    }
    return profit;
  }
}
