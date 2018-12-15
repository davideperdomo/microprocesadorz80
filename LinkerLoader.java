import java.io.*;

public class LinkerLoader {

  private int program;
  private int org;


  /*
    long = 00000000|00000000|00000000|00000000|00000000(*)
    Leer org = 50
    Leer end
  */


  public void chargeProgram(String file, Memory m) throws FileNotFoundException, IOException {
    FileReader f = new FileReader(file);
    BufferedReader b = new BufferedReader(f);
    PrintWriter outfile = new PrintWriter("programBinary.txt", "UTF-8");
    String line = null;
    int[] instruction = null;
    int addr = 0;
    while ((line = b.readLine()) != null) {
      instruction = this.decodeLine(line);
      outfile.println(this.binaryInstruction(instruction));
      for (int i = 0; i < 5; i++) {
        m.set(this.org + addr++, instruction[i]);
      }
    }
    b.close();
    outfile.close();
  }


  public int[] decodeLine(String line) {
    long inst = Long.parseLong(line.substring(0, line.length() - 1), 2);
    long opcode = inst & 0x00_00_00_ff_00_00_00_00 >> 32;
    if (opcode == 50) {
      this.org = op2;
    }

    if (line.charAt(line.length() - 1) == '*') {
      inst += this.org;  // Posible suma > 65535
    }
    int[] result = new int[5];
    long filter = 0x00_00_00_00_00_00_00_ff;
    for (int i = 4; i >= 0; i--) {
      result[i] = (int) inst & filter >> 8*(i%4);
      filter <<= 8;
    }
    return result;
  }

  public String binaryInstruction(int[] instruction) {
    StringBuilder sb = new StringBuilder("");
    IR ir = new IR();
    ir.decodeInstruction(instruction);
    String aux = null;
    aux = String.format("%8s", Integer.toBinaryString(ir.opcode).replace(' ', '0');
    sb.append(aux);
    aux = String.format("%16s", Integer.toBinaryString(ir.op1).replace(' ', '0');
    sb.append(aux);
    aux = String.format("%16s", Integer.toBinaryString(ir.op2).replace(' ', '0');
    sb.append(aux);
    return sb.toString();
  }
}
