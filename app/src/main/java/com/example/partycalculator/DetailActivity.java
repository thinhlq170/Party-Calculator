package com.example.partycalculator;

import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.partycalculator.models.Member;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DetailActivity extends Activity {

    Toolbar detailToolbar;
    Button buttonV;
    TableLayout tbLayout;
    ArrayList<Member> lstMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tbLayout = findViewById(R.id.tableDetail);
        detailToolbar = findViewById(R.id.toolbar);
        buttonV = findViewById(R.id.addMemberButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            detailToolbar.setTitle(bundle.getString("PartyName"));
        }

        buttonV.setOnClickListener(v -> {
            addRow(tbLayout);
        });

        addTable(tbLayout);

    }

    public void addTable(TableLayout layout) {

        TableRow tbr = new TableRow(this);

        //table header
        TextView tv1 = new TextView(this);
        tv1.setText(R.string.name);
        tv1.setTextColor(Color.BLACK);
        setPad(tv1);
        tbr.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(R.string.phone);
        tv2.setTextColor(Color.BLACK);
        setPad(tv2);
        tbr.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(R.string.paid_amount);
        tv3.setTextColor(Color.BLACK);
        setPad(tv3);
        tbr.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(R.string.change_amount);
        tv4.setTextColor(Color.BLACK);
        setPad(tv4);
        tbr.addView(tv4);

        TextView tv5 = new TextView(this);
        tv5.setText(R.string.join_date);
        tv5.setTextColor(Color.BLACK);
        setPad(tv5);
        tbr.addView(tv5);

        layout.addView(tbr);
    }

    private void setPad(TextView view) {
        int size = 60;
        view.setPadding(size, size, size, size);
        view.setGravity(Gravity.CENTER);
    }

    private void addRow(TableLayout layout) {
        TableRow tbr = new TableRow(this);


        EditText nameTv = new EditText(this);
        nameTv.setTextColor(Color.GRAY);
        setPad(nameTv);
        tbr.addView(nameTv);

        EditText phoneTv = new EditText(this);
        phoneTv.setTextColor(Color.GRAY);
        setPad(phoneTv);
        tbr.addView(phoneTv);

        EditText paidTv = new EditText(this);
        paidTv.setTextColor(Color.GRAY);
        paidTv.setInputType(InputType.TYPE_CLASS_NUMBER);
        setPad(paidTv);
        tbr.addView(paidTv);

        TextView changeTv = new EditText(this);
        changeTv.setTextColor(Color.GRAY);
        setPad(changeTv);
        tbr.addView(changeTv);

        TextView joinDateTv = new EditText(this);
        joinDateTv.setTextColor(Color.GRAY);
        setPad(joinDateTv);
        tbr.addView(joinDateTv);

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String today = dateFormat.format(date);

        String memName = nameTv.getText().toString();
        String memPhone = phoneTv.getText().toString();
        String memPaidStr = paidTv.getText().toString();
        BigDecimal memChange = BigDecimal.ZERO;
        BigDecimal memPaid = BigDecimal.ZERO;
        if(memPaidStr != null && !memPaidStr.isEmpty()) {
            memPaid = new BigDecimal(memPaidStr);
        }

        Member member = new Member(memName, memPhone, memPaid, memChange, today);
        lstMember.add(member);
        layout.addView(tbr);
    }
}