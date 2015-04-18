package beso.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import beso.dao.BesoDao;

@Document(collection = "teams")
public class Team implements Saveable {

  @Id
  private String id;
  private final String name;

  public Team(final String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean is(final Team team) {
    return team.getName().equals(name);
  }

  public Boolean lose(final Match match) {
    if (!match.isFinished() || !this.played(match)) {
      return null;
    }
    if (this.is(match.getTeam1())) {
      return match.getGoalsTeam1() < match.getGoalsTeam2();
    } else {
      return match.getGoalsTeam1() > match.getGoalsTeam2();
    }
  }

  public Boolean notLose(final Match match) {
    if (!match.isFinished() || !this.played(match)) {
      return null;
    }
    if (this.is(match.getTeam1())) {
      return match.getGoalsTeam1() >= match.getGoalsTeam2();
    } else {
      return match.getGoalsTeam1() <= match.getGoalsTeam2();
    }
  }

  public Boolean notWon(final Match match) {
    if (!match.isFinished() || !this.played(match)) {
      return null;
    }
    if (this.is(match.getTeam1())) {
      return match.getGoalsTeam1() <= match.getGoalsTeam2();
    } else {
      return match.getGoalsTeam1() >= match.getGoalsTeam2();
    }
  }

  public boolean played(final Match match) {
    return this.is(match.getTeam1()) || this.is(match.getTeam2());
  }

  @Override
  public void save() {
    BesoDao.me().save(this);
  }

  public Boolean won(final Match match) {
    if (!match.isFinished() || !this.played(match)) {
      return null;
    }
    if (this.is(match.getTeam1())) {
      return match.getGoalsTeam1() > match.getGoalsTeam2();
    } else {
      return match.getGoalsTeam1() < match.getGoalsTeam2();
    }
  }
}