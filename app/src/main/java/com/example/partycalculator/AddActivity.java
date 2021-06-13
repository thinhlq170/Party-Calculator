package com.example.partycalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;
import com.example.partycalculator.repositories.MemberRepo;
import com.example.partycalculator.repositories.PartyRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddActivity extends Activity implements View.OnClickListener{

    ArrayList<Member> lstMember = new ArrayList<>();
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    Button buttonCalculate;
    EditText partyNameTv;
    ScrollView scrollLstMemView;
    private static final int CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE = 8;
    Long partyId;
    Party party;
    PartyRepo partyRepo;
    MemberRepo memberRepo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);
        buttonCalculate = findViewById(R.id.button_calculating);
        partyNameTv = findViewById(R.id.party_name);
        scrollLstMemView = findViewById(R.id.scroll_list_mem);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
        buttonCalculate.setOnClickListener(this);
        partyRepo = new PartyRepo();
        memberRepo = new MemberRepo();

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //convert dp to pixels
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            ViewGroup.LayoutParams params = scrollLstMemView.getLayoutParams();
            params.height = height;
            scrollLstMemView.setLayoutParams(params);
        }

        // View detail
        if(getIntent().getExtras() != null) {
            partyId = getIntent().getExtras().getLong("partyId");
            party = partyRepo.getPartyById(partyId);
            partyNameTv.setText(party.getName());
            if(party != null)
                lstMember = memberRepo.getListMemberByPartyId(party.getId());
        } else {
            party = new Party();
            lstMember = new ArrayList<>();
        }
        if(lstMember != null && lstMember.size() > 0) {
            addViewPartyDetail(lstMember);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"SetTextI18n", "ResourceType", "InflateParams"})
    private void addMemberView(Member member) {
        final View memberView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);

        ImageView imagePurchase = memberView.findViewById(R.id.image_purchase_detail);
        TextView changeAmountText = memberView.findViewById(R.id.change_amount);
        EditText editNameText = memberView.findViewById(R.id.edit_member_name);
        TextView paidAmountText = memberView.findViewById(R.id.edit_paid_amount);
        String changeAmountStr;
        String paidAmountStr;

        if(member != null) {
            memberView.setId(Math.toIntExact(member.getId()));
            editNameText.setText(member.getName());
            paidAmountText.setText(member.getPaidAmount().toString());
            paidAmountStr = member.getPaidAmount() == null ? "" : getCurrencyFormat(member.getPaidAmount());
            changeAmountText.setText(member.getChangeAmount().toString());
            changeAmountStr = member.getChangeAmount() == null ? "" : getCurrencyFormat(member.getChangeAmount());
        } else {
            member = new Member();
            changeAmountStr = changeAmountText.getText().toString();
            paidAmountStr = changeAmountText.getText().toString();
        }

        changeAmountText.setOnClickListener(v -> {
            showWhenTextOversize(memberView, changeAmountStr);
        });
        paidAmountText.setOnClickListener(v -> {
            showWhenTextOversize(memberView, paidAmountStr);
        });

        ImageView imageClose = memberView.findViewById(R.id.image_remove);
        Member finalMember = member;
        imageClose.setOnClickListener(v -> {
            new AlertDialog.Builder(AddActivity.this)
                    .setIcon(R.drawable.ic_recycle_bin)
                    .setTitle(getResources().getString(R.string.delete_member_dialog_title))
                    .setMessage(getResources().getString(R.string.delete_member_dialog_message))
                    .setPositiveButton(getResources().getString(R.string.accept_text_dialog),
                            (dialog, which) -> {
                                if(finalMember.getId() != null && finalMember.getId() > 0) {
                                    memberRepo.deleteGroceriesByMemberId(finalMember.getId());
                                    memberRepo.deleteARow(finalMember.getId());
                                }
                                removeView(memberView);
                    })
                    .setNegativeButton(getResources().getString(R.string.deny_text_dialog), null)
                    .show();
        });
        imagePurchase.setOnClickListener(v -> {
            String name = editNameText.getText().toString();
            if(isNullOrEmpty(name)) {
                editNameText.setError(getString(R.string.name_is_required));
            }
            MemberRepo memberRepo = new MemberRepo();
            long memberId;
            if(memberView.getId() > 0) {
                memberId = memberView.getId();
            } else {
                Member mem = new Member();
                mem.setName(name);
                mem.setPartyId(partyId);
                memberId = memberRepo.insert(mem);
            }
            String title = partyNameTv.getText().toString();
            party.setName(title);
            partyRepo.update(party);
            Intent intent = new Intent(AddActivity.this, AddGroceryActivity.class);
            intent.putExtra("memberId", memberId);
            intent.putExtra("partyId", partyId);
            startActivity(intent);
        });
        layoutList.addView(memberView);
    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                addMemberView(null);
                break;
            case R.id.button_submit_list:
                handleBackToMainActivity();
                break;
            case R.id.button_calculating:
                calculateAmount(Boolean.FALSE);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    private boolean checkIfValidAndRead() {
        lstMember.clear();
        boolean result = true;

        for(int i = 0; i < layoutList.getChildCount(); i++) {
            View memView = layoutList.getChildAt(i);

            EditText editNameText = memView.findViewById(R.id.edit_member_name);
            TextView editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);

            Member member = new Member();
            String name = editNameText.getText().toString();
            String paidAmountTxt = editPaidAmountText.getText().toString();

            if(!isNullOrEmpty(name)) {
                member.setName(name);
            } else {
                result = false;
                editNameText.setError(getString(R.string.name_is_required));
            }

            if(!isNullOrEmpty(paidAmountTxt)) {
                BigDecimal paidAmount = new BigDecimal(paidAmountTxt);
                member.setPaidAmount(paidAmount);
            }
            member.setPartyId(partyId);

            if(result) {
                if(memView.getId() > 0) {
                    memberRepo.update(member);
                } else {
                    long memId = memberRepo.insert(member);
                    memView.setId(Math.toIntExact(memId));
                }
            }
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
        return text == null || text.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addViewPartyDetail(ArrayList<Member> lstMem) {
        for(int i = 0; i < lstMem.size(); i++) {
            addMemberView(lstMem.get(i));
        }
    }

    private String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @SuppressLint("ResourceType")
    private void calculateAmount(Boolean isSubmit) {
        if(layoutList.getChildCount() <= 0) {
            Toast.makeText(this, "Please add member or details correctly!", Toast.LENGTH_SHORT).show();
        } else {
            BigDecimal totalAmount = BigDecimal.ZERO;
            int memberNumber = layoutList.getChildCount();

            //Calculating total amount
            for(int i = 0; i < memberNumber; i++) {
                View memView = layoutList.getChildAt(i);
                TextView paidAmountText = memView.findViewById(R.id.edit_paid_amount);
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
                TextView paidAmountText = memView.findViewById(R.id.edit_paid_amount);
                String paidAmountStr = paidAmountText.getText().toString();


                BigDecimal paidAmount = BigDecimal.ZERO;
                if(!isNullOrEmpty(paidAmountStr))
                    paidAmount = new BigDecimal(paidAmountStr);

                BigDecimal changeAmount = paidAmount.subtract(averageAmount);

                String changeAmountStr = changeAmount.toString();
                changeAmountText.setText(changeAmountStr);
                changeAmountText.setOnClickListener(v -> {
                    showWhenTextOversize(memView, changeAmountStr);
                });
                paidAmountText.setOnClickListener(v -> {
                    showWhenTextOversize(memView, paidAmountStr);
                });

                if(memView.getId() > 0) {
                    MemberRepo memberRepo = new MemberRepo();
                    Member memberDTO = memberRepo.getMemberById((long) memView.getId());
                    memberDTO.setChangeAmount(changeAmount);
                    memberRepo.update(memberDTO);
                }
            }

            if(isSubmit.equals(Boolean.FALSE)) {
                ((TextView)findViewById(R.id.total_amount)).setText(String.format("Total Amount: %s", getCurrencyFormat(totalAmount)));
                ((TextView)findViewById(R.id.average_amount)).setText(String.format("Average Amount: %s", getCurrencyFormat(averageAmount)));
            }
        }
    }

    private String getCurrencyFormat(BigDecimal n) {
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.0");
        return df.format(n);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onButtonShowPopupWindowClick(View view, String content) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        TextView textView = popupView.findViewById(R.id.popup_text);
        textView.setText(content);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView totalText = findViewById(R.id.total_amount);
        String totalStr = totalText.getText().toString();
        TextView averageAmountText = findViewById(R.id.average_amount);
        String averageAmountStr = averageAmountText.getText().toString();
        Boolean isCalculated = Boolean.FALSE;
        if(!isNullOrEmpty(totalStr) && !isNullOrEmpty(averageAmountStr)) {
            isCalculated = Boolean.TRUE;
        }
        outState.putBoolean("isCalculated", isCalculated);
        outState.putSerializable("lstMem", lstMember);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Member> lstMem = (ArrayList<Member>) savedInstanceState.getSerializable("lstMem");
        Boolean isCalculated = savedInstanceState.getBoolean("isCalculated");
        if(lstMem != null && lstMem.size() > 0) {
            lstMember = lstMem;
            //addViewPartyDetail(lstMember);
            if(isCalculated.equals(Boolean.TRUE))
                calculateAmount(Boolean.FALSE);
        }
    }

    private void showWhenTextOversize(View view, String text) {
        if(text.length() > CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE) {
            onButtonShowPopupWindowClick(view, text);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleBackToMainActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleBackToMainActivity() {
        if(checkIfValidAndRead()) {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            String partyName = partyNameTv.getText().toString();
            party.setName(partyName);
            calculateAmount(Boolean.TRUE);
            party.setUpdateDate(getCurrentTime());
            partyRepo.update(party);
            startActivity(intent);
        }
    }
}