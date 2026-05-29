package roboBlocos;

import aima.core.agent.impl.DynamicAction;

public class BActions extends DynamicAction{
  //Attributes
  //Atributos das ações
  public static final String EMPILHAR = "Empilhar";
  public static final String DESEMPILHAR = "Desempilhar";

  //Construtor para construir uma ação dinâmica
  public BActions(String type){
    super(type);
  }
} 
