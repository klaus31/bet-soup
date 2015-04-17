package beso.recommendation;

import beso.pojo.Quota;
import beso.pojo.WagerOn;

public class WagerOnFactoryFavorite implements WagerOnFactory {

  public WagerOnFactory theBestWagerOnFactoryInTheHolyFuckingWorldMakingYouVeryRich = new WagerOnFactoryRateBetween(1, 1.3);

  @Override
  public WagerOn getWagerOn(final Quota quota) {
    return theBestWagerOnFactoryInTheHolyFuckingWorldMakingYouVeryRich.getWagerOn(quota);
  }
}
