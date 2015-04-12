package beso.base;

import java.util.List;

import beso.dao.BesoDao;
import beso.model.Team;

public class Main {

  public static void main(final String... args) {
    // TODO put out next bet recommendations of matches not finished yet
    // just a dummy output yet
    List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams) {
      System.out.println(team.getName());
    }
  }
}
