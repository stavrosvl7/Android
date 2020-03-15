package com.unipi.stavrosvl7.ChatApp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.unipi.stavrosvl7.ChatApp.Adapters.ContactsAdapter;
import com.unipi.stavrosvl7.ChatApp.Model.Chat;
import com.unipi.stavrosvl7.ChatApp.Model.User;
import com.unipi.stavrosvl7.ChatApp.Notifications.Token;
import com.unipi.stavrosvl7.ChatApp.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatFragmentAdapter chatFragmentAdapter;
    private List<User> myUsers;
    private List<String> usersList;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        usersList = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(firebaseUser.getUid())); {
                        if(!usersList.contains(chat.getReceiver())){
                            usersList.add(chat.getReceiver());
                        }
                    }

                    if (chat.getReceiver().equals(firebaseUser.getUid())) {
                        if(!usersList.contains(chat.getSender())){
                            usersList.add(chat.getSender());
                        }
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void readChats(){
        myUsers = new ArrayList<>();

        DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference("Users");

        databaseReferenceChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for(String id : usersList){
                        if (user.getId().equals(id)) {
                            if(myUsers.size() != 0){
                                for(int i=0;i<myUsers.size();i++){
                                    User user1 = myUsers.get(i);
                                    if(!user.getId().equals(user1.getId())){
                                        myUsers.add(user);
                                        break;
                                    }
                                }
                            }
                            else {
                                myUsers.add(user);
                            }
                        }
                    }
                }

                chatFragmentAdapter = new ChatFragmentAdapter(getContext(),myUsers);
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
