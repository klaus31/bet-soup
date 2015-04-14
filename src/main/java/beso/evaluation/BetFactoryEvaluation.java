package beso.evaluation;

import java.util.List;

import beso.model.Odds;
import beso.recommendation.BetFactory;

public interface BetFactoryEvaluation {

  Double rate(BetFactory factory, List<Odds> odds);
}
