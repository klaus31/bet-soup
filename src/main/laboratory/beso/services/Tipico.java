package beso.services;

import java.util.Calendar;
import java.util.List;

import beso.model.Competition;
import beso.model.Match;
import beso.model.Odds;

public class Tipico extends OddsServiceAbstract {

  public Tipico(final List<Match> matches) {
    super(matches);
  }

  private String getLeagueShortcut(final Competition competition) {
    if (competition.isFootballBundesliga1()) {
      return "g42301-1-bundesliga";
    } else if (competition.isFootballBundesliga2()) {
      return "g41301-2-bundesliga";
    }
    return null;
  }

  @Override
  Odds getOdds(final Match match, final String content) {
    final String team1 = getTipicoTeamName(match.getTeam1().getName());
    final String team2 = getTipicoTeamName(match.getTeam2().getName());
    try {
      String part = content.substring(content.indexOf("Archiv") + 1);
      part = part.substring(part.indexOf("Archiv") + 1);
      part = part.substring(part.indexOf("Archiv") + 1);
      part = part.substring(part.indexOf(team1));
      if (part.indexOf(team2) > 50) {
        part = part.substring(part.indexOf(team1));
      }
      part = part.substring(part.indexOf(team2) + team2.length());
      part = part.substring(0, 20).trim();
      final String[] oddsString = part.split(" ");
      final double rateTeam1 = Double.parseDouble(oddsString[0].replace(',', '.'));
      final double rateDraw = Double.parseDouble(oddsString[1].replace(',', '.'));
      final double rateTeam2 = Double.parseDouble(oddsString[2].replace(',', '.'));
      return new Odds(match, rateTeam1, rateDraw, rateTeam2);
    } catch (Exception e) {
      return null;
    }
  }

  private String getTipicoTeamName(final String defaultTeamName) {
    String result = defaultTeamName;
    if (result.equals("Bayer 04 Leverkusen")) {
      result = "Bayer Leverkusen";
    } else if (result.equals("TSG 1899 Hoffenheim")) {
      result = "Hoffenheim";
    } else if (result.equals("1. FSV Mainz 05")) {
      result = "FSV Mainz 05";
    } else if (result.equals("1. FC Köln")) {
      result = "1.FC Köln";
    } else if (result.equals("Borussia Dortmund")) {
      result = "Dortmund";
    } else if (result.equals("Bayern München")) {
      result = "München";
    } else if (result.equals("1. FC Heidenheim 1846")) {
      result = "Heidenheim";
    } else if (result.equals("SV Darmstadt 98")) {
      result = "Darmstadt";
    } else if (result.equals("RB Leipzig")) {
      result = "Leipzig";
    } else if (result.equals("SV Sandhausen")) {
      result = "Sandhausen";
    } else if (result.equals("VfR Aalen")) {
      result = "Aalen";
    } else if (result.equals("Jahn Regensburg")) {
      result = "Regensburg";
    } else if (result.equals("FC Erzgebirge Aue") || result.equals("Erzgebirge Aue")) {
      result = "Aue";
    } else if (result.equals("FC Hansa Rostock")) {
      result = "Rostock";
    } else if (result.equals("SG Dynamo Dresden")) {
      result = "Dresden";
    } else if (result.equals("TSV 1860 München")) {
      result = "1860";
    } else if (result.equals("VfL Bochum")) {
      result = "Bochum";
    } else if (result.equals("1. FC Union Berlin")) {
      result = "Union Berlin";
    } else if (result.equals("Alemannia Aachen")) {
      result = "Aachen";
    } else if (result.equals("Rot-Weiss Oberhausen")) {
      result = "Oberhausen";
    } else if (result.equals("Eintracht Braunschweig")) {
      result = "Braunschweig";
    } else if (result.equals("SpVgg Greuther Fuerth")) {
      result = "Fuerth";
    } else if (result.equals("Fortuna Düsseldorf")) {
      result = "Düsseldorf";
    } else if (result.equals("Arminia Bielefeld")) {
      result = "Bielefeld";
    } else if (result.equals("FC Ingolstadt 04")) {
      result = "Ingolstadt";
    } else if (result.equals("Energie Cottbus")) {
      result = "Cottbus";
    } else if (result.equals("Karlsruher SC")) {
      result = "Karlsruher";
    } else if (result.equals("FC Schalke 04")) {
      result = "Schalke";
    } else if (result.equals("SC Paderborn 07")) {
      result = "Paderborn";
    } else if (result.equals("Borussia Mönchengladbach")) {
      result = "M'gladbach";
    }
    return result;
  }

  @Override
  String getUrl(final Match match) {
    if (!match.isFinished()) {
      return null;
    }
    final String urlFormat = "https://www.tipico.de/de/ergebnisse/fussball/deutschland/%s/%s/kw%s/";
    final String leagueShortcut = getLeagueShortcut(match.getCompetition());
    return String.format(urlFormat, leagueShortcut, match.getStartCalendar().get(Calendar.YEAR), match.getStartCalendar().get(Calendar.WEEK_OF_YEAR));
  }
}
