package src.funcionamentoDoSistema;

public class TesteBlocos{
  //Método principal
  public static void main(String[] args){
    RunDemoBlocos rdb = new RunDemoBlocos();
    // Define os 5 blocos dinamicamente.
    //String[] blocos = {"A", "B", "C", "D", "E"}; 
    String[] blocos = {"A", "B", "C", "D"}; 
    rdb.run(blocos);
  }
}
