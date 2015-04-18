package beso.recommendation;

import beso.pojo.Quota;
import beso.pojo.WagerOn;

public interface WagerOnFactory {

  String getWagerOnDescription();

  WagerOn getWagerOn(Quota quota);
}
