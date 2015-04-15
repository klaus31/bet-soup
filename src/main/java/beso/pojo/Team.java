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

  @Override
  public void save() {
    BesoDao.me().save(this);
  }
}