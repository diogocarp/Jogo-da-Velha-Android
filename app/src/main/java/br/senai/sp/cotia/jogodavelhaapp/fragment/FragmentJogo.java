package br.senai.sp.cotia.jogodavelhaapp.fragment;


import android.content.DialogInterface;
import android.icu.util.Freezable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
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

    // variavel para contar o numero de jogadores
    private int numJogadas = 0;

    // variáveis para o placar
    private int placarJog1 = 0, placarJog2 =0, placarEmpate = 0;
    private MenuInflater inflater;
    private String rodadas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // habilita o menu neste fragment
        setHasOptionsMenu(true);
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

        //preecher o tabuleiro com ""
        for(String[] vetor:tabuleiro){
            Arrays.fill(vetor, "");
        }

        // instancia o Random
        random = new Random();

        // define os simbolos dos jogadores
        simbJog1 = PrefsUtil.getSimboloJog1(getContext());
        simbJog2 = PrefsUtil.getSimboloJog2(getContext());
        rodadas = PrefsUtil.numeroRodadas(getContext());

        // altera o símbolo do jogador no placar
        binding.jogador1.setText(getResources().getString(R.string.jogador1, simbJog1));
        binding.jogador2.setText(getResources().getString(R.string.jogador2, simbJog2));
        binding.numRodadas.setText(rodadas);

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

    private void atualizaVez(){
        // verifica de quem é a vez e "acende" o placar  do jogador em questão
        if(simbolo.equals(simbJog1)){

            binding.linear1.setBackgroundColor(getResources().getColor(R.color.teal_200));
            binding.linear2.setBackgroundColor(getResources().getColor(R.color.white));

        }else{
            binding.linear2.setBackgroundColor(getResources().getColor(R.color.teal_200));
            binding.linear1.setBackgroundColor(getResources().getColor(R.color.white));

        }



    }

    private boolean venceu() {

        // verifica se venceu nas linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0].equals(simbolo) &&
                    tabuleiro[i][1].equals(simbolo) &&
                    tabuleiro[i][2].equals(simbolo)) {
                return true;

            }
        }
        //verifica se venceu na coluna

        for (int i = 0; i < 3; i++) {
            if (tabuleiro[0][i].equals(simbolo) &&
                    tabuleiro[1][i].equals(simbolo) &&
                    tabuleiro[2][i].equals(simbolo)) {
                return true;

            }
        }


        // verifica se venceu nas diagonais

        if (tabuleiro[0][0].equals(simbolo) &&
                tabuleiro[1][1].equals(simbolo) &&
                tabuleiro[2][2].equals(simbolo)) {
            return true;

        }
        if (tabuleiro[0][2].equals(simbolo) &&
                tabuleiro[1][1].equals(simbolo) &&
                tabuleiro[2][0].equals(simbolo)) {
            return true;
        }
        return false;
    }

    private void atualizarPlacar(){

        binding.placar1.setText(placarJog1+"");
        binding.placar2.setText(placarJog2+"");
        binding.empate.setText(placarEmpate+"");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.inflater = inflater;
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void resetaTudo(){

            for (String[] vetor : tabuleiro) {
                Arrays.fill(vetor, "");


            }
            for(Button botao : botoes){


                botao.setClickable(true);
                botao.setBackgroundColor(getResources().getColor(R.color.teal_200));
                botao.setText("");

            }

            // sorteia quem ira iniciar o prox jogo
            sorteia();

            // atualiza a vez no placar
            atualizaVez();

            // zerar o número de jogadas
            numJogadas = 0;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // verifica qual botao foi clicado no menu
        switch (item.getItemId()){

            case R.id.menu_resetar:
                ResetaAlert();
                break;

                //caso tenha clicado nas preferencias
            case R.id.menu_preferencias:
                NavHostFragment.findNavController(FragmentJogo.this).
                        navigate(R.id.action_fragmentJogo_to_prefFragment);
                break;

            case R.id.menu_inicio:
                NavHostFragment.findNavController(FragmentJogo.this).
                        navigate(R.id.action_fragmentJogo_to_fragmentInicio);
                break;


        }
        return true;
    }

    private View.OnClickListener listenerBotoes = btPress -> {
        //incrementa as jogadas
        numJogadas++;

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

        //desabilitar o botao
        botao.setClickable(false);

        // troco o background do botao
        botao.setBackgroundColor(getResources().getColor(R.color.white));
        botao.setTextColor(getResources().getColor(R.color.purple_200));

        //verifica se venceu
        if(numJogadas >= 5 && venceu()){
            // exibe um Toast informando que o jogador venceu
            Toast.makeText(getContext(),R.string.venceu, Toast.LENGTH_SHORT).show();

            // verifica quem venceu e atualiza o placar
            if(simbolo.equals(simbJog1)){
                placarJog1++;

            }else{
                placarJog2++;

            }

            minimoChance(rodadas);

            // atualiza o placar
            atualizarPlacar();


            //reseta o tabuleiro
            resetaTudo();

        }else if (numJogadas == 9){

            // exibe um Toast informando que o jogador venceu
            Toast.makeText(getContext(),R.string.deuvelha, Toast.LENGTH_SHORT).show();
            placarEmpate++;
            atualizarPlacar();
            //reseta o tabuleiro
            resetaTudo();

        }else{
            //inverter a vez
            simbolo = simbolo.equals(simbJog1) ? simbJog2 : simbJog1;

            //atualizaVez
            atualizaVez();
        }



    };

    @Override
    public void onStart() {
        super.onStart();
        // para "sumir" com a toolbar
        // pegar uma referencia do tipo AppCompatActivity
        AppCompatActivity minhaActivity = (AppCompatActivity) getActivity();

        minhaActivity.getSupportActionBar().show();
        minhaActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private AlertDialog alerta;

    private void ResetaAlert() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Alerta");

        builder.setMessage("Deseja reeniciar o jogo?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                    placarJog1 = 0;
                    placarJog2 = 0;
                    placarEmpate = 0;
                    resetaTudo();
                    atualizarPlacar();

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });
        alerta = builder.create();
        alerta.show();
    }

    private void minimoChance(String numRodadas){

        int rod = Integer.parseInt(numRodadas);
        double metade;
        metade = rod / 2.0;
        if(placarJog1 > metade){

            Toast.makeText(getContext(),R.string.jogo, Toast.LENGTH_LONG).show();

        }else if(placarJog2 > metade){

            Toast.makeText(getContext(),R.string.jogo, Toast.LENGTH_LONG).show();

        }else{

            atualizaVez();
        }

    }

}