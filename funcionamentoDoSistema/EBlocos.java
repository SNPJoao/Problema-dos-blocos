package roboBlocos.funcionamentoDoSistema;

import java.util.List;
import java.util.Stack;
import roboBlocos.funcionamentoDoSistema.Blocos;

public class EBlocos{
  //Attributes
  private List<Stack<Blocos>> pilhas;  //Atributo que representa as pilhas de blocos

  //Constructor
  public EBlocos(List<Stack<Blocos>> pilhasIniciais){
    this.pilhas = pilhasIniciais;
    return;
  }

  //Método que realiza a ação de empilhar blocos
  public void empilhar(Blocos a, Blocos b){
    if(a == null || b == null){ //Verifica se são objetos não nulos
      System.out.println("Blocos inválidos.");
      return;
    }

    if(a.getTemAcima() == false && b.getTemAcima() == false){ //Verifica se são blocos livres e, se forem, realiza
      Stack<Blocos> pilhaDeA;                                 //a ação de empilhar um bloco em outra pilha.
      for(Stack<Blocos> pilha : pilhas){  
        if(pilha.contains(a)){  
          pilha.pop();
          if(!pilha.isEmpty()) 
            pilha.peek().setTemAcima(false);
          break;
        }
      }
      for(Stack<Blocos> pilha : pilhas){  
        if(pilha.contains(b)){
          b.setTemAcima(true);
          pilha.push(a);
          //System.out.println("Empilhou");
          break;
        }
      }
      pilhas.removeIf(Stack::isEmpty);
    }
    else System.out.println("Um dos blocos não está livre.");

    return;
  }
  
  //Método que realiza a ação de desempilhar um bloco
  public void desempilhar(Blocos a){
    if(a == null){  //Verifica se não é um objeto nulo
      System.out.println("Bloco inválido.");
      return;
    }
    if(a.getTemAcima() == false){ //Verifica se é um bloco livre e, se for, então realiza a ação de desempilhar o bloco da pilha
      for(Stack<Blocos> pilha : pilhas){
        if(pilha.contains(a)){
          if((pilha.size() > 1)){
            pilha.pop();
            Stack<Blocos> novaPilha = new Stack<>();
            pilha.peek().setTemAcima(false);
            novaPilha.push(a);
            pilhas.add(novaPilha);
            //System.out.println("Desempilhou");
          }
          break;  
        }
      }
      pilhas.removeIf(Stack::isEmpty);
      return;
    }
    else System.out.println("Bloco não está livre.");
    return;
  }
  
  public String getPilhas(){ //Obtém uma String que representa como as pilhas ficam organizadas no final
    String aux = "Pilhas de blocos: ";
    for(Stack<Blocos> pilha : pilhas){
      for(Blocos bloco : pilha){
        if(bloco == pilha.peek())
          aux += bloco.getNome();
        else aux += bloco.getNome()+"->";
      }
      aux += ", ";
    }
    return aux;
  }

  public List<Stack<Blocos>> getPilhas2(){ //Retorna a lista de pilhas de blocos
    return this.pilhas;
  }
}
