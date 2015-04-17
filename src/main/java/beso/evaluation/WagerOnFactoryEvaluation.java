package beso.evaluation;

import java.util.List;

import beso.pojo.BetResult;
import beso.pojo.Quota;
import beso.recommendation.WagerOnFactory;
import static beso.pojo.BetResult.LOSE;
import static beso.pojo.BetResult.WIN;

public class WagerOnFactoryEvaluation {

  public WagerOnEvaluationResult getEvaluationResult(final WagerOnFactory factory, final List<Quota> quotas) {
    int win = 0;
    int lose = 0;
    for (Quota quota : quotas) {
      final BetResult betResult = BetResultFactory.get(factory.getWagerOn(quota), quota);
      if (betResult == WIN) {
        win++;
      } else if (betResult == LOSE) {
        lose++;
      }
    }
    return new WagerOnEvaluationResult(win, lose);
  }
}
