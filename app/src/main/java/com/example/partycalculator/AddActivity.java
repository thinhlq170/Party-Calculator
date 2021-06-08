package com.example.partycalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Toolbar;

import com.example.partycalculator.dtos.MemberDTO;
import com.example.partycalculator.dtos.PartyDTO;
import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;
import com.example.partycalculator.repositories.MemberRepo;
import com.example.partycalculator.repositories.PartyRepo;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AddActivity extends Activity implements View.OnClickListener{

    ArrayList<MemberDTO> lstMember = new ArrayList<>();
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    Button buttonCalculate;
    EditText partyNameTv;
    ArrayList<PartyDTO> lstParty;
    Boolean isViewDetail = Boolean.FALSE;
    ScrollView scrollLstMemView;
    private static final int CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE = 7;
    Integer partyId;
    PartyDTO party;
    PartyRepo partyRepo;

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
            partyId = getIntent().getExtras().getInt("partyId");
            party = partyRepo.getPartyById(partyId);
            partyNameTv.setText(party.getName());
            if(party.getListMembers().size() > 0)
                lstMember = party.getListMembers();
            isViewDetail = Boolean.TRUE;
        } else {
            party = new PartyDTO();
            isViewDetail = Boolean.FALSE;
            lstMember = new ArrayList<>();
        }
        if(lstMember != null && lstMember.size() > 0) {
            addViewPartyDetail(lstMember);
            calculateAmount(Boolean.FALSE);
        }

    }

    @SuppressLint({"SetTextI18n", "ResourceType", "InflateParams"})
    private void addMemberView(MemberDTO dto) {
        final View memberView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);

        ImageView imagePurchase = memberView.findViewById(R.id.image_purchase_detail);
        TextView changeAmountText = memberView.findViewById(R.id.change_amount);
        EditText editNameText = memberView.findViewById(R.id.edit_member_name);
        TextView paidAmountText = memberView.findViewById(R.id.edit_paid_amount);

        if(dto != null) {
            editNameText.setText(dto.getName());
            paidAmountText.setText(dto.getPaidAmount().toString());
            changeAmountText.setText(dto.getChangeAmount().toString());
        }

        ImageView imageClose = memberView.findViewById(R.id.image_remove);
        imageClose.setOnClickListener(v -> removeView(memberView));
        imagePurchase.setOnClickListener(v -> {
            String name = editNameText.getText().toString();
            if(isNullOrEmpty(name)) {
                editNameText.setError(getString(R.string.name_is_required));
            }
            MemberRepo memberRepo = new MemberRepo();
            MemberDTO mem = new MemberDTO();
            int memberId = -1;
            if(dto != null) {
                if(dto.getId() > 0) {
                    memberId = dto.getId();
                    mem = memberRepo.getMemberById(dto.getId());
                }
            } else {
                mem.setName(name);
                mem.setPartyId(partyId);
                memberId = memberRepo.insert(mem);
                memberView.setId(memberId);
            }
            String title = partyNameTv.getText().toString();
            party.setName(title);
            partyRepo.update(party);
            Intent intent = new Intent(AddActivity.this, AddGroceryActivity.class);
            intent.putExtra("memberId", memberId);
            startActivity(intent);
        });
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
                addMemberView(null);
                break;
            case R.id.button_submit_list:
                if(checkIfValidAndRead()) {
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    String partyName = partyNameTv.getText().toString();
                    party.setName(partyName);
                    calculateAmount(Boolean.TRUE);
                    if(isViewDetail.equals(Boolean.TRUE)) {
                        party.setUpdateDate(getCurrentTime());
                    } else {
                        party.setDate(getCurrentTime());
                    }
                    partyRepo.insert(party);
                    startActivity(intent);
                }
                break;
            case R.id.button_calculating:
                calculateAmount(Boolean.FALSE);
                break;
        }
    }

    private boolean checkIfValidAndRead() {
        lstMember.clear();
        boolean result = true;

        for(int i = 0; i < layoutList.getChildCount(); i++) {
            View memView = layoutList.getChildAt(i);

            EditText editNameText = memView.findViewById(R.id.edit_member_name);
            TextView editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);

            MemberDTO member = new MemberDTO();
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
            member.setChangeAmount(BigDecimal.ZERO);
            member.setPartyId(partyId);

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

    @SuppressLint({"SetTextI18n", "ResourceType"})
    private void addViewPartyDetail(ArrayList<MemberDTO> lstMem) {
        layoutList.removeAllViewsInLayout();
        for(int i = 0; i < lstMem.size(); i++) {
            addMemberView(lstMem.get(i));
//            final View memView = getLayoutInflater().inflate(R.layout.row_add_member, null, false);
//            EditText editNameText = memView.findViewById(R.id.edit_member_name);
//            TextView editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);
//            TextView editChangeAmountText = memView.findViewById((R.id.change_amount));
//            ImageView imagePurchase = memView.findViewById(R.id.image_purchase_detail);
//
//            MemberDTO member = lstMem.get(i);
//            String changeAmountStr = getCurrencyFormat(member.getChangeAmount());
//            String paidAmountStr = getCurrencyFormat(member.getPaidAmount());
//            editPaidAmountText.setOnClickListener(v -> {
//                if(paidAmountStr.length() > CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE) {
//                    onButtonShowPopupWindowClick(memView, paidAmountStr);
//                }
//            });
//            editChangeAmountText.setOnClickListener(v -> {
//                if(changeAmountStr.length() > CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE) {
//                    onButtonShowPopupWindowClick(memView, changeAmountStr);
//                }
//            });
//            imagePurchase.setOnClickListener(v -> {
//                String name = editNameText.getText().toString();
//                if(isNullOrEmpty(name)) {
//                    editNameText.setError(getString(R.string.name_is_required));
//                }
//                MemberRepo memberRepo = new MemberRepo();
//                MemberDTO mem = new MemberDTO();
//                if(imagePurchase.getId() > 0) {
//                    mem = memberRepo.getMemberById(imagePurchase.getId());
//                } else {
//                    mem.setName(name);
//                    mem.setPartyId(partyId);
//                    int imagePurchaseId = memberRepo.insert(mem);
//                    imagePurchase.setId(imagePurchaseId);
//                }
//                Intent intent = new Intent(AddActivity.this, AddGroceryActivity.class);
//                intent.putExtra("memberObj", (Serializable) mem);
//                startActivity(intent);
//            });
//            editNameText.setText(member.getName());
//            editPaidAmountText.setText(member.getPaidAmount().toString());
//            editChangeAmountText.setText(changeAmountStr);
//
//            ImageView imageClose = memView.findViewById(R.id.image_remove);
//            imageClose.setOnClickListener(v -> removeView(memView));
//            layoutList.addView(memView);
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
                    if(changeAmountStr.length() > CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE) {
                        onButtonShowPopupWindowClick(memView, changeAmountStr);
                    }
                });
                paidAmountText.setOnClickListener(v -> {
                    if(paidAmountStr.length() > CHANGE_AMOUNT_MAX_LENGTH_APPEARANCE) {
                        onButtonShowPopupWindowClick(memView, paidAmountStr);
                    }
                });

                if(memView.getId() > 0) {
                    MemberRepo memberRepo = new MemberRepo();
                    MemberDTO memberDTO = memberRepo.getMemberById(memView.getId());
                    memberDTO.setChangeAmount(averageAmount);
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
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
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
        ArrayList<MemberDTO> lstMem = getLstMemberCurrentState();
        outState.putSerializable("lstMem", lstMem);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<MemberDTO> lstMem = (ArrayList<MemberDTO>) savedInstanceState.getSerializable("lstMem");
        Boolean isCalculated = savedInstanceState.getBoolean("isCalculated");
        if(lstMem != null && lstMem.size() > 0) {
            lstMember = lstMem;
            addViewPartyDetail(lstMember);
            if(isCalculated.equals(Boolean.TRUE))
                calculateAmount(Boolean.FALSE);
        }
    }

    private ArrayList<MemberDTO> getLstMemberCurrentState() {

        ArrayList<MemberDTO> lstMem = new ArrayList<>();

        for(int i = 0; i < layoutList.getChildCount(); i++) {
            View memView = layoutList.getChildAt(i);

            EditText editNameText = memView.findViewById(R.id.edit_member_name);
            TextView editPaidAmountText = memView.findViewById(R.id.edit_paid_amount);

            MemberDTO member = new MemberDTO();
            String name = editNameText.getText().toString();
            String paidAmountTxt = editPaidAmountText.getText().toString();

            if(!isNullOrEmpty(name)) {
                member.setName(name);
            }

            if(!isNullOrEmpty(paidAmountTxt)) {
                BigDecimal paidAmount = new BigDecimal(paidAmountTxt);
                member.setPaidAmount(paidAmount);
            }

            member.setChangeAmount(BigDecimal.ZERO);
            lstMem.add(member);
        }
        return lstMem;
    }
}