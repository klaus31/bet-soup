package beso.dao;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import beso.pojo.Competition;
import beso.pojo.Match;
import beso.pojo.Team;

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

  public long countAll(final Class<?> entityClass) {
    return mongoOperation.count(new Query(), entityClass);
  }

  public long countMatchesFinished() {
    return mongoOperation.count(new QueryBuilderMatch().played().build(), Match.class);
  }

  public long countMatchesFinishedWithoutQuota() {
    final Query query = new QueryBuilderMatch().played().withoutQuota().build();
    return mongoOperation.count(query, Match.class);
  }

  public long countMatchesWithoutResult() {
    final Query query = new QueryBuilderMatch().withoutResult().build();
    return mongoOperation.count(query, Match.class);
  }

  public boolean exists(final Competition competition) {
    return competition != null && competition.getName() != null && findCompetition("name", competition.getName()) != null;
  }

  public boolean exists(final Team team) {
    return team != null && team.getName() != null && findTeam("name", team.getName()) != null;
  }

  public <T> List<T> find(final Query query, final Class<T> entityClass) {
    return mongoOperation.find(query, entityClass);
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
    final Query query = new QueryBuilderMatch().withHomeTeam(team1).withAwayTeam(team2).start(start).competition(competition).build();
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
    return mongoOperation.find(new QueryBuilderMatch().played().sortByStartDesc().build(), Match.class);
  }

  public List<Match> findMatchesFinishedWithoutQuota() {
    Query query = new QueryBuilderMatch().played().sortByStartDesc().withoutQuota().build();
    return mongoOperation.find(query, Match.class);
  }

  public List<Match> findMatchesFinishedWithQuotas(final int limit) {
    Query query = new QueryBuilderMatch().withQuota().played().build();
    query.limit(limit);
    return mongoOperation.find(query, Match.class);
  }

  public List<Match> findMatchesWithoutResult() {
    Query query = new QueryBuilderMatch().withoutResult().build();
    return mongoOperation.find(query, Match.class);
  }

  public List<Match> findMatchesWithoutResult(final Competition competition) {
    Query query = new QueryBuilderMatch().withoutResult().competition(competition).build();
    return mongoOperation.find(query, Match.class);
  }

  public List<Match> findMatchesWithoutResult(final Competition competition, final int limit) {
    Query query = new QueryBuilderMatch().withoutResult().competition(competition).sortByStartAsc().build();
    return mongoOperation.find(query.limit(limit), Match.class);
  }

  public <T> T findOne(final Query query, final Class<T> entityClass) {
    return mongoOperation.findOne(query, entityClass);
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
    final Query query = new Query(Criteria.where("_id").is(team.getId()));
    mongoOperation.remove(query, Team.class);
  }

  public void save(final Object objectToSave) {
    mongoOperation.save(objectToSave);
  }

  public void setApplicationContext(final ApplicationContext ctx) {
    mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
  }

  public void update(final Team team, final String key, final Object newValue) {
    final Query query = new Query(Criteria.where("_id").is(team.getId()));
    mongoOperation.updateFirst(query, Update.update(key, newValue), Team.class);
  }
}
