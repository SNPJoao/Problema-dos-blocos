package roboBlocos.funcionamentoDoSistema;

public class Blocos{
  //Attributes
  private String nome;
  private boolean temAcima;

  //Constructor
  public Blocos(String nome, boolean temAcima){
    this.nome = nome;
    this.temAcima = temAcima;
  }
  
  //Setters and Getters
  public String getNome(){
    return this.nome;
  }
  public void setNome(String nome){
    this.nome = nome;
    return;
  }
  public void setTemAcima(boolean temAcima){
    this.temAcima = temAcima;
    return;
  }
  public boolean getTemAcima(){
    return this.temAcima;
  } 

  //Overrides de métodos para garantir a igualdade entre blocos
  @Override
  public boolean equals(Object o){
    if(this == o)
      return true;
    if(o == null || getClass() != o.getClass())
      return false;
    Blocos blocos = (Blocos) o;
    return nome.equals(blocos.nome);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(nome);
  }
}
