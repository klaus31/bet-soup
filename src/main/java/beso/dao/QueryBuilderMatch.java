package beso.dao;

import java.util.Date;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import beso.pojo.Competition;
import beso.pojo.Team;

public class QueryBuilderMatch {

  private final Query query = new Query();

  public Query build() {
    return query;
  }

  public QueryBuilderMatch competition(final Competition competition) {
    query.addCriteria(Criteria.where("competition").is(competition.getId()));
    return this;
  }

  public QueryBuilderMatch played() {
    return withResult();
  }

  public QueryBuilderMatch sortByStartAsc() {
    query.with(new Sort(Sort.Direction.ASC, "start"));
    return this;
  }

  public QueryBuilderMatch sortByStartDesc() {
    query.with(new Sort(Sort.Direction.DESC, "start"));
    return this;
  }

  public QueryBuilderMatch start(final Date start) {
    query.addCriteria(Criteria.where("start").is(start));
    return this;
  }

  public QueryBuilderMatch with(final Team team) {
    final Criteria criteriaPlayedAtHome = Criteria.where("team1").is(team.getId());
    final Criteria criteriaPlayedAway = Criteria.where("team2").is(team.getId());
    query.addCriteria(new Criteria().orOperator(criteriaPlayedAtHome, criteriaPlayedAway));
    return this;
  }

  public QueryBuilderMatch withAwayTeam(final Team team) {
    query.addCriteria(Criteria.where("team2").is(team.getId()));
    return this;
  }

  public QueryBuilderMatch withHomeTeam(final Team team) {
    query.addCriteria(Criteria.where("team1").is(team.getId()));
    return this;
  }

  public QueryBuilderMatch withoutQuota() {
    query.addCriteria(Criteria.where("rateTeam1").is(null));
    query.addCriteria(Criteria.where("rateTeam2").is(null));
    query.addCriteria(Criteria.where("rateDraw").is(null));
    return this;
  }

  public QueryBuilderMatch withoutResult() {
    query.addCriteria(Criteria.where("goalsTeam1").is(null));
    return this;
  }

  public QueryBuilderMatch withQuota() {
    query.addCriteria(Criteria.where("rateTeam1").ne(null));
    query.addCriteria(Criteria.where("rateTeam2").ne(null));
    query.addCriteria(Criteria.where("rateDraw").ne(null));
    return this;
  }

  public QueryBuilderMatch withResult() {
    query.addCriteria(Criteria.where("goalsTeam1").ne(null));
    return this;
  }
}
