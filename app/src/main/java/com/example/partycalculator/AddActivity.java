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

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddActivity extends Activity implements View.OnClickListener{

    ArrayList<Member> lstMember = new ArrayList<>();
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
    }

    private void addView() {
        @SuppressLint("InflateParams")
        final View memberView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);
        ImageView imageClose = (ImageView)memberView.findViewById(R.id.image_remove);
        imageClose.setOnClickListener(v -> removeView(memberView));
        layoutList.addView(memberView);
    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }


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
                    bundle.putSerializable("list", lstMember);
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

            EditText editNameText = (EditText)memView.findViewById(R.id.edit_member_name);
            EditText editChaneAmountText = (EditText)memView.findViewById(R.id.edit_change_amount);
            EditText editPhoneText = (EditText)memView.findViewById(R.id.edit_phone);

            Member member = new Member();
            String name = editNameText.getText().toString();
            String changeAmountTxt = editChaneAmountText.getText().toString();
            String phone = editPhoneText.getText().toString();

            if(!isNullOrEmpty(name)) {
                member.setName(name);
            } else {
                result = false;
                break;
            }

            if(!isNullOrEmpty(changeAmountTxt)) {
                BigDecimal changeAmount = new BigDecimal(changeAmountTxt);
                member.setChangeAmount(changeAmount);
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

            lstMember.add(member);
        }

        if(lstMember.size() == 0) {
            result = false;
            Toast.makeText(this, "Add member first!", Toast.LENGTH_SHORT).show();
        } else if (!result) {
            Toast.makeText(this, "Enter all details correctly!", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private boolean isNullOrEmpty(String text) {
        boolean result = true;
        if(text != null && !text.isEmpty()) {
            result = false;
        }
        return result;
    }
}