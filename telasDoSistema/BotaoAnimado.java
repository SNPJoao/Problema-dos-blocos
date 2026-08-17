package roboBlocos.telasDoSistema;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Botão com cantos arredondados, sombra e uma pequena animação de "zoom" ao passar o mouse por cima.
//Usado tanto no menu principal quanto na tela do mundo dos blocos, para manter a mesma identidade visual.
public class BotaoAnimado extends JButton{
  private float escala = 1f;
  private float escalaAlvo = 1f;
  private final Color cor;

  public BotaoAnimado(String texto, Color cor){
    super(texto);
    this.cor = cor;
    setFocusPainted(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setForeground(Color.WHITE);
    setFont(new Font("Arial", Font.BOLD, 20));
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    addMouseListener(new MouseAdapter(){
      public void mouseEntered(MouseEvent e){
        escalaAlvo = 1.1f;
      }
      public void mouseExited(MouseEvent e){
        escalaAlvo = 1f;
      }
    });
  }

  //Aproxima a escala atual da escala alvo a cada frame (efeito suave de "zoom")
  public void atualizar(){
    escala += (escalaAlvo - escala) * 0.2f;
  }

  @Override
  protected void paintComponent(Graphics g){
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int w = getWidth();
    int h = getHeight();

    g2d.translate(w / 2.0, h / 2.0);
    g2d.scale(escala, escala);
    g2d.translate(-w / 2.0, -h / 2.0);

    g2d.setColor(new Color(0, 0, 0, 90));
    g2d.fillRoundRect(4, 5, w - 4, h - 5, 18, 18);

    g2d.setColor(isEnabled() ? cor : cor.darker().darker());
    g2d.fillRoundRect(0, 0, w - 4, h - 5, 18, 18);

    g2d.setFont(getFont());
    FontMetrics fm = g2d.getFontMetrics();
    int textoX = (w - 4 - fm.stringWidth(getText())) / 2;
    int textoY = (h - 5 + fm.getAscent()) / 2 - 4;
    g2d.setColor(Color.WHITE);
    g2d.drawString(getText(), textoX, textoY);
    g2d.dispose();
  }
}
