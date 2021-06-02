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
    Party party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.list_party);
        if(getIntent().getExtras() != null) {
            party = (Party) getIntent().getExtras().get("partyObj");
            lstParty.add(party);
        }
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
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            if(lstParty.get(position) != null) {
                intent.putExtra("partyObj", lstParty.get(position));
            }
            startActivity(intent);
        });


    }

    private void showListParty(ArrayList<Party> lstParty) {
        partyListViewAdapter = new PartyListViewAdapter(lstParty);
        lstViewParty = findViewById(R.id.listparty);
        lstViewParty.setAdapter(partyListViewAdapter);
    }


}