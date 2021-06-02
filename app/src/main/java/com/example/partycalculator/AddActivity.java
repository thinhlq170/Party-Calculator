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
import android.widget.Toast;

import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddActivity extends Activity implements View.OnClickListener{

    ArrayList<Member> lstMember = new ArrayList<>();
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    EditText partyNameTv;
    Party party;

    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    String today = dateFormat.format(date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        partyNameTv = findViewById(R.id.party_name);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);

        if(getIntent().getExtras() != null) {
            party = (Party)getIntent().getExtras().get("partyObj");
            partyNameTv.setText(party.getName());
            if(party.getLstMember().size() > 0) {
                addViewPartyDetail(party.getLstMember());
            }
        }
    }

    private void addView() {
        @SuppressLint("InflateParams")
        final View memberView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);
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
                addView();
                break;
            case R.id.button_submit_list:
                if(checkIfValidAndRead()) {
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    String partyName = partyNameTv.getText().toString();
                    Party party = new Party();
                    party.setName(partyName);
                    party.setDate(today);
                    party.setLstMember(lstMember);
                    bundle.putSerializable("list", lstMember);
                    intent.putExtra("partyObj", party);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
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
            EditText editPhoneText = memView.findViewById(R.id.edit_phone);

            Member member = new Member();
            String name = editNameText.getText().toString();
            String paidAmountTxt = editPaidAmountText.getText().toString();
            String phone = editPhoneText.getText().toString();

            if(!isNullOrEmpty(name)) {
                member.setName(name);
            } else {
                result = false;
                break;
            }

            if(!isNullOrEmpty(paidAmountTxt)) {
                BigDecimal paidAmount = new BigDecimal(paidAmountTxt);
                member.setPaidAmount(paidAmount);
            } else {
                result = false;
                break;
            }

            if(!isNullOrEmpty(phone)) {
                member.setPhone(phone);
            } else {
                result = false;
                break;
            }

            member.setChangeAmount(BigDecimal.ZERO);
            member.setJoinDate(today);

            lstMember.add(member);
        }

        String partyName = partyNameTv.getText().toString();

        if(isNullOrEmpty(partyName)) {
            result = false;
            Toast.makeText(this, "Enter party name!", Toast.LENGTH_SHORT).show();
        } else if(lstMember.size() == 0) {
            result = false;
            Toast.makeText(this, "Please add member!", Toast.LENGTH_SHORT).show();
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

    private void addViewPartyDetail(ArrayList<Member> lstMem) {
        for(int i = 0; i < lstMem.size(); i++) {
            final View memView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);
            EditText editNameText = memView.findViewById(R.id.edit_member_name);
            EditText editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);
            EditText editPhoneText = memView.findViewById(R.id.edit_phone);

            Member member = lstMem.get(i);

            String phone = isNullOrEmpty(member.getPhone()) ? "" : member.getPhone();

            editNameText.setText(member.getName());
            editPaidAmountText.setText(member.getPaidAmount().toString());
            editPhoneText.setText(phone);
            ImageView imageClose = memView.findViewById(R.id.image_remove);
            imageClose.setOnClickListener(v -> removeView(memView));
            layoutList.addView(memView);
        }
    }
}