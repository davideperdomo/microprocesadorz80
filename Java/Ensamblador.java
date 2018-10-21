import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

class Ensamblador {
  ArrayList<String> n_registros;
  private byte[] programa;
  private int tamano;
  private final int BITS = 16;

  public Ensamblador(){
    this.tamano = Math.pow(2, BITS);
    this.byte = new byte[this.tamano];
    String registros[]={"A", "B", "C", "D", "E", "H", "L"};
    this.n_registros=new ArrayList<String>(Arrays.asList(registros));
  }

  private int getidx(String nombre){
    return this.n_registros.indexOf(nombre);
  }

  public void ensamblar(String archivo) throws FileNotFoundException, IOException {

    String cadena = null;
    FileReader f = new FileReader(archivo);
    BufferedReader b = new BufferedReader(f);
    String[] line = null;
    String[] operandos = null;

    while((cadena = b.readLine()) != null) {

        line = cadena.split(" ");

        switch (line[1]) {
          case "ORG":
            //Localidad en memoria de inicio de programa
            break;
          case "EQU":
            //Definir puerto de entrada
            break;
          case "LD":
            //Transferencia de bits
            String[] operandos = line[2].split(",");
            if(n_registros.contains(operandos[1])){
              p.registros_8bit[this.getidx(operandos[0])] = p.registros_8bit[this.getidx(operandos[1])];
            }else{
              byte[] aByte =	DatatypeConverter.parseHexBinary(operandos[1].split("H")[0]);
              p.registros_8bit[this.getidx(operandos[0])] = aByte[0];
            }
            break;
          case "ADD":
            //Suma
            operandos = line[2].split(",");

            // Verificar si es registro o mem


            // String[] sumandos = line[2].split(",");
            // p.suma(p.registros_8bit[this.getidx(sumandos[1])]);
            // break;
          case "OUT":
            //Mostrar acumulador por puerto definido
            System.out.println(p.registros_8bit[0] & 0xFF);
            break;
          case "HALT":
            break;
          case "END":
            //Fin de programa
            break;
          default:
            System.out.println("no opcode found: "+line[1]);
        }
    }
    b.close();
  }
}
