package beso.services;

import java.util.List;

import beso.model.Match;
import beso.model.Odds;

public interface OddsService {

  List<Match> getMatchesWithoutOddsFound();

  List<Odds> getOddsFound();
}
