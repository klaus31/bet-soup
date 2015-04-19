package beso.services;

import java.text.SimpleDateFormat;
import java.util.List;

import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Team;

public class QuotaServiceCurrentFussbalportalDe extends QuotaServiceAbstract {

  public QuotaServiceCurrentFussbalportalDe(final List<Match> matches) {
    super(matches);
  }

  private String getLeagueShortcut(final Competition competition) {
    if (competition.isFootballBundesliga1()) {
      return "1-bundesliga/bundesliga-wettquoten";
    } else if (competition.isFootballBundesliga2()) {
      return "2-bundesliga/2-bundesliga-wettquoten";
    }
    return null;
  }

  @Override
  Match getMatchWithQuota(final Match match, final String content) {
    try {
      final String team1Name = getSearchString(match.getTeam1());
      final String team2Name = getSearchString(match.getTeam2());
      if (content.indexOf(new SimpleDateFormat("dd.MM. HH:mm").format(match.getStart())) > 0) {
        // right day
        int endOfTeam2 = endOf(team1Name + ".+" + team2Name + ".+?[1-9]", content);
        if (endOfTeam2 > 0) {
          // team is part of the page
          String finalCut = content.substring(endOfTeam2 - 1);
          String[] quotenString = finalCut.split(" ");
          match.setRateTeam1(Double.parseDouble(quotenString[0].replace(',', '.')));
          match.setRateDraw(Double.parseDouble(quotenString[1].replace(',', '.')));
          match.setRateTeam2(Double.parseDouble(quotenString[2].replace(',', '.')));
        }
      }
    } catch (Exception e) {
      // ok - it was just a try worth it
    }
    return match.hasQuota() ? match : null;
  }

  private String getSearchString(final Team team) {
    String result = team.getName();
    if (result.equals("Bayer 04 Leverkusen")) {
      result = "Bayer Leverkusen";
    } else if (result.equals("TSG 1899 Hoffenheim")) {
      result = "1899 Hoffenheim";
    } else if (result.equals("SpVgg Greuther Fuerth")) {
      result = "Greuther Fürth";
    } else if (result.equals("Eintracht Braunschweig")) {
      result = "Braunschweig";
    } else if (result.equals("1. FC Union Berlin")) {
      result = "Union Berlin";
    } else if (result.equals("FC Ingolstadt 04")) {
      result = "Ingolstadt";
    } else if (result.equals("FC Erzgebirge Aue")) {
      result = "Erzgebirge Aue";
    } else if (result.equals("1. FC Heidenheim 1846")) {
      result = "Heidenheim";
    } else if (result.equals("TSV 1860 München")) {
      result = "1860";
    } else if (result.equals("FC Schalke 04")) {
      result = "Schalke 04";
    } else if (result.equals("SC Paderborn 07")) {
      result = "SC Paderborn";
    } else if (result.equals("Borussia Mönchengladbach")) {
      result = "Mönchengladbach";
    }
    return result;
  }

  @Override
  String getUrl(final Match match) {
    final String urlFormat = "http://www.fussballportal.de/news/%s/";
    final String leagueShortcut = getLeagueShortcut(match.getCompetition());
    return String.format(urlFormat, leagueShortcut);
  }
}
