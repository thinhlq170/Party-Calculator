package com.example.partycalculator.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.partycalculator.R;
import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;
import com.example.partycalculator.repositories.MemberRepo;

import java.util.ArrayList;

public class PartyListViewAdapter extends BaseAdapter {

    final ArrayList<Party> lstParty;

    public PartyListViewAdapter(ArrayList<Party> lstParty) {
        this.lstParty = lstParty;
    }

    @Override
    public int getCount() {
        return lstParty.size();
    }

    @Override
    public Object getItem(int position) {
        return lstParty.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstParty.get(position).getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewParty;

        if(convertView == null) {
            viewParty = View.inflate(parent.getContext(), R.layout.party_view, null);
        } else {
            viewParty = convertView;
        }

        //bind du lieu vao view
        MemberRepo memberRepo = new MemberRepo();
        Party party = (Party) getItem(position);
        viewParty.setId(Math.toIntExact(party.getId())); // set id row = partyId
        ArrayList<Member> lstMem = memberRepo.getListMemberByPartyId(party.getId());
        ((TextView) viewParty.findViewById(R.id.numberMem)).setText(String.format("Số người: %d", lstMem.size()));
        ((TextView) viewParty.findViewById(R.id.nameparty)).setText(String.format("Tiệc: %s", party.getName()));
        if(party.getUpdateDate() != null && !party.getUpdateDate().isEmpty()) {
            ((TextView) viewParty.findViewById(R.id.dateparty)).setText(String.format("Last modified: %s\nCreated: %s", party.getUpdateDate(), party.getDate()));
        } else {
            ((TextView) viewParty.findViewById(R.id.dateparty)).setText(String.format("Created: %s", party.getDate()));
        }
        return viewParty;
    }
}
