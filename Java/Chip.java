class Chip{
    private short adressbus;
    private byte databus;
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
    
    public byte getDatabus(){
        return databus;
    }
    public void setDatabus(byte data){
        this.databus = data;
    }
}