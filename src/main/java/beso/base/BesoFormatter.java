package beso.base;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import beso.pojo.BetResult;
import beso.pojo.Budget;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Profit;
import beso.pojo.Quota;
import beso.pojo.Team;
import beso.pojo.Wager;
import beso.pojo.WagerOn;

public class BesoFormatter {

  public static String format(final BetResult betResult) {
    return betResult == null ? "???" : betResult.toString();
  }

  public static String format(final Budget budget) {
    return formatEuro(budget.getValue());
  }

  public static String format(final Competition competition) {
    return competition.getName();
  }

  public static String format(final Date date) {
    return new SimpleDateFormat("dd.MM.yy HH:mm").format(date);
  }

  private static String format(final Date start, final Team team1, final Team team2) {
    return format(start) + "  " + format(team1, team2);
  }

  public static String format(final double number, final String pattern) {
    final DecimalFormat df = new DecimalFormat(pattern);
    return df.format(number);
  }

  @Deprecated
  public static String format(final Match match) {
    final String dateAndName = format(match.getStart(), match.getTeam1(), match.getTeam2());
    final String format = "%s:%s";
    final String matchResult = match.isFinished() ? String.format(format, match.getGoalsTeam1(), match.getGoalsTeam2()) : String.format(format, "?", "?");
    return dateAndName + "  " + matchResult;
  }

  public static Object format(final Profit profit) {
    return formatEuro(profit.getValue());
  }

  public static String format(final Quota quota) {
    final String rateTeam1 = format(quota.getRateTeam1(), "0.00");
    final String rateTeam2 = format(quota.getRateTeam2(), "0.00");
    final String rateDraw = format(quota.getRateDraw(), "0.00");
    final String rates = String.format("%s|%s|%s", rateTeam1, rateDraw, rateTeam2);
    return format(quota.getMatch()) + "  " + rates;
  }

  public static String format(final Team team) {
    return team.getName();
  }

  @Deprecated
  private static String format(final Team team1, final Team team2) {
    return team1.getName() + ":" + team2.getName();
  }

  public static String format(final WagerOn bet) {
    return bet == null ? "NOT" : bet.toString();
  }

  public static String formatEuro(final double value) {
    return format(value, "0.00 €");
  }

  public static String formatPercent(final Double number) {
    final DecimalFormat df = new DecimalFormat("0.00 %");
    return df.format(number);
  }

  @Deprecated
  public static String formatVerbose(final Wager wager) {
    final Quota quota = wager.getQuota();
    final Profit profitChance = wager.getActualProfit();
    final Double value = wager.getValue();
    final BetResult betResult = wager.getBetResult();
    return format(quota) + "  " + format(wager.getWagerOn()) + "  " + formatEuro(value) + "  " + formatEuro(profitChance.getValue()) + " " + format(betResult);
  }
}
