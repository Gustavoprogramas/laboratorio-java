package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    final int largura = 1280;
    final int altura = 720;

    int mundoLargura = 10000;
    int mundoAltura = 5000;

    int cameraX = 0;
    int cameraY = 0;

    ArrayList<Fase> fases = new ArrayList<>();

    int faseAtual = 0;

    Fase fase;

    final int HITBOX_OFFSET_X = 10;
    final int HITBOX_OFFSET_Y = 1;

    final int PLAYER_W = 88;
    final int PLAYER_H = 122;
    final int HITBOX_W = PLAYER_W - 20;
    final int HITBOX_H = PLAYER_H - 20;
    final int INTERVALO_TIRO = 90;
    final int VELOCIDADE_PROJETIL = 7;
    final int PROJETIL_W = 32;
    final int PROJETIL_H = 32;
    final int DISTANCIA_MAX_PROJETIL = 400;

    int playerX = 100;
    int playerY = 614
            - HITBOX_OFFSET_Y
            - HITBOX_H;

    int velocidadeX = 0;
    int velocidadeY = 0;

    final int VELOCIDADE = 5;
    final int FORCA_PULO = -20;
    final int GRAVIDADE = 1;

    Rectangle getPlayerHitbox() {

        return new Rectangle(
                playerX + HITBOX_OFFSET_X,
                playerY + HITBOX_OFFSET_Y,
                HITBOX_W,
                HITBOX_H);
    }

    boolean direita = false;
    boolean esquerda = false;

    boolean noChao = false;

    boolean olhandoDireita = true;

    Thread gameThread;

    BufferedImage player_idle;
    BufferedImage player_walking1;
    BufferedImage player_walking2;
    BufferedImage player_jumping;
    Clip somPulo;
    Clip somCanhao;
    Clip somEspinho;
    Clip somBatata;
    Clip somCanhaom;
    Clip somCaindo;

    BufferedImage fundo;
    BufferedImage plataforma;
    BufferedImage batata;
    BufferedImage espinho;
    BufferedImage canhao;
    BufferedImage projetilImagem;
    ArrayList<Projetil> projeteis = new ArrayList<>();

    int frameAnimacao = 0;
    int contadorAnimacao = 0;

    final int VELOCIDADE_ANIMACAO = 9;

    Rectangle batataHitbox = new Rectangle(
            860,
            230,
            44,
            44);

    boolean batataColetada = false;

    Clip carregarSom(String caminho) {

        try {

            AudioInputStream audio = AudioSystem.getAudioInputStream(
                    getClass().getResource(caminho));

            Clip clip = AudioSystem.getClip();

            clip.open(audio);

            return clip;

        } catch (Exception e) {

            System.out.println(
                    "Erro carregando som: " + caminho);

            e.printStackTrace();

            return null;
        }
    }

    void tocarSom(Clip som) {

        if (som == null) {
            return;
        }

        som.stop();

        som.setFramePosition(0);

        som.start();
    }

    void carregarSons() {

        somPulo = carregarSom(
                "/assets/sons/pulo.wav");

        somCanhao = carregarSom(
                "/assets/sons/canhao.wav");

        somEspinho = carregarSom(
                "/assets/sons/espinho.wav");

        somBatata = carregarSom(
                "/assets/sons/batata.wav");
        somCanhaom = carregarSom("/assets/sons/canhaomorte.wav");
        somCaindo = carregarSom("/assets/sons/caindo.wav");
    }

    void atualizarCamera() {

        cameraX = playerX + PLAYER_W / 2 - largura / 2;
        cameraY = playerY + PLAYER_H / 2 - altura / 2;

        if (cameraX < 0) {
            cameraX = 0;
        }

        if (cameraY < 0) {
            cameraY = 0;
        }

        if (cameraX > mundoLargura - largura) {
            cameraX = mundoLargura - largura;
        }

        if (cameraY > mundoAltura - altura) {
            cameraY = mundoAltura - altura;
        }
    }

    void verificarEspinhos() {

        Rectangle playerHitbox = getPlayerHitbox();

        for (Rectangle esp : fase.espinhos) {

            Rectangle hitboxEspinho = new Rectangle(
                    esp.x + 10,
                    esp.y + 5,
                    Math.max(1, esp.width - 20),
                    Math.max(1, esp.height - 5));

            if (playerHitbox.intersects(hitboxEspinho)) {

                tocarSom(somEspinho);
                reiniciarFase();

                return;
            }
        }
    }

    void dispararCanhao(Canhao c) {
        double tiroX;
        if (c.direcao == 1) {
            tiroX = c.x + c.largura - 10;

        } else {
            tiroX = c.x - PROJETIL_W + 10;

        }
        double tiroY = c.y
                + c.altura / 2.0
                - PROJETIL_H / 2.0;
        projeteis.add(
                new Projetil(tiroX, tiroY, PROJETIL_H, PROJETIL_W, VELOCIDADE_PROJETIL * c.direcao, 0));
        Canhao maisPerto = pegarCanhaoMaisPerto();

        if (c == maisPerto) {

            tocarSom(
                    somCanhao);
        }

    }

    void atualizarProjeteis() {

        for (int i = projeteis.size() - 1; i >= 0; i--) {
            Projetil p = projeteis.get(i);
            p.atualizar();
            double distanciaPercorrida = Math.abs(p.x - p.xInicial);
            if (distanciaPercorrida >= DISTANCIA_MAX_PROJETIL) {
                projeteis.remove(i);
                continue;
            }

            if (p.x < -100
                    ||
                    p.x > mundoLargura + 100
                    ||
                    p.y < -100
                    ||
                    p.y > mundoAltura + 100) {
                projeteis.remove(i);
                continue;
            }
            boolean bateu = false;
            for (Rectangle plat : fase.plataformas) {
                if (p.getHitbox().intersects(plat)) {
                    bateu = true;
                    break;
                }
            }
            if (bateu) {
                projeteis.remove(i);
                continue;
            }

            if (p.getHitbox().intersects(getPlayerHitbox())) {
                tocarSom(somCanhaom);
                reiniciarFase();
                return;
            }
        }
    }

    void atualizarCanhoes() {
        if (fase == null) {
            return;
        }
        for (Canhao c : fase.canhoes) {
            c.contadorTiro++;
            if (c.contadorTiro >= INTERVALO_TIRO) {
                c.contadorTiro = 0;
                dispararCanhao(c);
            }
        }
    }

    public int getQuantidadeFases() {
        return fases.size();
    }

    void reiniciarFase() {
        carregarFase(faseAtual);
    }

    void verificarBatata() {

        if (batataColetada) {
            return;
        }

        Rectangle playerHitbox = getPlayerHitbox();

        if (playerHitbox.intersects(
                batataHitbox)) {
            tocarSom(somBatata);
            batataColetada = true;

            proximaFase();
        }
    }

    Canhao pegarCanhaoMaisPerto() {
        if (fase == null
                ||
                fase.canhoes.isEmpty()) {

            return null;
        }

        Canhao maisPerto = null;

        double menorDistancia = Double.MAX_VALUE;

        double playerCentroX = playerX + PLAYER_W / 2.0;

        double playerCentroY = playerY + PLAYER_H / 2.0;

        for (Canhao c : fase.canhoes) {

            double canhaoCentroX = c.x + c.largura / 2.0;

            double canhaoCentroY = c.y + c.altura / 2.0;

            double dx = canhaoCentroX
                    - playerCentroX;

            double dy = canhaoCentroY
                    - playerCentroY;

            double distancia = Math.sqrt(
                    dx * dx
                            +
                            dy * dy);

            if (distancia < menorDistancia) {

                menorDistancia = distancia;

                maisPerto = c;
            }
        }

        return maisPerto;
    }

    void proximaFase() {

        int proxima = faseAtual + 1;

        if (proxima < fases.size()) {

            carregarFase(proxima);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Você terminou todas as fases!");

            carregarFase(0);
        }
    }

    void carregarFase(int numero) {
        projeteis.clear();
        faseAtual = numero;
        fase = fases.get(faseAtual);

        playerX = fase.playerX;
        playerY = fase.playerY;

        mundoLargura = fase.mundoLargura;
        mundoAltura = fase.mundoAltura;

        velocidadeX = 0;
        velocidadeY = 0;

        direita = false;
        esquerda = false;

        noChao = false;

        batataColetada = false;

        batataHitbox = new Rectangle(
                fase.batataX,
                fase.batataY,
                64,
                64);
    }

    public void iniciarFase(int numero) {

        carregarFase(numero);

        requestFocusInWindow();
    }

    public GamePanel() {

        setPreferredSize(
                new Dimension(largura, altura));

        setFocusable(true);

        addKeyListener(this);
        criarFases();
        carregarFase(0);

        carregarImagens();
        carregarSons();
    }

    void carregarImagens() {

        try {

            player_idle = ImageIO.read(
                    getClass().getResource("/assets/steph_frente.png"));

            player_walking1 = ImageIO.read(
                    getClass().getResource("/assets/steph_lado_1.png"));

            player_walking2 = ImageIO.read(
                    getClass().getResource("/assets/steph_lado_2.png"));

            player_jumping = ImageIO.read(
                    getClass().getResource("/assets/steph_pulando.png"));

            fundo = ImageIO.read(
                    getClass().getResource("/assets/fundo.png"));

            plataforma = ImageIO.read(
                    getClass().getResource("/assets/plataforma.png"));

            batata = ImageIO.read(
                    getClass().getResource("/assets/batata.png"));
            espinho = ImageIO.read(
                    getClass().getResource("/assets/espinhos.png"));
            canhao = ImageIO.read(
                    getClass().getResource("/assets/canhao.png"));
            projetilImagem = ImageIO.read(
                    getClass().getResource("/assets/projetil.png"));

        } catch (Exception e) {

            System.out.println("Erro ao carregar imagens!");

            e.printStackTrace();
        }
    }

    void criarFases() {

        // ==================================
        // FASE 1
        // ==================================

        Fase fase1 = new Fase(
                100,
                400,
                850,
                220,
                1280,
                720);

        fase1.adicionarPlataforma(0, 614, 1280, 106);
        fase1.adicionarPlataforma(400, 450, 250, 40);
        fase1.adicionarPlataforma(750, 300, 250, 40);

        fases.add(fase1);

        // ==================================
        // FASE 2
        // ==================================

        Fase fase2 = new Fase(
                50,
                400,
                1100,
                100,
                1280,
                720);

        fase2.adicionarPlataforma(0, 614, 1280, 106);
        fase2.adicionarPlataforma(200, 500, 200, 40);
        fase2.adicionarPlataforma(500, 400, 180, 40);
        fase2.adicionarPlataforma(750, 300, 180, 40);
        fase2.adicionarPlataforma(1000, 180, 200, 40);

        fases.add(fase2);

        // ==================================
        // FASE 3
        // ==================================

        Fase fase3 = new Fase(
                100,
                400,
                1000,
                130,
                1280,
                720);

        fase3.adicionarPlataforma(0, 614, 1280, 106);
        fase3.adicionarPlataforma(230, 520, 150, 40);
        fase3.adicionarPlataforma(400, 420, 150, 40);
        fase3.adicionarPlataforma(650, 350, 150, 40);
        fase3.adicionarPlataforma(850, 250, 150, 40);
        fase3.adicionarPlataforma(1000, 180, 180, 40);

        fases.add(fase3);

        // ==================================
        // FASE 4
        // ==================================

        Fase fase4 = new Fase(
                90,
                680,
                1080,
                310,
                1280,
                890);

        fase4.adicionarPlataforma(290, 670, 250, 40);
        fase4.adicionarPlataforma(670, 330, 250, 40);
        fase4.adicionarPlataforma(640, 570, 250, 40);
        fase4.adicionarPlataforma(0, 820, 250, 40);
        fase4.adicionarPlataforma(320, 420, 250, 40);

        fases.add(fase4);

        // ==================================
        // FASE 5
        // ==================================

        Fase fase5 = new Fase(
                100,
                1100,
                2860,
                170,
                3200,
                1400);

        fase5.adicionarPlataforma(0, 1250, 600, 100);
        fase5.adicionarPlataforma(650, 1120, 220, 40);
        fase5.adicionarPlataforma(950, 990, 220, 40);
        fase5.adicionarPlataforma(1250, 860, 220, 40);
        fase5.adicionarPlataforma(1550, 730, 220, 40);
        fase5.adicionarPlataforma(1850, 600, 220, 40);
        fase5.adicionarPlataforma(2150, 470, 220, 40);
        fase5.adicionarPlataforma(2450, 340, 220, 40);
        fase5.adicionarPlataforma(2750, 220, 300, 40);

        fase5.adicionarEspinho(420, 1260, 100, 30);
        fase5.adicionarEspinho(1300, 850, 90, 30);
        fase5.adicionarEspinho(2200, 460, 90, 30);

        fases.add(fase5);

        // ==================================
        // FASE 6
        // ==================================

        Fase fase6 = new Fase(
                80,
                928,
                2490,
                40,
                2600,
                1200);

        fase6.adicionarPlataforma(0, 1050, 560, 100);
        fase6.adicionarPlataforma(620, 940, 300, 40);
        fase6.adicionarPlataforma(1000, 830, 300, 40);
        fase6.adicionarPlataforma(1380, 720, 300, 40);
        fase6.adicionarPlataforma(1760, 610, 300, 40);
        fase6.adicionarPlataforma(2110, 500, 360, 40);
        fase6.adicionarPlataforma(2250, 360, 300, 40);
        fase6.adicionarPlataforma(2240, 190, 320, 40);

        fase6.adicionarEspinho(390, 1060, 100, 30);
        fase6.adicionarEspinho(1110, 820, 90, 30);
        fase6.adicionarEspinho(2320, 350, 90, 30);
        fase6.adicionarEspinho(1890, 600, 90, 30);

        fase6.adicionarCanhao(760, 880, 110, 80, -1);
        fase6.adicionarCanhao(1440, 660, 110, 80, 1);
        fase6.adicionarCanhao(2180, 440, 110, 80, -1);

        fases.add(fase6);

        // ==================================
        // FASE 7
        // ==================================

        Fase fase7 = new Fase(
                120,
                470,
                40,
                180,
                3080,
                720);

        fase7.adicionarPlataforma(70, 6150, 220, 40);
        fase7.adicionarPlataforma(40, 630, 220, 40);
        fase7.adicionarPlataforma(430, 570, 220, 40);
        fase7.adicionarPlataforma(760, 670, 220, 40);
        fase7.adicionarPlataforma(1040, 570, 220, 40);
        fase7.adicionarPlataforma(1340, 670, 220, 40);
        fase7.adicionarPlataforma(1640, 570, 220, 40);
        fase7.adicionarPlataforma(2000, 580, 220, 40);
        fase7.adicionarPlataforma(2140, 420, 220, 40);
        fase7.adicionarPlataforma(1800, 300, 220, 40);
        fase7.adicionarPlataforma(1480, 290, 220, 40);
        fase7.adicionarPlataforma(1540, 150, 220, 40);
        fase7.adicionarPlataforma(2230, 140, 220, 40);
        fase7.adicionarPlataforma(1210, 120, 220, 40);
        fase7.adicionarPlataforma(910, 260, 220, 40);
        fase7.adicionarPlataforma(580, 300, 220, 40);
        fase7.adicionarPlataforma(310, 180, 220, 40);
        fase7.adicionarPlataforma(40, 330, 220, 40);

        fase7.adicionarCanhao(440, 510, 110, 80, 1);
        fase7.adicionarCanhao(1060, 510, 110, 80, -1);
        fase7.adicionarCanhao(1160, 510, 110, 80, 1);
        fase7.adicionarCanhao(1650, 510, 110, 80, -1);
        fase7.adicionarCanhao(1750, 510, 110, 80, 1);
        fase7.adicionarCanhao(2240, 360, 110, 80, -1);
        fase7.adicionarCanhao(2340, 80, 110, 80, -1);
        fase7.adicionarCanhao(1030, 200, 110, 80, -1);

        fases.add(fase7);

        // ==================================
        // FASE 8
        // ==================================

        Fase fase8 = new Fase(
                150,
                1800,
                990,
                30,
                2000,
                2000);

        fase8.adicionarPlataforma(740, 1730, 220, 40);
        fase8.adicionarPlataforma(910, 1730, 220, 40);
        fase8.adicionarPlataforma(1220, 1580, 220, 40);
        fase8.adicionarPlataforma(1390, 1460, 220, 40);
        fase8.adicionarPlataforma(1120, 1320, 220, 40);
        fase8.adicionarPlataforma(680, 1320, 220, 40);
        fase8.adicionarPlataforma(800, 1320, 220, 40);
        fase8.adicionarPlataforma(1240, 1580, 220, 40);
        fase8.adicionarPlataforma(430, 1200, 220, 40);
        fase8.adicionarPlataforma(660, 1060, 220, 40);
        fase8.adicionarPlataforma(410, 930, 220, 40);
        fase8.adicionarPlataforma(750, 830, 220, 40);
        fase8.adicionarPlataforma(860, 830, 220, 40);
        fase8.adicionarPlataforma(1010, 830, 220, 40);
        fase8.adicionarPlataforma(1190, 670, 220, 40);
        fase8.adicionarPlataforma(1420, 510, 220, 40);
        fase8.adicionarPlataforma(1150, 390, 220, 40);
        fase8.adicionarPlataforma(1350, 240, 220, 40);
        fase8.adicionarPlataforma(1130, 120, 220, 40);
        fase8.adicionarPlataforma(100, 1910, 220, 40);
        fase8.adicionarPlataforma(250, 1910, 220, 40);
        fase8.adicionarPlataforma(370, 1910, 220, 40);
        fase8.adicionarPlataforma(500, 1910, 220, 40);
        fase8.adicionarPlataforma(590, 1910, 220, 40);

        fase8.adicionarEspinho(330, 1900, 100, 30);
        fase8.adicionarEspinho(1350, 1570, 100, 30);
        fase8.adicionarEspinho(1460, 1450, 100, 30);
        fase8.adicionarEspinho(1150, 1310, 100, 30);
        fase8.adicionarEspinho(510, 1190, 100, 30);
        fase8.adicionarEspinho(420, 920, 100, 30);
        fase8.adicionarEspinho(770, 1050, 100, 30);
        fase8.adicionarEspinho(870, 820, 100, 30);
        fase8.adicionarEspinho(1530, 500, 100, 30);
        fase8.adicionarEspinho(1210, 380, 100, 30);
        fase8.adicionarEspinho(1160, 380, 100, 30);
        fase8.adicionarEspinho(1470, 230, 100, 30);

        fase8.adicionarCanhao(580, 1850, 110, 80, -1);
        fase8.adicionarCanhao(750, 1670, 110, 80, 1);
        fase8.adicionarCanhao(1230, 1520, 110, 80, 1);
        fase8.adicionarCanhao(810, 1260, 110, 80, -1);
        fase8.adicionarCanhao(970, 770, 110, 80, 1);
        fase8.adicionarCanhao(1250, 60, 110, 80, -1);

        fases.add(fase8);

    }

    public void startGame() {

        gameThread = new Thread(this);

        gameThread.start();
    }

    @Override
    public void run() {

        while (gameThread != null) {

            atualizar();

            repaint();

            try {

                Thread.sleep(16);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    void atualizar() {

        atualizarMovimentoHorizontal();

        atualizarMovimentoVertical();

        limitarMundo();

        verificarEspinhos();

        verificarBatata();

        atualizarAnimacao();

        atualizarCamera();
        atualizarCanhoes();
        atualizarProjeteis();
    }

    void atualizarMovimentoHorizontal() {

        if (direita && !esquerda) {

            velocidadeX = VELOCIDADE;

            olhandoDireita = true;

        } else if (esquerda && !direita) {

            velocidadeX = -VELOCIDADE;

            olhandoDireita = false;

        } else {

            velocidadeX = 0;
        }

        playerX += velocidadeX;
    }

    void atualizarMovimentoVertical() {
        if (fase == null) {
            return;
        }

        int playerYAnterior = playerY;

        velocidadeY += GRAVIDADE;

        playerY += velocidadeY;

        noChao = false;

        for (Rectangle plat : fase.plataformas) {

            verificarColisaoPlataforma(
                    plat,
                    playerYAnterior);

            if (noChao) {
                break;
            }
        }

    }

    void verificarColisaoPlataforma(
            Rectangle plat,
            int playerYAnterior) {

        if (velocidadeY < 0) {
            return;
        }

        int peAnterior = playerYAnterior
                + HITBOX_OFFSET_Y
                + HITBOX_H;

        int peAtual = playerY
                + HITBOX_OFFSET_Y
                + HITBOX_H;

        int esquerdaPlayer = playerX + HITBOX_OFFSET_X;

        int direitaPlayer = esquerdaPlayer + HITBOX_W;

        boolean dentroHorizontalmente =

                direitaPlayer > plat.x
                        &&
                        esquerdaPlayer < plat.x + plat.width;

        boolean cruzouTopo =

                peAnterior <= plat.y
                        &&
                        peAtual >= plat.y;

        if (dentroHorizontalmente
                &&
                cruzouTopo) {

            /*
             * Coloca os pés exatamente
             * no topo da plataforma.
             */

            playerY = plat.y
                    - HITBOX_OFFSET_Y
                    - HITBOX_H;

            velocidadeY = 0;

            noChao = true;
        }
    }

    void atualizarAnimacao() {

        /*
         * Não precisamos animar caminhada enquanto
         * estamos pulando, porque usamos
         * steph_pulando.png.
         */

        if (noChao
                &&
                (direita || esquerda)) {

            contadorAnimacao++;

            if (contadorAnimacao >= VELOCIDADE_ANIMACAO) {

                contadorAnimacao = 0;

                if (frameAnimacao == 0) {

                    frameAnimacao = 1;

                } else {

                    frameAnimacao = 0;
                }
            }

        } else {

            contadorAnimacao = 0;

            frameAnimacao = 0;
        }
    }

    void limitarMundo() {

        if (playerX < 0) {
            playerX = 0;
        }

        if (playerX + PLAYER_W > mundoLargura) {
            playerX = mundoLargura - PLAYER_W;
        }

        if (playerY < 0) {
            playerY = 0;
        }

        if (playerY > mundoAltura + 200) {
            tocarSom(somCaindo);
            reiniciarFase();
        }
    }

    void limitarTela() {

        if (playerX < 0) {

            playerX = 0;
        }

        if (playerX + PLAYER_W > largura) {

            playerX = largura - PLAYER_W;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(
                fundo,
                0,
                0,
                largura,
                altura,
                null);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24));

        g.setColor(Color.WHITE);

        g.drawString(
                "FASE " + (faseAtual + 1),
                20,
                35);
        if (fase == null) {
            return;
        }

        for (Rectangle plat : fase.plataformas) {

            g.drawImage(
                    plataforma,
                    plat.x - cameraX,
                    plat.y - cameraY,
                    plat.width,
                    plat.height,
                    null);
            for (Rectangle esp : fase.espinhos) {

                g.drawImage(
                        espinho,

                        esp.x - cameraX,
                        esp.y - cameraY,

                        esp.width,
                        esp.height,

                        null);
            }

            for (Canhao c : fase.canhoes) {
                if (c.direcao == 1) {
                    g.drawImage(canhao, c.x - cameraX, c.y - cameraY, c.largura, c.altura, null);

                } else {
                    g.drawImage(canhao, c.x - cameraX + c.largura, c.y - cameraY, -c.largura, c.altura, null);
                }
            }

            for (Projetil p : projeteis) {
                g.drawImage(projetilImagem, (int) p.x - cameraX, (int) p.y - cameraY, p.largura, p.altura, null);
            }
        }

        if (!batataColetada) {

            g.drawImage(
                    batata,
                    batataHitbox.x - cameraX,
                    batataHitbox.y - cameraY,
                    batataHitbox.width,
                    batataHitbox.height,
                    null);
        }

        BufferedImage imagemAtual;

        /*
         * PRIORIDADE 1:
         *
         * Se estiver no ar, sempre mostra
         * steph_pulando.png.
         */

        if (!noChao) {

            imagemAtual = player_jumping;

            /*
             * PRIORIDADE 2:
             *
             * Se estiver andando no chão,
             * anima os frames.
             */

        } else if (direita || esquerda) {

            if (frameAnimacao == 0) {

                imagemAtual = player_walking1;

            } else {

                imagemAtual = player_walking2;
            }

            /*
             * PRIORIDADE 3:
             *
             * Parada.
             */

        } else {

            imagemAtual = player_idle;
        }

        /*
         * O sprite parado é frontal.
         *
         * Então não precisamos espelhá-lo.
         */

        if (noChao
                &&
                !direita
                &&
                !esquerda) {

            g.drawImage(
                    imagemAtual,
                    playerX - cameraX,
                    playerY - cameraY,
                    PLAYER_W,
                    PLAYER_H,
                    null);

        } else {
            if (olhandoDireita) {

                g.drawImage(
                        imagemAtual,

                        playerX - cameraX,
                        playerY - cameraY,

                        PLAYER_W,
                        PLAYER_H,

                        null);

            } else {

                g.drawImage(
                        imagemAtual,

                        playerX - cameraX + PLAYER_W,
                        playerY - cameraY,

                        -PLAYER_W,
                        PLAYER_H,

                        null);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_A) {

            esquerda = true;
        }

        if (tecla == KeyEvent.VK_D) {

            direita = true;
        }

        if (tecla == KeyEvent.VK_SPACE
                &&
                noChao) {

            velocidadeY = FORCA_PULO;

            noChao = false;
            tocarSom(somPulo);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_A) {

            esquerda = false;
        }

        if (tecla == KeyEvent.VK_D) {

            direita = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}