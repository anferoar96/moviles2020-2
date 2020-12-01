package co.edu.unal.reto7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    int gameState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameState=1; // 1 can play; 2 gameover; 3 Draw
    }

    public void GameBoardClick(View view) {
        ImageView selectedImage=(ImageView)view;

        int selectedBlock=0;

        switch (selectedImage.getId()){
            case R.id.iv_11: selectedBlock=1; break;
            case R.id.iv_12: selectedBlock=2; break;
            case R.id.iv_13: selectedBlock=3; break;
            case R.id.iv_21: selectedBlock=4; break;
            case R.id.iv_22: selectedBlock=5; break;
            case R.id.iv_23: selectedBlock=6; break;
            case R.id.iv_31: selectedBlock=7; break;
            case R.id.iv_32: selectedBlock=8; break;
            case R.id.iv_33: selectedBlock=9; break;
        }

        PlayGame(selectedBlock,selectedImage);
    }

    int activePlayer=1;
    ArrayList<Integer> player1=new ArrayList<Integer>();
    ArrayList<Integer> player2=new ArrayList<Integer>();

    private void PlayGame(int selectedBlock, ImageView selectedImage) {
        if(gameState==1){
            if(activePlayer==1){
                selectedImage.setImageResource(R.drawable.ttt_x);
                player1.add(selectedBlock);
                activePlayer=2;
                Autoplay();
            }else if(activePlayer==2){
                selectedImage.setImageResource(R.drawable.ttt_o);
                player2.add(selectedBlock);
                activePlayer=1;
            }
            selectedImage.setEnabled(false);
            CheckWinner();
        }
    }

    private void Autoplay() {
        ArrayList<Integer> emptyBlocks=new ArrayList<Integer>();
        for(int i=1;i<=9;i++){
            if(!(player1.contains(i) || player2.contains(i))){
                emptyBlocks.add(i);
            }
        }

        if(emptyBlocks.size()==0){
            CheckWinner();
            if(gameState==1){
                AlertDialog.Builder b=new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
                showAlert("Draw");
            }

            gameState=3; //Draw
        }else{
            Random r=new Random();
            int randomIndex=r.nextInt(emptyBlocks.size());
            int selectedBlock=emptyBlocks.get(randomIndex);
            ImageView selectedImage=(ImageView) findViewById(R.id.iv_11);

            switch (selectedBlock){
                case 1:selectedImage=(ImageView) findViewById(R.id.iv_11); break;
                case 2:selectedImage=(ImageView) findViewById(R.id.iv_12); break;
                case 3:selectedImage=(ImageView) findViewById(R.id.iv_13); break;
                case 4:selectedImage=(ImageView) findViewById(R.id.iv_21); break;
                case 5:selectedImage=(ImageView) findViewById(R.id.iv_22); break;
                case 6:selectedImage=(ImageView) findViewById(R.id.iv_23); break;
                case 7:selectedImage=(ImageView) findViewById(R.id.iv_31); break;
                case 8:selectedImage=(ImageView) findViewById(R.id.iv_32); break;
                case 9:selectedImage=(ImageView) findViewById(R.id.iv_33); break;
            }
            PlayGame(selectedBlock,selectedImage);
        }
    }

    private void showAlert(String title) {
        AlertDialog.Builder b=new AlertDialog.Builder(this,R.style.TransparentDialog);
        b.setTitle(title)
                .setMessage("Start a new game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ResetGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent m = new Intent(getApplicationContext(),MenuActivity.class);
                        startActivity(m);
                        finish();
                    }
                }).show();
    }

    private void ResetGame() {
        gameState=1;
        activePlayer=1;
        player1.clear();
        player2.clear();

        ImageView iv;
        iv=(ImageView) findViewById(R.id.iv_11); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_12); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_13); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_21); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_22); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_23); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_31); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_32); iv.setImageResource(0);iv.setEnabled(true);
        iv=(ImageView) findViewById(R.id.iv_33); iv.setImageResource(0);iv.setEnabled(true);
    }

    private void CheckWinner() {
        int winner=0;
        //Player 1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)){winner =1; }
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)){winner =1; }
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)){winner =1; }

        if(player1.contains(1) && player1.contains(4) && player1.contains(7)){winner =1; }
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)){winner =1; }
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)){winner =1; }

        if(player1.contains(1) && player1.contains(5) && player1.contains(9)){winner =1; }
        if(player1.contains(3) && player1.contains(5) && player1.contains(7)){winner =1; }

        //Player 2
        if(player2.contains(1) && player2.contains(2) && player2.contains(3)){winner =2; }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6)){winner =2; }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9)){winner =2; }

        if(player2.contains(1) && player2.contains(4) && player2.contains(7)){winner =2; }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8)){winner =2; }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9)){winner =2; }

        if(player2.contains(1) && player2.contains(5) && player2.contains(9)){winner =2; }
        if(player2.contains(3) && player2.contains(5) && player2.contains(7)){winner =2; }

        if(winner!=0 && gameState==1){
            if(winner==1){
                showAlert("You win");
            }else if(winner==2){
                showAlert("You lost");
            }
            gameState=2;
        }
    }
}