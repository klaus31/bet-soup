package beso;

import java.util.Calendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import beso.dao.SpringMongoTestConfig;
import beso.model.Competition;
import beso.model.Match;
import beso.model.Odds;
import beso.model.Team;

public class TestBeanFactory {

  public static ApplicationContext getApplicationContext() {
    return new AnnotationConfigApplicationContext(SpringMongoTestConfig.class);
  }

  public static Match getMatch() {
    return getMatch("1. FC Onion", "1. FC Garlic");
  }

  public static Match getMatch(final String nameTeam1, final String nameTeam2) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, -1);
    Competition competition = new Competition(Competition.FOOTBALL_BUNDESLIGA_1);
    return new Match(competition, getTeam(nameTeam1), getTeam(nameTeam2), calendar.getTime());
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

  public static Team getTeam(final String name) {
    Team team = new Team(name);
    return team;
  }
}
