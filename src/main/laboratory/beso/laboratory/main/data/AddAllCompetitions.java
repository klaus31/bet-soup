package beso.laboratory.main.data;

import beso.dao.BesoDao;
import beso.model.Competition;

public class AddAllCompetitions {

  public static void main(final String... args) {
    final AddAllCompetitions step = new AddAllCompetitions();
    step.start();
  }

  private void start() {
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
