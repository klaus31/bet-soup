package beso.laboratory.main.data;

import java.util.List;

import beso.dao.BesoDao;
import beso.pojo.Team;

public class DeleteAllTeams {

  public static void main(final String[] args) {
    final DeleteAllTeams me = new DeleteAllTeams();
    me.deleteTeams();
  }

  private void deleteTeams() {
    System.err.println("Please kill this line of code to avoid accidents");
    System.exit(0);
    final List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams) {
      System.out.println(team.getName());
      BesoDao.me().remove(team);
    }
  }
}
