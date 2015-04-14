package beso.laboratory.main.data;

import java.util.Calendar;
import java.util.List;

import beso.dao.BesoDao;
import beso.model.Competition;
import beso.model.Match;
import beso.services.OddsServiceOpenLigaDb;
import static beso.base.BesoFormatter.format;

public class AddTeamsAndMatches {

  public static void main(final String[] args) {
    final AddTeamsAndMatches step = new AddTeamsAndMatches();
    step.start();
  }

  // insert all matches of all known competitions of the last 5 years
  private void start() {
    final List<Competition> competitions = BesoDao.me().findCompetitions();
    for (Competition competition : competitions) {
      final OddsServiceOpenLigaDb openLigaDb = new OddsServiceOpenLigaDb(competition);
      final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
      int indexYear = currentYear - 5;
      final List<Match> matches = openLigaDb.getMatchData(indexYear);
      while (++indexYear <= currentYear) {
        matches.addAll(openLigaDb.getMatchData(indexYear));
      }
      for (Match match : matches) {
        System.out.println(format(match));
        match.save();
      }
      System.out.println(matches.size() + " upserted");
    }
  }
}
