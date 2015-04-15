package beso.recommendation;

import beso.pojo.Quota;
import beso.pojo.WagerOn;

public interface WagerOnFactory {

  WagerOn getWagerOn(Quota quota);
}
