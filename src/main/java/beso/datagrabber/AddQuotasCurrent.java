package beso.datagrabber;

import java.io.PrintStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.dao.QueryBuilderMatch;
import beso.main.Launchable;
import beso.pojo.Match;
import beso.services.QuotaService;
import beso.services.QuotaServiceCurrentFussbalportalDe;
import beso.tools.BesoAsciiArtTable;

@Component
public class AddQuotasCurrent implements Launchable {

  @Autowired
  private PrintStream defaultPrintStream;
  @Autowired
  private BesoAsciiArtTable table;

  @Override
  public Object getDoc() {
    return "try to parse new quotas for matches not finished yet";
  }

  // insert all matches of all known competitions of the last 5 years
  @Override
  public void launch() {
    final Query query = new QueryBuilderMatch().withoutQuota().withoutResult().sortByStartAsc().build().limit(30);
    final List<Match> matches = BesoDao.me().find(query, Match.class);
    final QuotaService quotaService = new QuotaServiceCurrentFussbalportalDe(matches);
    final List<Match> matchesWithQuotaFound = quotaService.getMatchesWithQuotaFound();
    table.addHeaderColsForMatch(false);
    table.addHeaderColsForQuota();
    for (Match matchWithQuotaFound : matchesWithQuotaFound) {
      table.addContentCols(matchWithQuotaFound, false);
      table.addContentColsQuota(matchWithQuotaFound);
      matchWithQuotaFound.save();
    }
    defaultPrintStream.println(matchesWithQuotaFound.size() + " quotas upserted");
  }
}
