import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class UI {
  public void init() throws FileNotFoundException, IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("-- Z80 PROCESSOR EMULATOR --");
    System.out.println("Load program: ");
    String archivo = br.readLine();
    Assembler a = new Assembler();
    //"programs/fibo.txt"
    Processor z80 = new Processor(a.assemble(archivo));
    z80.runProgram();
    br.close();
  }
}