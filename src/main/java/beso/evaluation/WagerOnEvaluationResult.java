package beso.evaluation;

public class WagerOnEvaluationResult {

  final int countLose;
  final int countWon;

  public WagerOnEvaluationResult(final int countWon, final int countLose) {
    this.countWon = countWon;
    this.countLose = countLose;
  }

  public int getCountWagersMade() {
    return countWon + countLose;
  }

  public int getCountWon() {
    return countWon;
  }

  public Double getSuccessRate() {
    final int countWagers = getCountWagersMade();
    if (countWagers == 0) {
      return null;
    }
    return 1D * countWon / countWagers;
  }
}
