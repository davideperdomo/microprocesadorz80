public class Processor {
  // 8 bits
  private final int A = 0, B = 1, C = 2, D = 3, E = 4, H = 5, L = 6;
  private final int Ac = 7, Bc = 8, Cc = 9, Dc = 10, Ec = 11, Hc = 12, Lc = 13;

  // 16 bits
  private final int PC = 0, SP = 1, IX = 2, IY = 3;

  // Registros
  private int[] reg_8bit;
  private int[] reg_16bit;

  // Banderas
  private boolean[] flags;
  private final int carry = 0;
  private final int sig = 1;
  private final int zero = 2;
  private final int pv = 3;
  private final int aux_carry = 4;
  // private final int z = 0;
  // private final int flag2 = 0;
  // private final int flag3 = 0;


  // Memoria
  private int[] mem;

  // Instruction register
  private IR ir;
  private boolean end;


  public Processor(int[] mem) {
    this.end = false;
    this.flags = new boolean[8];
    this.mem = mem;
    this.reg_8bit = new int[14];
    this.reg_16bit =  new int[4];
    this.ir = new IR();
    for (int i = 0; i < 14; i++) {
      reg_8bit[i] = 0;
    }
    for (int i = 0; i < 4; i++) {
      reg_16bit[i] = 0;
    }
  }

  public void runProgram() {
    while(!this.end) {
      this.fetch();
      this.execute();
    }
  }

  public void fetch() {
    int[] instruction = new int[5];
    instruction[0] = this.mem[this.reg_16bit[PC]];
    instruction[1] = this.mem[this.reg_16bit[PC]+1];
    instruction[2] = this.mem[this.reg_16bit[PC]+2];
    instruction[3] = this.mem[this.reg_16bit[PC]+3];
    instruction[4] = this.mem[this.reg_16bit[PC]+4];
    this.ir.decodeInstruction(instruction);
    this.reg_16bit[PC] += 5;
  }

  public void checkByteLimit() {

  }

  public void checkShortLimit() {

  }

  public void execute() {
    int res = 0;
    String output = null;
    switch (this.ir.opcode) {
      case -1:
        System.out.println("halt");
        this.end = true;
        System.out.println("Fin del programa");
      break;
      // add (with reg 8 bits) accum->op1
      case 0:
        System.out.println("suma");
        res = ALU.add(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // add (with num 8 bits) accum
      case 1:
        System.out.println("suma");
        res = ALU.add(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
      break;

      // add (with mem 8 bits) accum
      case 2:
        System.out.println("suma");
        res = ALU.add(this.reg_8bit[A], this.mem[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // sub (with reg 8 bits) accum
      case 3:
        System.out.println("resta");
        res = ALU.sub(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // sub (with num 8 bits) accum
      case 4:
        System.out.println("resta");
        res = ALU.sub(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
      break;

      // sub (with mem 8 bits) accum
      case 5:
        System.out.println("resta");
        res = ALU.sub(this.reg_8bit[A], this.mem[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 6  increment1 (with reg 8 bits)-> op2
      case 6:
        System.out.println("incremento");
        res = this.reg_8bit[this.ir.op2]++;
      break;
      // 7  increment1 (with reg 16 bits)
      case 7:
        System.out.println("incremento");
        res = this.reg_16bit[this.ir.op2]++;
      break;

      // 8  increment1 (with mem 8 bits)
      case 8:
        System.out.println("incremento");
        res = this.mem[this.ir.op2]++;
      break;

      // 9  decrement1 (with reg 8 bits)
      case 9:
        System.out.println("decremento");
        res = this.reg_8bit[this.ir.op2]--;
      break;

      // 10  decrement1 (with reg 16 bits)
      case 10:
        System.out.println("decremento");
        res = this.reg_16bit[this.ir.op2]--;
      break;

      // 11  decrement1 (with mem 8 bits)
      case 11:
        System.out.println("decremento");
        res = this.mem[this.ir.op2]--;
      break;

      // 12  complement1 (only acc)
      case 12:
        System.out.println("complemento 1");
        res = ALU.complement1(this.reg_8bit[A]);
        this.reg_8bit[A] = res;
      break;

      // 13  complement2 (only acc)
      case 13:
        System.out.println("complemento2");
        res = ALU.complement2(this.reg_8bit[A]);
        this.reg_8bit[A] = res;
      break;

      // 14  load (with reg 8 <- reg 8)
      case 14:
        System.out.println("load");
        this.reg_8bit[this.ir.op1] = this.reg_8bit[this.ir.op2];
      break;

      // 15  load (with reg 16 <- reg 16)
      case 15:
        System.out.println("load");
        this.reg_16bit[this.ir.op1] = this.reg_16bit[this.ir.op2];
      break;

      // 16  load (with reg 8 <- mem 8)
      case 16:
        System.out.println("load");
        this.reg_8bit[this.ir.op1] = this.mem[this.ir.op2];
      break;

      // 17  load (with mem 8 <- reg 8)
      case 17:
        System.out.println("load");
        this.mem[this.ir.op1] = this.reg_8bit[this.ir.op2];
      break;

      // 18  load (with reg 8 <- num 8)
      case 18:
        System.out.println("load");
        this.reg_8bit[this.ir.op1] = this.ir.op2;
      break;

      // 19  load (with mem 8 <- num 8)
      case 19:
        System.out.println("load");
        this.mem[this.ir.op1] = this.ir.op2;
      break;

      // 20  load (with mem 8 <- mem 8)
      case 20:
        System.out.println("load");
        this.mem[this.ir.op1] = this.mem[this.ir.op2];
      break;

      // 21  Input (only acc)
      case 21:

      break;

      // 22  Output (only acc)
      case 22:
        System.out.println("out");
        output = Integer.toBinaryString(this.reg_8bit[A]);
        System.out.print("Bin: ");
        if (output.length() < 8) {
          for (int i = 0; i < 8 - output.length(); i++) {
            System.out.print("0");
          }
        } else {
          output = output.substring(0,8);
        }
       System.out.print(output + ", Dec: " + this.reg_8bit[A] + "\n");
      break;

      // 23  AND (with reg 8 bits) acc
      case 23:
        System.out.println("and");
        res = ALU.and(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 24  AND (with mem 8 bits) acc
      case 24:
        System.out.println("and");
        res = ALU.and(this.reg_8bit[A], this.mem[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 25  AND (with num 8 bits) acc
      case 25:
        System.out.println("and");
        res = ALU.and(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
      break;

      // 26  OR (with reg 8 bits) acc
      case 26:
        System.out.println("or");
        res = ALU.or(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 27  OR (with mem bits) acc
      case 27:
        System.out.println("or");
        res = ALU.or(this.reg_8bit[A], this.mem[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 28  OR (with num 8 bits) acc
      case 28:
        System.out.println("or");
        res = ALU.or(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
      break;

      // 29  XOR (with reg 8 bits) acc
      case 29:
        System.out.println("xor");
        res = ALU.xor(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 30  XOR (with mem bits) acc
      case 30:
        System.out.println("xor");
        res = ALU.xor(this.reg_8bit[A], this.mem[this.ir.op2]);
        this.reg_8bit[A] = res;
      break;

      // 31  XOR (with num 8 bits) acc
      case 31:
        System.out.println("xor");
        res = ALU.xor(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
      break;

      // 32  comparation (with reg 8 bits) acc
      case 32:
        System.out.println("comparacion");
        if (ALU.equal(this.reg_8bit[A], this.reg_8bit[this.ir.op2])) {
          // TODO Banderas
          this.flags[this.carry]=false;
          this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.reg_8bit[this.ir.op2])) {
          // TODO Banderas
          this.flags[this.carry]=true;
          this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          this.flags[this.carry]=false;
          this.flags[this.zero]=true;
        }
      break;

      // 33  comparation (with num 8 bits) acc
      case 33:
        System.out.println("comparacion");
        if (ALU.equal(this.reg_8bit[A], this.ir.op2)) {
          // TODO Banderas
          this.flags[this.carry]=false;
          this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.ir.op2)) {
          // TODO Banderas
          this.flags[this.carry]=true;
          this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          this.flags[this.carry]=false;
          this.flags[this.zero]=true;
        }
      break;

      // 34  comparation (with mem 8 bits) acc
      case 34:
        System.out.println("comparacion");
        if (ALU.equal(this.reg_8bit[A], this.mem[this.ir.op2])) {
          // TODO Banderas
          this.flags[this.carry]=false;
          this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.mem[this.ir.op2])) {
          // TODO Banderas
          this.flags[this.carry]=true;
          this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          this.flags[this.carry]=false;
          this.flags[this.zero]=true;
        }
      break;

      // 35  rigthRotation (with reg 8 bits)
      case 35:
        System.out.println("rotacion derecha");
        this.reg_8bit[this.ir.op2] = ALU.rigthRotation8(this.reg_8bit[this.ir.op2]);
      break;

      // 36  rigthRotation (with reg 16 bits)
      case 36:
        System.out.println("rotacion derecha");
        this.reg_16bit[this.ir.op2] = ALU.rigthRotation16(this.reg_16bit[this.ir.op2]);
      break;

      // 37  rigthRotation (with mem 8 bits)
      case 37:
        System.out.println("rotacion derecha");
        this.mem[this.ir.op2] = ALU.rigthRotation8(this.mem[this.ir.op2]);
      break;

      // 38  leftRotation (with reg 8 bits)
      case 38:
        System.out.println("rotacion izquierda");
        this.reg_8bit[this.ir.op2] = ALU.leftRotation8(this.reg_8bit[this.ir.op2]);
      break;

      // 39  leftRotation (with reg 16 bits)
      case 39:
        System.out.println("rotacion izquierda");
        this.reg_16bit[this.ir.op2] = ALU.leftRotation16(this.reg_16bit[this.ir.op2]);
      break;

      // 40  leftRotation (with mem 8 bits)
      case 40:
        System.out.println("rotacion izquierda");
        this.mem[this.ir.op2] = ALU.leftRotation8(this.mem[this.ir.op2]);
      break;

      // 41  checkBit (with reg 8 bits) modify z-flag
      case 41:

      break;

      // 42  checkBit (with mem 8 bits) modify z-flag
      case 42:
      break;

      // 43  setBit (with reg 8 bits)
      case 43:
      break;

      // 44  setBit (with mem 8 bits)
      case 44:
      break;

      // 45  resetBit (with reg 8 bits)
      case 45:
      break;

      // 46  resetBit (with mem 8 bits)
      case 46:
      break;

      // 47  jump pos(is a memory address)
      case 47:
        System.out.println("jump pos");
        // Supongo que en el operando 2 esta la dir
        if (this.reg_8bit[A] > 0){
          // Avanzar PC
          this.reg_16bit[PC] = this.ir.op2;
        }
      break;

      // 48 jump zero
      case 48:
        System.out.println("jump zero");
        if (this.reg_8bit[A] == 0){
          // Avanzar PC
          this.reg_16bit[PC] = this.ir.op2;
        }
      break;

      // 49 jump
      case 49:
        System.out.println("jump");
      case 50:
        // Avanzar PC
        this.reg_16bit[PC] = this.ir.op2;
      break;
    }
  }


  //  opcode      op 1              op 2
  //  00000000 | 00000000 00000000| 00000000 00000000

  /*
  0  add (with reg 8 bits) accum
  1  add (with num 8 bits) accum
  2  add (with mem 8 bits) accum
  3  sub (with reg 8 bits) accum
  4  sub (with num 8 bits) accum
  5  sub (with mem 8 bits) accum
  6  increment1 (with reg 8 bits)
  7  increment1 (with reg 16 bits)
  8  increment1 (with mem 8 bits)
  9  decrement1 (with reg 8 bits)
  10  decrement1 (with reg 16 bits)
  11  decrement1 (with mem 8 bits)
  12  complement1 (only acc)
  13  complement2 (only acc)
  14  load (with reg 8 - reg 8)/
  15  load (with reg 16 - reg 16)
  16  load (with reg 8 - mem 8)
  17  load (with mem 8 - reg 8)/
  18  load (with reg 8 - num 8)/
  19  load (with mem 8 - num 8)
  20  load (with mem 8 - mem 8)
  21  Input (only acc)
  22  Output (only acc)
  23  AND (with reg 8 bits) acc
  24  AND (with mem 8 bits) acc
  25  AND (with num 8 bits) acc
  26  OR (with reg 8 bits) acc
  27  OR (with mem bits) acc
  28  OR (with num 8 bits) acc
  29  XOR (with reg 8 bits) acc
  30  XOR (with mem bits) acc
  31  XOR (with num 8 bits) acc
  32  comparation (with reg 8 bits) acc
  33  comparation (with num 8 bits) acc
  34  comparation (with mem 8 bits) acc
  35  rigthRotation (with reg 8 bits)
  36  rigthRotation (with reg 16 bits)
  37  rigthRotation (with mem 8 bits)
  38  leftRotation (with reg 8 bits)
  39  leftRotation (with reg 16 bits)
  40  leftRotation (with mem 8 bits)
  41  checkBit (with reg 8 bits) modify z-flag
  42  checkBit (with mem 8 bits) modify z-flag
  43  setBit (with reg 8 bits)
  44  setBit (with mem 8 bits)
  45  resetBit (with reg 8 bits)
  46  resetBit (with reg 16 bits)
  47  jump pos (is a memory address)
  48  jump zero (is a memory address)
  -1 end
  */

}
