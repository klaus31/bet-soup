package beso.evaluation;

import java.util.List;

import beso.model.Odds;
import beso.recommendation.BetFactory;

interface BetFactoryEvaluation {

  Double rate(BetFactory factory, List<Odds> odds);
}
