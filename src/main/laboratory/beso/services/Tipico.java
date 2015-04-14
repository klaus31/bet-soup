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
    final String team1 = getTipicoUnambiguousStringForTeam(match.getTeam1().getName());
    final String team2 = getTipicoUnambiguousStringForTeam(match.getTeam2().getName());
    try {
      String part = content.substring(content.indexOf("Archiv") + 1);
      part = part.substring(part.indexOf("Archiv") + 1);
      part = part.substring(part.indexOf("Archiv") + 1);
      part = part.substring(part.indexOf(team1));
      if (part.indexOf(team2) > 50) { // two matchdays this week and this is the second match (hopefully)
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

  private String getTipicoUnambiguousStringForTeam(final String defaultTeamName) {
    switch (defaultTeamName) {
    case "Bayer 04 Leverkusen":
      return "Leverkusen";
    case "TSG 1899 Hoffenheim":
      return "Hoffenheim";
    case "1. FSV Mainz 05":
      return "Mainz";
    case "1. FC Nürnberg":
      return "Nürnberg";
    case "1. FC Köln":
      return "Köln";
    case "Borussia Dortmund":
      return "Dortmund";
    case "Bayern München":
      return "München";
    case "1. FC Heidenheim 1846":
      return "Heidenheim";
    case "SV Darmstadt 98":
      return "Darmstadt";
    case "RB Leipzig":
      return "Leipzig";
    case "SV Sandhausen":
      return "Sandhausen";
    case "VfR Aalen":
      return "Aalen";
    case "Jahn Regensburg":
      return "Regensburg";
    case "FC Erzgebirge Aue":
    case "Erzgebirge Aue":
      return "Aue";
    case "FC Hansa Rostock":
      return "Rostock";
    case "SG Dynamo Dresden":
      return "Dresden";
    case "TSV 1860 München":
      return "1860";
    case "VfL Bochum":
      return "Bochum";
    case "1. FC Union Berlin":
      return "Union Berlin";
    case "Alemannia Aachen":
      return "Aachen";
    case "Rot-Weiss Oberhausen":
      return "Oberhausen";
    case "Eintracht Braunschweig":
      return "Braunschweig";
    case "SpVgg Greuther Fuerth":
      return "Fuerth";
    case "Fortuna Düsseldorf":
      return "Düsseldorf";
    case "Arminia Bielefeld":
      return "Bielefeld";
    case "FC Ingolstadt 04":
      return "Ingolstadt";
    case "Energie Cottbus":
      return "Cottbus";
    case "Karlsruher SC":
      return "Karlsruher";
    case "FC Schalke 04":
      return "Schalke";
    case "VfL Wolfsburg":
      return "Wolfsburg";
    case "Hamburger SV":
      return "Hamburg";
    case "Hannover 96":
      return "Hannover";
    case "Werder Bremen":
      return "Bremen";
    case "FC St. Pauli":
      return "Pauli";
    case "Eintracht Frankfurt":
    case "FSV Frankfurt":
      // XXX this does not work if FSV and Eintracht are playing the same competition the week
      return "Frankfurt";
    case "VfB Stuttgart":
      return "Stuttgart";
    case "Hertha BSC":
      return "Hertha";
    case "MSV Duisburg":
      return "Duisburg";
    case "FC Augsburg":
      return "Augsburg";
    case "SC Freiburg":
      return "Freiburg";
    case "SC Paderborn 07":
      return "Paderborn";
    case "Borussia Mönchengladbach":
      // Mönchengladbach, M'gladbach, M'Gladbach ...
      return "ladbach";
    case "1. FC Kaiserslautern":
      // Kaiserslautern, K'lautern ...
      return "lautern";
    default:
      System.out.println(defaultTeamName);
      return defaultTeamName;
    }
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
