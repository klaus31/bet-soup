package beso.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import beso.model.Match;
import beso.model.Odds;
import beso.model.Team;

public class BesoFormatter {

  private static final int MIN_CHARCOUNT_TEAM_NAME = 25;

  private static String format(final Date date) {
    return new SimpleDateFormat("dd.MM.yy HH:mm").format(date);
  }

  private static String format(final Date start, final Team team1, final Team team2) {
    return format(start) + "  " + format(team1, team2);
  }

  public static String format(final Match match) {
    return format(match.getStart(), match.getTeam1(), match.getTeam2());
  }

  public static String format(final Odds odds) {
    return format(odds.getMatch()) + "  " + String.format("%s | %s | %s", odds.getRateTeam1(), odds.getRateDraw(), odds.getRateTeam2());
  }

  public static String format(final Team team) {
    return team.getName();
  }

  private static String format(final Team team1, final Team team2) {
    String result1 = team1.getName() + " ";
    while (result1.length() < MIN_CHARCOUNT_TEAM_NAME) {
      result1 += ".";
    }
    String result2 = " " + team2.getName();
    while (result2.length() < MIN_CHARCOUNT_TEAM_NAME) {
      result2 = "." + result2;
    }
    return result1 + result2;
  }
}
