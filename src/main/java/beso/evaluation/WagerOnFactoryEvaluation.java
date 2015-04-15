package beso.evaluation;

import java.util.List;

import beso.pojo.BetResult;
import beso.pojo.Quota;
import beso.recommendation.WagerOnFactory;
import static beso.pojo.BetResult.LOOSE;
import static beso.pojo.BetResult.WIN;

public class WagerOnFactoryEvaluation {

  public Double rate(final WagerOnFactory factory, final List<Quota> quotas) {
    int win = 0;
    int fail = 0;
    for (Quota quota : quotas) {
      final BetResult betResult = BetResultFactory.get(factory.getWagerOn(quota), quota);
      if (betResult == WIN) {
        win++;
      } else if (betResult == LOOSE) {
        fail++;
      }
    }
    final int countBets = win + fail;
    if (countBets == 0) {
      return null;
    }
    return 1D * win / countBets;
  }
}
