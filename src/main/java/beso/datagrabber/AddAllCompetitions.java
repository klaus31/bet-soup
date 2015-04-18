package beso.datagrabber;

import org.springframework.stereotype.Component;

import beso.dao.BesoDao;
import beso.main.Launchable;
import beso.pojo.Competition;

@Component
public class AddAllCompetitions implements Launchable {

  @Override
  public Object getDoc() {
    return "add all competitions (hard coded) to the database";
  }

  @Override
  public void launch() {
    Competition competition = new Competition(Competition.FOOTBALL_BUNDESLIGA_1);
    if (!BesoDao.me().exists(competition)) {
      competition.save();
    }
    competition = new Competition(Competition.FOOTBALL_BUNDESLIGA_2);
    if (!BesoDao.me().exists(competition)) {
      competition.save();
    }
  }
}
