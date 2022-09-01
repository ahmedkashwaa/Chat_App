package com.example.whatsaap;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;

    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;
Activity activity;
    boolean s=false ;
    String getSenderId;
    String message;
    java.util.HashMap<String,String> HashMap=new HashMap<String,String>();


    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.senderchatlayout,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.recieverchatlayout,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);


        Messages wow = messagesArrayList.get(messagesArrayList.size()-1);

        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.textViewmessaage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());


        }
        else
        {
            RecieverViewHolder viewHolder=(RecieverViewHolder)holder;
            viewHolder.textViewmessaage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());



        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance() ;
        DatabaseReference ref = database.getReference();

     getSenderId = messages.getSenderId();
     message = messages.getMessage();
      //  if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {

      //      Toast.makeText(context, "dont send"+messages.getMessage(), Toast.LENGTH_SHORT).show();
      //  }
      //  else {
     //       Toast.makeText(context, "Send now"+messages.getMessage(), Toast.LENGTH_SHORT).show();
     //   }
        HashMap.put("CksBLhEQM3Uoz0QORxVUtsTx4QF2","Ahmed Shehata"); HashMap.put("fL6r8fAzhiY306GqqmKvLrFjJxl2","كابتن ماجد");
        HashMap.put("1wwbjhrjiOZXh9QKs3ugaCb7ZPw2","Fras"); HashMap.put("NizvYCKrrLOpqFIsXgoJqH3YxxE2","Abdelrahman Moustafa"); HashMap.put("TqwHF5voOXQUTgI4DsCXCGWzd443","abdelrahman elshahat");
        HashMap.put("rJMOBbDTaYX3Q4OT59nIBQuA7G92","Ahmed Abdrabou"); HashMap.put("zjSdJfgMSPTiUWXuiLeuVd89nBf2","Mostafa");
        HashMap.put("oT4A5FPLCfX5tYrN1rWE5Yk4z0M2","Mohamed Salah"); HashMap.put("IY65dp0CbzXX0IYuzIjwM5OCU8D2","S3");
        HashMap.put("7Tn4RaFfPJVB7nhWwQa7kUWNeFP2","عويس الهكر قدن"); HashMap.put("CTEgvxW34kUll9yejchQYLSme6w2","Badawi");
        HashMap.put("QigYQXKYKwdXafFxnqH1jWJeDBs1","Snow"); HashMap.put("pL6Pc4U5NgNht9tzw1eX0fCzcCU2","Kachwa3");



        ref.child("chats").child(firebaseAuth.getCurrentUser().getUid()+messages.getSenderId()).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                   if (!(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))) {

                       RecieverViewHolder viewHolder=(RecieverViewHolder)holder;
                       String o =  viewHolder.textViewmessaage.getText().toString();
                       sendNotification(HashMap.get(messages.getSenderId()) + " by2olk : " + o);

                   }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);



        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))




        {

            return  ITEM_SEND;
        }
        else
        {

            return ITEM_RECIEVE;
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }








    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewmessaage;
        TextView timeofmessage;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewmessaage;
        TextView timeofmessage;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_rabbit)
                        .setContentTitle("You Have New Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }









}
