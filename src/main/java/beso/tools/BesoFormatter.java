package beso.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import beso.pojo.BetResult;
import beso.pojo.Budget;
import beso.pojo.Competition;
import beso.pojo.Profit;
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

  public static String format(final double number) {
    return format(number, "0.00");
  }

  public static String format(final double number, final String pattern) {
    final DecimalFormat df = new DecimalFormat(pattern);
    return df.format(number);
  }

  public static String format(final Profit profit) {
    return formatEuro(profit.getValue());
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
}
