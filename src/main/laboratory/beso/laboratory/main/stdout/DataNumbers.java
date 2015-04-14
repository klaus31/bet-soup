package beso.laboratory.main.stdout;

import java.util.List;

import beso.dao.BesoDao;
import beso.model.Competition;
import beso.model.Match;
import beso.model.Odds;
import beso.model.Team;
import static beso.base.BesoFormatter.appendToLength;

public class DataNumbers {

  public static void main(final String... args) {
    final DataNumbers main = new DataNumbers();
    main.start();
  }

  private void start() {
    // count things (without using mongos count)
    final List<Competition> competitions = BesoDao.me().findCompetitions();
    final int LENGTH = 6;
    System.out.println(appendToLength(competitions.size(), LENGTH) + " competitions");
    List<Odds> odds = BesoDao.me().findOdds();
    System.out.println(appendToLength(odds.size(), LENGTH) + " odds");
    List<Match> matches = BesoDao.me().findMatches();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches");
    matches = BesoDao.me().findMatchesFinished();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches finished");
    matches = BesoDao.me().findMatchesFinishedAndWithoutOdds();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches finished but without odds");
    matches = BesoDao.me().findMatchesWithoutResult();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches without result");
    List<Team> teams = BesoDao.me().findTeams();
    System.out.println(appendToLength(teams.size(), LENGTH) + " teams");
  }
}
