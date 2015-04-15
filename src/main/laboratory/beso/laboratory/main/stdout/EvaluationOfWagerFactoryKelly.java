package beso.laboratory.main.stdout;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.pojo.Budget;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Wager;
import beso.recommendation.WagerFactoryKelly;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryFavorite;

public class EvaluationOfWagerFactoryKelly {

  public static void main(final String... args) {
    final EvaluationOfWagerFactoryKelly main = new EvaluationOfWagerFactoryKelly();
    main.start();
  }

  private void start() {
    List<Quota> quotas = BesoDao.me().findQuotas(100);
    final Budget totalBudget = new Budget(100);
    final WagerOnFactory wagerOnFactory = new WagerOnFactoryFavorite();
    final WagerFactoryKelly wagerFactory = new WagerFactoryKelly(wagerOnFactory, quotas);
    wagerFactory.doNotRecommandNotToBetMatches();
    final List<Wager> wagers = wagerFactory.getWagerRecommendation(Quota.getMatches(quotas), totalBudget);
    final Profit sum = new Profit();
    for (Wager wager : wagers) {
      System.out.println(BesoFormatter.formatVerbose(wager));
      sum.add(wager.getActualProfit());
    }
  }
}