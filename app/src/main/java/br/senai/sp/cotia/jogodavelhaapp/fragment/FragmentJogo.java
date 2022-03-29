package br.senai.sp.cotia.jogodavelhaapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

import br.senai.sp.cotia.jogodavelhaapp.R;
import br.senai.sp.cotia.jogodavelhaapp.databinding.FragmentJogoBinding;


public class FragmentJogo extends Fragment {

    // variavel para acessar os elementos na view
    private FragmentJogoBinding binding;

    // vetor para agrupar ps botoes
    private Button[] botoes;

    // variavel que representa o tabueiro
    private String[][] tabuleiro;

    // variavel para os simbolos
    private String simbolo, simbJog1, simbJog2;

    // varivel Random para sortear quem começa

    private Random random;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // instancia o bind

        binding = FragmentJogoBinding.inflate(inflater, container, false);

        binding.bt00.setOnClickListener(listenerBotoes);
        binding.bt01.setOnClickListener(listenerBotoes);
        binding.bt02.setOnClickListener(listenerBotoes);


        //instancia o vetor
        botoes = new Button[9];

        //agrupa os botoes no vetor
        botoes[0] = binding.bt00;
        botoes[1] = binding.bt01;
        botoes[2] = binding.bt02;
        botoes[3] = binding.bt10;
        botoes[4] = binding.bt11;
        botoes[5] = binding.bt12;
        botoes[6] = binding.bt20;
        botoes[7] = binding.bt21;
        botoes[8] = binding.bt22;

        //associa o listener aos botoes
        for(Button bt : botoes){
            bt.setOnClickListener(listenerBotoes);

        }


        // inicializa tabuleiro
        tabuleiro = new String[3][3];

        // instancia o Random
        random = new Random();

        // define os simbolos dos jogadores
        simbJog1 = "X";
        simbJog2 = "O";

        // sorteia quem inicia o jogo
        sorteia();

        //retorna a view do Fragment
        return binding.getRoot();


    }

    private void sorteia(){
        // caso o random gere um valor V, jogador 1 começa
        // caso contrario jogador 2 começa
        if(random.nextBoolean()){

            simbolo = simbJog1;

        }else{

            simbolo = simbJog2;

        }




    }

    private View.OnClickListener listenerBotoes = btPress -> {
       // pega o nome do botao
        String bomeBotao = getContext().getResources().getResourceName(btPress.getId());

        // extrai os 2 ultimos caracteres do nomeBotao
        String posicao = bomeBotao.substring(bomeBotao.length()-2);

        //extrai a posição em linha e coluna
        int linha = Character.getNumericValue(posicao.charAt(0));
        int coluna = Character.getNumericValue(posicao.charAt(1));

        // marca no tabuleiro o simbolo que foi jogado
        tabuleiro[linha][coluna] = simbolo;

        // faz um casting de View par button
        Button botao = (Button) btPress;

        // trocar o texto do botao que foi clicado
        botao.setText(simbolo);



    };
}