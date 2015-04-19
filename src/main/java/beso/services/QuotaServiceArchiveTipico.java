package beso.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import beso.pojo.Competition;
import beso.pojo.Match;

public class QuotaServiceArchiveTipico extends QuotaServiceAbstract {

  public QuotaServiceArchiveTipico(final List<Match> matches) {
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
  Match getMatchWithQuota(final Match match, final String content) {
    final String teamMarker1 = getTipicoTeamRegex(match.getTeam1().getName());
    final String teamMarker2 = getTipicoTeamRegex(match.getTeam2().getName());
    try {
      String contentCut = content.substring(endOf(".+Archiv.+?" + new SimpleDateFormat("dd.MM").format(match.getStart()), content)).trim();
      contentCut = contentCut.substring(0, contentCut.indexOf("Wettschein"));
      final String regex = teamMarker1 + ".+?" + match.getGoalsTeam1() + ":" + match.getGoalsTeam2() + ".+?" + teamMarker2 + ".+?[1-9]";
      final int finalCutAt = endOf(regex, contentCut) - 1;
      if (finalCutAt < 0) {
        System.err.println("Tipico service failed: " + match.getId() + " (" + teamMarker1 + ":" + teamMarker2 + ")");
        return null;
      }
      contentCut = contentCut.substring(finalCutAt).trim();
      final String[] quotaString = contentCut.split(" ");
      final double rateTeam1 = Double.parseDouble(quotaString[0].replace(',', '.'));
      final double rateDraw = Double.parseDouble(quotaString[1].replace(',', '.'));
      final double rateTeam2 = Double.parseDouble(quotaString[2].replace(',', '.'));
      match.setRateTeam1(rateTeam1);
      match.setRateDraw(rateDraw);
      match.setRateTeam2(rateTeam2);
      return match;
    } catch (Exception e) {
      System.err.println("Tipico service failed: " + match.getId() + " (" + teamMarker1 + ":" + teamMarker2 + ")");
      return null;
    }
  }

  private String getTipicoTeamRegex(final String defaultTeamName) {
    switch (defaultTeamName) {
    case "Bayer 04 Leverkusen":
      return "Leverkusen";
    case "TSG 1899 Hoffenheim":
      return "Hoffenheim";
    case "1. FSV Mainz 05":
      return "Mainz";
    case "1. FC Nürnberg":
      return "N(ü|u|ue)rnberg";
    case "1. FC Köln":
      return "K(ö|o|oe)ln";
    case "Borussia Dortmund":
      return "Dortmund";
    case "Bayern München":
      return "M(ü|u|ue)nchen";
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
      return "(Aue|AUE)";
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
      return "Ober\\.?";
    case "Eintracht Braunschweig":
      return "Braunschweig";
    case "SpVgg Greuther Fuerth":
      return "F(ü|u|ue)rth";
    case "Fortuna Düsseldorf":
      return "D(ü|u|ue)sseldorf";
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
      return "(Hamburg|HSV)";
    case "Hannover 96":
      return "Hannover";
    case "Werder Bremen":
      return "Bremen";
    case "FC St. Pauli":
      return "Pauli";
    case "Eintracht Frankfurt":
      return "Eintracht Frankfurt";
    case "FSV Frankfurt":
      return "FSV";
    case "VfB Stuttgart":
      return "Stuttgart";
    case "Hertha BSC":
      return "Hertha";
    case "MSV Duisburg":
      return "Duisburg";
    case "FC Augsburg":
      return "Augsburg";
    case "VfL Osnabrück":
      return "Osnabr(ü|u|ue)ck";
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
      System.err.println(defaultTeamName);
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
    Calendar start = match.getStartCalendar();
    start.setFirstDayOfWeek(Calendar.MONDAY);
    return String.format(urlFormat, leagueShortcut, start.get(Calendar.YEAR), start.get(Calendar.WEEK_OF_YEAR));
  }
}
