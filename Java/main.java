import java.io.IOException;

public class main {

  public static void main(String[] args)  throws IOException {
    Ensamblador e = new Ensamblador();
    Memoria mem = new Memoria(byte[]);
    Procesador z80 = new Procesador();
    z80.runProgram();
    e.ensamblar("program.txt", z80);
  }
}
