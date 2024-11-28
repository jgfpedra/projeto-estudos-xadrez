package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

@XmlRootElement
public class Rei extends Peca {

    public Rei(){
        
    }

    public Rei(Cor cor){
        super(cor, 0);
    }

    @Override
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1},
            {1, 1},
            {-1, 1},
            {1, -1},
            {-1, -1}
        };
        for (int[] dir : direcoes) {
            int linhaAtual = origem.getLinha();
            int colunaAtual = origem.getColuna();
            while (true) {
                linhaAtual += dir[0];
                colunaAtual += dir[1];
    
                if (linhaAtual < 0 || linhaAtual >= 8 || colunaAtual < 0 || colunaAtual >= 8) {
                    break;
                }
    
                Posicao novaPosicao = new Posicao(linhaAtual, colunaAtual);
                Peca pecaNaCasa = tabuleiro.obterPeca(novaPosicao);
    
                if (pecaNaCasa == null) {
                    movimentosValidos.add(novaPosicao);
                } else {
                    if (pecaNaCasa.getCor() != this.getCor()) {
                        movimentosValidos.add(novaPosicao);
                    }
                    break;
                }
            }
        }
        return movimentosValidos;
    }    
}