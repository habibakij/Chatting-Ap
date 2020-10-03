package com.freechetwithyounme.chettingapp.AllUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.LogIn;
import com.freechetwithyounme.chettingapp.MainActivity;
import com.freechetwithyounme.chettingapp.R;
import com.freechetwithyounme.chettingapp.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private ImageView mImageView;
    private TextView displayName, displayNumber, displayLocation, displayEmail, displayProfession, displayInstitute, displayBio, displayAge, displayGender;
    private Button sendRequest, cancleRequest;

    private DatabaseReference frndReqDatabase;
    private DatabaseReference frndDatabase;
    private String currentUser;
    private String recieveUid;

    private String user_state= "not_friend";
    String disply_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.user_profile_app_ber);
        ImageView back= findViewById(R.id.toolbar_back);
        TextView toolbarText= toolbar.findViewById(R.id.toolbar_text);
        toolbarText.setText("Profile");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, MainActivity.class));
            }
        });

        Initialize();

        recieveUid= getIntent().getStringExtra("passuID");
        currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(recieveUid);
        mDatabaseReference.keepSynced(true);
        frndReqDatabase= FirebaseDatabase.getInstance().getReference("FriendRequest");
        frndReqDatabase.keepSynced(true);
        frndDatabase= FirebaseDatabase.getInstance().getReference("Friends");
        frndDatabase.keepSynced(true);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                disply_image= dataSnapshot.child("imageuri").getValue().toString();
                String display_name= dataSnapshot.child("name").getValue().toString();
                String display_number= dataSnapshot.child("number").getValue().toString();
                String display_birthday= dataSnapshot.child("age").getValue().toString();
                String display_gender= dataSnapshot.child("gender").getValue().toString();
                String display_location= dataSnapshot.child("location").getValue().toString();
                String display_email= dataSnapshot.child("email").getValue().toString();
                String display_profession= dataSnapshot.child("profession").getValue().toString();
                String display_institute= dataSnapshot.child("institute").getValue().toString();
                String display_bio= dataSnapshot.child("bio").getValue().toString();

                String takeBirthYear = null;
                String[] birthYearSplit= display_birthday.split("/");
                for (String split: birthYearSplit){
                    if (split.equals("default")){
                        takeBirthYear= String.valueOf(2000);
                    } else if (split.length()>3){
                        takeBirthYear=split;
                    }
                }
                int currentYear= Calendar.getInstance().get(Calendar.YEAR);
                Log.e(TAG, "Birth Year: "+takeBirthYear);
                int _age= currentYear-Integer.parseInt(takeBirthYear);

                displayName.setText(display_name);
                displayAge.setText(String.valueOf("Age\n"+_age));
                displayGender.setText(display_gender);
                displayLocation.setText(display_location);
                displayNumber.setText("Phone\n"+display_number);
                displayEmail.setText(display_email);
                displayProfession.setText(display_profession);
                displayInstitute.setText(display_institute);
                displayBio.setText(display_bio);

                Picasso.with(UserProfile.this)
                        .load(disply_image)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(mImageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(UserProfile.this)
                                        .load(disply_image)
                                        .into(mImageView);
                            }
                        });

                ////----------------friend recieve / friend list-----------

                frndReqDatabase.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(recieveUid)){
                            String req_type= dataSnapshot.child(recieveUid).child("request_type").getValue().toString();
                            if (req_type.equals("receive")){
                                user_state= "request_accept";
                                sendRequest.setText("Accept Request");

                                cancleRequest.setVisibility(View.VISIBLE);
                                cancleRequest.setEnabled(true);

                                cancleRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelFriendRequest();
                                    }
                                });
                            } else if (req_type.equals("send")){
                                user_state="request_send";
                                sendRequest.setText("cancel request");

                                cancleRequest.setVisibility(View.INVISIBLE);
                                cancleRequest.setEnabled(false);
                            }
                        } else {
                            frndDatabase.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(recieveUid)){
                                        user_state="friend";
                                        sendRequest.setText("Unfriend");

                                        cancleRequest.setVisibility(View.INVISIBLE);
                                        cancleRequest.setEnabled(false);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancleRequest.setVisibility(View.INVISIBLE);
        cancleRequest.setEnabled(false);

        if (recieveUid.equals(currentUser)){
            Toast.makeText(this, "Hey Man this is you !!", Toast.LENGTH_SHORT).show();
            sendRequest.setVisibility(View.GONE);
        } else {
            sendRequest.setVisibility(View.VISIBLE);
            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest.setEnabled(false);

                    if (user_state == "not_friend") {
                        SendFriendRequest();
                    }
                    if (user_state == "request_send") {
                        CancelFriendRequest();
                    }
                    if (user_state.equals("request_accept")) {
                        AcceptRequest();
                    }
                    if (user_state.equals("friend")) {
                        Unfriends();
                    }
                }
            });
        }
    }


    public void Initialize() {
        mImageView = findViewById(R.id.display_profile_image);
        displayName = findViewById(R.id.display_profile_name);
        displayLocation = findViewById(R.id.display_profile_location);
        displayEmail = findViewById(R.id.display_profile_email);
        displayProfession = findViewById(R.id.display_profile_profession);
        displayInstitute = findViewById(R.id.display_profile_institute);
        displayBio = findViewById(R.id.display_profile_bio);
        displayNumber = findViewById(R.id.display_profile_number);
        sendRequest = findViewById(R.id.send_frnd_request);
        cancleRequest = findViewById(R.id.cancel_frnd_request);
        displayAge= findViewById(R.id.display_profile_age);
        displayGender= findViewById(R.id.display_profile_gender);
    }

    public void SendFriendRequest() {
        frndReqDatabase.child(currentUser).child(recieveUid).child("request_type")
                .setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    frndReqDatabase.child(recieveUid).child(currentUser).child("request_type")
                            .setValue("receive").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendRequest.setEnabled(true);
                            user_state = "request_send";
                            sendRequest.setText("cancel request");

                            cancleRequest.setVisibility(View.INVISIBLE);
                            cancleRequest.setEnabled(false);
                            Toast.makeText(UserProfile.this, "request send successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(UserProfile.this, "failed send request", Toast.LENGTH_LONG).show();
                }
                sendRequest.setEnabled(true);
            }
        });
    }

    public void CancelFriendRequest() {
        frndReqDatabase.child(currentUser).child(recieveUid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        frndReqDatabase.child(recieveUid).child(currentUser).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        sendRequest.setEnabled(true);
                                        user_state = "not_friend";
                                        sendRequest.setText("send friend request");

                                        cancleRequest.setVisibility(View.INVISIBLE);
                                        cancleRequest.setEnabled(false);
                                    }
                                });
                    }
                });
    }

    public void AcceptRequest(){
        final String getTime= DateFormat.getDateTimeInstance().format(new Date());

        frndDatabase.child(currentUser).child(recieveUid).child("date").setValue(getTime)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        frndDatabase.child(recieveUid).child(currentUser).child("date").setValue(getTime)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        frndReqDatabase.child(currentUser).child(recieveUid).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        frndReqDatabase.child(recieveUid).child(currentUser).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        sendRequest.setEnabled(true);
                                                                        user_state="friend";
                                                                        sendRequest.setText("Unfriend");

                                                                        cancleRequest.setVisibility(View.INVISIBLE);
                                                                        cancleRequest.setEnabled(false);
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public void Unfriends(){
        frndDatabase.child(currentUser).child(recieveUid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                frndDatabase.child(recieveUid).child(currentUser).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            sendRequest.setEnabled(true);
                            user_state= "not_friend";
                            sendRequest.setText("send request");

                            cancleRequest.setVisibility(View.INVISIBLE);
                            cancleRequest.setEnabled(false);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
