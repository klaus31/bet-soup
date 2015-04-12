package beso;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import beso.dao.SpringMongoTestConfig;
import beso.model.Match;
import beso.model.Odds;
import beso.model.Team;

public class MockFactory {

  public static ApplicationContext getApplicationContext() {
    return new AnnotationConfigApplicationContext(SpringMongoTestConfig.class);
  }

  public static Match getMatch() {
    Team team1 = new Team("1. FC Onion");
    Team team2 = new Team("1. FC Garlic");
    return new Match(team1, team2);
  }

  public static Odds getOdds(final double rateTeam1, final double rateDraw, final double rateTeam2) {
    Match match = getMatch();
    return new Odds(match, rateTeam1, rateDraw, rateTeam2);
  }

  public static Odds getOddsWithFinishedMatch(final int goalsTeam1, final int goalsTeam2, final double rateTeam1, final double rateDraw, final double rateTeam2) {
    Odds result = getOdds(rateTeam1, rateDraw, rateTeam2);
    result.getMatch().setGoals(goalsTeam1, goalsTeam2);
    return result;
  }
}
