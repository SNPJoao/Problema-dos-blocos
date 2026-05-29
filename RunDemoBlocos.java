package roboBlocos;

import roboBlocos.*;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.SearchForStates;
import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import java.util.Optional;

public class RunDemoBlocos{
  //Attributes
  List<Stack<Blocos>> pilhasIniciais = new ArrayList<>();

  //Método para rodar as buscas
  public void run(String[] nomeBlocos){
    for(String nome : nomeBlocos){ //Gera os blocos todos livres, sendo uma pilha para cada bloco
      Blocos bloco = new Blocos(nome, false);
      Stack<Blocos> pilha = new Stack<>();
      pilha.push(bloco);
      pilhasIniciais.add(pilha);
      System.out.println("Bloco: "+bloco.getNome());
    }
    this.runBreadthFirstSearch(pilhasIniciais);
    //this.rundepthFirstLimitedSearch(pilhasIniciais);
    return;
  }

  //Método de busca em largura
  private void runBreadthFirstSearch(List<Stack<Blocos>> pilhasIniciais){
    EBlocos inicial = new EBlocos(pilhasIniciais);
    Problem<EBlocos, BActions> problem;
    problem = new GeneralProblem<>(inicial, BFunctions::getActions, BFunctions::getResult, BFunctions::testGoal);
    /*SearchForActions<EBlocos, BActions> search = new BreadthFirstSearch<>();
    Optional<List<BActions>> actions = search.findActions(problem);
    this.printActions(actions);
    SearchForStates<EBlocos, BActions> searchState = (SearchForState<>) search;
    System.out.println(searchState.findState().getPilhas());*/

    QueueBasedSearch<EBlocos, BActions> search = new BreadthFirstSearch<>();
    Optional<List<BActions>> actions = search.findActions(problem);
    this.printActions(actions);
    Optional<EBlocos> estado = search.findState(problem);
    Object aux = estado.get();
    EBlocos estadoFinal = (EBlocos) aux;
    System.out.println(estadoFinal.getPilhas());
    return;
  }

  //Método de busca em profundidade limitada
  private void rundepthFirstLimitedSearch(List<Stack<Blocos>> pilhasIniciais) {
    EBlocos inicial = new EBlocos(pilhasIniciais);   //estado
    Problem<EBlocos, BActions> problem;  //jactions acoes
    problem = new GeneralProblem<>(inicial, BFunctions::getActions, BFunctions::getResult, BFunctions::testGoal);    
	  //SearchForActions<EBlocos, BActions> search = new DepthLimitedSearch<>(8);
    DepthLimitedSearch<EBlocos, BActions> search = new DepthLimitedSearch<>(8);
	  Optional<List<BActions>> actions = search.findActions(problem);
    Optional<EBlocos> estado = search.findState(problem);
    Object aux = estado.get();
    EBlocos estadoFinal = (EBlocos) aux;
    this.printActions(actions);
    System.out.println(estadoFinal.getPilhas());
    return;
  }

  //Método para printar as ações
  private void printActions(Optional<List<BActions>> actions){
    List<BActions> acList;
    Object aux = actions.get();
    acList = ((List<BActions>) aux);
    for (int i = 0; i < acList.size(); i++){
      String act = (String) acList.get(i).getName();
      System.out.println(act);
    }
  }
}
