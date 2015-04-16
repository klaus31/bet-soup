package beso.recommendation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beso.base.BesoTable;
import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Team;

@Component
public class AllTeams implements Launchable {

  @Autowired
  private BesoTable table;

  @Override
  public void launch(final String... args) {
    final List<Team> teams = BesoDao.me().findTeams();
    table.addHeaderCols("id", "name");
    for (Team team : teams) {
      table.add(team.getId(), team.getName());
    }
    table.print();
  }
}
