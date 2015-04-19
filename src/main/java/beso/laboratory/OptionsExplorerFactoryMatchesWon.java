package beso.laboratory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.dao.QueryBuilderMatch;
import beso.pojo.Match;
import beso.pojo.Team;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryMatchResults;

// TODO this does not make much sense: we are testing one factory, result table compares different factories
@Component
public class OptionsExplorerFactoryMatchesWon extends OptionsExplorerWagerOnFactory {

  private static final int LAST_N_MATCHES = 1;

  @Override
  public Object getDoc() {
    return "try options for wager on last " + LAST_N_MATCHES + " matches won (home or away)";
  }

  private List<Match> getMatchesFinished(final Team team) {
    final Query query = new QueryBuilderMatch().with(team).withResult().sortByStartDesc().build().limit(LAST_N_MATCHES + 1);
    return BesoDao.me().find(query, Match.class).subList(1, LAST_N_MATCHES + 1);
  }

  private List<Match> getMatchesToEvaluate(final Team team) {
    final Query query = new QueryBuilderMatch().with(team).withResult().sortByStartDesc().withQuota().build().limit(1);
    return BesoDao.me().find(query, Match.class);
  }

  @Override
  protected List<OptionsExplorerWagerOnSubject> getSubjects() {
    final List<OptionsExplorerWagerOnSubject> result = new ArrayList<>();
    final List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams) {
      final List<Match> matchesFinished = getMatchesFinished(team);
      final List<Match> matchesToEvaluate = getMatchesToEvaluate(team);
      if (matchesFinished.size() < LAST_N_MATCHES || matchesToEvaluate == null) {
        continue;
      }
      final WagerOnFactory wagerOnFactory = new WagerOnFactoryMatchResults(team, matchesFinished);
      result.add(new OptionsExplorerWagerOnSubject(wagerOnFactory, matchesToEvaluate));
    }
    return result;
  }
}