package beso.laboratory.main.data;

import java.util.Calendar;
import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.model.Competition;
import beso.model.Match;
import beso.services.OpenLigaDb;

public class AddTeamsAndMatches {

  public static void main(final String[] args) {
    AddTeamsAndMatches step = new AddTeamsAndMatches();
    step.start();
  }

  // insert all matches of all known competitions of the last 5 years
  private void start() {
    List<Competition> competitions = BesoDao.me().findCompetitions();
    for (Competition competition : competitions) {
      OpenLigaDb openLigaDb = new OpenLigaDb(competition);
      final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
      int indexYear = currentYear - 5;
      List<Match> matches = openLigaDb.getMatchData(indexYear);
      while (++indexYear <= currentYear) {
        matches.addAll(openLigaDb.getMatchData(indexYear));
      }
      for (Match match : matches) {
        System.out.println(BesoFormatter.format(match.getStart(), match.getTeam1(), match.getTeam2()));
        match.save();
      }
      System.out.println(matches.size() + " upserted");
    }
  }
}
