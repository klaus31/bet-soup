package beso.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import beso.dao.BesoDao;

@Document(collection = "odds")
public class Odds implements Saveable {

  @Id
  private String id;
  @DBRef
  private final Match match;
  private final double rateDraw;
  private final double rateTeam1;
  private final double rateTeam2;

  public Odds(final Match match, final double rateTeam1, final double rateDraw, final double rateTeam2) {
    this.match = match;
    this.rateTeam1 = rateTeam1;
    this.rateTeam2 = rateTeam2;
    this.rateDraw = rateDraw;
  }

  public Match getMatch() {
    return match;
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
    match.save();
  }
}