package beso.evaluation;

import java.util.List;

import beso.model.Odds;
import beso.recommendation.BetFactory;
import static beso.evaluation.Outcome.FAIL;
import static beso.evaluation.Outcome.WIN;

public class BetFactoryEvaluationPrognosis implements BetFactoryEvaluation {

  /**
   * return the percentage of the prognosis of the factory that were right.
   * ignore matches not made and ambigous bet prognosis.
   */
  @Override
  public Double rate(final BetFactory factory, final List<Odds> matchesOdds) {
    int win = 0;
    int fail = 0;
    for (Odds matchOdds : matchesOdds) {
      Outcome outcome = OutcomeFactory.get(factory, matchOdds);
      if (outcome == WIN) {
        win++;
      } else if (outcome == FAIL) {
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
