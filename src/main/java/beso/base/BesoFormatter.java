package beso.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import beso.model.Competition;
import beso.model.Match;
import beso.model.Odds;
import beso.model.Team;

public class BesoFormatter {

  private static final int MIN_CHARCOUNT_TEAM_NAME = 25;

  private static String appendToLength(final char character, final String subject, final int length) {
    if (subject.length() < length) {
      return subject + StringUtils.repeat(character, length - subject.length());
    }
    return subject;
  }

  public static String appendToLength(final double subject, final int length) {
    return appendToLength(subject + "", length);
  }

  public static String appendToLength(final int subject, final int length) {
    return appendToLength(subject + "", length);
  }

  public static String appendToLength(final String subject, final int length) {
    return appendToLength(' ', subject, length);
  }

  public static String format(final Competition competition) {
    return competition.getName();
  }

  private static String format(final Date date) {
    return new SimpleDateFormat("dd.MM.yy HH:mm").format(date);
  }

  private static String format(final Date start, final Team team1, final Team team2) {
    return format(start) + "  " + format(team1, team2);
  }

  public static String format(final Match match) {
    final String dateAndName = format(match.getStart(), match.getTeam1(), match.getTeam2());
    final String format = "%s:%s";
    final String matchResult = match.isFinished() ? String.format(format, match.getGoalsTeam1(), match.getGoalsTeam2()) : String.format(format, "?", "?");
    return dateAndName + "  " + matchResult;
  }

  public static String format(final Odds odds) {
    String rateTeam1 = appendToLength(odds.getRateTeam1(), 4);
    String rateTeam2 = appendToLength(odds.getRateTeam2(), 4);
    String rateDraw = appendToLength(odds.getRateDraw(), 4);
    return format(odds.getMatch()) + "  " + String.format("%s | %s | %s", rateTeam1, rateDraw, rateTeam2);
  }

  public static String format(final Team team) {
    return team.getName();
  }

  private static String format(final Team team1, final Team team2) {
    String result1 = appendToLength('.', team1.getName() + " ", MIN_CHARCOUNT_TEAM_NAME);
    String result2 = prependToLength('.', " " + team2.getName(), MIN_CHARCOUNT_TEAM_NAME);
    return result1 + result2;
  }

  private static String prependToLength(final char character, final String subject, final int length) {
    if (subject.length() < length) {
      return StringUtils.repeat(character, length - subject.length()) + subject;
    }
    return subject;
  }
}
