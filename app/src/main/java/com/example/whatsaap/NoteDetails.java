package com.example.whatsaap;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class NoteDetails extends ParentActivity {
 private EditText titleET;
 private EditText descET;

 private int receivedID;
 private boolean openAsUpdate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleET = findViewById(R.id.et_title);
        descET = findViewById(R.id.et_describtion);


        receivedID = getIntent().getIntExtra("id",-1);
        if (receivedID != -1){
            setTitle(R.string.update_note);
            titleET.setText(getIntent().getStringExtra("title"));
            descET.setText(getIntent().getStringExtra("desc"));
            Button update_btn = findViewById(R.id.btn_update);
            update_btn.setVisibility(View.VISIBLE);
            openAsUpdate = true;
        } else {

            setTitle(R.string.add_note);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       if(openAsUpdate==true){
           return false;
       }
       else {
           MenuInflater inflater = getMenuInflater();
           inflater.inflate(R.menu.save_note_menu,menu);


           return true;
       }

        //inflater

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item_save_note){
            // save note
            saveNote();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveNote(){
        String writtenTitle = titleET.getText().toString();
        String writtenDesc = descET.getText().toString();
        if(writtenTitle.isEmpty())
           // red error will appear
            titleET.setError(getString(R.string.required_field));

        else{
            ContentValues values = new ContentValues();
            values.put("title",writtenTitle);
            values.put("describtion",writtenDesc);
            DBHelper helper = new DBHelper(this);
          SQLiteDatabase db = helper.getWritableDatabase();
          // insert into NOTA ( title , description) values (writtenTitle , writtenDesc)
           long id=   db.insert("NOTA", null,values);
            if(id!= -1){
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
                finish();
            }


        }
    }

    public void UpdateNote(View view) {
        String writtenTitle = titleET.getText().toString();
        String writtenDesc = descET.getText().toString();
        if(writtenTitle.isEmpty())
            // red error will appear
            titleET.setError(getString(R.string.required_field));
        else {
            ContentValues values = new ContentValues();
            values.put("title",writtenTitle);
            values.put("describtion",writtenDesc);
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            // UPDATE NOTA SET title = writtenTitle , description = writtenDesc
            String [] whereArgs = {String.valueOf(receivedID)};
           int id = db.update("NOTA",values,"_id==?",whereArgs);
           if (id != 0){

               Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
               finish();
           }
        }
    }
}