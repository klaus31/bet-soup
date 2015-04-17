package beso.datagrabber;

import java.util.List;

import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Team;

@Component
public class DeleteAllTeams implements Launchable {

  @Override
  public void launch() {
    System.err.println("Please kill this line of code to avoid accidents");
    System.exit(0);
    final List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams) {
      System.out.println(team.getName());
      BesoDao.me().remove(team);
    }
  }
}
