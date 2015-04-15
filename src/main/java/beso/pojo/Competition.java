package beso.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import beso.dao.BesoDao;

@Document(collection = "competitions")
public class Competition implements Saveable {

  public static final String FOOTBALL_BUNDESLIGA_1 = "1. Fussball Bundesliga";
  public static final String FOOTBALL_BUNDESLIGA_2 = "2. Fussball Bundesliga";
  @Id
  private String id;
  private final String name;

  public Competition(final String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isFootballBundesliga1() {
    return getName().equals(FOOTBALL_BUNDESLIGA_1);
  }

  public boolean isFootballBundesliga2() {
    return getName().equals(FOOTBALL_BUNDESLIGA_2);
  }

  @Override
  public void save() {
    BesoDao.me().save(this);
  }
}
