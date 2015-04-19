package beso.laboratory;

import java.util.List;

import beso.pojo.Match;
import beso.recommendation.WagerOnFactory;

public class OptionsExplorerWagerOnSubject {

  private final List<Match> matchesToEvaluate;
  private final WagerOnFactory wagerOnFactory;

  public OptionsExplorerWagerOnSubject(final WagerOnFactory wagerOnFactory, final List<Match> matchesToEvaluate) {
    this.wagerOnFactory = wagerOnFactory;
    this.matchesToEvaluate = matchesToEvaluate;
  }

  public List<Match> getMatchesToEvaluate() {
    return matchesToEvaluate;
  }

  public WagerOnFactory getWagerOnFactory() {
    return wagerOnFactory;
  }
}
