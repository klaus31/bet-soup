package beso.stake;

import java.util.List;

import beso.model.Match;

public interface StakeStrategy {

  List<Stake> getStakeRecommendation(final List<Match> matches, final double totalStake);
}
