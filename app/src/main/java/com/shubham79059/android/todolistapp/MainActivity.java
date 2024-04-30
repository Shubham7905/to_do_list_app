package com.shubham79059.android.todolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shubham79059.android.todolistapp.adapter.WorksAdapter;
import com.shubham79059.android.todolistapp.db.DatabaseHelper;
import com.shubham79059.android.todolistapp.db.entity.Work;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Variables
    private WorksAdapter worksAdapter;
    private ArrayList<Work> workArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My To Do List");

        // RecyclerView
        recyclerView = findViewById(R.id.recycler_view_works);
        db = new DatabaseHelper(this);

        // Works List
        workArrayList.addAll(db.getAllWorks());

        worksAdapter = new WorksAdapter(this, workArrayList, MainActivity.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(worksAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditWorks(false, null, -1);
            }
        });

    }

    public void addAndEditWorks(final boolean isUpdated, final Work work, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_work, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView workTitle = view.findViewById(R.id.new_work_title);
        final EditText newWork = view.findViewById(R.id.name);
        final EditText workDesc = view.findViewById(R.id.desc);

        workTitle.setText(!isUpdated ? "Add New Work" : "Edit Work");

        if (isUpdated && work != null){
            newWork.setText(work.getName());
            workDesc.setText(work.getDesc());
        }

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(isUpdated ? "Updated" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isUpdated){
                                    DeleteWork(work, position);
                                }
                                else{
                                    dialogInterface.cancel();
                                }
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(newWork.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter a Work", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    alertDialog.dismiss();
                }
                if (isUpdated && work != null){
                    UpdateWork(newWork.getText().toString(), workDesc.getText().toString(), position);
                }
                else{
                    CreateWork(newWork.getText().toString(), workDesc.getText().toString());
                }
            }
        });

    }

    private void DeleteWork(Work work, int position) {

        workArrayList.remove(position);
        db.deleteWork(work);
        worksAdapter.notifyDataSetChanged();

    }

    private void UpdateWork(String name, String desc, int position){

        Work work = workArrayList.get(position);

        work.setName(name);
        work.setDesc(desc);

        db.updateWork(work);

        workArrayList.set(position, work);
        worksAdapter.notifyDataSetChanged();

    }

    private void CreateWork(String name, String desc){

        long id = db.insertWork(name, desc);
        Work work = db.getWork(id);

        if (work != null){
            workArrayList.add(0, work);
            worksAdapter.notifyDataSetChanged();
        }

    }

    // Menu Bar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}