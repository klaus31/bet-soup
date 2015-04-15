package beso.dao;

import java.util.Calendar;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import beso.TestBeanFactory;
import beso.pojo.Match;
import beso.pojo.Team;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CrudTest {

  @BeforeClass
  public static void setupBesoDao() {
    BesoDao.me().setApplicationContext(TestBeanFactory.getApplicationContext());
  }

  @Test
  public void crudAMatch() {
    Match match = TestBeanFactory.getMatch("1. FC Onion", "1. FC Garlic");
    match.getTeam1().save();
    match.getTeam2().save();
    match.getCompetition().save();
    match.save();
    assertNotNull(match.getId());
    // r
    Match savedMatch = BesoDao.me().findMatch(match.getId());
    assertEquals(match.getId(), savedMatch.getId());
    assertEquals("1. FC Onion", savedMatch.getTeam1().getName());
    assertEquals("1. FC Garlic", savedMatch.getTeam2().getName());
    assertNotNull(savedMatch.getStart());
  }

  @Test
  public void crudATeam() {
    // c
    Team team = new Team("1. FC Soup");
    team.save();
    assertNotNull(team.getId());
    // r
    Team savedTeam = BesoDao.me().findTeam("name", "1. FC Soup");
    assertEquals(team.getId(), savedTeam.getId());
    assertEquals("1. FC Soup", savedTeam.getName());
    // u
    BesoDao.me().update(savedTeam, "name", "RB Soup");
    Team updatedTeam = BesoDao.me().findTeam(team.getId());
    assertEquals(team.getId(), updatedTeam.getId());
    assertEquals("RB Soup", updatedTeam.getName());
    // d
    final int sizeBeforeRemove = BesoDao.me().findTeams().size();
    BesoDao.me().remove(team);
    List<Team> teams = BesoDao.me().findTeams();
    assertEquals(sizeBeforeRemove - 1, teams.size());
  }

  @Test
  public void teamExists() {
    Team team = TestBeanFactory.getTeam("Foo " + Calendar.getInstance().getTimeInMillis());
    assertFalse(BesoDao.me().exists(team));
    team.save();
    assertTrue(BesoDao.me().exists(team));
  }
}
