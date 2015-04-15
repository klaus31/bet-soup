package beso.recommendation;

import java.util.List;

import beso.pojo.Budget;
import beso.pojo.Match;
import beso.pojo.Wager;

public interface WagerFactory {

  List<Wager> getWagerRecommendation(final List<Match> matches, final Budget totalBudget);
}
