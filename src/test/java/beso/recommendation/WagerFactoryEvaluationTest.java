package beso.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import beso.TestBeanFactory;
import beso.evaluation.WagerOnFactoryEvaluation;
import beso.pojo.Match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WagerFactoryEvaluationTest {

  @Test
  public void getRateOfWagerOnFactoryRateBetween() {
    // given
    WagerOnFactory factory = new WagerOnFactoryRateBetween(1D, 1.5D);
    WagerOnFactoryEvaluation evaluation = new WagerOnFactoryEvaluation();
    List<Match> matches = new ArrayList<>();
    // when no matches is given
    Double rate = evaluation.getEvaluationResult(factory, matches).getSuccessRate();
    // then
    assertNull(rate);
    // when one hit is added
    matches.add(TestBeanFactory.getMatch(1, 1, 2D, 1.3D, 2D));
    rate = evaluation.getEvaluationResult(factory, matches).getSuccessRate();
    // then 100 % is right
    assertEquals(1, rate, .01);
    // when add a fail
    matches.add(TestBeanFactory.getMatch(1, 2, 2D, 1.3D, 2D));
    rate = evaluation.getEvaluationResult(factory, matches).getSuccessRate();
    // then 50 % is right
    assertEquals(.5, rate, .01);
    // when add another fail
    matches.add(TestBeanFactory.getMatch(1, 2, 2D, 1.3D, 2D));
    rate = evaluation.getEvaluationResult(factory, matches).getSuccessRate();
    // then 33 % is right
    assertEquals(.33, rate, .01);
    // when add ambiguous bet
    matches.add(TestBeanFactory.getMatch(1, 2, 1.3D, 1.3D, 2D));
    rate = evaluation.getEvaluationResult(factory, matches).getSuccessRate();
    // then 33 % is still right
    assertEquals(.33, rate, .01);
    // when add a null bet
    matches.add(TestBeanFactory.getQuota(1.3D, 1.3D, 2D));
    rate = evaluation.getEvaluationResult(factory, matches).getSuccessRate();
    // then 33 % is still right
    assertEquals(.33, rate, .01);
  }
}
