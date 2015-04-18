package beso.datagrabber;

import java.io.PrintStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Match;
import beso.pojo.Quota;
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
    final List<Match> matches = BesoDao.me().findMatchesFinishedAndWithoutQuota();
    final QuotaService quotaService = new QuotaServiceTipicoArchive(matches);
    final List<Quota> quotas = quotaService.getQuotasFound();
    table.addHeaderColsForMatch(false);
    table.addHeaderColsForQuota();
    for (Quota quota : quotas) {
      table.addContentCols(quota.getMatch(), false);
      table.addContentCols(quota);
      quota.save();
    }
    defaultPrintStream.println(quotas.size() + " quotas upserted");
  }
}
