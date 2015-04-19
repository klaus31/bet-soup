package beso.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import beso.dao.BesoDao;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Team;
import beso.tools.UrlReader;

public class TeamAndMatchesServiceOpenLigaDb {

  private final Competition competition;

  public TeamAndMatchesServiceOpenLigaDb(final Competition competition) {
    if (!BesoDao.me().exists(competition)) {
      competition.save();
    }
    this.competition = competition;
  }

  private String getLeagueShortcut() {
    if (competition.isFootballBundesliga1()) {
      return "bl1";
    } else if (competition.isFootballBundesliga2()) {
      return "bl2";
    }
    return null;
  }

  private Match getMatch(final JSONObject matchJson) {
    final Team team1 = getTeam(matchJson.getJSONObject("Team1"));
    final Team team2 = getTeam(matchJson.getJSONObject("Team2"));
    // start
    final String timestamp = matchJson.getString("MatchDateTimeUTC");
    final DateTime dateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(timestamp);
    // create match
    Match match = BesoDao.me().findMatch(competition, team1, team2, dateTime.toDate());
    if (match == null) {
      match = new Match(competition, team1, team2, dateTime.toDate());
    }
    if (matchJson.getBoolean("MatchIsFinished")) {
      final JSONArray matchResults = matchJson.getJSONArray("MatchResults");
      if (matchResults.length() == 2) {
        JSONObject matchResult = matchResults.getJSONObject(0);
        if (matchResult.getInt("ResultOrderID") != 1) {
          matchResult = matchResults.getJSONObject(1);
        }
        match.setGoals(matchResult.getInt("PointsTeam1"), matchResult.getInt("PointsTeam2"));
      }
    }
    return match;
  }

  public List<Match> getMatchData(final int year) {
    final List<Match> matches = new ArrayList<>();
    final String url = String.format("http://www.openligadb.de/api/getmatchdata/%s/%s", getLeagueShortcut(), year);
    final JSONArray matchesJson = UrlReader.getJsonArrayFromUrl(url);
    for (int index = 0; index < matchesJson.length(); index++) {
      final JSONObject matchJson = matchesJson.getJSONObject(index);
      matches.add(getMatch(matchJson));
    }
    return matches;
  }

  private Team getTeam(final JSONObject jsonObject) {
    Team team = new Team(jsonObject.getString("TeamName"));
    // add teams, if they does not exist
    if (!BesoDao.me().exists(team)) {
      team.save();
    } else {
      team = BesoDao.me().findTeam(team);
    }
    return team;
  }
}
