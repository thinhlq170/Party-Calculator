package com.example.partycalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.example.partycalculator.adapters.PartyListViewAdapter;
import com.example.partycalculator.models.Party;
import com.example.partycalculator.repositories.PartyRepo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends Activity {

    PartyListViewAdapter partyListViewAdapter;
    ListView lstViewParty;
    FloatingActionButton addButton;
    Toolbar toolbar;
    ArrayList<Party> lstPartyDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PartyRepo partyRepo = new PartyRepo();

        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.list_party);
        lstPartyDto = partyRepo.getListAllParty();
        Collections.sort(lstPartyDto);
        showListParty(lstPartyDto);

        addButton = findViewById(R.id.addparty);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
            Party party = new Party();
            party.setDate(getCurrentTime());
            long partyId = partyRepo.insert(party);
            intent.putExtra("partyId", partyId);
            startActivity(intent);
        });

        lstViewParty.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
            intent.putExtra("partyId", id);
            startActivity(intent);
        });
        lstViewParty.setOnItemLongClickListener((parent, view, position, id) -> {
            final int pos = position;
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(R.drawable.ic_recycle_bin)
                    .setTitle(getResources().getString(R.string.delete_party_item_title))
                    .setMessage(getResources().getString(R.string.delete_party_message))
                    .setPositiveButton(getResources().getString(R.string.accept_text_dialog),
                            (dialog, which) -> {
                                lstPartyDto.remove(pos);
                                partyRepo.deleteGroceriesByPartyId(id);
                                partyRepo.deleteMembersByPartyId(id);
                                partyRepo.deleteARow(id);
                                partyListViewAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getResources().getString(R.string.deny_text_dialog), null)
                    .show();
            return true;
        });
    }

    private void showListParty(ArrayList<Party> lstParty) {
        partyListViewAdapter = new PartyListViewAdapter(lstParty);
        lstViewParty = findViewById(R.id.listparty);
        lstViewParty.setAdapter(partyListViewAdapter);
    }

    private String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}