package beso.base;

public class Beso {

  public static void exitIf(final boolean thisIsTrue) {
    exitIf(thisIsTrue, "FAaAiiLl NOo.oOo.oOo.oOo.oOo.oO");
  }

  public static void exitIf(final boolean thisIsTrue, final String message) {
    if (thisIsTrue) {
      System.err.println(message);
      Thread.dumpStack();
      System.exit(666);
    }
  }
}
