package beso.laboratory.main.stdout;

import java.util.List;

import beso.base.BesoFormatter;
import beso.dao.BesoDao;
import beso.evaluation.WagerOnFactoryEvaluation;
import beso.pojo.Quota;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryRateBetween;

public class EvaluationOfWagerOnFactoryRateBetween {

  public static void main(final String... args) {
    final EvaluationOfWagerOnFactoryRateBetween main = new EvaluationOfWagerOnFactoryRateBetween();
    main.start();
  }

  private void start() {
    final List<Quota> quotas = BesoDao.me().findQuotas();
    final WagerOnFactoryEvaluation bfe = new WagerOnFactoryEvaluation();
    double min = 1;
    double max = 1.1;
    while (max < 3.0) {
      final WagerOnFactory factory = new WagerOnFactoryRateBetween(min, max);
      final String percent = BesoFormatter.formatPercent(bfe.rate(factory, quotas));
      final String output = String.format("quota between %s and %s –→ %s won", BesoFormatter.format(min, "0.0"), BesoFormatter.format(max, "0.0"), percent);
      System.out.println(output);
      max += .1;
    }
  }
}