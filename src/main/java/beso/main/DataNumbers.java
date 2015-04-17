package beso.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Quota;
import beso.pojo.Team;
import beso.tools.BesoTable;

@Component
public class DataNumbers implements Launchable {

  @Autowired
  private BesoTable table;

  @Override
  public void launch() {
    // count things (without using mongos count)
    table.addHeadline("DATA NUMBERS");
    table.addHeadline("count of documents in your database");
    table.addHeaderCols("data", "count");
    table.add("competitions", BesoDao.me().countAll(Competition.class));
    table.add("quotas", BesoDao.me().countAll(Quota.class));
    table.add("matches", BesoDao.me().countAll(Match.class));
    table.add("matches finished", BesoDao.me().countMatchesFinished());
    table.add("matches finished but without quota", BesoDao.me().countMatchesFinishedAndWithoutQuota());
    table.add("matches without result", BesoDao.me().countMatchesWithoutResult());
    table.add("teams", BesoDao.me().countAll(Team.class));
    table.print();
  }
}
