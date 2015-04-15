package beso.base;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import beso.pojo.BetResult;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Team;
import beso.pojo.Wager;
import beso.pojo.WagerOn;

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

  public static String format(final WagerOn bet) {
    final String result = bet == null ? "NOT" : bet.toString();
    return prependToLength(result, WagerOn.TEAM_1_WIN.toString().length() + 1);
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

  public static String format(final double number, final String pattern) {
    final DecimalFormat df = new DecimalFormat(pattern);
    return df.format(number);
  }

  public static String format(final Match match) {
    final String dateAndName = format(match.getStart(), match.getTeam1(), match.getTeam2());
    final String format = "%s:%s";
    final String matchResult = match.isFinished() ? String.format(format, match.getGoalsTeam1(), match.getGoalsTeam2()) : String.format(format, "?", "?");
    return dateAndName + "  " + matchResult;
  }

  public static String format(final Quota quota) {
    final String rateTeam1 = prependToLength(format(quota.getRateTeam1(), "0.00"), 5);
    final String rateTeam2 = prependToLength(format(quota.getRateTeam2(), "0.00"), 5);
    final String rateDraw = prependToLength(format(quota.getRateDraw(), "0.00"), 5);
    final String rates = String.format("%s|%s|%s", rateTeam1, rateDraw, rateTeam2);
    return format(quota.getMatch()) + "  " + rates;
  }

  private static String format(final BetResult betResult) {
    final String result = betResult == null ? "???" : betResult.toString();
    return prependToLength(result, BetResult.UNKNOWN.toString().length() + 1);
  }

  public static Object format(final Profit profit) {
    return formatEuro(profit.getValue());
  }

  public static String format(final Team team) {
    return team.getName();
  }

  private static String format(final Team team1, final Team team2) {
    String result1 = appendToLength('.', team1.getName() + " ", MIN_CHARCOUNT_TEAM_NAME);
    String result2 = prependToLength('.', " " + team2.getName(), MIN_CHARCOUNT_TEAM_NAME);
    return result1 + result2;
  }

  private static String formatEuro(final double value) {
    return format(value, "0.00 €");
  }

  public static String formatPercent(final Double number) {
    final DecimalFormat df = new DecimalFormat("0.00 %");
    return df.format(number);
  }

  public static String formatVerbose(final Wager wager) {
    final Quota quota = wager.getQuota();
    final Profit profitChance = wager.getActualProfit();
    final Double value = wager.getValue();
    final BetResult betResult = wager.getBetResult();
    return format(quota) + "  " + format(wager.getWagerOn()) + "  " + prependToLength(formatEuro(value), 7) + "  " + prependToLength(formatEuro(profitChance.getValue()), 7) + " " + format(betResult);
  }

  private static String prependToLength(final char character, final String subject, final int length) {
    if (subject.length() < length) {
      return StringUtils.repeat(character, length - subject.length()) + subject;
    }
    return subject;
  }

  private static String prependToLength(final String subject, final int length) {
    return prependToLength(' ', subject, length);
  }
}
