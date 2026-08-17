package roboBlocos.telasDoSistema;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

//Tela de menu principal, com os botões "Iniciar" e "Sair"
public class Menu extends JPanel{
  private final RoboBlocosTela frame;
  private final List<BotaoAnimado> botoes = new ArrayList<>();

  public Menu(RoboBlocosTela frame){
    this.frame = frame;
    setLayout(null);
    setBackground(new Color(44, 62, 80));

    Dimension tela = frame.getTamanhoTela();
    setPreferredSize(tela);

    int centroX = tela.width / 2 - 100;

    BotaoAnimado botaoIniciar = new BotaoAnimado("Iniciar", new Color(46, 204, 113));
    botaoIniciar.setBounds(centroX, tela.height / 2 - 70, 200, 60);
    botaoIniciar.addActionListener(e -> frame.startBlocksProblem());

    BotaoAnimado botaoSair = new BotaoAnimado("Sair", new Color(231, 76, 60));
    botaoSair.setBounds(centroX, tela.height / 2 + 20, 200, 60);
    botaoSair.addActionListener(e -> System.exit(0));

    botoes.add(botaoIniciar);
    botoes.add(botaoSair);
    for(BotaoAnimado b : botoes)
      add(b);

    Timer t = new Timer(16, e -> {
      for(BotaoAnimado b : botoes)
        b.atualizar();
      repaint();
    });
    t.start();
  }

  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    GradientPaint gradiente = new GradientPaint(
        0, 0, new Color(44, 62, 80),
        0, getHeight(), new Color(41, 128, 185));
    g2d.setPaint(gradiente);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 46));
    String titulo = "Mundo dos Blocos";
    FontMetrics fm = g2d.getFontMetrics();
    g2d.drawString(titulo, (getWidth() - fm.stringWidth(titulo)) / 2, getHeight() / 2 - 150);

    g2d.setFont(new Font("Arial", Font.PLAIN, 18));
    String subtitulo = "Um problema clássico de busca em inteligência artificial";
    FontMetrics fm2 = g2d.getFontMetrics();
    g2d.setColor(new Color(255, 255, 255, 200));
    g2d.drawString(subtitulo, (getWidth() - fm2.stringWidth(subtitulo)) / 2, getHeight() / 2 - 110);
  }
}
