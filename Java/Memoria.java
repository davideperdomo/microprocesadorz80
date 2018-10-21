class Memoria {

  private byte[] registros;
  private int tamano;

  public Memoria(int bits){
    this.tamano = Math.pow(2, bits);
    this.registros = new byte[this.tamano];
  }

  public Memoria(byte[] registros) {
    this.tamano = registros.length;
    this.registros = new byte[this.tamano];
    for (int i = 0; i < this.tamano; i++) {
      this.registros[i] = resgistros[i];
    }
  }

  public byte obtener(short direccion){
    return this.registros[direccion];
  }

  public void asignar(short direccion, byte valor){
    this.registros[direccion] = valor;
  }

  public int obtenerTamano() {
    return this.tamano;
  }
}
