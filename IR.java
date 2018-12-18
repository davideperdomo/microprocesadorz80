package z80;

public class IR {

  public int opcode;
  public int op1;
  public int op2;

  public void clear() {
    this.op1 = 0;
    this.op2 = 0;
    this.opcode = 0;
  }

  public void decodeInstruction(int[] instruction){
    this.clear();
    this.opcode = instruction[0];
    this.op1 |= (0x00_00_00_ff & instruction[1]);
    this.op1 <<= 8;
    this.op1 |= (0x00_00_00_ff & instruction[2]);
    this.op2 |= (0x00_00_00_ff & instruction[3]);
    this.op2 <<= 8;
    this.op2 |= (0x00_00_00_ff & instruction[4]);
  }

  public int[] encodeInstruction() {
    int[] instruction = new int[5];
    instruction[0] = this.opcode;
    instruction[1] = (0x00_00_ff_00 & this.op1) >> 8;
    instruction[2] = (0x00_00_00_ff & this.op1);
    instruction[3] = (0x00_00_ff_00 & this.op2) >> 8;
    instruction[4] = (0x00_00_00_ff & this.op2);
    return instruction;
  }

  public long encodeToLong() {
    long result = 0, aux = 0;
    result = this.opcode;
    result <<= 32;
    aux = this.op1;
    aux <<= 16;
    result |= aux;
    aux = this.op2;
    result |= aux;
    return result;
  }
}
