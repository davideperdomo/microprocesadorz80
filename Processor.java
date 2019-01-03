package z80;

import java.io.IOException;

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
  private Memory mem;

  // Instruction register
  private IR ir;
  private boolean end;

  //Pins
  private final Chip chip;

  //UI
  private UI ui;

  public Processor(Memory mem, UI ui) {
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
    this.chip = new Chip(ui);
    this.ui = ui;
  }

  public void setReg_8bit(int reg, int val){
      this.reg_8bit[reg]=val;
      this.ui.printLabelReg(reg,String.valueOf(val));
  }
  public void setReg_16bit(int reg, int val){
      this.reg_16bit[reg]=val;
      this.ui.printLabel16Reg(reg,String.valueOf(val));
  }
  public void setMem(int pos, int val){
      this.mem.set(pos, val);
      ui.printLabelMem(pos, String.valueOf(val));
  }
  public void setFlags(int flag, boolean val){
      this.flags[flag]=val;
      this.ui.printLabelFlag(flag,val);
  }
  public void runProgram() throws IOException, InterruptedException {
    int i=0;
    
    while(!this.end) {
      this.fetch();
      this.execute();
      Thread.sleep(10);
      i++;
    }
  }

  public void fetch() {
    int[] instruction = new int[5];
    instruction[0] = this.mem.get(this.reg_16bit[PC]);
    instruction[1] = this.mem.get(this.reg_16bit[PC]+1);
    instruction[2] = this.mem.get(this.reg_16bit[PC]+2);
    instruction[3] = this.mem.get(this.reg_16bit[PC]+3);
    instruction[4] = this.mem.get(this.reg_16bit[PC]+4);
    this.ir.decodeInstruction(instruction);
    this.setReg_16bit(PC, this.reg_16bit[PC]+5);
    //this.reg_16bit[PC] += 5;
  }

  public void checkByteLimit() {

  }

  public void checkShortLimit() {

  }

  private int toDec(String address) {
    return Integer.parseInt(address, 16);
  }

  public void execute() throws IOException{
    int res = 0, addr = 0, aux = 0;
    String output = null;
    this.ui.printLabel(String.valueOf(this.ir.opcode));
    this.ui.printLabelOp(String.valueOf(this.ir.op1));
    this.ui.printLabelOp2(String.valueOf(this.ir.op2));
//    System.out.print(this.ir.opcode + " " );

    switch (this.ir.opcode) {
      // add (with reg 8 bits) accum->op1
      case 0:
        System.out.println("suma " + this.ir.op2 + " - " + this.ir.op2);
        res = ALU.add(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.setReg_8bit(A,res);
        break;

      // add (with mem ind) accum
      case 1:
        System.out.println("suma" + this.ir.op1 + " - " +this.ir.op2);
        addr = this.reg_8bit[this.ir.op1] << 8;
        addr |= this.reg_8bit[this.ir.op2];
        res = ALU.add(this.reg_8bit[A], this.mem.get(addr));
        this.setReg_8bit(A,res);
        break;

      // add (with mem dir) accum
      case 2:
        System.out.println("suma");
        res = ALU.add(this.reg_8bit[A], this.mem.get(this.ir.op2));
        this.setReg_8bit(A,res);
        break;

      //add (with num 8 bits) accum
      case 3:
        System.out.println("suma");
        res = ALU.add(this.reg_8bit[A], this.ir.op2);
        this.setReg_8bit(A,res);
        //this.reg_8bit[A] = res;
        break;

      // sub (with reg 8 bits) accum
      case 4:
        System.out.println("resta");
        res = ALU.sub(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.setReg_8bit(A,res);
        //this.reg_8bit[A] = res;
        break;

      // sub (with num 8 bits) accum
      case 5:
        System.out.println("resta");
        addr = this.reg_8bit[this.ir.op1] << 8;
        addr |= this.reg_8bit[this.ir.op2];
        res = ALU.sub(this.reg_8bit[A], this.mem.get(addr));
        this.setReg_8bit(A,res);
        //this.reg_8bit[A] = res;
        break;

      // sub (with mem 8 bits) accum
      case 6:
        System.out.println("resta");
        res = ALU.sub(this.reg_8bit[A], this.mem.get(this.ir.op2));
        this.setReg_8bit(A,res);
        //this.reg_8bit[A] = res;
        break;

      // sub (with mem 8 bits) accum
      case 7:
        System.out.println("resta");
        res = ALU.sub(this.reg_8bit[A], this.ir.op2);
        this.setReg_8bit(A,res);
        //this.reg_8bit[A] = res;
        break;

      // increment1 (with reg 8 bits)-> op2
      case 8:
        System.out.println("incremento");
        res = this.reg_8bit[this.ir.op2]++;
        break;

      // increment1 (with reg 16 bits)
      case 9:
        System.out.println("incremento");
        res = this.reg_16bit[this.ir.op2]++;
        break;

      // increment1 (with mem ind 8 bits)
      case 10:
        System.out.println("incremento");
        addr = this.reg_8bit[this.ir.op1] << 8;
        addr |= this.reg_8bit[this.ir.op2];
        this.setMem(addr, this.mem.get(addr)+1);
        res = this.mem.get(addr);
        break;

      // increment1 (with mem dir 8 bits)
      case 11:
        System.out.println("incremento");
        this.setMem(this.ir.op2, this.mem.get(this.ir.op2)+1);
        res = this.mem.get(this.ir.op2);
        break;

      // decrement1 (with reg 8 bits)
      case 12:
        System.out.println("decremento");
        res = this.reg_8bit[this.ir.op2]--;
        break;

      // decrement1 (with reg 16 bits)
      case 13:
        System.out.println("decremento");
        res = this.reg_16bit[this.ir.op2]--;
        break;

      // decrement1 (with mem ind 8 bits)
      case 14:
        System.out.println("incremento");
        addr = this.reg_8bit[this.ir.op1] << 8;
        addr |= this.reg_8bit[this.ir.op2];
        this.setMem(addr, this.mem.get(addr)-1);
        res = this.mem.get(addr);
        break;

      // decrement1 (with mem dir 8 bits)
      case 15:
        System.out.println("incremento");
        this.setMem(this.ir.op2, this.mem.get(this.ir.op2)-1);
        res = this.mem.get(this.ir.op2);
        break;

      // complement1 (only acc)
      case 16:
        System.out.println("complemento 1");
        res = ALU.complement1(this.reg_8bit[A]);
        this.reg_8bit[A] = res;
        break;

      // complement2 (only acc)
      case 17:
        System.out.println("complemento2");
        res = ALU.complement2(this.reg_8bit[A]);
        this.setReg_8bit(A,res);
        //this.reg_8bit[A] = res;
        break;

      // load reg 8 - reg 8
      case 18:
        System.out.println("load");
        this.setReg_8bit(this.ir.op1, this.reg_8bit[this.ir.op2]);
        break;

      // load reg 8 - mem ind 8
      case 19:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        this.setReg_8bit(this.ir.op1, this.mem.get(addr));
        break;

      //load (with reg 8 - mem dir)
      case 20:
        System.out.println("load");
        this.setReg_8bit(this.ir.op1, this.mem.get(this.ir.op2));
        break;

      //load (with reg 8 - num 8)
      case 21:
        System.out.println("load");
        this.setReg_8bit(this.ir.op1, this.ir.op2);
        break;

      //load (with reg 16 - reg 16)
      case 22:
        System.out.println("load");
        this.setReg_16bit(this.ir.op1, this.reg_16bit[this.ir.op2]);
        break;

      //load (with reg 16 - num )
      case 23:
        System.out.println("load");
        this.setReg_8bit(this.ir.op1, this.ir.op2);
        break;

      //load (with reg 16 - 2 reg 8 )
      case 24:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        this.setReg_16bit(this.ir.op1, addr);
        break;

      // load (with 2 reg 8 - reg 16)
      case 25:
        System.out.println("load");
        aux = this.reg_16bit[this.ir.op2];
        addr = (0x00_00_ff_00 & this.ir.op1) >>> 8;
        this.setReg_8bit(addr, (0x00_00_ff_00 & aux >>> 8));
        addr = 0x00_00_00_ff & this.ir.op1;
        this.setReg_8bit(addr, 0x00_00_00_ff & aux);
        break;

      //load (with mem ind 8 - reg 8)
      case 26:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op1) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op1];
        this.setMem(addr, this.reg_8bit[this.ir.op2]);
        break;

      // load (with mem ind 8 - mem ind 8)
      case 27:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op1) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op1];
        aux = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        aux |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        this.setMem(addr, this.mem.get(aux));
        break;

      //load (with mem ind 8 - mem dir 8)
      case 28:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op1) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op1];
        this.setMem(addr, this.mem.get(this.ir.op2));
        break;

      // load (with mem ind 8 - num 8)
      case 29:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op1) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op1];
        this.setMem(addr, this.ir.op2);
        break;

      //load (with mem dir 8 - reg 8)
      case 30:
        System.out.println("load");
        this.setMem(this.ir.op1,  this.reg_8bit[this.ir.op2]);
        break;

      // load (with mem dir 8 - mem ind 8)
      case 31:
        System.out.println("load");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        this.setMem(this.ir.op1, this.mem.get(addr));
        break;

      //load (with mem dir 8 - mem dir 8)
      case 32:
        System.out.println("load");
        this.setMem(this.ir.op1, this.mem.get(this.ir.op2));
        break;

      // load (with mem dir 8 - num 8)
      case 33:
        System.out.println("load");
        this.setMem(this.ir.op1, this.ir.op2);
        break;

      //Input (only acc)
      case 34:
        System.out.println("input");
        this.setReg_8bit(A,this.chip.getDatabus());
        //this.reg_8bit[A] = this.chip.getDatabus();
        break;

      // Output (only acc)
      case 35:
        System.out.println("output");
        //output = Integer.toBinaryString(this.reg_8bit[A]);
        System.out.print("Bin: ");
        output =  String.format("%8s", Integer.toBinaryString(this.reg_8bit[A])).replace(' ', '0');
        this.chip.setDatabus(this.reg_8bit[A]);
        System.out.print(output + ", Dec: " + this.reg_8bit[A] + "\n");
        break;

      //  AND (with reg 8 bits) acc
      case 36:
        System.out.println("and");
        res = ALU.and(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
        break;

      // AND (with mem ind  8 bits) acc
      case 37:
        System.out.println("and");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        res = ALU.and(this.reg_8bit[A], this.mem.get(addr));
        this.reg_8bit[A] = res;
        break;

      // AND (with mem dir  8 bits) acc
      case 38:
        System.out.println("and");
        res = ALU.and(this.reg_8bit[A], this.mem.get(this.ir.op2));
        this.reg_8bit[A] = res;
        break;

      // AND (with num 8 bits) acc
      case 39:
        System.out.println("and");
        res = ALU.and(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
        break;

      //  OR (with reg 8 bits) acc
      case 40:
        System.out.println("or");
        res = ALU.or(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
        break;

      // OR (with mem 8 bits) acc
      case 41:
        System.out.println("or");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        res = ALU.or(this.reg_8bit[A], this.mem.get(addr));
        this.reg_8bit[A] = res;
        break;

      // OR (with mem 8 bits) acc
      case 42:
        System.out.println("or");
        res = ALU.or(this.reg_8bit[A], this.mem.get(this.ir.op2));
        this.reg_8bit[A] = res;
        break;

      // OR (with num 8 bits) acc
      case 43:
        System.out.println("or");
        res = ALU.or(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
        break;

      //  XOR (with reg 8 bits) acc
      case 44:
        System.out.println("xor");
        res = ALU.xor(this.reg_8bit[A], this.reg_8bit[this.ir.op2]);
        this.reg_8bit[A] = res;
        break;

      // XOR (with mem 8 bits) acc
      case 45:
        System.out.println("xor");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        res = ALU.xor(this.reg_8bit[A], this.mem.get(addr));
        this.reg_8bit[A] = res;
        break;

      // XOR (with mem 8 bits) acc
      case 46:
        System.out.println("xor");
        res = ALU.xor(this.reg_8bit[A], this.mem.get(this.ir.op2));
        this.reg_8bit[A] = res;
        break;

      // XOR (with num 8 bits) acc
      case 47:
        System.out.println("xor");
        res = ALU.xor(this.reg_8bit[A], this.ir.op2);
        this.reg_8bit[A] = res;
        break;

      // comparation (with reg 8 bits) acc
      case 48:
        System.out.println("comparacion");
        if (ALU.equal(this.reg_8bit[A], this.reg_8bit[this.ir.op2])) {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.reg_8bit[this.ir.op2])) {
          // TODO Banderas
          setFlags(this.carry,true);
          setFlags(this.zero,false);
          //this.flags[this.carry]=true;
          //this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }
        break;

      // comparation (with reg 8 bits) acc
      case 49:
        System.out.println("comparacion");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        if (ALU.equal(this.reg_8bit[A], this.mem.get(addr))) {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.mem.get(addr))) {
          // TODO Banderas
          setFlags(this.carry,true);
          setFlags(this.zero,false);
          //this.flags[this.carry]=true;
          //this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }
        break;

      // comparation (with reg 8 bits) acc
      case 50:
        System.out.println("comparacion");
        if (ALU.equal(this.reg_8bit[A], this.mem.get(this.ir.op2))) {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.mem.get(this.ir.op2))) {
          // TODO Banderas
          setFlags(this.carry,true);
          setFlags(this.zero,false);
          //this.flags[this.carry]=true;
          //this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }
        break;

      // comparation (with reg 8 bits) acc
      case 51:
        System.out.println("comparacion");
        if (ALU.equal(this.reg_8bit[A], this.ir.op2)) {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }else if(ALU.lessThan(this.reg_8bit[A], this.ir.op2)) {
          // TODO Banderas
          setFlags(this.carry,true);
          setFlags(this.zero,false);
          //this.flags[this.carry]=true;
          //this.flags[this.zero]=false;
        } else {
          // TODO Banderas
          setFlags(this.carry,false);
          setFlags(this.zero,true);
          //this.flags[this.carry]=false;
          //this.flags[this.zero]=true;
        }
        break;

      // rigthRotation (with reg 8 bits)
      case 52:
        System.out.println("rotacion derecha");
        this.reg_8bit[this.ir.op2] = ALU.rigthRotation8(this.reg_8bit[this.ir.op2]);
        break;

      // rigthRotation (with reg 16 bits)
      case 53:
        System.out.println("rotacion derecha");
        this.reg_16bit[this.ir.op2] = ALU.rigthRotation16(this.reg_16bit[this.ir.op2]);
        break;

      // rigthRotation (with mem 8 bits)
      case 54:
        System.out.println("rotacion derecha");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        this.setMem(addr, ALU.rigthRotation8(this.mem.get(addr)));
        break;

      // rigthRotation (with mem 8 bits)
      case 55:
        System.out.println("rotacion derecha");
        this.setMem(this.ir.op2, ALU.rigthRotation8(this.mem.get(this.ir.op2)));
        break;

      // leftRotation (with reg 8 bits)
      case 56:
        System.out.println("rotacion izquierda");
        this.reg_8bit[this.ir.op2] = ALU.leftRotation8(this.reg_8bit[this.ir.op2]);
        break;

      // leftRotation (with reg 16 bits)
      case 57:
        System.out.println("rotacion izquierda");
        this.reg_16bit[this.ir.op2] = ALU.leftRotation16(this.reg_16bit[this.ir.op2]);
        break;

      case 58:
        System.out.println("rotacion izquierda");
        addr = this.reg_8bit[(0x00_00_ff_00 & this.ir.op2) >>> 8] << 8;
        addr |= this.reg_8bit[0x00_00_00_ff & this.ir.op2];
        this.setMem(addr, ALU.leftRotation8(this.mem.get(addr)));
        break;

      case 59:
        System.out.println("rotacion izquierda");
        this.setMem(this.ir.op2, ALU.leftRotation8(this.mem.get(this.ir.op2)));
        break;

      // TODO: BIT SET RESET

      // jump C, mem ind (acc > 0)
      case 69:
        System.out.println("jump pos");
        if (this.reg_8bit[A] > 0){
          // Avanzar PC
          addr = this.reg_8bit[this.ir.op1] << 8;
          addr |= this.reg_8bit[this.ir.op2];
//          this.reg_16bit[PC] = addr;
          this.setReg_16bit(PC, addr);
        }
        break;

      // jump pos(is a memory address)
      case 70:
        System.out.println("jump pos");
        if (this.reg_8bit[A] > 0){
          // Avanzar PC
//          this.reg_16bit[PC] = this.ir.op2;
          this.setReg_16bit(PC, this.ir.op2);
        }
        break;

      // jump zero
      case 71:
        System.out.println("jump zero");
        if (this.reg_8bit[A] == 0){
          // Avanzar PC
          addr = this.reg_8bit[this.ir.op1] << 8;
          addr |= this.reg_8bit[this.ir.op2];
//          this.reg_16bit[PC] = addr;
          this.setReg_16bit(PC, addr);
        }
        break;

      // jump zero(is a memory address)
      case 72:
        System.out.println("jump zero");
        if (this.reg_8bit[A] == 0){
          // Avanzar PC
//          this.reg_16bit[PC] = this.ir.op2;
          this.setReg_16bit(PC, this.ir.op2);
        }
        break;

      // jump addr
      case 73:
        System.out.println("jump");
        addr = this.reg_8bit[this.ir.op1] << 8;
        addr |= this.reg_8bit[this.ir.op2];
        this.setReg_16bit(PC, addr);
//        this.reg_16bit[PC] = addr;
        break;

      // jump label
      case 74:
        // Avanzar PC
        this.setReg_16bit(PC, this.ir.op2);
//        this.reg_16bit[PC] = this.ir.op2;
        break;

      case 75:
        System.out.println("halt");
        break;

      case 76:
        System.out.println("end");
        this.end = true;
        System.out.println("Fin del programa");
        break;

      case 77:
        System.out.println("org");
//        this.reg_16bit[PC] = this.ir.op2;
        this.setReg_16bit(PC, this.ir.op2);
        break;

      case 78:
        System.out.println("equ");
        break;

      case 79:
        System.out.println("call");
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
