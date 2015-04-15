package beso.recommendation;

import java.util.List;

import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Team;
import static beso.base.BesoFormatter.format;

public class AllTeams implements Launchable {

  @Override
  public void launch(final String... args) {
    final List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams) {
      System.out.println(format(team));
    }
  }
}
