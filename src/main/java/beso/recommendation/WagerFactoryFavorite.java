package beso.recommendation;

import java.util.List;

import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Quota;
import beso.pojo.Wager;

public class WagerFactoryFavorite implements WagerFactory {

  private final WagerFactory factory;

  public WagerFactoryFavorite(final List<Quota> referenceQuotas) {
    this(new WagerOnFactoryFavorite(), referenceQuotas);
  }

  WagerFactoryFavorite(final WagerOnFactory factory, final List<Quota> referenceQuotas) {
    this.factory = new WagerFactoryKelly(factory, referenceQuotas);
  }

  @Override
  public List<Wager> getWagerRecommendation(final List<Match> matches, final Budget totalBudget) {
    return factory.getWagerRecommendation(matches, totalBudget);
  }
}
