package beso.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import beso.model.Match;
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

  public List<Team> findTeams() {
    return mongoOperation.findAll(Team.class);
  }

  public Match findMatch(final String id) {
    return findMatch("id", id);
  }

  public Match findMatch(final String key, final Object value) {
    Query searchTeamQuery = new Query(Criteria.where(key).is(value));
    return mongoOperation.findOne(searchTeamQuery, Match.class);
  }

  public Team findTeam(final String id) {
    return findTeam("id", id);
  }

  public Team findTeam(final String key, final Object value) {
    Query searchTeamQuery = new Query(Criteria.where(key).is(value));
    return mongoOperation.findOne(searchTeamQuery, Team.class);
  }

  public void remove(final Team team) {
    Query searchTeamQuery = new Query(Criteria.where("id").is(team.getId()));
    mongoOperation.remove(searchTeamQuery, Team.class);
  }

  public void save(final Object objectToSave) {
    mongoOperation.save(objectToSave);
  }

  public void setApplicationContext(final ApplicationContext ctx) {
    mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
  }

  public void update(final Team team, final String key, final Object newValue) {
    Query searchTeamQuery = new Query(Criteria.where("id").is(team.getId()));
    mongoOperation.updateFirst(searchTeamQuery, Update.update(key, newValue), Team.class);
  }
}
