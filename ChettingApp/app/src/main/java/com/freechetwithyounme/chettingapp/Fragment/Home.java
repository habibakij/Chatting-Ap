package com.freechetwithyounme.chettingapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.AllUser.UserProfile;
import com.freechetwithyounme.chettingapp.Model.HomeModel;
import com.freechetwithyounme.chettingapp.R;
import com.freechetwithyounme.chettingapp.ShowAllUser.ShowUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Home extends Fragment {
    private static final String TAG = "HomeActivity";
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private String uID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView= view.findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        databaseReference= FirebaseDatabase.getInstance().getReference("User");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<HomeModel> options = new FirebaseRecyclerOptions.Builder<HomeModel>()
                .setQuery(databaseReference, HomeModel.class).build();

        final FirebaseRecyclerAdapter<HomeModel, HomeViewHolder> adapter = new FirebaseRecyclerAdapter<HomeModel, HomeViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final HomeViewHolder holder, int position, @NonNull final HomeModel model) {
                        final String passUid = getRef(position).getKey();
                        //uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        //Log.i(TAG,uID);
                        String takeBirthYear = null;
                        String birthYear= model.getAge();
                        String[] birthYearSplit= birthYear.split("/");
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

                        String nameAndAge= model.getName()+", "+age;

                        holder.homeNameAndAge.setText(nameAndAge);
                        holder.homeLocation.setText(model.getLocation());

                        Picasso.with(getContext())
                                .load(model.getImageuri())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.homeImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(getContext())
                                                .load(model.getImageuri())
                                                .placeholder(R.mipmap.ic_launcher)
                                                .into(holder.homeImage);
                                    }
                                });

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), UserProfile.class);
                                intent.putExtra("passuID", passUid);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.homeviewsample, null);
                        return new HomeViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView homeNameAndAge, homeLocation;
        ImageView homeImage;
        View mView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mView= itemView;
            homeImage= itemView.findViewById(R.id.home_image);
            homeNameAndAge= itemView.findViewById(R.id.home_name);
            homeLocation= itemView.findViewById(R.id.home_location);

            /*homeStatus= itemView.findViewById(R.id.home_status);
            homeEmail= itemView.findViewById(R.id.home_email);
            homeNumber= itemView.findViewById(R.id.home_number);
            homeOccupation= itemView.findViewById(R.id.home_occupation);
            homeBio= itemView.findViewById(R.id.home_bio);*/
        }
    }
}
