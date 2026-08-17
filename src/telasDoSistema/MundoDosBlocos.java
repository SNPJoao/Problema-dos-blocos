package src.telasDoSistema;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import src.funcionamentoDoSistema.BActions;
import src.funcionamentoDoSistema.Blocos;

//Tela em que o usuário monta uma configuração de blocos arrastando-os com o mouse e, em
//seguida, pede para o robô resolver e assistir à animação da solução encontrada pela busca.
public class MundoDosBlocos extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
  private final RoboBlocosTela frame;
  private final Cenario cenario;
  private final Timer timer;

  private final List<BotaoAnimado> botoes = new ArrayList<>();
  private BotaoAnimado botaoIniciarRobo;
  private BotaoAnimado botaoResetar;
  private BotaoAnimado botaoSair;

  //Estado do arrasto do mouse
  private BlocoVisual blocoArrastado;
  private ColunaBlocos colunaOrigemArrasto;
  private double offsetX, offsetY;

  //Estado da animação do robô
  private List<BActions> filaDeAcoes = new ArrayList<>();
  private int indiceAcaoAtual = 0;
  private boolean animandoRobo = false;
  private boolean interacaoBloqueada = false;
  private int contadorDePausa = 0;
  private static final int PAUSA_ENTRE_ACOES_EM_FRAMES = 22; //~350ms a 16ms por frame

  private String mensagemStatus = "Arraste os blocos para montar uma configuração e clique em \"Iniciar robô\".";

  public MundoDosBlocos(RoboBlocosTela frame){
    this.frame = frame;
    Dimension tela = frame.getTamanhoTela();
    setPreferredSize(tela);
    setBackground(Color.WHITE);
    setLayout(null);

    cenario = new Cenario(tela.width, tela.height);

    criarBotoes(tela);

    setFocusable(true);
    addMouseListener(this);
    addMouseMotionListener(this);

    timer = new Timer(16, this);
    timer.start();
  }

  //Cria os 3 botões da tela: iniciar robô, resetar e sair
  private void criarBotoes(Dimension tela){
    botaoIniciarRobo = new BotaoAnimado("Iniciar robô", new Color(46, 204, 113));
    botaoIniciarRobo.setBounds(40, 30, 190, 50);
    botaoIniciarRobo.addActionListener(e -> iniciarRobo());

    botaoResetar = new BotaoAnimado("Resetar", new Color(241, 196, 15));
    botaoResetar.setBounds(250, 30, 160, 50);
    botaoResetar.addActionListener(e -> resetar());

    botaoSair = new BotaoAnimado("Sair", new Color(231, 76, 60));
    botaoSair.setBounds(tela.width - 190, 30, 150, 50);
    botaoSair.addActionListener(e -> frame.showMenu());

    botoes.add(botaoIniciarRobo);
    botoes.add(botaoResetar);
    botoes.add(botaoSair);
    for(BotaoAnimado b : botoes)
      add(b);
  }

  //Converte a configuração visual atual (colunas de blocos) para o modelo de dados do robô
  //(List<Stack<Blocos>>), na mesma estrutura que RunDemoBlocos.java já utiliza.
  private List<Stack<Blocos>> converterParaModeloLogico(){
    List<Stack<Blocos>> pilhas = new ArrayList<>();
    for(ColunaBlocos coluna : cenario.getColunas()){
      if(coluna.isEmpty())
        continue;
      Stack<Blocos> pilha = new Stack<>();
      List<BlocoVisual> blocosDaColuna = coluna.getBlocos();
      for(int i = 0; i < blocosDaColuna.size(); i++){
        boolean temAlgoAcima = i < blocosDaColuna.size() - 1;
        pilha.push(new Blocos(blocosDaColuna.get(i).getNome(), temAlgoAcima));
      }
      pilhas.add(pilha);
    }
    return pilhas;
  }

  //Dispara a busca (em segundo plano, para não travar a interface) e, ao concluir, inicia a animação
  private void iniciarRobo(){
    if(interacaoBloqueada)
      return;

    List<Stack<Blocos>> pilhasIniciais = converterParaModeloLogico();

    interacaoBloqueada = true;
    botaoIniciarRobo.setEnabled(false);
    botaoResetar.setEnabled(false);
    mensagemStatus = "Calculando o plano de ações do robô...";

    SwingWorker<List<BActions>, Void> worker = new SwingWorker<>(){
      @Override
      protected List<BActions> doInBackground(){
        return RoboSolver.resolver(pilhasIniciais);
      }

      @Override
      protected void done(){
        List<BActions> resultado;
        try{
          resultado = get();
        } catch(Exception ex){
          resultado = new ArrayList<>();
        }
        filaDeAcoes = resultado;
        indiceAcaoAtual = 0;
        contadorDePausa = 0;

        if(filaDeAcoes.isEmpty()){
          animandoRobo = false;
          interacaoBloqueada = false;
          botaoIniciarRobo.setEnabled(true);
          botaoResetar.setEnabled(true);
          mensagemStatus = "Nenhuma ação necessária (ou nenhuma solução encontrada).";
          JOptionPane.showMessageDialog(MundoDosBlocos.this,
              "O robô não encontrou ações a executar.\nOs blocos já podem estar na configuração "
              + "objetivo, ou não há solução alcançável a partir do arranjo atual.",
              "Busca concluída", JOptionPane.INFORMATION_MESSAGE);
        } else{
          animandoRobo = true;
          mensagemStatus = "Executando o plano encontrado pela busca...";
        }
      }
    };
    worker.execute();
  }

  //Restaura a configuração inicial (todos os blocos em fila sobre a mesa)
  private void resetar(){
    animandoRobo = false;
    interacaoBloqueada = false;
    filaDeAcoes = new ArrayList<>();
    indiceAcaoAtual = 0;
    contadorDePausa = 0;
    cenario.gerarConfiguracaoInicial();
    botaoIniciarRobo.setEnabled(true);
    botaoResetar.setEnabled(true);
    mensagemStatus = "Arraste os blocos para montar uma configuração e clique em \"Iniciar robô\".";
  }

  //Verifica se todos os blocos do cenário já chegaram na posição alvo (animação parada)
  private boolean todosBlocosChegaram(){
    for(ColunaBlocos coluna : cenario.getColunas())
      for(BlocoVisual bloco : coluna.getBlocos())
        if(!bloco.chegou())
          return false;
    return true;
  }

  //Laço principal (chamado a cada 16ms pelo Timer): atualiza animações e avança a fila de ações do robô
  @Override
  public void actionPerformed(ActionEvent e){
    for(ColunaBlocos coluna : cenario.getColunas())
      for(BlocoVisual bloco : coluna.getBlocos())
        bloco.atualizar();

    for(BotaoAnimado b : botoes)
      b.atualizar();

    if(animandoRobo && todosBlocosChegaram()){
      contadorDePausa++;
      if(contadorDePausa >= PAUSA_ENTRE_ACOES_EM_FRAMES){
        contadorDePausa = 0;
        if(indiceAcaoAtual < filaDeAcoes.size()){
          String textoAcao = filaDeAcoes.get(indiceAcaoAtual).toString();
          cenario.aplicarAcao(textoAcao);
          indiceAcaoAtual++;
          mensagemStatus = "Executando ação " + indiceAcaoAtual + " de " + filaDeAcoes.size()
              + ": " + textoAcao.trim();
        } else{
          animandoRobo = false;
          interacaoBloqueada = false;
          botaoIniciarRobo.setEnabled(true);
          botaoResetar.setEnabled(true);
          mensagemStatus = "Concluído! Os blocos foram organizados pelo robô.";
        }
      }
    }
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    cenario.draw(g2d);

    //Desenha por cima o bloco que está sendo arrastado no momento (ele fica fora das colunas)
    if(blocoArrastado != null)
      blocoArrastado.draw(g2d);

    desenharStatus(g2d);
  }

  //Desenha a mensagem de status na parte inferior da tela
  private void desenharStatus(Graphics2D g2d){
    g2d.setFont(new Font("Arial", Font.PLAIN, 18));
    g2d.setColor(new Color(60, 60, 60));
    FontMetrics fm = g2d.getFontMetrics();
    int x = (getWidth() - fm.stringWidth(mensagemStatus)) / 2;
    int y = getHeight() - 30;
    g2d.drawString(mensagemStatus, x, y);
  }

  // ---------------- Eventos de mouse: arrastar e soltar blocos ----------------

  @Override
  public void mousePressed(MouseEvent e){
    if(interacaoBloqueada)
      return;

    BlocoVisual bloco = cenario.blocoNoPonto(e.getX(), e.getY());
    if(bloco == null)
      return;

    ColunaBlocos coluna = cenario.colunaDoBloco(bloco);
    if(coluna == null || coluna.topo() != bloco)
      return; //Só é possível pegar o bloco livre (o do topo da pilha)

    blocoArrastado = bloco;
    colunaOrigemArrasto = coluna;
    offsetX = e.getX() - bloco.getX();
    offsetY = e.getY() - bloco.getY();

    coluna.remover(bloco, cenario.getAlturaDaMesa());
  }

  @Override
  public void mouseDragged(MouseEvent e){
    if(blocoArrastado == null)
      return;
    double novoX = e.getX() - offsetX;
    double novoY = e.getY() - offsetY;
    blocoArrastado.definirPosicaoImediata(novoX, novoY);
    repaint();
  }

  @Override
  public void mouseReleased(MouseEvent e){
    if(blocoArrastado == null)
      return;

    ColunaBlocos colunaDestino = cenario.colunaProximaDoTopo(e.getX(), blocoArrastado);
    if(colunaDestino != null){
      colunaDestino.empilhar(blocoArrastado, cenario.getAlturaDaMesa());
    } else{
      double xSolto = e.getX() - BlocoVisual.TAMANHO / 2.0;
      ColunaBlocos novaColuna = cenario.novaColuna(xSolto);
      novaColuna.empilhar(blocoArrastado, cenario.getAlturaDaMesa());
    }

    cenario.removerColunaSeVazia(colunaOrigemArrasto);

    blocoArrastado = null;
    colunaOrigemArrasto = null;
  }

  @Override
  public void mouseClicked(MouseEvent e){
  }

  @Override
  public void mouseEntered(MouseEvent e){
  }

  @Override
  public void mouseExited(MouseEvent e){
  }

  @Override
  public void mouseMoved(MouseEvent e){
  }
}
