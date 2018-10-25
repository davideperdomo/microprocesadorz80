import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class Chip{
    private short adressbus;
    private int databus;
    private boolean[] inputports;
    //MREQ,IORQ,RD,WR,RFSH,HALT
    private boolean[] outputports;
    //INT,WAIT,RESET,BUSRQ 
    
    Chip(){
        this.adressbus = (short) 0;
        this.databus = (byte) 0;
        this.inputports = new boolean[7];
        this.inputports = new boolean[4];
    }
    public void input() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("databus input");
        String input = br.readLine();
        this.databus = Integer.parseInt(input, 2);
        br.close();
    }
    public int getDatabus() throws IOException{
        this.input();
        return databus;
    }
    public void setDatabus(int data){
        this.databus = data;
    }
}