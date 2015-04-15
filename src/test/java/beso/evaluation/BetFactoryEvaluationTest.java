package beso.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import beso.TestBeanFactory;
import beso.pojo.Quota;
import beso.recommendation.WagerOnFactory;
import beso.recommendation.WagerOnFactoryRateBetween;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BetFactoryEvaluationTest {

  @Test
  public void getBetFactoryEvaluationPrognosis() {
    // given
    WagerOnFactory factory = new WagerOnFactoryRateBetween(1D, 1.5D);
    WagerOnFactoryEvaluation evaluation = new WagerOnFactoryEvaluation();
    List<Quota> quotas = new ArrayList<>();
    // when no quota is given
    Double rate = evaluation.rate(factory, quotas);
    // then
    assertNull(rate);
    // when one hit is added
    quotas.add(TestBeanFactory.getQuotaWithFinishedMatch(1, 1, 2D, 1.3D, 2D));
    rate = evaluation.rate(factory, quotas);
    // then 100 % is right
    assertEquals(1, rate, .01);
    // when add a fail
    quotas.add(TestBeanFactory.getQuotaWithFinishedMatch(1, 2, 2D, 1.3D, 2D));
    rate = evaluation.rate(factory, quotas);
    // then 50 % is right
    assertEquals(.5, rate, .01);
    // when add another fail
    quotas.add(TestBeanFactory.getQuotaWithFinishedMatch(1, 2, 2D, 1.3D, 2D));
    rate = evaluation.rate(factory, quotas);
    // then 33 % is right
    assertEquals(.33, rate, .01);
    // when add ambiguous bet
    quotas.add(TestBeanFactory.getQuotaWithFinishedMatch(1, 2, 1.3D, 1.3D, 2D));
    rate = evaluation.rate(factory, quotas);
    // then 33 % is still right
    assertEquals(.33, rate, .01);
    // when add a null bet
    quotas.add(TestBeanFactory.getQuota(1.3D, 1.3D, 2D));
    rate = evaluation.rate(factory, quotas);
    // then 33 % is still right
    assertEquals(.33, rate, .01);
  }
}
