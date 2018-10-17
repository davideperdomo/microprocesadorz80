import java.io.IOException;

public class main {
 
  public static void main(String[] args)  throws IOException {
    Procesador z80 = new Procesador();
    Ensamblador e = new Ensamblador();
    e.ensamblar("program.txt", z80);
  }
}
