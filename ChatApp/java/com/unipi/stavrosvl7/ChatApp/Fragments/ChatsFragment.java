package com.unipi.stavrosvl7.ChatApp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.unipi.stavrosvl7.ChatApp.Adapters.ChatFragmentAdapter;
import com.unipi.stavrosvl7.ChatApp.Model.User;
import com.unipi.stavrosvl7.ChatApp.Notifications.Token;
import com.unipi.stavrosvl7.ChatApp.R;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatFragmentAdapter chatFragmentAdapter;
    private ArrayList<User> myUsers;

    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myUsers = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        readChats();

        return view;
    }

    private void readChats(){
        DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference("Users");

        databaseReferenceChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (!user.getId().equals(firebaseUser.getUid())) {
                        myUsers.add(user);
                    }
                }
                chatFragmentAdapter = new ChatFragmentAdapter(getContext(), myUsers);
                recyclerView.setAdapter(chatFragmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

    @Override
    public void onResume() {
        readChats();
        super.onResume();
    }

}
