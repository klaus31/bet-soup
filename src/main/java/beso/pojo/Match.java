package beso.pojo;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import beso.base.Beso;
import beso.dao.BesoDao;

@Document(collection = "matches")
public class Match implements Saveable {

  @DBRef
  private final Competition competition;
  private Integer goalsTeam1 = null;
  private Integer goalsTeam2 = null;
  @Id
  private String id;
  private final Date start;
  @DBRef
  private final Team team1;
  @DBRef
  private final Team team2;

  public Match(final Competition competition, final Team team1, final Team team2, final Date start) {
    this.team1 = team1;
    this.team2 = team2;
    this.start = start;
    this.competition = competition;
  }

  public Competition getCompetition() {
    return competition;
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

  public Quota getQuota() {
    return BesoDao.me().findQuota(this);
  }

  public Date getStart() {
    return start;
  }

  public Calendar getStartCalendar() {
    Calendar result = Calendar.getInstance();
    result.setTime(this.start);
    return result;
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
    Beso.exitWithErrorIf(team1.getId() == null, "store teams first");
    Beso.exitWithErrorIf(team2.getId() == null, "store teams first");
    Beso.exitWithErrorIf(competition.getId() == null, "store competition first");
    BesoDao.me().save(this);
  }

  public void setGoals(final int goalsTeam1, final int goalsTeam2) {
    this.goalsTeam1 = goalsTeam1;
    this.goalsTeam2 = goalsTeam2;
  }

  public void setId(final String id) { // TODO mach weg, wenns geht
    this.id = id;
  }

  public boolean startsAtSameDayAs(final Match match) {
    Beso.exitWithErrorIf(match == null);
    Calendar helperCalendar = Calendar.getInstance();
    helperCalendar.setTime(this.getStart());
    final String checkString = helperCalendar.get(Calendar.DAY_OF_YEAR) + helperCalendar.get(Calendar.YEAR) + "";
    helperCalendar.setTime(match.getStart());
    return checkString.equals(helperCalendar.get(Calendar.DAY_OF_YEAR) + helperCalendar.get(Calendar.YEAR) + "");
  }
}