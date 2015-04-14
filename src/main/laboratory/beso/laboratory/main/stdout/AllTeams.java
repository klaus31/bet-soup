package beso.laboratory.main.stdout;

import java.util.List;

import beso.dao.BesoDao;
import beso.model.Team;
import static beso.base.BesoFormatter.format;

public class AllTeams {

  public static void main(final String... args) {
    final AllTeams main = new AllTeams();
    main.start();
  }

  private void start() {
    final List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams) {
      System.out.println(format(team));
    }
  }
}
