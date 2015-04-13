package beso.laboratory.main.stdout;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.model.Competition;
import beso.model.Match;

public class NextMatchesFootballBundesliga1st {

  public static void main(final String... args) {
    final NextMatchesFootballBundesliga1st main = new NextMatchesFootballBundesliga1st();
    main.start();
  }

  private void start() {
    final Competition competition = BesoDao.me().findFootballBundesliga1st();
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
