package beso.recommendation;

import org.junit.BeforeClass;
import org.junit.Test;

import beso.TestBeanFactory;
import beso.model.Bet;
import beso.model.Match;
import beso.model.Odds;
import static beso.model.Bet.TEAM_1_WIN;
import static beso.model.Bet.TEAM_2_WIN;

import static org.junit.Assert.assertEquals;

public class BetFactoryTest {

  private static Match match;

  @BeforeClass
  public static void construct() {
    match = TestBeanFactory.getMatch();
  }

  @Test
  public void getBetWhenOddsBetween() {
    BetFactory factory = new BetFactoryRateBetween(1D, 1.5D);
    assertEquals(TEAM_1_WIN, factory.getBet(new Odds(match, 1, 2, 3)));
    assertEquals(TEAM_2_WIN, factory.getBet(new Odds(match, 1.51, 2, 1.3)));
    assertEquals(Bet.DRAW, factory.getBet(new Odds(match, 1.51, 1.5, 2.3)));
    assertEquals(null, factory.getBet(new Odds(match, 2, 2, 2.3)));
    assertEquals(Bet.AMBIGUOUS, factory.getBet(new Odds(match, 1.2, 1.4, 2.3)));
  }
}
