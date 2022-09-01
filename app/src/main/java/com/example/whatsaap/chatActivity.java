package com.example.whatsaap;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class chatActivity extends AppCompatActivity  {

    TabLayout tabLayout;
    TabItem mchat,mcall,mstatus;
    ViewPager viewPager;

    com.example.whatsaap.PagerAdapter pagerAdapter;
    androidx.appcompat.widget.Toolbar mtoolbar;

    FirebaseAuth firebaseAuth;


    FirebaseFirestore firebaseFirestore;

ArrayList<String> userss = new ArrayList<>();
ArrayList<String> names = new ArrayList<>();

    String reciv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tabLayout=findViewById(R.id.include);
        mchat=findViewById(R.id.chat);
     //   mcall=findViewById(R.id.calls);
        mstatus=findViewById(R.id.status);
        viewPager=findViewById(R.id.fragmentcontainer);


        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        mtoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


        userss.add("CksBLhEQM3Uoz0QORxVUtsTx4QF2");  userss.add("fL6r8fAzhiY306GqqmKvLrFjJxl2"); userss.add("1wwbjhrjiOZXh9QKs3ugaCb7ZPw2");
        userss.add("NizvYCKrrLOpqFIsXgoJqH3YxxE2"); userss.add("TqwHF5voOXQUTgI4DsCXCGWzd443");  userss.add("rJMOBbDTaYX3Q4OT59nIBQuA7G92"); userss.add("zjSdJfgMSPTiUWXuiLeuVd89nBf2");
        userss.add("oT4A5FPLCfX5tYrN1rWE5Yk4z0M2"); userss.add("IY65dp0CbzXX0IYuzIjwM5OCU8D2"); userss.add("7Tn4RaFfPJVB7nhWwQa7kUWNeFP2"); userss.add("CTEgvxW34kUll9yejchQYLSme6w2");
        userss.add("QigYQXKYKwdXafFxnqH1jWJeDBs1"); userss.add("pL6Pc4U5NgNht9tzw1eX0fCzcCU2");


        names.add("Ahmed Shehata"); names.add("كابتن ماجد"); names.add("Fras"); names.add("Abdelrahman Moustafa"); names.add("abdelrahman elshahat");
        names.add("Ahmed Abdrabou"); names.add("Mostafa"); names.add("Mohamed Salah"); names.add("S3"); names.add("عويس الهكر قدن"); names.add("Badawi");
        names.add("Snow"); names.add("Kachwa3");


       FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        for(int i=0; i<userss.size();i++){


            if(userss.get(i).equals(firebaseAuth.getCurrentUser().getUid())){
                reciv = userss.get(i);
                userss.remove(i);
                names.remove(i);

            }

        }


        for (int i=0;i<userss.size();i++){
            String s = names.get(i);
            String n = userss.get(i);


            ref.child("chats").child(firebaseAuth.getCurrentUser().getUid()+userss.get(i)).child("messages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()) {
                       sendNotification(s + " Sent You Message ");


                 }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
       }
     /*   ref.child("chats").child(firebaseAuth.getCurrentUser().getUid()+reciv).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                sendNotification("Click To Go");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */









        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_more_vert_24);
        mtoolbar.setOverflowIcon(drawable);


        pagerAdapter=new com.example.whatsaap.PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 /*|| tab.getPosition()==2*/)
                {
                    pagerAdapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));






    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.profile:
                Intent intent=new Intent(chatActivity.this,ProfileActivity.class);
                Intent ii = getIntent();
                startActivity(ii);
                startActivity(intent);

                break;

            case R.id.settings:


                Intent i = new Intent(chatActivity.this,SettingsActivity2.class);
                 ii = getIntent();
                finish();
                startActivity(ii);
                startActivity(i);

                break;



        }



        return  true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);


        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }



    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_rabbit)
                        .setContentTitle("You Have New Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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