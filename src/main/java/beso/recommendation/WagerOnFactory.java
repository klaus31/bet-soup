package beso.recommendation;

import beso.pojo.Match;
import beso.pojo.WagerOn;

public interface WagerOnFactory {

  WagerOn getWagerOn(Match match);

  String getWagerOnDescription();
}
