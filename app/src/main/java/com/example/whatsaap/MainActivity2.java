package com.example.whatsaap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private ArrayList<NoteModel> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle(R.string.all_notes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
    }

    public void openNoteDetailsActivity(View view) {
        Intent i = new Intent(MainActivity2.this,NoteDetails.class);
        startActivity(i);
    }


    // get data from data base
    private void getNotes(){
        // to fix duplicated values
        notes.clear();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM NOTA ",null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            notes.add(new NoteModel(id,title,desc));
        }
        listNotes();
    }

    private void listNotes(){
        View view = findViewById(R.id.layout_no_notes);
        if(notes.size()==0)
            view.setVisibility(View.VISIBLE);
        else{
            view.setVisibility(View.INVISIBLE);
            adapter = new NoteAdapter(this,notes);
            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setAdapter(adapter);
            swipeToDelete();
        }
    }

    private void swipeToDelete(){
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0
                ,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDeleteDialog(position);


            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }
    private void deleteFromDB(int position){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] args = {notes.get(position).getId()+""};
        // DELETE FROM NOTA WHERE _id == notes.get(position).getID()
        int deletedRows=  db.delete("NOTA","_id ==?",args);
        if(deletedRows !=0)
            Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();

    }
    private void showDeleteDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete_dialog_positive, (dialog, which) -> {
                    deleteFromDB(position);
                    // To notify array list and adapter that some data deleted
                    notes.remove(position);
                    adapter.notifyDataSetChanged();
                    if(notes.size()==0){
                        View view = findViewById(R.id.layout_no_notes);
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(R.string.delete_dialog_negative, (dialog, which) -> {
                    adapter.notifyItemChanged(position);
                })
                // avoid problem by clicking in any place outside the dialog or back button
                .setCancelable(false)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item_settings){
            Intent i = new Intent(MainActivity2.this , SettingsActivity.class);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

}