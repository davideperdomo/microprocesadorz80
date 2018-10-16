// revisar las variables que tienen signo por defecto

class Procesador {

  /*
  0: acarreo
  1: signo
  2: cero
  3: p_v
  4: acarreo_auxiliar
  */
  private byte banderas;
  // private short pc; // contador de programa
  // private short sp; // apuntador de stack
  private short[] registros_16bit;
  public byte[] registros_8bit;
  private final int A = 0, B = 1, C = 2, D = 3, E = 4, H = 5, L = 6;
  private final int Ac = 7, Bc = 8, Cc = 9, Dc = 10, Ec = 11, Hc = 12, Lc = 13;
  private final int PC = 0, SP = 1, IX = 2, IY = 3;
  private final int BIT_8 = 8, BIT_16 = 16;
  // private short IX, IY;
  // registro de Interrupciones pendiente


  public Procesador() {
    this.banderas = (byte) 0;
    this.registros_8bit = new byte[14];
    this.registros_16bit =  new short[4];

    for (int i = 0; i < 14; i++) {
      registros_8bit[i] = (byte) 0;
    }

    for (int i = 0; i < 4; i++) {
      registros_16bit[i] = (short) 0;
    }
  }


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

  public void asignarAcarreo(short res) {
    this.asignarBit(0, res > 255);
  }

  public void asignarSigno(short res) {
    this.asignarBit(1, res < 0);
  }

  public void asignarCero(short res) {
    this.asignarBit(2, res == 0);
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

  public void suma(byte op) {
    short res = ALU.suma(this.registros_8bit[A], op);

    this.registros_8bit[A]= (byte) res;

    // Asignar banderas
    this.asignarCero(res);
    this.asignarAcarreo(res);
  }

  public void resta(byte op) {
    short res = ALU.resta(this.registros_8bit[A], op);

    this.registros_8bit[A] = (byte) res;

    // Asignar banderas
    this.asignarCero(res);
    this.asignarAcarreo(res);
    this.asignarSigno(res);         // Estar pendiente de esta operacion
  }

  public void incremento1(int registro, int size) {
    if (registro >= 0) {
      if (size == BIT_8) {
        if (registro < 14) {
          this.registros_8bit[registro]++;
        } else {
          System.out.println("Error");
        }
      } else {
        if (registro < 4) {
          this.registros_16bit[registro]++;
        } else {
          System.out.println("Error");
        }
      }
    } else {
      System.out.println("Error");
    }
  }

  public void decremento1(int registro, int size) {
    if (registro >= 0) {
      if (size == BIT_8) {
        if (registro < 14) {
          this.registros_8bit[registro]--;
        } else {
          System.out.println("Error");
        }
      } else {
        if (registro < 4) {
          this.registros_16bit[registro]--;
        } else {
          System.out.println("Error");
        }
      }
    } else {
      System.out.println("Error");
    }
  }


  public void complemento1() {
    this.registros_8bit[A] = ALU.complemento1(this.registros_8bit[A]);
    this.asignarCero(0x00FF & this.registros_8bit[A]);
  }

  public void complemento2() {
    short res = ALU.complemento2(this.registros_8bit[A]);

    this.asignarCero(res);
    this.asignarAcarreo(res);
  }

}
