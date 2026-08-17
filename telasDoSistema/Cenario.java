package roboBlocos.telasDoSistema;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/*Classe que representa o cenário do mundo dos blocos: o fundo branco, a mesa (linha) e as
  colunas de blocos que estão sobre ela. É o "modelo visual" da cena, usado pela tela
  MundoDosBlocos tanto para desenhar quanto para saber onde cada bloco está.*/ 
public class Cenario{
  private final int width, height;
  private final int alturaDaMesa;
  private final List<ColunaBlocos> colunas = new ArrayList<>();

  //Nomes e cores fixas dos 5 blocos do problema
  private static final String[] NOMES_BLOCOS = {"A", "B", "C", "D", "E"};
  private static final Color[] CORES_BLOCOS = {
      new Color(231, 76, 60),   //A - vermelho
      new Color(52, 152, 219),  //B - azul
      new Color(46, 204, 113),  //C - verde
      new Color(243, 156, 18),  //D - laranja
      new Color(155, 89, 182)   //E - roxo
  };

  /*private static final String[] NOMES_BLOCOS = {"A", "B", "C", "D"};
  private static final Color[] CORES_BLOCOS = {
      new Color(231, 76, 60),   //A - vermelho
      new Color(52, 152, 219),  //B - azul
      new Color(46, 204, 113),  //C - verde
      new Color(243, 156, 18)};*/

  //Construtor
  public Cenario(int width, int height){
    this.width = width;
    this.height = height;
    this.alturaDaMesa = (int) (height * 0.72);
    gerarConfiguracaoInicial();
  }

  //Gera a configuração inicial: todos os blocos desempilhados, em uma única linha sobre a mesa
  public void gerarConfiguracaoInicial(){
    colunas.clear();
    int n = NOMES_BLOCOS.length;
    int espacamento = BlocoVisual.TAMANHO + 50;
    int inicioX = (width - (n - 1) * espacamento) / 2 - BlocoVisual.TAMANHO / 2;

    for(int i = 0; i < n; i++){
      double x = inicioX + i * espacamento;
      ColunaBlocos coluna = new ColunaBlocos(x);
      BlocoVisual bloco = new BlocoVisual(NOMES_BLOCOS[i], CORES_BLOCOS[i], x, alturaDaMesa - BlocoVisual.TAMANHO);
      coluna.empilhar(bloco, alturaDaMesa);
      colunas.add(coluna);
    }
  }

  public int getAlturaDaMesa(){
    return alturaDaMesa;
  }

  public List<ColunaBlocos> getColunas(){
    return colunas;
  }

  //Encontra o bloco (de qualquer coluna) que contém o ponto (px, py) informado
  public BlocoVisual blocoNoPonto(double px, double py){
    for(ColunaBlocos coluna : colunas)
      for(BlocoVisual bloco : coluna.getBlocos())
        if(bloco.contem(px, py))
          return bloco;
    return null;
  }

  //Encontra a coluna que contém um determinado bloco
  public ColunaBlocos colunaDoBloco(BlocoVisual bloco){
    for(ColunaBlocos coluna : colunas)
      if(coluna.getBlocos().contains(bloco))
        return coluna;
    return null;
  }

  //Encontra uma coluna cujo bloco do topo esteja próximo horizontalmente do ponto informado
  //(usado para saber sobre qual pilha o usuário está soltando um bloco)
  public ColunaBlocos colunaProximaDoTopo(double px, BlocoVisual ignorar){
    for(ColunaBlocos coluna : colunas){
      if(coluna.isEmpty())
        continue;
      if(coluna.topo() == ignorar)
        continue;
      if(Math.abs(px - coluna.getX()) < BlocoVisual.TAMANHO * 0.8)
        return coluna;
    }
    return null;
  }

  //Remove uma coluna da lista, caso ela tenha ficado vazia
  public void removerColunaSeVazia(ColunaBlocos coluna){
    if(coluna != null && coluna.isEmpty())
      colunas.remove(coluna);
  }

  //Cria e registra uma nova coluna vazia na posição x informada (ajustada para não sobrepor outra)
  public ColunaBlocos novaColuna(double xDesejado){
    double x = ajustarPosicaoLivre(xDesejado);
    ColunaBlocos coluna = new ColunaBlocos(x);
    colunas.add(coluna);
    return coluna;
  }

  //Ajusta uma posição x para não ficar em cima de outra coluna já existente
  private double ajustarPosicaoLivre(double xDesejado){
    double x = Math.max(60, Math.min(xDesejado, width - 60 - BlocoVisual.TAMANHO));
    boolean sobrepondo;
    int tentativas = 0;
    do{
      final double xf = x;
      sobrepondo = colunas.stream().anyMatch(c -> !c.isEmpty() && Math.abs(c.getX() - xf) < BlocoVisual.TAMANHO);
      if(sobrepondo)
        x += BlocoVisual.TAMANHO + 20;
      tentativas++;
    } while(sobrepondo && tentativas < 50);
    return x;
  }

  //Procura a primeira posição livre na mesa (usado quando o robô desempilha um bloco)
  public double proximaPosicaoLivre(){
    int espacamento = BlocoVisual.TAMANHO + 40;
    double x = 60;
    while(x < width - 80){
      final double xf = x;
      boolean ocupado = colunas.stream().anyMatch(c -> !c.isEmpty() && Math.abs(c.getX() - xf) < BlocoVisual.TAMANHO);
      if(!ocupado)
        return x;
      x += espacamento;
    }
    return x;
  }

  //Encontra um bloco pelo nome, em qualquer coluna
  public BlocoVisual encontrarBloco(String nome){
    for(ColunaBlocos coluna : colunas)
      for(BlocoVisual bloco : coluna.getBlocos())
        if(bloco.getNome().equals(nome))
          return bloco;
    return null;
  }

  //Aplica visualmente uma ação do robô (texto vindo do BActions, ex.: "Desempilhar C " ou "Empilhar B em A ")
  public void aplicarAcao(String textoAcao){
    if(textoAcao == null)
      return;
    String[] partes = textoAcao.trim().split("\\s+");
    if(textoAcao.contains("Desempilhar") && partes.length >= 2){
      desempilharVisual(partes[1]);
    }
    else if(textoAcao.contains("Empilhar") && partes.length >= 4){
      empilharVisual(partes[1], partes[3]);
    }
  }

  //Move visualmente um bloco de sua coluna para uma nova posição livre sobre a mesa
  private void desempilharVisual(String nomeBloco){
    BlocoVisual bloco = encontrarBloco(nomeBloco);
    if(bloco == null)
      return;
    ColunaBlocos origem = colunaDoBloco(bloco);
    if(origem == null)
      return;
    origem.remover(bloco, alturaDaMesa);
    removerColunaSeVazia(origem);

    ColunaBlocos nova = novaColuna(proximaPosicaoLivre());
    nova.empilhar(bloco, alturaDaMesa);
  }

  //Move visualmente o bloco A para cima do bloco B
  private void empilharVisual(String nomeA, String nomeB){
    BlocoVisual blocoA = encontrarBloco(nomeA);
    BlocoVisual blocoB = encontrarBloco(nomeB);
    if(blocoA == null || blocoB == null)
      return;

    ColunaBlocos origemA = colunaDoBloco(blocoA);
    ColunaBlocos destino = colunaDoBloco(blocoB);
    if(origemA == null || destino == null)
      return;

    origemA.remover(blocoA, alturaDaMesa);
    removerColunaSeVazia(origemA);
    destino.empilhar(blocoA, alturaDaMesa);
  }

  //Desenha o fundo branco, a mesa e todos os blocos
  public void draw(Graphics2D g2d){
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width, height);

    g2d.setColor(new Color(40, 40, 40));
    g2d.setStroke(new BasicStroke(4));
    g2d.drawLine(40, alturaDaMesa, width - 40, alturaDaMesa);

    for(ColunaBlocos coluna : colunas)
      for(BlocoVisual bloco : coluna.getBlocos())
        bloco.draw(g2d);
  }
}
