package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.Cor;
import partida.Posicao;

public class Cavalo extends Peca {

    public Cavalo(Cor cor) {
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        // All possible L-shaped moves for the Cavalo
        int[][] direcoes = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},  // Two squares in one direction, one square perpendicular
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}   // One square in one direction, two squares perpendicular
        };
        // Iterate through all possible L-shaped moves
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];

            // Check if the move is within the bounds of the board (0 to 7)
            if (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
                movimentosValidos.add(new Posicao(novaLinha, novaColuna));
            }
        }
        return movimentosValidos;
    }
}