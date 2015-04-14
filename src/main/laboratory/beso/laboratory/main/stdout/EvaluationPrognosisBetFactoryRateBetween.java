package beso.laboratory.main.stdout;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.evaluation.BetFactoryEvaluation;
import beso.evaluation.BetFactoryEvaluationPrognosis;
import beso.model.Odds;
import beso.recommendation.BetFactory;
import beso.recommendation.BetFactoryRateBetween;

public class EvaluationPrognosisBetFactoryRateBetween {

  public static void main(final String... args) {
    final EvaluationPrognosisBetFactoryRateBetween main = new EvaluationPrognosisBetFactoryRateBetween();
    main.start();
  }

  private void start() {
    final List<Odds> odds = BesoDao.me().findOdds();
    final BetFactoryEvaluation bfe = new BetFactoryEvaluationPrognosis();
    double min = 1;
    double max = 1.5;
    while (max < 2.5) {
      final BetFactory factory = new BetFactoryRateBetween(min, max);
      final String percent = BesoFormatter.formatPercent(bfe.rate(factory, odds));
      final String output = String.format("%s %s –→ %s", BesoFormatter.format(min, "0.0"), BesoFormatter.format(min, "0.0"), percent);
      System.out.println(output);
      max += .1;
    }
  }
}