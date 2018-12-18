import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class Assembler {
  private int programSize;

  private HashMap<String, Integer> reg_8bit;
  private HashMap<String, Integer> reg_16bit;
  private HashMap<String, Integer> labels;
  private HashSet<String> opcodes;

  public Assembler() {
    this.programSize = 0;
    this.reg_8bit = new HashMap();
    this.reg_16bit = new HashMap();
    this.labels = new HashMap();
    this.opcodes = new HashSet();
    this.opcodes.add("ADD");
    this.opcodes.add("SUB");
    this.opcodes.add("LD");
    this.opcodes.add("AND");
    this.opcodes.add("OR");
    this.opcodes.add("XOR");
    this.opcodes.add("OUT");
    this.opcodes.add("JP");
    this.opcodes.add("INC");
    this.opcodes.add("DEC");
    this.opcodes.add("CPL");
    this.opcodes.add("NEG");
    this.opcodes.add("CP");
    this.opcodes.add("RL");
    this.opcodes.add("RR");
    this.opcodes.add("BIT");
    this.opcodes.add("SET");
    this.opcodes.add("RESET");
    this.opcodes.add("CALL");
    this.opcodes.add("HALT");
    this.opcodes.add("END");
    this.opcodes.add("ORG");
    this.opcodes.add("EQU");
    this.reg_8bit.put("A", 0);
    this.reg_8bit.put("B", 1);
    this.reg_8bit.put("C", 2);
    this.reg_8bit.put("D", 3);
    this.reg_8bit.put("E", 4);
    this.reg_8bit.put("H", 5);
    this.reg_8bit.put("L", 6);
    this.reg_8bit.put("Ac", 7);
    this.reg_8bit.put("Bc", 8);
    this.reg_8bit.put("Cc", 9);
    this.reg_8bit.put("Dc", 10);
    this.reg_8bit.put("Ec", 11);
    this.reg_8bit.put("Hc", 12);
    this.reg_8bit.put("Lc", 13);
    this.reg_16bit.put("PC", 0);
    this.reg_16bit.put("SP", 1);
    this.reg_16bit.put("IX", 2);
    this.reg_16bit.put("IY", 3);
  }

  private int toDec(String address) {
    return Integer.parseInt(address, 16);
  }

  private void chargeLabels(String file) throws FileNotFoundException, IOException {
    String input = null;
    FileReader f = new FileReader(file);
    BufferedReader b = new BufferedReader(f);
    String[] line = null;
    int auxPointer = 0;
    while ((input = b.readLine()) != null) {
      this.programSize++;
      if (input.charAt(0) == '#') continue;
      line = input.split(" ");
      if (line.length == 3 || !this.opcodes.contains(line[0])) {
        this.labels.put(line[0], auxPointer);
      }
      auxPointer+=5;
    }
    b.close();
  }

  public void assemble(String inputFile) throws FileNotFoundException, IOException {
    this.chargeLabels(inputFile);
    boolean relocate = false;
    String line = null, label = null, opcode = null;
    String[] aux = null, params = null;
    FileReader f = new FileReader(inputFile);
    BufferedReader b = new BufferedReader(f);
    PrintWriter outFile = new PrintWriter("relocatableCode.txt", "UTF-8");

    while((line = b.readLine()) != null) {
      relocate = false;
      if (line.charAt(0) == '#') continue;
      aux = line.split(" ");
      params = null;
      label = null;

      // Dividir linea
      if (aux.length == 3) {
        label = aux[0];
        opcode = aux[1];
        params = aux[2].split(",");
        relocate = true;
      } else if (aux.length == 2) {
        // label opcode
        if (!this.opcodes.contains(aux[0])) {
          label = aux[0];
          opcode = aux[1];
          relocate = true;
        // opcode x
        } else {
          opcode = aux[0];
          // opcode label
          if (this.labels.contains(aux[1])) {
            label = aux[1];
            relocate = true;
          // opcode params
          } else {
            params = aux[1].split(",");
          }
        }
      } else {
        opcode = aux[0];
      }

      // Inicio
      switch(opcode) {

        case "ADD":
          //Reg
          if (this.reg_8bit.containsKey(params[1])) {
            this.ir.opcode = 0;
            this.ir.op1 = this.reg_8bit.get("A");
            this.ir.op2 = this.reg_8bit.get(params[1]);

          //Mem
          } else if (params[1].contains("(")) {
            // direccion indirecto (XX)
            if (params[1].length() == 4) {
              this.ir.opcode = 1;
              this.ir.op1 = this.reg_8bit.get(params[1].substring(1,2));
              this.ir.op2 = this.reg_8bit.get(params[1].substring(2,3));

            // direccion directo (xxxxH)
            } else {
              this.ir.opcode = 2;
              this.ir.op2 = this.toDec(params[1].substring(1,5));
            }

          } else {
            //Num hex XXH
            this.ir.opcode = 3;
            this.ir.op1 = this.reg_8bit.get("A");
            this.ir.op2 = this.toDec(params[1].substring(0, 2));
          }
          break;

        case "SUB":
          //Reg
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 4;
            this.ir.op1 = this.reg_8bit.get("A");
            this.ir.op2 = this.reg_8bit.get(params[0]);

          //Mem
          } else if (params[1].contains("(")) {
            // direccion indirecto (XX)
            if (params[1].length() == 4) {
              this.ir.opcode = 5;
              this.ir.op1 = this.reg_8bit.get(params[0].substring(1,2));
              this.ir.op2 = this.reg_8bit.get(params[0].substring(2,3));

            // direccion directo (xxxxH)
            } else {
              this.ir.opcode = 6;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }

          } else {
            //Num hex XXH
            this.ir.opcode = 7;
            this.ir.op1 = this.reg_8bit.get("A");
            this.ir.op2 = this.toDec(params[0].substring(0, 2));
          }
          break;

        case "INC":
          // Reg 8 bits
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 8;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Reg 16 bits
          } else if (this.reg_16bit.containsKey(params[0])) {
            this.ir.opcode = 9;
            this.ir.op2 = this.reg_16bit.get(params[0]);
          // Mem
          } else {
            this.ir.opcode = 10;
            this.ir.op2 = this.toDec(params[0].substring(1,5));
          }
          break;

        case "DEC":
          // Reg 8 bits
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 11;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Reg 16 bits
          } else if (this.reg_16bit.containsKey(params[0])) {
            this.ir.opcode = 12;
            this.ir.op2 = this.reg_16bit.get(params[0]);
          // Mem
          } else {
            this.ir.opcode = 13;
            this.ir.op2 = this.toDec(params[0].substring(1,5));
          }
          break;

        case "CPL":
          this.ir.opcode = 14;
          break;

        case "NEG":
          this.ir.opcode = 15;
          break;

        case "LD":
          // Reg 8 bits
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.op1 = this.reg_8bit.get(params[0]);

            // desde reg
            if (this.reg_8bit.containsKey(params[1])) {
              this.ir.opcode 16;
              this.ir.op2 = this.reg_8bit.get(params[1]);

            // desde mem
            } else if (params[1].contains("(")) {
              // memoria indirecto (XX)
              if (params[1].length() == 4) {
                this.ir.opcode = 17;
                this.ir.op2 = this.reg_8bit.get(params[1].substring(1,2));
                this.ir.op2 <<= 8;
                this.ir.op2 |= this.reg_8bit.get(params[1].substring(2,3));
              // memoria directo
              } else {
                this.ir.opcode = 18;
                this.ir.op2 = this.toDec(params[1].substring(1,5));
              }
            // desde num
            } else {
              this.ir.opcode = 19;
              this.ir.op2 = this.toDec(params[1].substring(0,2));
            }

          // Reg 16 bits
          } else if (this.reg_16bit.containsKey(params[0])) {
            this.ir.op1 = this.reg_16bit.get(params[0]);

            // desde reg 16 bits
            if (this.reg_16bit.containsKey(params[1])) {
              this.ir.opcode = 20;
              this.ir.op2 = this.reg_16bit.get(params[1]);

            // desde num hex
            } else if (params[1].length() == 5){
              this.ir.opcode = 21;
              this.ir.op2 = this.toDec(params[1].substring(0,4));

            // desde par reg 8 bits
            } else {
              this.ir.opcode = 22;
              this.ir.op2 = this.reg_8bit.get(params[1].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[1].substring(1,2));
            }

          // Par reg 8 bit desde reg 16 bit
          } else if (params[0].length() == 2) {
            this.ir.opcode = 23;
            this.ir.op1 = this.reg_8bit.get(params[0].substring(0,1));
            this.ir.op1 <<= 8;
            this.ir.op1 |= this.reg_8bit.get(params[0].substring(1,2));
            this.ir.op2 = this.reg_16bit.get(params[1]);

          // Mem
          } else {
            // memoria indirecto
            if (params[0].length() == 4) {
              this.ir.op1 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op1 <<= 8;
              this.ir.op1 |= this.reg_8bit.get(params[0].substring(1,2));

              // desde registro
              if (this.reg_8bit.containsKey(params[1])) {
                this.ir.opcode = 24;
                this.it.op2 = this.reg_8bit.get(params[1]);
              // desde mem
              } else if (params[1].contains("(")) {

                // desde mem ind
                if (params[1].length() == 4) {
                  this.ir.opcode = 25;
                  this.ir.op2 = this.reg_8bit.get(params[1].substring(0,1));
                  this.ir.op2 <<= 8;
                  this.ir.op2 |= this.reg_8bit.get(params[1].substring(1,2));

                // desde mem directa
                } else {
                  this.ir.opcode = 26;
                  this.ir.op2 = this.toDec(params[1].substring(1,5));
                }

              // desde num
              } else {
                this.ir.opcode = 27;
                this.ir.op2 = this.toDec(params[1].substring(0,2));
              }

            // memoria directa
            } else {
              this.ir.op1 = this.toDec(params[0].substring(1,5));

              // desde registro
              if (this.reg_8bit.containsKey(params[1])) {
                this.ir.opcode = 28;
                this.it.op2 = this.reg_8bit.get(params[1]);
              // desde mem
              } else if (params[1].contains("(")) {

                // desde mem ind
                if (params[1].length() == 4) {
                  this.ir.opcode = 29;
                  this.ir.op2 = this.reg_8bit.get(params[1].substring(0,1));
                  this.ir.op2 <<= 8;
                  this.ir.op2 |= this.reg_8bit.get(params[1].substring(1,2));

                // desde mem directa
                } else {
                  this.ir.opcode = 30;
                  this.ir.op2 = this.toDec(params[1].substring(1,5));
                }

              // desde num
              } else {
                this.ir.opcode = 31;
                this.ir.op2 = this.toDec(params[1].substring(0,2));
              }
            }
          }
          break;
// -----------------------------------------------------------------------------
        case "OUT":
          //Mostrar acumulador por puerto definid
          this.ir.opcode = 33;
          break;

        case "AND":
          this.ir.op1 = this.reg_8bit.get("A");
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 34;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Mem
          } else if (params[0].contains("(")) {
            // Mem ind
            if (params[0].length() == 4) {
              this.ir.opcode = 35;
              this.ir.op2 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[0].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 36;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }

          // Num
          } else {
            this.ir.opcode = 37;
            this.ir.op2 = this.toDec(params[0].substring(0,2));
          }
          break;

        case "OR":
          this.ir.op1 = this.reg_8bit.get("A");
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 38;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Mem
          } else if (params[0].contains("(")) {
            // Mem ind
            if (params[0].length() == 4) {
              this.ir.opcode = 39;
              this.ir.op2 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[0].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 40;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }

          // Num
          } else {
            this.ir.opcode = 41;
            this.ir.op2 = this.toDec(params[0].substring(0,2));
          }
          break

        case "XOR":
          this.ir.op1 = this.reg_8bit.get("A");
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 42;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Mem
          } else if (params[0].contains("(")) {
            // Mem ind
            if (params[0].length() == 4) {
              this.ir.opcode = 43;
              this.ir.op2 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[0].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 44;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }

          // Num
          } else {
            this.ir.opcode = 45;
            this.ir.op2 = this.toDec(params[0].substring(0,2));
          }
          break;

        case "CP":
          this.ir.op1 = this.reg_8bit.get("A");
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 46;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Mem
          } else if (params[0].contains("(")) {
            // Mem ind
            if (params[0].length() == 4) {
              this.ir.opcode = 47;
              this.ir.op2 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[0].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 48;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }

          // Num
          } else {
            this.ir.opcode = 49;
            this.ir.op2 = this.toDec(params[0].substring(0,2));
          }
          break;

        case "RR":
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 50;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Reg 16 bit
          } else if (this.reg_16bit.containsKey(params[0])) {
            this.ir.opcode = 51;
            this.ir.op2 = this.reg_16bit.get(params[0]);
          // Mem
          } else {
            // Mem ind
            if (params[0].length() == 4) {
              this.ir.opcode = 52;
              this.ir.op2 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[0].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 53;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }
          }
          break;

        case "RL":
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[0])) {
            this.ir.opcode = 54;
            this.ir.op2 = this.reg_8bit.get(params[0]);
          // Reg 16 bit
          } else if (this.reg_16bit.containsKey(params[0])) {
            this.ir.opcode = 55;
            this.ir.op2 = this.reg_16bit.get(params[0]);
          // Mem
          } else {
            // Mem ind
            if (params[0].length() == 4) {
              this.ir.opcode = 56;
              this.ir.op2 = this.reg_8bit.get(params[0].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[0].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 57;
              this.ir.op2 = this.toDec(params[0].substring(1,5));
            }
          }
          break;

        case "BIT":
          this.ir.op1 = this.toDec(params[0]);
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[1])) {
            this.ir.opcode = 58;
            this.ir.op2 = this.reg_8bit.get(params[1]);
          // Mem
          } else {
            // Mem ind
            if (params[1].length() == 4) {
              this.ir.opcode = 59;
              this.ir.op2 = this.reg_8bit.get(params[1].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[1].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 60;
              this.ir.op2 = this.toDec(params[1].substring(1,5));
            }
          }
          break;

        case "SET":
          this.ir.op1 = this.toDec(params[0]);
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[1])) {
            this.ir.opcode = 61;
            this.ir.op2 = this.reg_8bit.get(params[1]);
          // Mem
          } else {
            // Mem ind
            if (params[1].length() == 4) {
              this.ir.opcode = 62;
              this.ir.op2 = this.reg_8bit.get(params[1].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[1].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 63;
              this.ir.op2 = this.toDec(params[1].substring(1,5));
            }
          }
          break;

        case "RESET":
          this.ir.op1 = this.toDec(params[0]);
          // Reg 8 bit
          if (this.reg_8bit.containsKey(params[1])) {
            this.ir.opcode = 64;
            this.ir.op2 = this.reg_8bit.get(params[1]);
          // Mem
          } else {
            // Mem ind
            if (params[1].length() == 4) {
              this.ir.opcode = 65;
              this.ir.op2 = this.reg_8bit.get(params[1].substring(0,1));
              this.ir.op2 <<= 8;
              this.ir.op2 |= this.reg_8bit.get(params[1].substring(1,2));
            // Mem dir
            } else {
              this.ir.opcode = 66;
              this.ir.op2 = this.toDec(params[1].substring(1,5));
            }
          }
          break;

        case "JP":

          break;
      }
    }
  }

}

/*
0  add (with reg) accum
1  add (with mem ind) accum
2  add (with mem dir)
3  add (with num 8 bits) accum
4  sub (with reg) accum
5  sub (with mem ind) accum
6  sub (with mem dir)
7  sub (with num 8 bits) accum
8  increment1 (with reg 8 bits)
9  increment1 (with reg 16 bits)
10 increment1 (with mem 8 bits)
11 decrement1 (with reg 8 bits)
12 decrement1 (with reg 16 bits)
13 decrement1 (with mem 8 bits)
14 complement1 (only acc)
15 complement2 (only acc)
16 load (with reg 8 - reg 8)
17 load (with reg 8 - mem ind) A, (HL) ind
18 load (with reg 8 - mem dir) A, (2016H) dir
19 load (with reg 8 - num 8)
20 load (with reg 16 - reg 16)
21 load (with reg 16 - num )
22 load (with reg 16 - 2 reg 8 )
23 load (with 2 reg 8 - reg 16)
24  load (with mem ind 8 - reg 8)
25  load (with mem ind 8 - mem ind 8)
26  load (with mem ind 8 - mem dir 8)
27  load (with mem ind 8 - num 8)
28  load (with mem dir 8 - reg 8)
29  load (with mem dir 8 - mem ind 8)
30  load (with mem dir 8 - mem dir 8)
31  load (with mem dir 8 - num 8)


32  Input (only acc)


33  Output (only acc)
34  AND (with reg 8 bits) acc
35  AND (with mem ind  8 bits) acc
36  AND (with mem dir  8 bits) acc
37  AND (with num 8 bits) acc
38  OR (with reg 8 bits) acc
39  OR (with mem ind) acc
40  OR (with mem dir) acc
41  OR (with num 8 bits) acc
42  XOR (with reg 8 bits) acc
43  XOR (with mem ind bits) acc
44  XOR (with mem dir bits) acc
45  XOR (with num 8 bits) acc
46  comparation (with reg 8 bits) acc
47  comparation (with mem ind 8 bits) acc
48  comparation (with mem dir 8 bits) acc
49  comparation (with num 8 bits) acc
50  rigthRotation (with reg 8 bits)
51  rigthRotation (with reg 16 bits)
52  rigthRotation (with mem ind 8 bits)
53  rigthRotation (with mem dir 8 bits)
54  leftRotation (with reg 8 bits)
55  leftRotation (with reg 16 bits)
56  leftRotation (with mem ind 8 bits)
57  leftRotation (with mem dir 8 bits)
58  checkBit (with reg 8 bits) modify z-flag
59  checkBit (with mem ind 8 bits) modify z-flag
60  checkBit (with mem dir 8 bits) modify z-flag
61  setBit (with reg 8 bits)
62  setBit (with mem ind 8 bits)
63  setBit (with mem dir 8 bits)
64  resetBit (with reg 8 bits)
65  resetBit (with mem ind 8 bits)
66  resetBit (with mem dir 8 bits)

67  jump C (acc > 0)
68  jump Z (acc == 0)
69  jump (is a memory address)

// TODO etiquta org call equ
*/
