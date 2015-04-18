package beso.recommendation;

import java.util.ArrayList;
import java.util.List;

import beso.pojo.Match;
import beso.pojo.WagerOn;

public class WagerOnFactoryFavorite implements WagerOnFactory {

  private final List<WagerOnFactory> factories = new ArrayList<>();

  public WagerOnFactoryFavorite() {
    factories.add(new WagerOnFactoryRateBetween(1, 1.3));
    // TODO handle this    factories.add(new WagerOnFactoryMatchResults(team, matches));
  }

  @Override
  public WagerOn getWagerOn(final Match match) {
    WagerOn result = null;
    for (WagerOnFactory factory : factories) {
      if (result == null) {
        result = factory.getWagerOn(match);
      } else {
        WagerOn currentWagerOn = factory.getWagerOn(match);
        if (currentWagerOn != result) {
          result = WagerOn.UNSURE;
        }
      }
    }
    return result;
  }

  @Override
  public String getWagerOnDescription() {
    String result = "Recommend a wager on if these factories agree: ";
    for (WagerOnFactory factory : factories) {
      result += factory.getWagerOnDescription();
    }
    return result;
  }
}
