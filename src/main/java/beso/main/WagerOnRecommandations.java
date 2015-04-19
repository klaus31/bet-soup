package beso.main;

import java.io.PrintStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.dao.QueryBuilderMatch;
import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Wager;
import beso.recommendation.WagerFactory;
import beso.recommendation.WagerFactoryFavorite;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryFavorite;
import beso.tools.BesoAsciiArtTable;

@Primary
@Component
public class WagerOnRecommandations implements Launchable {

  @Autowired
  private PrintStream defaultPrintStream;
  @Autowired
  private BesoAsciiArtTable table;

  @Override
  public Object getDoc() {
    return "matches with quote not played yet and a recommandation";
  }

  @Override
  public void launch() {
    final Query queryMatchesToWagerOn = new QueryBuilderMatch().withQuota().withoutResult().sortByStartDesc().build();
    final List<Match> matchesMayWagerOn = BesoDao.me().find(queryMatchesToWagerOn, Match.class);
    final Query queryReferenceMatches = new QueryBuilderMatch().withQuota().withResult().build();
    final List<Match> referenceMatches = BesoDao.me().find(queryReferenceMatches, Match.class);
    final WagerOnFactory wagerOnFactory = new WagerOnFactoryFavorite();
    final WagerFactory wagerFactory = new WagerFactoryFavorite(wagerOnFactory, referenceMatches);
    // TODO parameterize the budget
    List<Wager> wagers = wagerFactory.getWagerRecommendation(matchesMayWagerOn, new Budget(10));
    if (wagers.size() == 0) {
      defaultPrintStream.println("no recommendations found. updating your database (run with parameters -t addQuotasCurrent) may help.");
    } else {
      table.addHeadline(getDoc());
      table.addHeaderColsForMatch(false);
      table.addHeaderColsForQuota();
      table.addHeaderColsForWager(false);
      for (Wager wager : wagers) {
        table.addContentCols(wager.getMatch(), false);
        table.addContentColsQuota(wager.getMatch());
        table.addContentCols(wager, false);
      }
      table.print();
    }
  }
}
