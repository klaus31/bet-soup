package beso.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.pojo.Team;
import beso.tools.BesoAsciiArtTable;

@Component
public class AllTeams implements Launchable {

  @Autowired
  private BesoAsciiArtTable table;

  @Override
  public Object getDoc() {
    return "show all teams in the database";
  }

  @Override
  public void launch() {
    final List<Team> teams = BesoDao.me().findTeams();
    table.addHeaderCols("id", "name");
    for (Team team : teams) {
      table.add(team.getId(), team.getName());
    }
    table.print();
  }
}
