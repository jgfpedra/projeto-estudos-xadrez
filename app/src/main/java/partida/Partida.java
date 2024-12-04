package partida;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import jogador.Jogador;
import jogador.JogadorIA;
import jogador.JogadorOnline;
import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

public class Partida implements Cloneable {
    private int turno;
    private EstadoJogo estadoJogo;
    private boolean check;
    private boolean checkMate;
    private boolean empate;
    private boolean partidaFinalizada;
    private boolean isOnline;
    private Jogador jogadorPreto;
    private Jogador jogadorBranco;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private HistoricoMovimentos historico;
    private LocalDateTime inicioPartida;
    private LocalDateTime fimPartida;

    public Partida(Jogador jogadorBranco, Jogador jogadorPreto, HistoricoMovimentos historicoMovimentos) {
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogadorBranco = jogadorBranco;
        this.jogadorPreto = jogadorPreto;
        this.jogadorAtual = jogadorBranco;
        if(jogadorBranco instanceof JogadorOnline & jogadorPreto instanceof JogadorOnline){
            isOnline = true;
        } else {
            isOnline = false;
        }
        this.tabuleiro = new Tabuleiro();
        if(historicoMovimentos == null){
            this.historico = new HistoricoMovimentos();
        } else {
            this.historico = historicoMovimentos;
            List<Movimento> movimentos = historicoMovimentos.getMovimentos();
            for(Movimento movimento : movimentos){
                jogar(movimento);
            }
        }
    }

    public void jogar(Movimento movimento) {
        if (inicioPartida == null) {
            inicioPartida = LocalDateTime.now();
        }

        tabuleiro.aplicarMovimento(movimento);
        historico.adicionarMovimento(movimento);

        if (verificaCheckMate()) {
            System.out.println("===CHECK MATE===");
            checkMate = true;
            estadoJogo = EstadoJogo.FIM;
            fimPartida = LocalDateTime.now();
            return;
        }
        if (verificaCheck()) {
            System.out.println("===CHECK===");
            check = true;
            estadoJogo = EstadoJogo.XEQUE;
        } else {
            check = false;
            estadoJogo = EstadoJogo.EM_ANDAMENTO;
        }
        if (verificaEmpate()) {
            System.out.println("===EMPATOU===");
            empate = true;
            estadoJogo = EstadoJogo.EMPATE;
            fimPartida = LocalDateTime.now();
            return;
        }
        mudarTurno();
    }

    public void voltaTurno() {
        if (historico.temMovimentos()) {
            Movimento ultimoMovimento = historico.obterUltimoMovimento();
            tabuleiro.desfazerMovimento(ultimoMovimento);
            historico.removerUltimoMovimento();
            turno--; 
            mudarTurno(); 
        }
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public int getTurno() {
        return turno;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void mudarTurno() {
        jogadorAtual = (jogadorAtual.equals(jogadorPreto)) ? jogadorBranco : jogadorPreto;
        turno++;
    
        if (jogadorAtual instanceof JogadorIA) {
            JogadorIA jogadorIA = (JogadorIA) jogadorAtual;
            jogadorIA.escolherMovimento(this);
        }
    }

    private boolean verificaCheck() {
        Posicao posicaoRei = tabuleiro.getPosicaoRei(jogadorAtual.getCor());
        return tabuleiro.isReiEmCheck(posicaoRei, jogadorAtual.getCor());
    }

    private boolean verificaCheckMate() {
        if (verificaCheck()) {
            return tabuleiro.temMovimentosValidosParaSairDoCheck(jogadorAtual.getCor());
        }
        return false;
    }

    public EstadoJogo getEstadoJogo() {
        return estadoJogo;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public boolean isEmpate() {
        return empate;
    }

    public LocalDateTime getInicioPartida() {
        return LocalDateTime.now();
    }

    public LocalDateTime getFimPartida() {
        return fimPartida;
    }

    public long getDuracaoPartidaEmMinutos() {
        if (fimPartida != null) {
            return java.time.Duration.between(inicioPartida, fimPartida).toMinutes();
        }
        return 0;
    }

    public boolean verificaEmpate() {
        int contadorReis = 0;
        int outrasPecas = 0;

        for (List<Casa> linha : tabuleiro.getCasas()) {
            for (Casa casa : linha) {
                Peca peca = casa.getPeca();
                if (peca != null) {
                    if (peca instanceof Rei) {
                        contadorReis++;
                    } else {
                        outrasPecas++;
                    }
                }
            }
        }
        return contadorReis == 2 && outrasPecas == 0;
    }

    public boolean isJogadorBrancoIA() {
        return (jogadorBranco instanceof JogadorIA);
    }

    public HistoricoMovimentos getHistoricoMovimentos() {
        return historico;
    }

    public void terminar() {
        this.partidaFinalizada = true;
    }
    
    public boolean isFinalizada() {
        return partidaFinalizada;
    }

    public boolean getIsOnline(){
        return isOnline;
    }

    @Override
    public Partida clone() {
        try {
            Partida novaPartida = (Partida) super.clone();
            novaPartida.tabuleiro = this.tabuleiro.clone();
            return novaPartida;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Jogador getJogadorBranco(){
        return jogadorBranco;
    }

    public Jogador getJogadorPreto(){
        return jogadorPreto;
    }

    public void setJogador2(Jogador jogador2){
        this.jogadorPreto = jogador2;
    }

    public void setTabuleiro(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }

    public String getEstadoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("EstadoJogo:").append(estadoJogo.toString()).append(";");
        sb.append("Turno:").append(turno).append(";");
        sb.append("JogadorAtual:").append(jogadorAtual.getCor()).append(";");
        sb.append("PecasCapturadasBranco:");
        sb.append("PecasCapturadasBranco:");
        if (tabuleiro.getCapturadasJogadorBranco() != null && !tabuleiro.getCapturadasJogadorBranco().isEmpty()) {
            for (Peca p : tabuleiro.getCapturadasJogadorBranco()) {
                sb.append(p.getClass().getSimpleName()).append(",").append(p.getCor()).append(";");
            }
        } else {
            sb.append(";");
        }
        
        sb.append("PecasCapturadasPreto:");
        if (tabuleiro.getCapturadasJogadorPreto() != null && !tabuleiro.getCapturadasJogadorPreto().isEmpty()) {
            for (Peca p : tabuleiro.getCapturadasJogadorPreto()) {
                sb.append(p.getClass().getSimpleName()).append(",").append(p.getCor()).append(";");
            }
        } else {
            sb.append(";");
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = tabuleiro.getCasas().get(i).get(j);
                Peca peca = casa.getPeca();
                if (peca != null) {
                    sb.append(i).append(",").append(j).append(",").append(peca.getCor()).append(",").append(peca.getMovCount()).append(",").append(peca.getClass().getSimpleName()).append(";");
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }    

    public void fromEstadoCompleto(String estadoCompleto) {
        String[] partes = estadoCompleto.split(";");
        tabuleiro.limparTabuleiro();
        List<Peca> capturadasBranco = new ArrayList<>(tabuleiro.getCapturadasJogadorBranco());
        List<Peca> capturadasPreto = new ArrayList<>(tabuleiro.getCapturadasJogadorPreto());
    
        for (String parte : partes) {
            if (parte.startsWith("EstadoJogo:")) {
                estadoJogo = fromString(parte.split(":")[1]);
            } else if (parte.startsWith("Turno:")) {
                turno = Integer.parseInt(parte.split(":")[1]);
            } else if (parte.startsWith("JogadorAtual:")) {
                Cor cor = Cor.valueOf(parte.split(":")[1]);
                jogadorAtual = (cor == Cor.BRANCO) ? jogadorBranco : jogadorPreto;
            } else if (parte.startsWith("PecasCapturadasBranco:")) {
                String capturadasStr = parte.split(":").length > 1 ? parte.split(":")[1] : "";
                if (!capturadasStr.isEmpty()) {
                    String[] capturadas = capturadasStr.split(";");
                    for (String capturada : capturadas) {
                        if (!capturada.isEmpty()) {
                            String[] dados = capturada.split(",");
                            if (dados.length == 2) {
                                String tipoPeca = dados[0];
                                Cor cor = Cor.valueOf(dados[1]);
                                Peca peca = criarPeca(tipoPeca, cor);
                                capturadasBranco.add(peca);
                            }
                        }
                    }
                }
            } else if (parte.startsWith("PecasCapturadasPreto:")) {
                String capturadasStr = parte.split(":").length > 1 ? parte.split(":")[1] : "";
                if (!capturadasStr.isEmpty()) {
                    String[] capturadas = capturadasStr.split(";");
                    for (String capturada : capturadas) {
                        if (!capturada.isEmpty()) {
                            String[] dados = capturada.split(",");
                            if (dados.length == 2) {
                                String tipoPeca = dados[0];
                                Cor cor = Cor.valueOf(dados[1]);
                                Peca peca = criarPeca(tipoPeca, cor);
                                capturadasPreto.add(peca);
                            }
                        }
                    }
                }
            } else if (parte.contains(",")) {
                String[] dados = parte.split(",");
                if (dados.length == 5) {
                    int linha = Integer.parseInt(dados[0]);
                    int coluna = Integer.parseInt(dados[1]);
                    Cor cor = Cor.valueOf(dados[2]);
                    int movCount = Integer.parseInt(dados[3]);
                    String tipoPeca = dados[4];
                    Peca peca = criarPeca(tipoPeca, cor);
                    if (peca != null) {
                        peca.setMovCount(movCount);
                        tabuleiro.getCasa(new Posicao(linha, coluna)).setPeca(peca);
                    }
                }
            }
        }
    
        tabuleiro.setCapturadasJogadorBranco(capturadasBranco);
        tabuleiro.setCapturadasJogadorPreto(capturadasPreto);
    }    

    public boolean ehTurnoDoJogador(boolean isJogador2) {
        if(jogadorAtual instanceof JogadorOnline){
            if (isJogador2) {
                return jogadorAtual.equals(jogadorPreto);
            } else {
                return jogadorAtual.equals(jogadorBranco);
            }
        }
        return false;
    }

    private Peca criarPeca(String tipo, Cor cor) {
        switch (tipo) {
            case "Peao": return new Peao(cor);
            case "Torre": return new Torre(cor);
            case "Cavalo": return new Cavalo(cor);
            case "Bispo": return new Bispo(cor);
            case "Rainha": return new Rainha(cor);
            case "Rei": return new Rei(cor);
            default: return null;
        }
    }

    public static EstadoJogo fromString(String texto) {
        for (EstadoJogo estado : EstadoJogo.values()) {
            if (estado.toString().equalsIgnoreCase(texto)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de jogo inválido: " + texto);
    }
}