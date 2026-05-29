package roboBlocos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import roboBlocos.*;
import aima.core.agent.Action;

public class BFunctions{
  //Método para obter as ações possíveis
  public static List<BActions> getActions(EBlocos state){
    List<BActions> actions = new ArrayList<>();
    
    List<Blocos> blocosLivres = new ArrayList<>();
    for(Stack<Blocos> pilha : state.getPilhas2()){  //Encontra os blocos livres
      if(!pilha.isEmpty())
        blocosLivres.add(pilha.peek());
    }

    for(Blocos bloco : blocosLivres){ //Para os blocos livres, encontra quais podem serem desempilhados e empilhados
      for(Stack<Blocos> pilha : state.getPilhas2()){
        if(pilha.isEmpty())
          continue;
        if(pilha.peek().equals(bloco) && pilha.size()>1)
          actions.add(new BActions(BActions.DESEMPILHAR+" "+bloco.getNome()+" "));
      }
      for(Blocos bloco2 : blocosLivres){
        if(!bloco2.equals(bloco))
          actions.add(new BActions(BActions.EMPILHAR+" "+bloco2.getNome()+" em "+bloco.getNome()+" "));
      }
    }
    return actions;
  }

  //Método para clonar o estado das pilhas para poder realizar altearções sem afetar a busca
  private static EBlocos clonarEstado(EBlocos state){ 
    List<Stack<Blocos>> novasPilhas = new ArrayList<>();
    for(Stack<Blocos> pilhas : state.getPilhas2()){
      if(pilhas.isEmpty())
        continue;
      Stack<Blocos> novaPilha = new Stack<>();
      for(Blocos bloco : pilhas){
        novaPilha.push(new Blocos(bloco.getNome(), bloco.getTemAcima()));
      }
      novasPilhas.add(novaPilha);
    }
    return new EBlocos(novasPilhas);
  }

  //Método para encontrar um bloco dado seu nome e o estado atual
  private static Blocos encontrarBlocoNoEstado(EBlocos state, String nome){
    for(Stack<Blocos> pilha : state.getPilhas2()){
      if(pilha.isEmpty())
        continue;
      for(Blocos bloco : pilha){
        if(bloco.getNome().equals(nome))
          return bloco;
      }
    }
    return null;
  }

  //Método para obter os resultados das ações realizadas 
  public static EBlocos getResult(EBlocos state, Action action){
    String actionName = action.toString();
    EBlocos novoEstado = clonarEstado(state);

    if(actionName.contains(BActions.DESEMPILHAR)){
      String[] partes = actionName.split(" ");
      //System.out.println(partes[1]);
      Blocos blocoA = encontrarBlocoNoEstado(novoEstado, partes[1]);
      novoEstado.desempilhar(blocoA);
    }
    if(actionName.contains(BActions.EMPILHAR)){
      String[] partes = actionName.split(" ");
      //System.out.println(partes[3]);
      //System.out.println(partes[1]+" "+partes[3]);
      Blocos blocoA = encontrarBlocoNoEstado(novoEstado, partes[1]);
      Blocos blocoB = encontrarBlocoNoEstado(novoEstado, partes[3]);
      //System.out.println("ablu"+blocoA.getNome()+" "+blocoA.getNome());
      novoEstado.empilhar(blocoA, blocoB);
    }
    return novoEstado;
  }

  //Testa se o objetivo foi atingido
  public static boolean testGoal(EBlocos state){
    //Gera um objetivo para ser testado
    List<Stack<Blocos>> objetivo = new ArrayList<>();
    Stack<Blocos> pilha = new Stack<>();
    Blocos blocoA = new Blocos("A", true);
    Blocos blocoB = new Blocos("B", false);
    Blocos blocoC = new Blocos("C", true);
    Blocos blocoD = new Blocos("D", false);
    pilha.push(blocoA);
    pilha.push(blocoB);
    objetivo.add(pilha);
    Stack<Blocos> pilha2 = new Stack<>();
    pilha2.push(blocoC);
    pilha2.push(blocoD);
    objetivo.add(pilha2);
    GTBlocos gt = new GTBlocos(objetivo);

    //Retorna o resultado do teste
    return gt.test(state);
  }
}
