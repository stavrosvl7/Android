package com.unipi.stavrosvl7.ChatApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.stavrosvl7.ChatApp.Model.User;
import com.unipi.stavrosvl7.ChatApp.R;
import com.unipi.stavrosvl7.ChatApp.MessageActivity;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private ArrayList<User> users;
    private Context myContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView username;
        public MyViewHolder(View v) {
            super(v);
            username = v.findViewById(R.id.userNameDisplayed);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsAdapter(Context context, ArrayList<User> myUsers) {
        this.users = myUsers;
        this.myContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_user, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final User user = users.get(position);
        holder.username.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, MessageActivity.class);
                intent.putExtra("userId", user.getId());
                myContext.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }

}

