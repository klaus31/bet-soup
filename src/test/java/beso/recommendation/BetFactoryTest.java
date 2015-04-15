package beso.recommendation;

import org.junit.BeforeClass;
import org.junit.Test;

import beso.TestBeanFactory;
import beso.pojo.Match;
import beso.pojo.Quota;
import beso.pojo.WagerOn;
import static beso.pojo.WagerOn.TEAM_1_WIN;
import static beso.pojo.WagerOn.TEAM_2_WIN;

import static org.junit.Assert.assertEquals;

public class BetFactoryTest {

  private static Match match;

  @BeforeClass
  public static void construct() {
    match = TestBeanFactory.getMatch();
  }

  @Test
  public void getWagerOnWhenQuotaBetween() {
    WagerOnFactory factory = new WagerOnFactoryRateBetween(1D, 1.5D);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(new Quota(match, 1, 2, 3)));
    assertEquals(TEAM_2_WIN, factory.getWagerOn(new Quota(match, 1.51, 2, 1.3)));
    assertEquals(WagerOn.DRAW, factory.getWagerOn(new Quota(match, 1.51, 1.5, 2.3)));
    assertEquals(null, factory.getWagerOn(new Quota(match, 2, 2, 2.3)));
    assertEquals(WagerOn.UNSURE, factory.getWagerOn(new Quota(match, 1.2, 1.4, 2.3)));
  }
}
