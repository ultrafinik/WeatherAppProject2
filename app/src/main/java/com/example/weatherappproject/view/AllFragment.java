package com.example.weatherappproject.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherappproject.R;
import com.example.weatherappproject.adapters.ComplectAdapter;
import com.example.weatherappproject.model.Complect;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllFragment extends Fragment {
    private RecyclerView rv;
    private FirebaseAuth mAuth;
    private DatabaseReference bd;
    private List<Complect> listComplect;
    private ComplectAdapter.OnComplectClickListener listener;
    private ComplectAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all, container, false);
        rv=view.findViewById(R.id.weatherComplectAll);
        mAuth = FirebaseAuth.getInstance();
        bd = FirebaseDatabase.getInstance().getReference();
        listComplect = new ArrayList<>();
        listener=new ComplectAdapter.OnComplectClickListener() {
            @Override
            public void onComplectClick(Complect complect, int position) {
                Intent intent=new Intent(getActivity(),ComplectActivity.class);
                intent.putExtra(DetailFragment.COMPLECT_EXTRA,complect);
                intent.putExtra(DetailFragment.FRAGMENT_EXTRA,"all");
                getActivity().startActivity(intent);
            }
        };
        getCloses();
        return view;
    }
    private void getCloses()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            Query users = bd.child("Users");
            users.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for (DataSnapshot postSnapshot : snapshot.getChildren())
                    {
                        if (firebaseUser.getEmail().equals(postSnapshot.child("email").getValue().toString()))
                        {
                            String email = postSnapshot.child("email").getValue().toString();
                            Query complects = bd.child("Complects");
                            complects.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listComplect.clear();
                                    for (DataSnapshot postSnapshot : snapshot.getChildren())
                                    {
                                        if (email.equals(postSnapshot.child("email").getValue().toString())) {
                                            Complect complect = new Complect();
                                            complect.setKey(postSnapshot.getKey());
                                            complect.setFootwear(postSnapshot.child("Footwear").getValue().toString());
                                            complect.setHeadgear(postSnapshot.child("Headgear").getValue().toString());
                                            complect.setOuterwear(postSnapshot.child("Outerwear").getValue().toString());
                                            complect.setPants(postSnapshot.child("Pants").getValue().toString());
                                            complect.setShirt(postSnapshot.child("Shirt").getValue().toString());
                                            complect.setEmail(email);
                                            complect.setTemp1(Integer.parseInt(postSnapshot.child("temp1").getValue().toString()));
                                            complect.setTemp2(Integer.parseInt(postSnapshot.child("temp2").getValue().toString()));
                                            listComplect.add(complect);
                                        }
                                    }
                                    adapter = new ComplectAdapter(listComplect, getActivity(),listener);
                                    rv.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }
}