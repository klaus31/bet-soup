package beso.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import beso.TestBeanFactory;
import beso.evaluation.BetFactoryEvaluation;
import beso.evaluation.BetFactoryEvaluationPrognosis;
import beso.model.Odds;
import beso.recommendation.BetFactory;
import beso.recommendation.BetFactoryRateBetween;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BetFactoryEvaluationTest {

  @Test
  public void getBetFactoryEvaluationPrognosis() {
    // given
    BetFactory factory = new BetFactoryRateBetween(1D, 1.5D);
    BetFactoryEvaluation evaluation = new BetFactoryEvaluationPrognosis();
    List<Odds> odds = new ArrayList<>();
    // when no odd is given
    Double rate = evaluation.rate(factory, odds);
    // then
    assertNull(rate);
    // when one hit is added
    odds.add(TestBeanFactory.getOddsWithFinishedMatch(1, 1, 2D, 1.3D, 2D));
    rate = evaluation.rate(factory, odds);
    // then 100 % is right
    assertEquals(1, rate, .01);
    // when add a fail
    odds.add(TestBeanFactory.getOddsWithFinishedMatch(1, 2, 2D, 1.3D, 2D));
    rate = evaluation.rate(factory, odds);
    // then 50 % is right
    assertEquals(.5, rate, .01);
    // when add another fail
    odds.add(TestBeanFactory.getOddsWithFinishedMatch(1, 2, 2D, 1.3D, 2D));
    rate = evaluation.rate(factory, odds);
    // then 33 % is right
    assertEquals(.33, rate, .01);
    // when add ambiguous bet
    odds.add(TestBeanFactory.getOddsWithFinishedMatch(1, 2, 1.3D, 1.3D, 2D));
    rate = evaluation.rate(factory, odds);
    // then 33 % is still right
    assertEquals(.33, rate, .01);
    // when add a null bet
    odds.add(TestBeanFactory.getOdds(1.3D, 1.3D, 2D));
    rate = evaluation.rate(factory, odds);
    // then 33 % is still right
    assertEquals(.33, rate, .01);
  }
}
