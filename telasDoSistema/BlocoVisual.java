package roboBlocos.telasDoSistema;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

//Classe que representa visualmente um bloco (posição, cor, nome) e sabe se desenhar e se animar
public class BlocoVisual{
  public static final int TAMANHO = 70; //Tamanho do lado do quadrado do bloco

  private final String nome;
  private final Color cor;
  private double x, y;             //Posição atual na tela
  private double destinoX, destinoY; //Posição alvo, usada para animar o movimento suavemente

  //Construtor
  public BlocoVisual(String nome, Color cor, double x, double y){
    this.nome = nome;
    this.cor = cor;
    this.x = x;
    this.y = y;
    this.destinoX = x;
    this.destinoY = y;
  }

  //Aproxima a posição atual da posição alvo a cada frame (efeito de animação suave)
  public void atualizar(){
    x += (destinoX - x) * 0.2;
    y += (destinoY - y) * 0.2;
  }

  //Verifica se o bloco já chegou (aproximadamente) na posição alvo
  public boolean chegou(){
    return Math.abs(destinoX - x) < 0.5 && Math.abs(destinoY - y) < 0.5;
  }

  //Define uma nova posição alvo, para onde o bloco vai se mover de forma animada
  public void moverPara(double novoDestinoX, double novoDestinoY){
    this.destinoX = novoDestinoX;
    this.destinoY = novoDestinoY;
  }

  //Define a posição atual imediatamente (sem animação), usado durante o arrasto do mouse
  public void definirPosicaoImediata(double novoX, double novoY){
    this.x = novoX;
    this.y = novoY;
    this.destinoX = novoX;
    this.destinoY = novoY;
  }

  //Verifica se um ponto (px, py) está dentro da área do bloco
  public boolean contem(double px, double py){
    return px >= x && px <= x + TAMANHO && py >= y && py <= y + TAMANHO;
  }

  //Desenha o bloco: sombra, quadrado colorido, borda e nome centralizado
  public void draw(Graphics2D g2d){
    g2d.setColor(new Color(0, 0, 0, 40));
    g2d.fillRoundRect((int) x + 4, (int) y + 6, TAMANHO, TAMANHO, 14, 14);

    g2d.setColor(cor);
    g2d.fillRoundRect((int) x, (int) y, TAMANHO, TAMANHO, 14, 14);

    g2d.setColor(cor.darker());
    g2d.drawRoundRect((int) x, (int) y, TAMANHO, TAMANHO, 14, 14);

    g2d.setFont(new Font("Arial", Font.BOLD, 26));
    g2d.setColor(corDeTextoComContraste());
    FontMetrics fm = g2d.getFontMetrics();
    int textoX = (int) x + (TAMANHO - fm.stringWidth(nome)) / 2;
    int textoY = (int) y + (TAMANHO + fm.getAscent()) / 2 - 4;
    g2d.drawString(nome, textoX, textoY);
  }

  //Escolhe branco ou preto para o texto, de acordo com o brilho da cor de fundo do bloco
  private Color corDeTextoComContraste(){
    double luminancia = (0.299 * cor.getRed() + 0.587 * cor.getGreen() + 0.114 * cor.getBlue()) / 255.0;
    return luminancia > 0.6 ? Color.BLACK : Color.WHITE;
  }

  //Getters
  public String getNome(){
    return nome;
  }
  public double getX(){
    return x;
  }
  public double getY(){
    return y;
  }
}
