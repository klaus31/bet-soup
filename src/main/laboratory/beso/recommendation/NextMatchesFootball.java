package beso.recommendation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import beso.base.BesoTable;
import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Competition;
import beso.pojo.Match;

@Primary
@Component
public class NextMatchesFootball implements Launchable {

  @Autowired
  private BesoTable table;

  @Override
  public void launch(final String... args) {
    print("next matches 1. Bundesliga", BesoDao.me().findFootballBundesliga1st());
    print("next matches 2. Bundesliga", BesoDao.me().findFootballBundesliga2nd());
  }

  private void print(final String headline, final Competition competition) {
    table.clear();
    final List<Match> matches = BesoDao.me().findMatchesWithoutResult(competition, 9);
    table.addHeadline(headline.toUpperCase());
    table.addHeaderColsForMatch(false);
    for (Match match : matches) {
      table.addContentCols(match, false);
    }
    table.print();
  }
}
