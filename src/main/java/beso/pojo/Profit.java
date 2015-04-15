package beso.pojo;

public class Profit {

  private double value;

  public Profit() {
    this(0D);
  }

  public Profit(final double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public void minus(final double value) {
    this.value -= value;
  }

  public void plus(final double value) {
    this.value += value;
  }

  public void add(Profit profit) {
    plus(profit.getValue());
  }
}
