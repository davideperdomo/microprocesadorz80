class ALU {
  private String nombre;

  public ALU() {
    this.nombre = "ALU Z80";
  }

  public short suma(short acumulador, short op) {
    return (short)(acumulador + op);
  }

  public short resta(short acumulador, short op) {
    return (short)(acumulador - op);
  }

  public byte AND(byte acumulador, byte op) {
    return (byte)(acumulador & op);
  }

  public byte OR(byte acumulador, byte op) {
    return (byte)(acumulador | op);
  }

  public byte XOR(byte acumulador, byte op) {
    return (byte)(acumulador ^ op);
  }

  public byte complemento1(byte op) {
    return (byte)(~op);
  }

  // Es probable un error aqui
  public short complemento2(byte op) {
    return (byte)(~op + 1);
  }

  public byte corrimientoIzquierda(byte op) {
    return (byte)(op << 1);
  }

  public byte corrimientoDerecha(byte op) {
    return (byte)(op >> 1);
  }

  public boolean menorQue(byte acumulador, byte op) {
    return acumulador < op;
  }

  public boolean igualQue(byte acumulador, byte op) {
    return acumulador == op;
  }

  public boolean mayorQue(byte acumulador, byte op) {
    return acumulador > op;
  }

  public byte ponerBit(byte op, int pos) {
    byte aux = (byte)(0x01 << pos);
    return (byte)(op | aux);
  }

  public byte limpiarBit(byte op, int pos) {
    byte aux = (byte)(0x01 << pos);
    aux ^= -1;
    return (byte)(op & aux);
  }

  public boolean probarBit(byte op, int pos) {
    byte aux = (byte)(0x01 << pos);
    aux &= op;
    return aux != 0;
  }
}
