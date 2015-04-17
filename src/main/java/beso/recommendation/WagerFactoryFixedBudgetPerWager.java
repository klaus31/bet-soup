package beso.recommendation;

import java.util.ArrayList;
import java.util.List;

import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Quota;
import beso.pojo.Wager;
import beso.pojo.WagerOn;

public class WagerFactoryFixedBudgetPerWager implements WagerFactory {

  private final WagerOnFactory factory;

  public WagerFactoryFixedBudgetPerWager() {
    this(new WagerOnFactoryFavorite());
  }

  public WagerFactoryFixedBudgetPerWager(final WagerOnFactory factory) {
    this.factory = factory;
  }

  @Override
  public List<Wager> getWagerRecommendation(final List<Match> matches, final Budget fixedBudget) {
    final List<Wager> wagers = new ArrayList<>(matches.size());
    for (Match match : matches) {
      final Quota quota = match.getQuota();
      WagerOn bet = factory.getWagerOn(quota);
      final Double profitRate = quota.getRate(bet);
      if ((profitRate != null && profitRate > 1)) {
        wagers.add(new Wager(fixedBudget, factory.getWagerOn(quota), quota));
      }
    }
    return wagers;
  }
}
