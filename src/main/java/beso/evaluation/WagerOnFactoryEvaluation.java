package beso.evaluation;

import java.util.List;

import beso.pojo.BetResult;
import beso.pojo.Match;
import beso.recommendation.WagerOnFactory;
import static beso.pojo.BetResult.LOSE;
import static beso.pojo.BetResult.WIN;

public class WagerOnFactoryEvaluation {

  public WagerOnEvaluationResult getEvaluationResult(final WagerOnFactory factory, final List<Match> matches) {
    int win = 0;
    int lose = 0;
    for (Match match : matches) {
      final BetResult betResult = BetResultFactory.get(factory.getWagerOn(match), match);
      if (betResult == WIN) {
        win++;
      } else if (betResult == LOSE) {
        lose++;
      }
    }
    return new WagerOnEvaluationResult(win, lose);
  }
}
