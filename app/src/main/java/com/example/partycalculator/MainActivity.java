package com.example.partycalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.example.partycalculator.adapters.PartyListViewAdapter;
import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {

    ArrayList<Party> lstParty = new ArrayList<>();
    PartyListViewAdapter partyListViewAdapter;
    ListView lstViewParty;
    FloatingActionButton addButton;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String duration = dateFormat.format(date);

        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.list_party);
        showListParty(lstParty);

        addButton = findViewById(R.id.addparty);
        addButton.setOnClickListener(view -> {
//            int position = lstParty.size() + 1;
//            lstParty.add(new Party(position, "Party" + position, duration));
//            showListParty(lstParty);
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        lstViewParty.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("PartyName", lstParty.get(position).name);
            startActivity(intent);
        });


    }

    private void showListParty(ArrayList<Party> lstParty) {
        partyListViewAdapter = new PartyListViewAdapter(lstParty);
        lstViewParty = findViewById(R.id.listparty);
        lstViewParty.setAdapter(partyListViewAdapter);
    }


}