package beso.laboratory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.pojo.Match;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryRateBetween;

@Component
public class OptionsExplorerFactoryRateBetween extends OptionsExplorerWagerOnFactory {

  private List<Match> matches;

  @Override
  public Object getDoc() {
    return "try options for wager on quotas between min and max";
  }

  @Override
  protected List<WagerOnFactory> getFactories() {
    List<WagerOnFactory> result = new ArrayList<>();
    final double CHECK_MIN_MAX = 1.5;
    final double CHECK_MAX_MAX = 2;
    double min = 1;
    double max = 1.1;
    while (min < CHECK_MIN_MAX) {
      while (max < CHECK_MAX_MAX) {
        final WagerOnFactory wagerOnFactory = new WagerOnFactoryRateBetween(min, max);
        result.add(wagerOnFactory);
        max += .1;
      }
      min += .1;
      max = min + .1;
    }
    return result;
  }

  @Override
  protected List<Match> getMatchesToEvaluate(final WagerOnFactory wagerOnFactory) {
    if (matches == null) {
      matches = BesoDao.me().findMatches();
    }
    return matches;
  }
}