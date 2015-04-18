package beso;

import java.util.Calendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import beso.dao.SpringMongoTestConfig;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Team;

public class TestBeanFactory {

  public static ApplicationContext getApplicationContext() {
    return new AnnotationConfigApplicationContext(SpringMongoTestConfig.class);
  }

  public static Match getMatch() {
    return getMatch("1. FC Onion", "1. FC Garlic");
  }

  public static Match getMatch(final int goalsTeam1, final int goalsTeam2) {
    Match match = getMatch();
    match.setGoals(goalsTeam1, goalsTeam2);
    return match;
  }

  public static Match getMatch(final Match match, final double rateTeam1, final double rateDraw, final double rateTeam2) {
    match.setRateDraw(rateDraw);
    match.setRateTeam1(rateTeam1);
    match.setRateTeam2(rateTeam2);
    return match;
  }

  public static Match getMatch(final String nameTeam1, final String nameTeam2) {
    return getMatch(getTeam(nameTeam1), getTeam(nameTeam2));
  }

  public static Match getMatch(final String teamName, final Team team) {
    return getMatch(getTeam(teamName), team);
  }

  public static Match getMatch(final Team team, final String teamName) {
    return getMatch(team, getTeam(teamName));
  }

  public static Match getMatch(final Team team1, final Team team2) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, -1);
    Competition competition = new Competition(Competition.FOOTBALL_BUNDESLIGA_1);
    return new Match(competition, team1, team2, calendar.getTime());
  }

  public static Match getQuota(final double rateTeam1, final double rateDraw, final double rateTeam2) {
    return getMatch(getMatch(), rateTeam1, rateDraw, rateTeam2);
  }

  public static Match getMatch(final int goalsTeam1, final int goalsTeam2, final double rateTeam1, final double rateDraw, final double rateTeam2) {
    Match result = getQuota(rateTeam1, rateDraw, rateTeam2);
    result.setGoals(goalsTeam1, goalsTeam2);
    return result;
  }

  public static Team getTeam(final String name) {
    Team team = new Team(name);
    return team;
  }
}
