package beso.recommendation;

import java.util.List;

import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Wager;

public class WagerFactoryFavorite implements WagerFactory {

  private final WagerFactory factory;

  public WagerFactoryFavorite(final List<Match> referenceMatches) {
    this(new WagerOnFactoryFavorite(), referenceMatches);
  }

  public WagerFactoryFavorite(final WagerOnFactory factory, final List<Match> referenceMatches) {
    this.factory = new WagerFactoryKelly(factory, referenceMatches);
  }

  @Override
  public List<Wager> getWagerRecommendation(final List<Match> matches, final Budget totalBudget) {
    return factory.getWagerRecommendation(matches, totalBudget);
  }
}
