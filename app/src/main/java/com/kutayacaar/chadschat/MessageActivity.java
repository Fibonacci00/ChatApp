package com.kutayacaar.chadschat;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolbar,imgSend;
    private ArrayList<Message> messages;
private MessageAdapter messageAdapter;
    String usernameOfRoommate,emailOfRoommate,chatRoomid;



    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);

    usernameOfRoommate = getIntent().getStringExtra("username_of_roommate");
    emailOfRoommate = getIntent().getStringExtra("email_of_roommate");

    recyclerView = findViewById(R.id.recyclerMessages);
    edtMessageInput = findViewById(R.id.edittext);
    imgSend = findViewById(R.id.sendImageView);
        txtChattingWith = findViewById(R.id.txtChatting);
        progressBar = findViewById(R.id.progressMessages);
imgToolbar = findViewById(R.id.img_toolbar);
txtChattingWith.setText(usernameOfRoommate);
        messages = new ArrayList<>();

imgSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseDatabase.getInstance().getReference("messages/"+chatRoomid).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailOfRoommate,edtMessageInput.getText().toString()));
        edtMessageInput.setText("");
    }
});
        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("image_of_roommate"),MessageActivity.this);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("image_of_roommate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgToolbar);

        setUpChatRoom();
    }
private void setUpChatRoom(){
    FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String myUserName= snapshot.getValue(User.class).getUsername();
                    if (usernameOfRoommate.compareTo(myUserName)>0){
                        chatRoomid= myUserName+ usernameOfRoommate;
                    }else if (usernameOfRoommate.compareTo(myUserName)==0){
                        chatRoomid= myUserName+ usernameOfRoommate;
                    }
                    attachMessage(chatRoomid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
}

private void attachMessage(String chatRoomid){

        FirebaseDatabase.getInstance().getReference("message/"+chatRoomid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

messages.clear();
for (DataSnapshot dataSnapshot:snapshot.getChildren()){
    messages.add(dataSnapshot.getValue(Message.class));
}
messageAdapter.notifyDataSetChanged();
recyclerView.scrollToPosition(messages.size()-1);
recyclerView.setVisibility(View.VISIBLE);
progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


}
}
