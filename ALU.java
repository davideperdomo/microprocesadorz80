package z80;

public class ALU {

  public static int add(int acc, int op) {
    return acc + op;
  }

  public static int sub(int acc, int op) {
    return acc - op;
  }

  public static int and(int acc, int op) {
    return acc & op;
  }

  public static int or(int acc, int op) {
    return acc | op;
  }

  public static int xor(int acc, int op) {
    return acc ^ op;
  }

  public static int complement1(int op) {
    return ~op;
  }

  // Es probable un error aqui
  public static int complement2(int op) {
    return ~op + 1;
  }

  public static int leftRotation8(int op) {
    int op_filtered = 0x00_00_00_ff & op;
    boolean lastBit = (1 << 8 & op_filtered) != 0;
    op_filtered <<= 1;
    op_filtered |= (lastBit) ? 1 : 0;
    return op_filtered;
  }

  public static int rigthRotation8(int op) {
    int op_filtered = ( 0x00_00_00_ff & op );
    boolean firstBit = (1 & op_filtered) != 0;
    op_filtered >>= 1;
    op_filtered |= (firstBit) ? 1 << 8 : 0;
    return op_filtered;
  }

  public static int leftRotation16(int op) {
    int op_filtered = ( 0x00_00_ff_ff & op );
    boolean lastBit = (1 << 16 & op_filtered) != 0;
    op_filtered <<= 1;
    op_filtered |= (lastBit) ? 1 : 0;
    return op_filtered;
  }

  public static int rigthRotation16(int op) {
    int op_filtered = ( 0x00_00_ff_ff & op );
    boolean firstBit = (1 & op_filtered) != 0;
    op_filtered >>= 1;
    op_filtered |= (firstBit) ? 1 << 16 : 0;
    return op_filtered;
  }

  public static boolean lessThan(int acc, int op) {
    return acc < op;
  }

  public static boolean equal(int acc, int op) {
    return acc == op;
  }

  public static boolean greaterThan(int acc, int op) {
    return acc > op;
  }

  public static int setBit(int op, int pos) {
    int aux = 1 << pos;
    return op | aux;
  }

  public static int resetBit(int op, int pos) {
    int aux = 1 << pos;
    aux ^= -1;
    return op & aux;
  }

  public static boolean checkBit(int op, int pos) {
    int aux = 1 << pos;
    aux &= op;
    return aux != 0;
  }
}
