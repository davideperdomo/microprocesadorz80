package z80;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class UI {
  private GUIJFrame f;

   public UI(){
       this.f=new GUIJFrame();
   }
    public void getProgram(String file) throws InterruptedException, IOException {
    //this.f=new GUIJFrame();
    f.setVisible(true);
    Memory mem = new Memory();
    Assembler a = new Assembler();
    a.assemble(file);
    LinkerLoader l = new LinkerLoader();
    l.chargeProgram("relocatableCode.txt", mem);
    Processor z80 = new Processor(mem, this);
    z80.runProgram();
    
    for (int i = 0; i < 20; i++) {
        System.out.println("addr: " + i + "; MEM: " + mem.get(i));            
    }
  }
  public void printLabel(String s){
      f.print(s);
  }
  public void printLabelOp(String s){
      f.print2(s);
  }
  public void printLabelOp2(String msg){
      f.printop2(msg);
  }
  public void printLabelReg(int reg,String msg){
      f.printreg(reg, msg);
  }
  public void printLabel16Reg(int reg, String msg){
      f.print16reg(reg,msg);
  }
  public void printLabelMem(int pos,String val){
      f.printmem(String.valueOf(pos),val);
  }
  public void printLabelFlag(int flag, boolean val){
      f.printflag(flag,val);
  }
  public void printOutput(String msg){
      f.printoutput(msg);
  }
}
