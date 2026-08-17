package src.telasDoSistema;

import src.funcionamentoDoSistema.BActions;
import src.funcionamentoDoSistema.BFunctions;
import src.funcionamentoDoSistema.Blocos;
import src.funcionamentoDoSistema.EBlocos;

import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.uninformed.BreadthFirstSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

//Classe que liga a interface gráfica ao motor de busca do robô.
//Não altera nenhuma classe do pacote funcionamentoDoSistema: apenas reaproveita seus métodos
//públicos (EBlocos, BFunctions, BActions), da mesma forma que RunDemoBlocos.java já faz,
//mas recebendo como estado inicial a configuração que o usuário montou arrastando os blocos.
public class RoboSolver{
  //Executa a busca em largura a partir da configuração informada e devolve a sequência de ações
  public static List<BActions> resolver(List<Stack<Blocos>> pilhasIniciais){
    EBlocos estadoInicial = new EBlocos(pilhasIniciais);
    Problem<EBlocos, BActions> problema =
        new GeneralProblem<>(estadoInicial, BFunctions::getActions, BFunctions::getResult, BFunctions::testGoal);

    QueueBasedSearch<EBlocos, BActions> busca = new BreadthFirstSearch<>();
    Optional<List<BActions>> acoes = busca.findActions(problema);
    return acoes.orElseGet(ArrayList::new);
  }
}
