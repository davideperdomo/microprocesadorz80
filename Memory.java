package z80;

public class Memory {

  private int[] M;
  private final int SIZE = 65535;

  public Memory() {
    this.M = new int[SIZE];
  }

  public int get(int addr) {
    return this.M[addr];
  }

  public void set(int addr, int value) {
    this.M[addr] = value;
  }
}
