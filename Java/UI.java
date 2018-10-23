import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class UI {
  public void init() throws FileNotFoundException, IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("MICROPROCESADOR Z80");
    System.out.println("Cargar programa: ");
    String archivo = br.readLine();
    System.out.println(archivo);
    //ensamblar(archivo);
    br.close();
  }
}