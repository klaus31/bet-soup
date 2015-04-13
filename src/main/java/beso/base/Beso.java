package beso.base;

public class Beso {

  public static final void exit(final String message) {
    System.err.println(message);
    Thread.dumpStack();
    System.exit(666);
  }

  public static final void exitIf(final boolean thisIsTrue) {
    exitIf(thisIsTrue, "FAaAiiLl NOo.oOo.oOo.oOo.oOo.oO");
  }

  public static final void exitIf(final boolean thisIsTrue, final String message) {
    if (thisIsTrue) {
      exit(message);
    }
  }
}
