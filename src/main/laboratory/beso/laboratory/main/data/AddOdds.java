package beso.laboratory.main.data;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.model.Match;
import beso.model.Odds;
import beso.services.OddsService;
import beso.services.Tipico;

public class AddOdds {

  public static void main(final String[] args) {
    final AddOdds step = new AddOdds();
    step.start();
  }

  // insert all matches of all known competitions of the last 5 years
  private void start() {
    final List<Match> matches = BesoDao.me().findMatchesFinishedAndWithoutOdds();
    final OddsService oddsService = new Tipico(matches);
    final List<Odds> oddsFound = oddsService.getOddsFound();
    for (Odds odds : oddsFound) {
      System.out.println(BesoFormatter.format(odds));
      odds.save();
    }
    System.out.println(oddsFound.size() + " odds upserted");
  }
}
