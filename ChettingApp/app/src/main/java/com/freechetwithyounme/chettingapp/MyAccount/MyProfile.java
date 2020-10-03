package com.freechetwithyounme.chettingapp.MyAccount;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {
    private static final String TAG = "MyProfileActivity";
    private StorageReference storageReference;

    private ImageView my_profile_imageView;
    private TextView my_Name, my_status, my_location, my_number, my_email, my_proffession, my_institute, my_bio, my_gender, my_age;
    private Button edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Initialize();

        Toolbar toolbar = findViewById(R.id.toolbar_myprofile);
        ImageView back= findViewById(R.id.toolbar_back);
        TextView toolbarText= toolbar.findViewById(R.id.toolbar_text);
        toolbarText.setText("Profile");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, MainActivity.class));
            }
        });

        try {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String getUID = firebaseUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(getUID);
            databaseReference.keepSynced(true);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String getName= Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    String getStatus= dataSnapshot.child("status").getValue().toString();
                    String getLocation= dataSnapshot.child("location").getValue().toString();
                    String getGender= dataSnapshot.child("gender").getValue().toString();
                    String getAge= dataSnapshot.child("age").getValue().toString();
                    String getNumber= dataSnapshot.child("number").getValue().toString();
                    String getEmail= dataSnapshot.child("email").getValue().toString();
                    String getProfession= dataSnapshot.child("profession").getValue().toString();
                    String getInstitute= dataSnapshot.child("institute").getValue().toString();
                    String getBio= dataSnapshot.child("bio").getValue().toString();
                    String getImage= dataSnapshot.child("imageuri").getValue().toString();


                    String takeBirthYear = null;
                    String[] birthYearSplit= getAge.split("/");
                    for (String split: birthYearSplit){
                        if (split.equals("default")){
                            takeBirthYear= String.valueOf(2000);
                        } else if (split.length()>3){
                            takeBirthYear=split;
                        }
                    }
                    //Log.e(TAG, "Birth Year: "+takeBirthYear);
                    int currentYear= Calendar.getInstance().get(Calendar.YEAR);
                    Log.e(TAG, "Birth Year: "+takeBirthYear);
                    int age= currentYear-Integer.parseInt(takeBirthYear);

                    my_Name.setText(getName);
                    my_age.setText(String.valueOf("Age\n"+age));
                    my_status.setText(getStatus);
                    my_location.setText(getLocation);
                    my_gender.setText(getGender);
                    my_number.setText("Phone\n"+getNumber);
                    my_email.setText(getEmail);
                    my_proffession.setText(getProfession);
                    my_institute.setText(getInstitute);
                    my_bio.setText(getBio);
                    Picasso.with(MyProfile.this).load(getImage).into(my_profile_imageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e){
            Toast.makeText(MyProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MyProfile.this, ProSetting.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void Initialize(){
        my_profile_imageView= findViewById(R.id.myimage);
        my_Name= findViewById(R.id.displyname);
        my_status= findViewById(R.id.mystatus);
        my_location= findViewById(R.id.mylocation);
        edit_profile= findViewById(R.id.editprofile);
        my_gender= findViewById(R.id.mygender);
        my_age= findViewById(R.id.myage);
        my_number= findViewById(R.id.mynumber);
        my_email= findViewById(R.id.myemail);
        my_proffession= findViewById(R.id.myprofession);
        my_institute= findViewById(R.id.myinstitute);
        my_bio= findViewById(R.id.mybio);
    }

    @Override
    public void onBackPressed() {
    }
}
