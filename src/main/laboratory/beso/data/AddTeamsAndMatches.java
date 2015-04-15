package beso.data;

import java.util.Calendar;
import java.util.List;

import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.services.QuotaServiceOpenLigaDb;
import static beso.base.BesoFormatter.format;

public class AddTeamsAndMatches implements Launchable {

  // insert all matches of all known competitions of the last 5 years
  @Override
  public void launch(final String... args) {
    final List<Competition> competitions = BesoDao.me().findCompetitions();
    for (Competition competition : competitions) {
      final QuotaServiceOpenLigaDb openLigaDb = new QuotaServiceOpenLigaDb(competition);
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
