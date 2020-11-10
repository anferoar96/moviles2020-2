package co.edu.unal.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class AndroidTicTacToeActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String player1="";
    String player2="";
    DocumentReference tictac;
    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    SharedPreferences mPrefs;
    private boolean mGameOver;
    private BoardView mBoardView;
    AlertDialog builder1;

    static final int DIALOG_MULTIPLAYER=0;
    static final int DIALOG_QUIT=1;
    static final int DIALOG_CREAR=2;
    static final int DIALOG_UNIRSE=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();
        mBoardView= findViewById(R.id.board);
        mInfoTextView=findViewById(R.id.information);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        startNewGame();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pop);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pop2);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED) {
            String difficultyLevel = mPrefs.getString("difficulty_level",getResources().getString(R.string.difficulty_harder));
            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.multiplayer:
                builder1=createDialog(DIALOG_MULTIPLAYER);
                builder1.show();

                Button crear=builder1.findViewById(R.id.botonCrear);
                crear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialogo=createDialog(DIALOG_CREAR);
                        dialogo.show();
                    }
                });
                Button unirse=builder1.findViewById(R.id.botonUnirse);
                unirse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialogo=createDialog(DIALOG_UNIRSE);
                        dialogo.show();
                    }
                });
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this,Settings.class),0);
                return true;
            case R.id.quit:
                builder1=createDialog(DIALOG_QUIT);
                builder1.show();
                return true;
        }
        return false;
    }

    protected AlertDialog createDialog(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog res =null;
        switch (id){
            case DIALOG_QUIT:{
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                res= builder.create();
                break;
            }
            case DIALOG_MULTIPLAYER:{
                LayoutInflater inflater=getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_multiplayer, null))
                        .setNegativeButton("Cancel", null);
                res=builder.create();

                break;
            }
            case DIALOG_CREAR:{
                LayoutInflater inflater=getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_create, null);
                //final EditText habitacion = (EditText) view.findViewById(R.id.habitacion);
                builder.setView(view)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Create",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText habitacion=view.findViewById(R.id.habitacion);
                                EditText usuario=view.findViewById(R.id.username);
                                player1=usuario.getText().toString();
                                String nom=habitacion.getText().toString();
                                Map<String, Object> player = new HashMap<>();
                                player.put("usurname",player1);
                                db.collection("usuarios").add(player);
                                Map<String,Object>juego=new HashMap<>();
                                juego.put("player1",player1);
                                juego.put("player2","");
                                juego.put("turno",player1);
                                juego.put("0","");
                                juego.put("1","");
                                juego.put("2","");
                                juego.put("3","");
                                juego.put("4","");
                                juego.put("5","");
                                juego.put("6","");
                                juego.put("7","");
                                juego.put("8","");
                                db.collection("juego").document(nom).set(juego);
                                tictac=db.collection("juego").document(nom);
                                dialog.dismiss();
                                builder1.dismiss();
                            }
                        });
                res=builder.create();
                break;
            }
            case  DIALOG_UNIRSE:{
                LayoutInflater inflater=getLayoutInflater();
                final View view2 = inflater.inflate(R.layout.dialog_join, null);
                builder.setView(view2)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Join",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText usuario=view2.findViewById(R.id.user);
                                EditText room=view2.findViewById(R.id.room);
                                player2=usuario.getText().toString();
                                String habi=room.getText().toString();
                                Map<String, Object> player = new HashMap<>();
                                player.put("usurname",player2);
                                db.collection("usuarios").add(player);
                                //TODO: Send info to firebase about joining  a game
                                tictac=db.collection("juego").document(habi);
                                tictac.update("player2",player2);
                                tictac.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                        @Nullable FirebaseFirestoreException e) {
                                        if (snapshot.exists()) {
                                            Log.d("R", "Existencia ");
                                        }
                                    }
                                });
                                dialog.dismiss();
                                builder1.dismiss();
                            }
                        });
                res=builder.create();
                break;
            }
        }

        return res;
    }


    private void startNewGame(){
        mInfoTextView.setText("");
        mGameOver=false;
        mGame.clearBoard();
        mBoardView.invalidate();

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int col=(int) motionEvent.getX()/mBoardView.getBoardCellWidth();
                int row=(int) motionEvent.getY()/mBoardView.getBoardCellHeight();
                int pos=row*3+col;
                Log.d("Random", Integer.toString(pos));
                if(!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER,pos)){
                    int winner = mGame.checkForWinner();

                }
                return false;
            }
        };

    private boolean setMove(char player, int location) {
// HUMAN_PLAYER=player COMPUTER_PLAYER=player2
        if (mGame.setMove(player, location)) {
            if(player==TicTacToeGame.HUMAN_PLAYER){
                tictac.update(String.valueOf(location),"x");
            }else{
                tictac.update(String.valueOf(location),"o");
            }

            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }
}

