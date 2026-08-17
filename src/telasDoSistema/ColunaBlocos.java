package src.telasDoSistema;

import java.util.ArrayList;
import java.util.List;

//Classe que representa uma pilha visual de blocos em uma posição x fixa sobre a mesa.
//Os blocos ficam guardados do fundo (índice 0) para o topo (último índice).
public class ColunaBlocos{
  private double x;
  private final List<BlocoVisual> blocos = new ArrayList<>();

  //Construtor
  public ColunaBlocos(double x){
    this.x = x;
  }

  public double getX(){
    return x;
  }

  public List<BlocoVisual> getBlocos(){
    return blocos;
  }

  public boolean isEmpty(){
    return blocos.isEmpty();
  }

  //Retorna o bloco livre (do topo) da pilha, ou null se estiver vazia
  public BlocoVisual topo(){
    return blocos.isEmpty() ? null : blocos.get(blocos.size() - 1);
  }

  //Empilha um bloco no topo desta coluna e recalcula as posições
  public void empilhar(BlocoVisual bloco, double alturaDaMesa){
    blocos.add(bloco);
    reposicionar(alturaDaMesa);
  }

  //Remove um bloco específico da coluna (usado ao desempilhar/arrastar) e recalcula as posições
  public void remover(BlocoVisual bloco, double alturaDaMesa){
    blocos.remove(bloco);
    reposicionar(alturaDaMesa);
  }

  //Recalcula a posição alvo (para animação) de cada bloco da coluna, do fundo para o topo
  public void reposicionar(double alturaDaMesa){
    double y = alturaDaMesa - BlocoVisual.TAMANHO;
    for(BlocoVisual bloco : blocos){
      bloco.moverPara(x, y);
      y -= BlocoVisual.TAMANHO;
    }
  }
}
