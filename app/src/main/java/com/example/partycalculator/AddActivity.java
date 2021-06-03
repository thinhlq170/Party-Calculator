package com.example.partycalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends Activity implements View.OnClickListener{

    ArrayList<Member> lstMember = new ArrayList<>();
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    Button buttonCalculate;
    EditText partyNameTv;
    Party party;
    ArrayList<Party> lstParty;
    Boolean isViewDetail = Boolean.FALSE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        buttonCalculate = findViewById(R.id.button_calculating);
        partyNameTv = findViewById(R.id.party_name);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
        buttonCalculate.setOnClickListener(this);

        // View detail
        if(getIntent().getExtras() != null) {
            party = (Party)getIntent().getExtras().get("partyObj");
            lstParty = (ArrayList<Party>) getIntent().getSerializableExtra("lstParty");
            if(party != null) { //view detail1
                partyNameTv.setText(party.getName());
                if(party.getLstMember().size() > 0) {
                    addViewPartyDetail(party.getLstMember());
                    calculateAmount();
                }
                isViewDetail = Boolean.TRUE;
            } else { //add party
                party = new Party();
                isViewDetail = Boolean.FALSE;
            }

            if(lstParty == null) {
                lstParty = new ArrayList<>();
            }
        }
    }

    private void addViewAddParty() {
        @SuppressLint("InflateParams")
        final View memberView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);
        EditText editChangeAmountText = memberView.findViewById(R.id.change_amount);
        TextView editJoinDateText = memberView.findViewById(R.id.join_date);

        // set default value for change amount and join date
        editChangeAmountText.setText("0");
        editChangeAmountText.setEnabled(false);
        editJoinDateText.setText(getCurrentTime());
        // end set default value for change amount and join date

        ImageView imageClose = memberView.findViewById(R.id.image_remove);
        imageClose.setOnClickListener(v -> removeView(memberView));
        layoutList.addView(memberView);
    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                addViewAddParty();
                break;
            case R.id.button_submit_list:
                if(checkIfValidAndRead()) {
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    //remove old party and create new list party
                    ArrayList<Party> lstPartyNew = new ArrayList<>();
                    if(lstParty != null) {
                        for(int i = 0; i < lstParty.size(); i++) {
                            if(!lstParty.get(i).getId().equals(party.getId())) {
                                lstPartyNew.add(lstParty.get(i));
                            }
                        }
                    }
                    String partyName = partyNameTv.getText().toString();
                    party.setName(partyName);
                    party.setLstMember(lstMember);
                    if(isViewDetail.equals(Boolean.TRUE)) {
                        party.setUpdateDate(getCurrentTime());
                    } else {
                        party.setDate(getCurrentTime());
                    }
                    lstPartyNew.add(party);
                    Collections.sort(lstPartyNew);
                    intent.putExtra("lstParty", (Serializable) lstPartyNew);
                    startActivity(intent);
                }
                break;
            case R.id.button_calculating:
                calculateAmount();
                break;
        }
    }

    private boolean checkIfValidAndRead() {
        lstMember.clear();
        boolean result = true;

        for(int i = 0; i < layoutList.getChildCount(); i++) {
            View memView = layoutList.getChildAt(i);

            EditText editNameText = memView.findViewById(R.id.edit_member_name);
            EditText editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);
            TextView joinDateTv = memView.findViewById(R.id.join_date);

            Member member = new Member();
            String name = editNameText.getText().toString();
            String paidAmountTxt = editPaidAmountText.getText().toString();
            String jDate = joinDateTv.getText().toString();

            if(!isNullOrEmpty(name)) {
                member.setName(name);
            } else {
                result = false;
                editNameText.setError(getString(R.string.name_is_required));
            }

            if(!isNullOrEmpty(paidAmountTxt)) {
                BigDecimal paidAmount = new BigDecimal(paidAmountTxt);
                member.setPaidAmount(paidAmount);
            } else {
                editPaidAmountText.setError(getString(R.string.paid_amount_is_required));
                result = false;
            }

            member.setChangeAmount(BigDecimal.ZERO);
            member.setJoinDate(jDate);

            lstMember.add(member);
        }

        String partyName = partyNameTv.getText().toString();

        if(isNullOrEmpty(partyName)) {
            result = false;
            Toast.makeText(this, "Enter party name!", Toast.LENGTH_SHORT).show();
        } else if(lstMember.size() == 0) {
            result = false;
            Toast.makeText(this, "Please add member or details correctly!", Toast.LENGTH_SHORT).show();
        }  else if (!result) {
            Toast.makeText(this, "Enter all details correctly!", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private Boolean isNullOrEmpty(String text) {
        boolean result = true;
        if(text != null && !text.isEmpty()) {
            result = false;
        }
        return result;
    }

    @SuppressLint("SetTextI18n")
    private void addViewPartyDetail(ArrayList<Member> lstMem) {
        for(int i = 0; i < lstMem.size(); i++) {
            @SuppressLint("InflateParams")
            final View memView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);
            EditText editNameText = memView.findViewById(R.id.edit_member_name);
            EditText editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);
            EditText editChangeAmountText = memView.findViewById((R.id.change_amount));
            TextView editJoinDate = memView.findViewById(R.id.join_date);

            Member member = lstMem.get(i);

            String phone = isNullOrEmpty(member.getPhone()) ? "" : member.getPhone();

            editNameText.setText(member.getName());
            editPaidAmountText.setText(member.getPaidAmount().toString());
            editChangeAmountText.setText(member.getChangeAmount().toString());
            editJoinDate.setText(member.getJoinDate());

            ImageView imageClose = memView.findViewById(R.id.image_remove);
            imageClose.setOnClickListener(v -> removeView(memView));
            layoutList.addView(memView);
        }
    }

    private String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        return currentTime;
    }

    private void calculateAmount() {
        if(layoutList.getChildCount() <= 0) {
            Toast.makeText(this, "Please add member or details correctly!", Toast.LENGTH_SHORT).show();
        } else {
            BigDecimal totalAmount = BigDecimal.ZERO;
            int memberNumber = layoutList.getChildCount();

            //Calculating total amount
            for(int i = 0; i < memberNumber; i++) {
                View memView = layoutList.getChildAt(i);
                EditText paidAmountText = memView.findViewById(R.id.edit_paid_amount);
                String paidAmountStr = paidAmountText.getText().toString();

                BigDecimal paidAmount = BigDecimal.ZERO;
                if(!isNullOrEmpty(paidAmountStr))
                    paidAmount = new BigDecimal(paidAmountStr);
                totalAmount = totalAmount.add(paidAmount);

            }
            BigDecimal averageAmount = totalAmount.divide(BigDecimal.valueOf(memberNumber), 2, RoundingMode.CEILING);
            party.setTotalAmount(totalAmount);
            party.setAverageAmount(averageAmount);

            //Calculating change amount for every member
            for(int i = 0; i < memberNumber; i++) {
                View memView = layoutList.getChildAt(i);
                TextView changeAmountText = memView.findViewById(R.id.change_amount);
                EditText paidAmountText = memView.findViewById(R.id.edit_paid_amount);
                String paidAmountStr = paidAmountText.getText().toString();


                BigDecimal paidAmount = new BigDecimal("0");
                if(!isNullOrEmpty(paidAmountStr))
                    paidAmount = new BigDecimal(paidAmountStr);

                BigDecimal changeAmount = paidAmount.subtract(averageAmount);
                changeAmountText.setText(getCurrencyFormat(changeAmount));
            }

            ((TextView)findViewById(R.id.total_amount)).setText(String.format("Total Amount: %s", getCurrencyFormat(totalAmount)));
            ((TextView)findViewById(R.id.average_amount)).setText(String.format("Average Amount: %s", getCurrencyFormat(averageAmount)));
        }
    }

    private String getCurrencyFormat(BigDecimal n) {
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
        return df.format(n);
    }
}