import java.io.IOException;

public class main {

  public static void main(String[] args)  throws IOException {
    Assembler a = new Assembler();
    Processor z80 = new Processor(a.assemble("program.txt"));
    z80.runProgram();
  }
}
