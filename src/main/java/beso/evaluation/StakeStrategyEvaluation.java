package beso.evaluation;

import java.util.List;

import beso.model.Match;
import beso.stake.Profit;
import beso.stake.StakeStrategy;

public interface StakeStrategyEvaluation {

  public Profit getProfit(final StakeStrategy stakeStrategy, final List<Match> matches);
}
