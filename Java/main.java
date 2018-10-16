class main {
  public static void main(String[] args) {
    Procesador p = new Procesador();

    System.out.println(p.registros_8bit[0]);
    p.incrementar();
    System.out.println(p.registros_8bit[0]);
  }
}
