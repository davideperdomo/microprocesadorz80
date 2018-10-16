class Procesador {

  /*
  0: acarreo
  1: signo
  2: cero
  3: p_v
  4: acarreo_auxiliar
  */
  private byte banderas;
  private short pc; // contador de programa
  private short sp; // apuntador de stack
  private ALU alu;
  private byte A, B, C, D, E, H, L;
  private byte Ac, Bc, Cc, Dc, Ec, Hc, Lc;

  private short IX, IY;
  // registro de Interrupciones pendiente

  // Estan en la ALU, volver static
  private void asignarBit(int bandera, boolean estado) {
    byte aux = (byte)(1 << bandera);
    if (estado) {
      this.banderas |= aux;
    } else {
      aux ^= -1;
      this.banderas &= aux;
    }
  }

  private boolean obtenerBit(int bandera) {
    byte aux = (byte)(0x01 << bandera);
    aux &= this.banderas;
    return aux != 0;
  }

  public void asignarAcarreo(boolean estado) {
    this.asignarBit(0, estado);
  }

  public void asignarSigno(boolean estado) {
    this.asignarBit(1, estado);
  }

  public void asignarCero(boolean estado) {
    this.asignarBit(2, estado);
  }

  public void asignarPV(boolean estado) {
    this.asignarBit(3, estado);
  }

  public void asignarAcarreoAuxiliar(boolean estado) {
    this.asignarBit(4, estado);
  }

  public void obtenerAcarreo() {
    this.obtenerBit(0);
  }

  public void obtenerSigno() {
    this.obtenerBit(1);
  }

  public void obtenerCero() {
    this.obtenerBit(2);
  }

  public void obtenerPV() {
    this.obtenerBit(3);
  }

  public void obtenerAcarreoAuxiliar() {
    this.obtenerBit(4);
  }

}
