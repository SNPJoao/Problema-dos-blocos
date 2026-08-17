package src.telasDoSistema;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Toolkit;

//Janela principal da aplicação. Responsável por alternar entre a tela de menu e a tela
//do mundo dos blocos, trocando o "content pane" do JFrame.
public class RoboBlocosTela extends JFrame{
  private final Dimension tamanhoTela;

  public static void main(String[] args){
    SwingUtilities.invokeLater(RoboBlocosTela::new);
  }

  public RoboBlocosTela(){
    setTitle("Problema dos blocos");

    //Obtemos o tamanho da tela pelo Toolkit (e não por getWidth()/getHeight()), pois logo
    //após a criação da janela esses valores ainda estariam zerados, já que o frame ainda
    //não foi exibido/realizado pelo sistema de janelas.
    tamanhoTela = Toolkit.getDefaultToolkit().getScreenSize();

    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    showMenu();
    setVisible(true);
  }

  //Retorna o tamanho da tela do usuário, usado pelas telas para se dimensionar corretamente
  public Dimension getTamanhoTela(){
    return tamanhoTela;
  }

  //Volta para a tela de menu
  public void showMenu(){
    setContentPane(new Menu(this));
    revalidate();
    repaint();
  }

  //Troca para a tela do mundo dos blocos
  public void startBlocksProblem(){
    MundoDosBlocos mundo = new MundoDosBlocos(this);
    setContentPane(mundo);
    revalidate();
    repaint();
    mundo.requestFocusInWindow();
  }
}
