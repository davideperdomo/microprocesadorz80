import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class UI {
  public void init() throws FileNotFoundException, IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Modo: ");
    int opcion = Integer.parseInt(br.readLine());
    switch(opcion){
      case 1:
        System.out.println("Cargar programa: ");
        String archivo = br.readLine();
        System.out.println(archivo);
        //ensamblar programa
        break;
      case 2:
        String linea = " ";
        while(!linea.equals("exit")){
          System.out.println("Ingrese instrucción : ");
          linea = br.readLine();
          System.out.println(linea);
          //ejecutar instruccion
        }
        break;
      case 3: 
        System.out.println("Ejecutar programa en memoria: ");
        String localidad = br.readLine();
        System.out.println(localidad);
        break;
    }
        
    br.close();
  }
}