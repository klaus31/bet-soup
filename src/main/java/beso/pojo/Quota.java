package beso.pojo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import beso.dao.BesoDao;

@Document(collection = "quotas")
public class Quota implements Saveable {

  public static List<Match> getMatches(final List<Quota> quotas) {
    List<Match> matches = new ArrayList<>(quotas.size());
    for (Quota quota : quotas) {
      matches.add(quota.getMatch());
    }
    return matches;
  }

  @Id
  private String id;
  @DBRef
  private final Match match;
  private final double rateDraw;
  private final double rateTeam1;
  private final double rateTeam2;

  public Quota(final Match match, final double rateTeam1, final double rateDraw, final double rateTeam2) {
    this.match = match;
    this.rateTeam1 = rateTeam1;
    this.rateTeam2 = rateTeam2;
    this.rateDraw = rateDraw;
  }

  public String getId() {
    return id;
  }

  public Match getMatch() {
    return match;
  }

  public Double getRate(final WagerOn bet) {
    if (bet == null) {
      return null;
    }
    switch (bet) {
    case TEAM_1_WIN:
      return rateTeam1;
    case TEAM_2_WIN:
      return rateTeam2;
    case DRAW:
      return rateDraw;
    default:
      return 0D;
    }
  }

  public double getRateDraw() {
    return rateDraw;
  }

  public double getRateTeam1() {
    return rateTeam1;
  }

  public double getRateTeam2() {
    return rateTeam2;
  }

  @Override
  public void save() {
    BesoDao.me().save(this);
  }
}