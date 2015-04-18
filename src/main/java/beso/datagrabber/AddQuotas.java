package beso.datagrabber;

import java.io.PrintStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Match;
import beso.services.QuotaService;
import beso.services.QuotaServiceTipicoArchive;
import beso.tools.BesoAsciiArtTable;

@Component
public class AddQuotas implements Launchable {

  @Autowired
  private PrintStream defaultPrintStream;
  @Autowired
  private BesoAsciiArtTable table;

  @Override
  public Object getDoc() {
    return "try to parse all quotas for all matches finished out of the internet and store it into the database";
  }

  // insert all matches of all known competitions of the last 5 years
  @Override
  public void launch() {
    final List<Match> matches = BesoDao.me().findMatchesFinishedWithoutQuota();
    final QuotaService quotaService = new QuotaServiceTipicoArchive(matches);
    final List<Match> matchesWithQuotaFound = quotaService.getMatchesWithQuotaFound();
    table.addHeaderColsForMatch(false);
    table.addHeaderColsForQuota();
    for (Match matchWithQuotaFound : matchesWithQuotaFound) {
      table.addContentCols(matchWithQuotaFound, false);
      table.addContentCols(matchWithQuotaFound);
      matchWithQuotaFound.save();
    }
    defaultPrintStream.println(matches.size() + " quotas upserted");
  }
}
