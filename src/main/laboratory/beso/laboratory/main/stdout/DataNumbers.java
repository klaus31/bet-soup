package beso.laboratory.main.stdout;

import java.util.List;

import beso.dao.BesoDao;
import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Quota;
import beso.pojo.Team;
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
    List<Quota> quotas = BesoDao.me().findQuotas();
    System.out.println(appendToLength(quotas.size(), LENGTH) + " quotas");
    List<Match> matches = BesoDao.me().findMatches();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches");
    matches = BesoDao.me().findMatchesFinished();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches finished");
    matches = BesoDao.me().findMatchesFinishedAndWithoutQuota();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches finished but without quota");
    matches = BesoDao.me().findMatchesWithoutResult();
    System.out.println(appendToLength(matches.size(), LENGTH) + " matches without result");
    List<Team> teams = BesoDao.me().findTeams();
    System.out.println(appendToLength(teams.size(), LENGTH) + " teams");
  }
}
