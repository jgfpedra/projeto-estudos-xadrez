package jogador;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Tabuleiro;

public class JogadorIA extends Jogador{

    public JogadorIA(Cor cor, String nome, Image imagem){
        super(cor, nome, imagem);
    }

    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'escolherMovimento'");
    }

    @Override
    public void temPecas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'temPecas'");
    }

}
