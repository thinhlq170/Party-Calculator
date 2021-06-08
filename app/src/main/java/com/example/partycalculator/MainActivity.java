package com.example.partycalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.example.partycalculator.adapters.PartyListViewAdapter;
import com.example.partycalculator.dtos.PartyDTO;
import com.example.partycalculator.models.Party;
import com.example.partycalculator.repositories.PartyRepo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    ArrayList<Party> lstParty;
    PartyListViewAdapter partyListViewAdapter;
    ListView lstViewParty;
    FloatingActionButton addButton;
    Toolbar toolbar;
    ArrayList<PartyDTO> lstPartyDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PartyRepo partyRepo = new PartyRepo();

        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.list_party);
        lstPartyDto = partyRepo.getListAllParty();
//        if(getIntent().getExtras() != null) {
//            lstParty = (ArrayList<Party>) getIntent().getSerializableExtra("lstParty");
//            if(lstParty == null) {
//                lstParty = new ArrayList<>();
//            }
//        } else {
//            lstParty = new ArrayList<>();
//        }
        Collections.sort(lstPartyDto);
        showListParty(lstPartyDto);

        addButton = findViewById(R.id.addparty);
        addButton.setOnClickListener(view -> {
//            int position = lstParty.size() + 1;
//            lstParty.add(new Party(position, "Party" + position, duration));
//            showListParty(lstParty);
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            PartyDTO dto = new PartyDTO();
            int partyId = partyRepo.insert(dto);
            intent.putExtra("partyId", partyId);
            startActivity(intent);
        });



        lstViewParty.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
//            if(lstParty.get(position) != null) {
//                intent.putExtra("partyObj", lstParty.get(position));
//            }
//            intent.putExtra("lstParty", (Serializable) lstParty);
            intent.putExtra("partyId", (int)id);
            startActivity(intent);
        });
    }

    private void showListParty(ArrayList<PartyDTO> lstParty) {
        partyListViewAdapter = new PartyListViewAdapter(lstParty);
        lstViewParty = findViewById(R.id.listparty);
        lstViewParty.setAdapter(partyListViewAdapter);
    }


}