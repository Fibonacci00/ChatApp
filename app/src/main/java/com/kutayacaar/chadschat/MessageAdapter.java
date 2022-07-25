package com.kutayacaar.chadschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<Message>messages;
    private String senderImg,receiverImg;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String receiverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);
      return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Glide.with(context).load(senderImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profImage);
holder.txtmessage.setText(messages.get(position).getContet());
ConstraintLayout constraintLayout = holder.ccll;
if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(constraintLayout);
    constraintSet.clear(R.id.profile_cardview,ConstraintSet.LEFT);
    constraintSet.clear(R.id.txt_messagecontent,ConstraintSet.LEFT);
    constraintSet.connect(R.id.profile_cardview,ConstraintSet.RIGHT,R.id.ccLayout,ConstraintSet.RIGHT,0);
    constraintSet.connect(R.id.txt_messagecontent,ConstraintSet.RIGHT,R.id.profile_cardview,ConstraintSet.LEFT,0);
    constraintSet.applyTo(constraintLayout);
}else {
    Glide.with(context).load(receiverImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profImage);

    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(constraintLayout);
    constraintSet.clear(R.id.profile_cardview,ConstraintSet.RIGHT);
    constraintSet.clear(R.id.txt_messagecontent,ConstraintSet.RIGHT);
    constraintSet.connect(R.id.profile_cardview,ConstraintSet.LEFT,R.id.ccLayout,ConstraintSet.LEFT,0);
    constraintSet.connect(R.id.txt_messagecontent,ConstraintSet.LEFT,R.id.profile_cardview,ConstraintSet.RIGHT,0);
    constraintSet.applyTo(constraintLayout);

}
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        TextView txtmessage;
        ImageView profImage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            ccll = itemView.findViewById(R.id.ccLayout);
            txtmessage = itemView.findViewById(R.id.txt_messagecontent);
            profImage =itemView.findViewById(R.id.small_profile);
        }
    }
}
