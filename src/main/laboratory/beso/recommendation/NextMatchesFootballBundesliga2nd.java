package beso.recommendation;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Competition;
import beso.pojo.Match;

public class NextMatchesFootballBundesliga2nd implements Launchable {

  @Override
  public void launch(final String... args) {
    final Competition competition = BesoDao.me().findFootballBundesliga2nd();
  final List<Match> matches = BesoDao.me().findMatchesWithoutResult(competition);
  Match previousInList = null;
  for (Match match : matches) {
    if (previousInList != null && !previousInList.startsAtSameDayAs(match)) {
      System.out.println("");
    }
    System.out.println(BesoFormatter.format(match));
    previousInList = match;
  }
  }
}
