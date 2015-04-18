package beso.laboratory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.pojo.Match;
import beso.pojo.Team;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryMatchResults;

@Primary
@Component
public class OptionsExplorerFactoryMatchesWon extends OptionsExplorerWagerOnFactory {

  private static final int LAST_N_MATCHES = 1;

  @Override
  public Object getDoc() {
    return "try options for wager on last " + LAST_N_MATCHES + " matches won (home or away)";
  }

  @Override
  protected List<WagerOnFactory> getFactories() {
    final List<WagerOnFactory> result = new ArrayList<>();
    final List<Team> teams = BesoDao.me().findTeams();
    for (Team team : teams.subList(0, 5)) { // TODO try out 2 teams by now
      final Criteria criteriaPlayedAtHome = Criteria.where("team1").is(team.getId());
      final Criteria criteriaPlayedAway = Criteria.where("team2").is(team.getId());
      final Query query = new Query(new Criteria().orOperator(criteriaPlayedAtHome, criteriaPlayedAway));
      query.addCriteria(Criteria.where("goalsTeam1").ne(null));
      List<Match> matches = BesoDao.me().find(query, Match.class);
      if (matches.size() < LAST_N_MATCHES) {
        continue;
      }
      result.add(new WagerOnFactoryMatchResults(team, matches.subList(0, LAST_N_MATCHES)));
    }
    return result;
  }
}