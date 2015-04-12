package beso.recommendation;

import beso.model.Bet;
import beso.model.Odds;

public interface BetFactory {

  Bet getBet(Odds odds);
}
