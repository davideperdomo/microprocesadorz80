class ALU {

  public static short suma(short acumulador, short op) {
    return (short)(acumulador + op);
  }

  public static short resta(short acumulador, short op) {
    return (short)(acumulador - op);
  }

  public static byte AND(byte acumulador, byte op) {
    return (byte)(acumulador & op);
  }

  public static byte OR(byte acumulador, byte op) {
    return (byte)(acumulador | op);
  }

  public static byte XOR(byte acumulador, byte op) {
    return (byte)(acumulador ^ op);
  }

  public static byte complemento1(byte op) {
    return (byte)(~op);
  }

  // Es probable un error aqui
  public static short complemento2(byte op) {
    return (byte)(~op + 1);
  }

  public static byte corrimientoIzquierda(byte op) {
    return (byte)(op << 1);
  }

  public static byte corrimientoDerecha(byte op) {
    return (byte)(op >> 1);
  }

  public static boolean menorQue(byte acumulador, byte op) {
    return acumulador < op;
  }

  public static boolean igualQue(byte acumulador, byte op) {
    return acumulador == op;
  }

  public static boolean mayorQue(byte acumulador, byte op) {
    return acumulador > op;
  }

  public static byte ponerBit(byte op, int pos) {
    byte aux = (byte)(0x01 << pos);
    return (byte)(op | aux);
  }

  public static byte limpiarBit(byte op, int pos) {
    byte aux = (byte)(0x01 << pos);
    aux ^= -1;
    return (byte)(op & aux);
  }

  public static boolean probarBit(byte op, int pos) {
    byte aux = (byte)(0x01 << pos);
    aux &= op;
    return aux != 0;
  }
}
