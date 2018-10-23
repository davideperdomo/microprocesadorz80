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
    String[] check = null;
    String operando = null;
    
    while((cadena = b.readLine()) != null) {

        line = cadena.split(" ");

        switch (line[1]) {
          case "LD":
            //Transferencia de bits
            operandos = line[2].split(",");
            check = operandos[1].split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
              //p.registros_8bit[this.getidx(operandos[0])] = memoria[check[1]];
            }else if(n_registros.contains(operandos[1])){
              // EL operando es un registro
              //p.registros_8bit[this.getidx(operandos[0])] = p.registros_8bit[this.getidx(operandos[1])];
            }else{
              //El opernado es un valor hexadecimal
              byte[] aByte =	DatatypeConverter.parseHexBinary(operandos[1].split("H")[0]);
             // p.registros_8bit[this.getidx(operandos[0])] = aByte[0];
            }
            break;
          case "ADD":
            //Suma
            operandos = line[2].split(",");
            check = operandos[1].split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operandos[1])){
              // EL operando es un registro
            }else{
              //El opernado es un valor hexadecimal
              byte[] aByte =	DatatypeConverter.parseHexBinary(operandos[1].split("H")[0]);
            }
            break;
          case "SUB":
            //resta
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // EL operando es un registro
            }else{
              //El opernado es un valor hexadecimal
              byte[] aByte =	DatatypeConverter.parseHexBinary(operando.split("H")[0]);
            }
             break;
          case "INC":
            //incremento
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // El operando es un registro
            }
            break;
          case "DEC":
            //decremento
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // El operando es un registro
            }
            break;
          case "CPL":
            //complemento 1
            break;
          case "NEG":
            //complemento 2
            break;
          case "AND":
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // EL operando es un registro
            }else{
              //El operanado es un valor hexadecimal
              byte[] aByte =	DatatypeConverter.parseHexBinary(operando.split("H")[0]);
            }
            break;
          case "OR":
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // EL operando es un registro
            }else{
              //El opernado es un valor hexadecimal
              byte[] aByte =	DatatypeConverter.parseHexBinary(operando.split("H")[0]);
            }
            break;
          case "XOR":
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // EL operando es un registro
            }else{
              //El opernado es un valor hexadecimal
              byte[] aByte =	DatatypeConverter.parseHexBinary(operando.split("H")[0]);
            }
            break;
          case "CP":
            operando = line[2];
            check = operando.split("[(H)]");
            if(check[0].equals("")){
              //El operando apunta a memoria
            }else if(n_registros.contains(operando)){
              // EL operando es un registro
            }
            //comparar
            break;
          case "RL":
            //Rotacion izquierda
            break;
          case "RR":
            //Roacion derecha
            break;
          case "BIT":
            //verificar bit
            break;
          case "SET":
            //Poner bit 1
            break;
          case "RESET":
            //Poner bit en 0
            break;
          case "JP":
            //salto
            break;
          case "CALL":
            //llamada a subrutina
            break;
          case "ORG":
            //Localidad en memoria de inicio de programa
            break;
          case "EQU":
            //Definir puerto de entrada
            break;
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
