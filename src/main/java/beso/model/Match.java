package beso.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import beso.base.Beso;
import beso.dao.BesoDao;

@Document(collection = "matches")
public class Match implements Saveable {

  private Integer goalsTeam1 = null;
  private Integer goalsTeam2 = null;
  @Id
  private String id;
  @DBRef
  private final Team team1;
  @DBRef
  private final Team team2;

  public Match(final Team team1, final Team team2) {
    this.team1 = team1;
    this.team2 = team2;
  }

  public Integer getGoalsTeam1() {
    return goalsTeam1;
  }

  public Integer getGoalsTeam2() {
    return goalsTeam2;
  }

  public String getId() {
    return id;
  }

  public Team getTeam1() {
    return team1;
  }

  public Team getTeam2() {
    return team2;
  }

  public boolean isFinished() {
    return goalsTeam1 != null;
  }

  @Override
  public void save() {
    Beso.exitIf(team1.getId() == null, "store teams first");
    Beso.exitIf(team2.getId() == null, "store teams first");
    BesoDao.me().save(this);
  }

  public void setGoals(final int goalsTeam1, final int goalsTeam2) {
    this.goalsTeam1 = goalsTeam1;
    this.goalsTeam2 = goalsTeam2;
  }

  public void setId(final String id) { // TODO mach weg, wenns geht
    this.id = id;
  }
}