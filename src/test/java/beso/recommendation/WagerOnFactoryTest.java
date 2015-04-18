package beso.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import beso.TestBeanFactory;
import beso.pojo.Match;
import beso.pojo.WagerOn;
import static beso.pojo.WagerOn.TEAM_1_WIN;
import static beso.pojo.WagerOn.TEAM_2_WIN;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WagerOnFactoryTest {

  private static Match match;

  @BeforeClass
  public static void construct() {
    match = TestBeanFactory.getMatch();
  }

  @Test
  public void getWagerOnWhenQuotaBetween() {
    WagerOnFactory factory = new WagerOnFactoryRateBetween(1D, 1.5D);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(TestBeanFactory.getMatch(match, 1, 2, 3)));
    assertEquals(TEAM_2_WIN, factory.getWagerOn(TestBeanFactory.getMatch(match, 1.51, 2, 1.3)));
    assertEquals(WagerOn.DRAW, factory.getWagerOn(TestBeanFactory.getMatch(match, 1.51, 1.5, 2.3)));
    assertEquals(null, factory.getWagerOn(TestBeanFactory.getMatch(match, 2, 2, 2.3)));
    assertEquals(WagerOn.UNSURE, factory.getWagerOn(TestBeanFactory.getMatch(match, 1.2, 1.4, 2.3)));
  }

  @Test
  public void testWagerOnFactoryMatchResults() {
    List<Match> lastMatches = new ArrayList<>(3);
    lastMatches.add(TestBeanFactory.getMatch(1, 0));
    lastMatches.add(TestBeanFactory.getMatch(1, 0));
    lastMatches.add(TestBeanFactory.getMatch(1, 0));
    // shouldRecommandTeamIfEnoughGivenMatchesWon
    WagerOnFactory factory = new WagerOnFactoryMatchResults(match.getTeam1(), lastMatches);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(match));
    // next match lost but only 75% required
    lastMatches.add(TestBeanFactory.getMatch(0, 0));
    factory = new WagerOnFactoryMatchResults(match.getTeam1(), lastMatches, .75F, MatchResultExpectation.WON);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(match));
    // not lose all games
    factory = new WagerOnFactoryMatchResults(match.getTeam1(), lastMatches, MatchResultExpectation.NOT_LOSE);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(match));
    // 76% required
    factory = new WagerOnFactoryMatchResults(match.getTeam1(), lastMatches, .76F, MatchResultExpectation.WON);
    assertNull(factory.getWagerOn(match));
    // team 2
    factory = new WagerOnFactoryMatchResults(match.getTeam2(), lastMatches);
    assertNull(factory.getWagerOn(match));
    // lose -> recommend the other team
    Match anotherMatch = TestBeanFactory.getMatch("1. FC Another Team", match.getTeam2());
    factory = new WagerOnFactoryMatchResults(match.getTeam2(), lastMatches, MatchResultExpectation.NOT_WON);
    factory = new WagerOnFactoryMatchResults(match.getTeam2(), lastMatches, .75F, MatchResultExpectation.LOSE);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(anotherMatch));
    assertEquals(TEAM_1_WIN, factory.getWagerOn(anotherMatch));
    lastMatches.get(lastMatches.size() - 1).setGoals(2, 1); // correct: team 2 even lose the last match
    factory = new WagerOnFactoryMatchResults(match.getTeam2(), lastMatches, MatchResultExpectation.LOSE);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(anotherMatch));
    factory = new WagerOnFactoryMatchResults(match.getTeam2(), lastMatches, MatchResultExpectation.LOSE);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(anotherMatch));
    factory = new WagerOnFactoryMatchResults(match.getTeam2(), lastMatches, MatchResultExpectation.NOT_WON);
    assertEquals(TEAM_1_WIN, factory.getWagerOn(anotherMatch));
  }
}
