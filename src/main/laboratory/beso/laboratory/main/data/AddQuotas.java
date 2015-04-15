package beso.laboratory.main.data;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.pojo.Match;
import beso.pojo.Quota;
import beso.services.QuotaService;
import beso.services.QuotaServiceTipicoArchive;

public class AddQuotas {

  public static void main(final String[] args) {
    final AddQuotas step = new AddQuotas();
    step.start();
  }

  // insert all matches of all known competitions of the last 5 years
  private void start() {
    final List<Match> matches = BesoDao.me().findMatchesFinishedAndWithoutQuota();
    final QuotaService quotaService = new QuotaServiceTipicoArchive(matches);
    final List<Quota> quotas = quotaService.getQuotasFound();
    for (Quota quota : quotas) {
      System.out.println(BesoFormatter.format(quota));
      quota.save();
    }
    System.out.println(quotas.size() + " quotas upserted");
  }
}
