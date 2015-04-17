package beso.data;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.base.BesoTable;
import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.services.QuotaServiceOpenLigaDb;

@Component
public class AddTeamsAndMatches implements Launchable {

  @Autowired
  private PrintStream defaultPrintStream;
  @Autowired
  private BesoTable table;

  // insert all matches of all known competitions of the last 5 years
  @Override
  public void launch(final String... args) {
    final List<Competition> competitions = BesoDao.me().findCompetitions();
    table.addHeadline("ADD ALL TEAMS AND MATCHES");
    table.addHeadline("this tries to get new teams and matches of all known competitions out of the internet and put it into your db. a team is unknown, if the team name differs from db name. special thx to www.openligadb.de");
    table.addHeaderColsForMatch(true);
    for (Competition competition : competitions) {
      final QuotaServiceOpenLigaDb openLigaDb = new QuotaServiceOpenLigaDb(competition);
      final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
      int indexYear = currentYear - 5;
      final List<Match> matches = openLigaDb.getMatchData(indexYear);
      while (++indexYear <= currentYear) {
        matches.addAll(openLigaDb.getMatchData(indexYear));
      }
      for (Match match : matches) {
        table.addContentCols(match, true);
        match.save();
      }
      table.print();
      defaultPrintStream.println(matches.size() + " upserted");
    }
  }
}
