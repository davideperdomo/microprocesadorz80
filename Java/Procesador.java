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
  private short[] registros_16bit;
  public byte[] registros_8bit;
  private final int A = 0, B = 1, C = 2, D = 3, E = 4, H = 5, L = 6; // 8 bits
  private final int Ac = 7, Bc = 8, Cc = 9, Dc = 10, Ec = 11, Hc = 12, Lc = 13; // 8 bits
  private final int PC = 0, SP = 1, IX = 2, IY = 3;  // 16 bits
  private final int BIT_8 = 8, BIT_16 = 16;
  private byte[] IR;
  private byte[] memoria;
  private short operando1, operando2;
  private byte codigo, estructura;

  public Procesador(byte[] memoria) {
    this.operando1 = (short) 0;
    this.operando1 = (short) 0;
    this.codigo = (byte) 0;
    this.estructura = (byte) 0;
    this.banderas = (byte) 0;
    this.memoria = memoria;
    this.registros_8bit = new byte[14];
    this.registros_16bit =  new short[4];
    this.IR = new byte[5];
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
    this.asignarCero((short)(0x00FF & this.registros_8bit[A]));
  }

  public void complemento2() {
    short res = ALU.complemento2(this.registros_8bit[A]);

    this.asignarCero(res);
    this.asignarAcarreo(res);
  }

  /*
  0  sumar
  1  restar
  2  incremento1
  3  decremento1
  4  complemento1
  5  complemento2
  6  cargar
  7  leerEntrada
  8  mostrarSalida
  9  AND
  10  OR
  11  XOR
  12  comparacion
  13  rotacionDerecha
  14  rotacionIzquierda
  15  verificarBit
  16  asignarBit
  17  resetearBit
  18  saltar
  */

  public void runProgram() {

    this.extraer();
    this.decodificar();
    this.ejecutar();

  }

  //  000 00000 | 00000000 00000000| 00000000 00000000

  // Tener en cuenta saltos
  private void avanzarPrograma() {
    this.registros_16bit[PC] += 5;
  }

  private void extraer() {
    this.IR[0] = this.memoria[this.registros_16bit[PC]];
    this.IR[1] = this.memoria[this.registros_16bit[PC]+1];
    this.IR[2] = this.memoria[this.registros_16bit[PC]+2];
    this.IR[3] = this.memoria[this.registros_16bit[PC]+3];
    this.IR[4] = this.memoria[this.registros_16bit[PC]+4];
    this.avanzarPrograma();
  }

  private void decodificar(){
    this.estructura = this.IR[0] & 0xE0;
    this.codigo = this.IR[0] & 0x1F;
    this.operando1 |= this.IR[1];
    this.operando1 <<= 8;
    this.operando1 |= this.IR[2];
    this.operando2 |= this.IR[3];
    this.operando2 <<= 8;
    this.operando2 |= this.IR[4];
  }

  /*
    0 op1-> reg, op2->num -- acumulador->op1
    1 op1-> reg, op2->mem -- acumulador->op1
    2 op1-> mem, op2->num
    3 op1-> mem, op2->reg -- acumulador->op1
    4 op1-> reg, op2->reg
    5 op1-> mem, op2->mem
  */


  private void ejecutar(){
    short aux1 = (short) 0, res = (short) 0;
    int codigo = (int) this.codigo;
    int estr = (int) (this.estructura >>> 5);
    byte aux2 = (byte) 0;
    switch(codigo) {

      // suma (acumulador)
      case 0:
        aux1 = (estr == 0) ? this.operando2 :
               (estr == 1) ? (short) memoria.obtener(this.operando2) :
               this.registros_8bit[(int) this.operando2];

        res = ALU.suma(this.registros_8bit[A], aux1);
        this.registros_8bit[A] = (byte) res;
        // TODO Banderas
        break;

      // resta (acumulador)
      case 1:
        aux1 = (estr == 0) ? this.operando2 :
               (estr == 1) ? (short) memoria.obtener(this.operando2) :
               this.registros_8bit[(int) this.operando2];

        res = ALU.resta(this.registros_8bit[A], aux1);
        this.registros_8bit[A] = (byte) res;
        // TODO Banderas
        break;

      // incremento1
      case 2:
        if (estr == 1){
            memoria.asignar(this.operando2 ,(byte)(memoria.obtener(this.operando2) + 1));
        } else {
          // TODO diferencia el tamaño...
          this.registros_8bit[(int) this.operando2]++;
          // this.registros_8bit[(int) this.operando2] = (byte) this.registros_8bit[(int) this.operando2] + 1;
        }
        // TODO Banderas
        break;

      // decremento1
      case 3:
        if (estr == 1){
            memoria.asignar(this.operando2 ,(byte)(memoria.obtener(this.operando2) - 1));
        } else {
          // TODO diferencia el tamaño...
          this.registros_8bit[(int) this.operando2]--;
          this.registros_8bit[(int) this.operando2] = (byte) this.registros_8bit[(int) this.operando2] - 1;
        }
        break;

      //  complemento1 (acumulador)
      case 4:
        this.registros_8bit[A] = ALU.complemento1(this.registros_8bit[A]);
        // TODO Banderas
        break;

      // complemento2 (acumulador)
      case 5:
        res = ALU.complemento2(this.registros_8bit[A]);
        this.registros_8bit[A] = (byte) res;
        // TODO Banderas
        break;
        
      // cargar
      case 6:

        break;

      // leerEntrada (acumulador)
      case 7:


        break;

      // Mostrar (acumulador)
      case 8:

        break;

      // AND (acumulador)
      case 9:
        aux1 = (estr == 0) ? (byte) this.operando2 :
               (estr == 1) ? (short) memoria.obtener(this.operando2) :
               this.registros_8bit[(int) this.operando2];

        this.registros_8bit[A] = ALU.AND(this.registros_8bit[A], aux1);
        // TODO Banderas
        break;

      // OR (acumulador)
      case 10:
        aux1 = (estr == 0) ? (byte) this.operando2 :
               (estr == 1) ? (short) memoria.obtener(this.operando2) :
               this.registros_8bit[(int) this.operando2];

        this.registros_8bit[A] = ALU.OR(this.registros_8bit[A], aux1);
      // TODO Banderas

        break;

      // XOR (acumulador)
      case 11:
        aux1 = (estr == 0) ? (byte) this.operando2 :
               (estr == 1) ? (short) memoria.obtener(this.operando2) :
               this.registros_8bit[(int) this.operando2];
        this.registros_8bit[A] = ALU.XOR(this.registros_8bit[A], aux1);
        // TODO Banderas
        break;

      // comparacion (acumulador)
      case 12:

        if (estr == 0) {
          aux2 = (byte) this.operando2;
          if (ALU.igualQue(this.registros_8bit[A], aux2)) {
            // TODO Banderas
            break;
          }else if(ALU.menorQue(this.registros_8bit[A], aux2)) {
            // TODO Banderas
            break;
          } else {
            // TODO Banderas
            break;
          }
        } else if (estr == 1) {
          aux2 = memoria.obtener(this.operando2);
          if (ALU.igualQue(this.registros_8bit[A], aux2 ) {
            // TODO Banderas
            break;
          }else if(ALU.menorQue(this.registros_8bit[A], aux2) {
            // TODO Banderas
            break;
          } else {
            // TODO Banderas
            break;
          }
        } else {
          aux2 = this.registros_8bit[(int) this.operando2];

          if (ALU.igualQue(this.registros_8bit[A], aux2)) {
            // TODO Banderas
            break;
          }else if(ALU.menorQue(this.registros_8bit[A], aux2) {
            // TODO Banderas
            break;
          } else {
            // TODO Banderas
            break;
          }
        }
        break;

      // rotacionDerecha
      case 13:

        break;

      //  rotacionIzquierda
      case 14:

        break;

      // verificarBit
      case 15:

        break;

      // asignarBit
      case 16:

        break;

      //resetearBit
      case 17:

        break;

      // saltar
      case 18:

        break;


    }
  }

}
