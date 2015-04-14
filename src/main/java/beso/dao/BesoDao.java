package beso.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import beso.model.Competition;
import beso.model.Match;
import beso.model.Odds;
import beso.model.Team;

public class BesoDao implements AutoCloseable {

  private static BesoDao me = null;

  public static final BesoDao me() {
    if (me == null) {
      me = new BesoDao();
    }
    return me;
  }

  private final ApplicationContext defaultCtx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
  private MongoOperations mongoOperation;

  private BesoDao() {
    mongoOperation = (MongoOperations) defaultCtx.getBean("mongoTemplate");
  }

  @Override
  public void close() throws Exception {
    ((ConfigurableApplicationContext) defaultCtx).close();
  }

  public boolean exists(final Competition competition) {
    return competition != null && competition.getName() != null && findCompetition("name", competition.getName()) != null;
  }

  public boolean exists(final Team team) {
    return team != null && team.getName() != null && findTeam("name", team.getName()) != null;
  }

  private Competition findCompetition(final String key, final String value) {
    final Query query = new Query(Criteria.where(key).is(value));
    return mongoOperation.findOne(query, Competition.class);
  }

  public List<Competition> findCompetitions() {
    return mongoOperation.findAll(Competition.class);
  }

  public Competition findFootballBundesliga1st() {
    final Query query = new Query(Criteria.where("name").is(Competition.FOOTBALL_BUNDESLIGA_1));
    return mongoOperation.findOne(query, Competition.class);
  }

  public Competition findFootballBundesliga2nd() {
    final Query query = new Query(Criteria.where("name").is(Competition.FOOTBALL_BUNDESLIGA_2));
    return mongoOperation.findOne(query, Competition.class);
  }

  public Match findMatch(final Competition competition, final Team team1, final Team team2, final Date start) {
    final Query query = new Query(Criteria.where("competition").is(competition.getId()));
    query.addCriteria(Criteria.where("team1").is(team1.getId()));
    query.addCriteria(Criteria.where("team2").is(team2.getId()));
    query.addCriteria(Criteria.where("start").is(start));
    return mongoOperation.findOne(query, Match.class);
  }

  public Match findMatch(final String id) {
    return findMatch("id", id);
  }

  public Match findMatch(final String key, final Object value) {
    final Query query = new Query(Criteria.where(key).is(value));
    return mongoOperation.findOne(query, Match.class);
  }

  public List<Match> findMatches() {
    return mongoOperation.findAll(Match.class);
  }

  public List<Match> findMatchesFinished() {
    final Query query = new Query(Criteria.where("goalsTeam1").ne(null));
    return mongoOperation.find(query, Match.class);
  }

  public List<Match> findMatchesFinishedAndWithoutOdds() {
    final List<Match> matchesFinished = findMatchesFinished();
    final List<Match> matches = new ArrayList<>();
    for (Match match : matchesFinished) {
      if (match.getOdds() == null) {
        matches.add(match);
      }
    }
    return matches;
  }

  public List<Match> findMatchesWithoutResult() {
    final Query query = new Query(Criteria.where("goalsTeam1").is(null));
    return mongoOperation.find(query, Match.class);
  }

  public List<Match> findMatchesWithoutResult(final Competition competition) {
    final Query query = new Query(Criteria.where("competition").is(competition));
    query.addCriteria(Criteria.where("goalsTeam1").is(null));
    return mongoOperation.find(query, Match.class);
  }

  public List<Odds> findOdds() {
    return mongoOperation.findAll(Odds.class);
  }

  public Odds findOdds(final Match match) {
    final Query query = new Query(Criteria.where("match").is(match));
    return mongoOperation.findOne(query, Odds.class);
  }

  public Team findTeam(final String id) {
    return findTeam("id", id);
  }

  public Team findTeam(final String key, final Object value) {
    final Query query = new Query(Criteria.where(key).is(value));
    return mongoOperation.findOne(query, Team.class);
  }

  public Team findTeam(final Team team) {
    return findTeam("name", team.getName());
  }

  public List<Team> findTeams() {
    return mongoOperation.findAll(Team.class);
  }

  public void remove(final Team team) {
    final Query query = new Query(Criteria.where("id").is(team.getId()));
    mongoOperation.remove(query, Team.class);
  }

  public void save(final Object objectToSave) {
    mongoOperation.save(objectToSave);
  }

  public void setApplicationContext(final ApplicationContext ctx) {
    mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
  }

  public void update(final Team team, final String key, final Object newValue) {
    final Query query = new Query(Criteria.where("id").is(team.getId()));
    mongoOperation.updateFirst(query, Update.update(key, newValue), Team.class);
  }
}
