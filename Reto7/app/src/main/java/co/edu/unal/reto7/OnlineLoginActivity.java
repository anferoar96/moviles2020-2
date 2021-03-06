package co.edu.unal.reto7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OnlineLoginActivity extends AppCompatActivity {

    ListView lv_loginUsers;
    ArrayList<String> list_loginUsers=new ArrayList<String>();
    ArrayAdapter adpt;

    ListView lv_requestedUsers;
    ArrayList<String> list_requestedUsers = new ArrayList<String>();
    ArrayAdapter reqUsersAdpt;

    TextView tvUserID,tvSendRequest,tvAcceptRequest;
    String LoginUserID,UserName,LoginUID;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_login);



        tvSendRequest =(TextView) findViewById(R.id.tvSendRequest);
        tvAcceptRequest=(TextView) findViewById(R.id.tvAcceptRequest);

        tvSendRequest.setText("Please wait...");
        tvAcceptRequest.setText("Please wait...");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        lv_loginUsers = (ListView) findViewById(R.id.lv_loginUsers);
        adpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list_loginUsers){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        lv_loginUsers.setAdapter(adpt);

        lv_requestedUsers=(ListView)  findViewById(R.id.lv_requestedUsers);
        reqUsersAdpt=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_requestedUsers){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        lv_requestedUsers.setAdapter(reqUsersAdpt);


        tvUserID =(TextView) findViewById(R.id.tvLoginUser);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                   LoginUID = user.getUid();
                   Log.d("Auth","onAuthStateChanged:singed");
                   LoginUserID=user.getEmail();
                   tvUserID.setText(LoginUserID);
                   UserName=convertEmailToString(LoginUserID);
                   UserName = UserName.replace(".", "");
                   myRef.child("users").child(UserName).child("request").setValue(LoginUID);
                   reqUsersAdpt.clear();
                   AcceptIncommmingRequests();
                } else {
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                    JoinOnlineGame();
                }
            }
        };
        myRef.getRoot().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateLoginUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lv_loginUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String requestToUser =((TextView)view).getText().toString();
                confirmRequest(requestToUser,"To");
            }
        });

        lv_requestedUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String requestFromUser=((TextView)view).getText().toString();
                confirmRequest(requestFromUser,"From");
            }
        });

    }

    private void confirmRequest(final String OtherPlayer, final String reqType) {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.connect_player_dialog,null);
        b.setView(dialogView);
        b.setTitle("Start Game?");
        b.setMessage("Connect with "+OtherPlayer);
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myRef.child("users").child(OtherPlayer).child("request").push().setValue(LoginUserID);
                if(reqType.equalsIgnoreCase("From")){
                    StartGame(OtherPlayer+":"+UserName,OtherPlayer,"From");
                }else{
                    StartGame(UserName+":"+OtherPlayer,OtherPlayer,"To");
                }
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.show();
    }

    private void StartGame(String Player, String otherPlayer, String requestType) {
        myRef.child("playing").child(Player).removeValue();
        Intent i =new Intent(getApplicationContext(),OnlineGameActivity.class);
        i.putExtra("player_session",Player);
        i.putExtra("user_name",UserName);
        i.putExtra("other_player",otherPlayer);
        i.putExtra("login_uid",LoginUID);
        i.putExtra("request_type",requestType);
        startActivity(i);

    }

    private void updateLoginUsers(DataSnapshot dataSnapshot) {
        String key="";
        Set<String> set=new HashSet<String>();
        Iterator i=dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            key=((DataSnapshot) i.next()).getKey();
            if(!key.equalsIgnoreCase(UserName)){
                set.add(key);
            }
        }

        adpt.clear();
        adpt.addAll(set);
        adpt.notifyDataSetChanged();
        tvSendRequest.setText("Send request to");
        tvAcceptRequest.setText("Accept request from");
    }

    private void AcceptIncommmingRequests() {
        myRef.child("users").child(UserName).child("request")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            HashMap<String,Object> map=(HashMap<String, Object>)dataSnapshot.getValue();
                            if(map!=null){
                                String value="";
                                for(String key:map.keySet()){
                                    value=(String)map.get(key);
                                    reqUsersAdpt.add(convertEmailToString(value));
                                    reqUsersAdpt.notifyDataSetChanged();
                                    myRef.child("users").child(UserName).child("request").setValue(LoginUID);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void JoinOnlineGame() {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        LayoutInflater inflater =this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.login_dialog,null);
        b.setView(dialogView);

        final EditText etEmail=(EditText) dialogView.findViewById(R.id.etEmail);
        final EditText etPassword=(EditText) dialogView.findViewById(R.id.etPassword);

        b.setTitle("Please register");
        b.setMessage("Enter your email and password");
        b.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RegisterUser(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent i =new Intent(getApplicationContext(),MenuActivity.class);
                 startActivity(i);
                 finish();
            }
        });
        b.show();
    }

    private String convertEmailToString(String email) {
        String value= email.substring(0,email.indexOf('@'));
        value=value.replace(".","");
        return value;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void RegisterUser(String email,String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth Complete", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}