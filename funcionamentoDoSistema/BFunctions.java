package roboBlocos.funcionamentoDoSistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import roboBlocos.funcionamentoDoSistema.*;
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

    for(Blocos bloco : blocosLivres){ //Para os blocos livres, encontra quais podem ser desempilhados e empilhados
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

  //Método para clonar o estado das pilhas para poder realizar alterações sem afetar a busca
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
      Blocos blocoA = encontrarBlocoNoEstado(novoEstado, partes[1]);
      novoEstado.desempilhar(blocoA);
    }
    if(actionName.contains(BActions.EMPILHAR)){
      String[] partes = actionName.split(" ");
      Blocos blocoA = encontrarBlocoNoEstado(novoEstado, partes[1]);
      Blocos blocoB = encontrarBlocoNoEstado(novoEstado, partes[3]);
      novoEstado.empilhar(blocoA, blocoB);
    }
    return novoEstado;
  }

  //Testa se um estado atingiu um objetivo (pilhas de blocos) recebido dinamicamente por parâmetro.
  public static boolean testGoal(EBlocos state, List<Stack<Blocos>> objetivo){
    GTBlocos gt = new GTBlocos(objetivo);
    return gt.test(state);
  }

  //Sobrecarga mantida para a compatibilidade com código já existente (interface gráfica via RoboSolver).
  //Isso garante que o problema dinâmico (onde quer que as peças comecem) almeje sempre a solução ordenada de 5 blocos.
  public static boolean testGoal(EBlocos state){
    return testGoal(state, objetivoPadrao());
    //Gera um objetivo para ser testado
    //List<Stack<Blocos>> objetivo = new ArrayList<>();
    //Stack<Blocos> pilha = new Stack<>();
  /*Blocos blocoA = new Blocos("A", true);
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
    Blocos blocoA = new Blocos("A", true);
    Blocos blocoB = new Blocos("B", true);
    Blocos blocoC = new Blocos("C", true);
    Blocos blocoD = new Blocos("D", false);
    pilha.push(blocoA);
    pilha.push(blocoB);
    pilha.push(blocoC);
    pilha.push(blocoD);
    objetivo.add(pilha);
    GTBlocos gt = new GTBlocos(objetivo);

    //Retorna o resultado do teste
    return gt.test(state);*/
  }

  //Novo objetivo padrão: uma única pilha ordenada com 5 blocos.
  //O bloco A é a base (tem blocos acima) e o bloco E é o topo (não tem blocos acima).
  private static List<Stack<Blocos>> objetivoPadrao(){
    List<Stack<Blocos>> objetivo = new ArrayList<>();
    Stack<Blocos> pilha = new Stack<>();
    
    // Todos os blocos, exceto o do topo, devem possuir "temAcima" marcado como true
    Blocos blocoA = new Blocos("A", true);
    Blocos blocoB = new Blocos("B", true);
    Blocos blocoC = new Blocos("C", true);
    Blocos blocoD = new Blocos("D", true);
    Blocos blocoE = new Blocos("E", false); // E é o topo, logo é o único que "não tem acima"
    
    // Empilhando na ordem, para que A fique na base e E no topo
    pilha.push(blocoA);
    pilha.push(blocoB);
    pilha.push(blocoC);
    pilha.push(blocoD);
    pilha.push(blocoE);
    
    objetivo.add(pilha);
    return objetivo;
  }
}
