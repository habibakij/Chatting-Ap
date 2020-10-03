package com.freechetwithyounme.chettingapp.ShowAllUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.AllUser.UserProfile;
import com.freechetwithyounme.chettingapp.LogIn;
import com.freechetwithyounme.chettingapp.MainActivity;
import com.freechetwithyounme.chettingapp.Model.HomeModel;
import com.freechetwithyounme.chettingapp.R;
import com.freechetwithyounme.chettingapp.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ShowUser extends AppCompatActivity {

    private String uID;
    //private FirebaseRecyclerAdapter<HomeModel, GridViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        Toolbar toolbar = findViewById(R.id.toolbar_register);
        ImageView back= findViewById(R.id.toolbar_back);
        TextView toolbarText= toolbar.findViewById(R.id.toolbar_text);
        toolbarText.setText("Show User's");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowUser.this, MainActivity.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.user_recyclerView);
        recyclerView.setHasFixedSize(true);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");

    }

    /*

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<HomeModel> options = new FirebaseRecyclerOptions.Builder<HomeModel>().setQuery(databaseReference, HomeModel.class).build();

        final FirebaseRecyclerAdapter<HomeModel, GridViewHolder> adapter = new FirebaseRecyclerAdapter<HomeModel, GridViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final GridViewHolder holder, int position, @NonNull final HomeModel model) {
                        final String passUid = getRef(position).getKey();
                        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        holder.gName.setText(model.getName());
                        holder.gLocation.setText(model.getLocation());

                        Picasso.with(ShowUser.this)
                                .load(model.getImageuri())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.gImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(ShowUser.this)
                                                .load(model.getImageuri())
                                                .placeholder(R.mipmap.ic_launcher)
                                                .into(holder.gImageView);
                                    }
                                });

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShowUser.this, UserProfile.class);
                                intent.putExtra("passuID", passUid);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.homeviewsample, null);
                        return new GridViewHolder(view);
                    }
                };
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



    public static class GridViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView gName;
        TextView gLocation;
        ImageView gImageView;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            gName = itemView.findViewById(R.id.gridName);
            gLocation = itemView.findViewById(R.id.gridLocation);
            gImageView = itemView.findViewById(R.id.gridImage);
        }
    }
*/

}
