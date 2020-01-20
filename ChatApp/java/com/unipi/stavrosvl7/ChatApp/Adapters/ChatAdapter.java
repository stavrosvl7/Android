package com.unipi.stavrosvl7.ChatApp.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unipi.stavrosvl7.ChatApp.Model.Chat;
import com.unipi.stavrosvl7.ChatApp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private ArrayList<Chat> dataset;
    private Context mContext;

    FirebaseUser firebaseUser;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_message;
        public MyViewHolder(View v) {
            super(v);
            textView_message = v.findViewById(R.id.showMessage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(Context context, ArrayList<Chat> myDataset) {
        mContext = context;
        dataset = myDataset;
    }

    private void startDownload(Uri uri) {
        DownloadManager mManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(uri);
        mManager.enqueue(mRqRequest);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
        else {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Chat chat = dataset.get(position);

        holder.textView_message.setText(chat.getMessage());

        holder.textView_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textViewText = ((TextView)view).getText().toString();
                if (URLUtil.isValidUrl(textViewText)) {
                    startDownload(Uri.parse(textViewText));
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public int getItemViewType(int position){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(dataset.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
