package com.example.partycalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.partycalculator.adapters.PartyListViewAdapter;
import com.example.partycalculator.models.Party;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {

    public ArrayList<Party> lstParty = new ArrayList<>();
    PartyListViewAdapter partyListViewAdapter;
    ListView lstViewParty;
    FloatingActionButton addButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String duration = dateFormat.format(date);

        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.list_party);

        addButton = findViewById(R.id.addparty);
        addButton.setOnClickListener(view -> {
            int position = lstParty.size() + 1;
            lstParty.add(new Party(position, "Party" + position, duration));
            showListParty(lstParty);
        });
        showListParty(lstParty);

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