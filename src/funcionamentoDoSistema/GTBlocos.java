package src.funcionamentoDoSistema;

import aima.core.search.framework.problem.GoalTest;
import java.util.List;
import java.util.Stack;
import src.funcionamentoDoSistema.EBlocos;
import src.funcionamentoDoSistema.Blocos;
import java.util.Set;
import java.util.HashSet;

public class GTBlocos implements GoalTest{
  //Attributes
  public List<Stack<Blocos>> pilhasGT;

  //Constructor
  public GTBlocos(List<Stack<Blocos>> pilhasGT){
    this.pilhasGT = pilhasGT;
  }

  //Método para testar se o objetivo foi atingido independentemente da ordem das pilhas, mas dependendo da ordem dos blocos
  public boolean test(Object state){
    EBlocos blocos = (EBlocos) state;
    Set<Stack<Blocos>> setObjetivo = new HashSet<>(this.pilhasGT);
    Set<Stack<Blocos>> setAtual = new HashSet<>(blocos.getPilhas2());
    return setObjetivo.equals(setAtual);
  }

  //Método para retornar as pilhas de blocos
  /*public String getPilhasGT(){
    String aux = "Pilhas de blocos objetivo: ";
    for(Stack<Blocos> pilha : pilhasGT){
      for(Blocos bloco : pilha){
        if(bloco == pilha.peek())
          aux += bloco.getNome();
        else aux += bloco.getNome()+"->";
      }
      aux += ", ";
    }
    return aux;
  }*/
}
