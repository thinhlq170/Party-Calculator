package com.example.partycalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.partycalculator.dtos.GroceryDTO;
import com.example.partycalculator.dtos.MemberDTO;
import com.example.partycalculator.repositories.GroceryRepo;
import com.example.partycalculator.repositories.MemberRepo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AddGroceryActivity extends Activity implements View.OnClickListener {

    Button saveButton;
    Button addItemButton;
    Button totalButton;
    Toolbar titleTb;
    MemberDTO member;
    ArrayList<GroceryDTO> listItem;
    ScrollView scrollListItemView;
    LinearLayout layoutListItem;
    TextView totalText;
    int memberId;

    GroceryRepo groceryRepo = new GroceryRepo();
    MemberRepo memberRepo = new MemberRepo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery);

        saveButton = findViewById(R.id.button_submit_list);
        saveButton.setOnClickListener(this);

        scrollListItemView = findViewById(R.id.scroll_list_grocery);
        layoutListItem = findViewById(R.id.layout_list_grocery);

        addItemButton = findViewById(R.id.button_add_item);
        addItemButton.setOnClickListener(this);

        totalButton = findViewById(R.id.button_total_amount_item);
        totalButton.setOnClickListener(this);

        totalText = findViewById(R.id.total_amount_item);


        titleTb = findViewById(R.id.toolbarMain);

        if(getIntent().getExtras() != null) {
            memberId = getIntent().getExtras().getInt("memberId");
            if(memberId > 0) {
                member = memberRepo.getMemberById(memberId);
                listItem = member.getListGroceries();
                if(listItem != null && listItem.size() > 0) {
                    showListItem(listItem);
                }
            } else {
                member = new MemberDTO();
            }
        }

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //convert dp to pixels
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            ViewGroup.LayoutParams params = scrollListItemView.getLayoutParams();
            params.height = height;
            scrollListItemView.setLayoutParams(params);
        }
        titleTb.setTitle(String.format("Danh sách đã chi của %s", member.getName()));
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_item:
                addItemView(null);
                break;
            case R.id.button_submit_list:
                if(checkIfValidAndRead()) {
                    Intent intent = new Intent(AddGroceryActivity.this, AddActivity.class);
                    intent.putExtra("partyId", member.getPartyId());
                    startActivity(intent);
                }
            case R.id.button_total_amount_item:
                if(layoutListItem.getChildCount() > 0) {
                    BigDecimal res = calculateTotalAmount();
                    if(res != null) {
                        totalText.setText(String.format("Tổng: %s", getCurrencyFormat(res)));
                    }
                }
        }
    }

    private void showListItem(ArrayList<GroceryDTO> lstItem) {
        if (listItem != null && lstItem.size() > 0) {
            for(int i = 0; i < lstItem.size(); i++) {
                addItemView(lstItem.get(i));
            }
        }
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void addItemView(GroceryDTO dto) {
        final View itemView = getLayoutInflater().inflate(R.layout.row_add_grocery, null, false);
        ImageView imageClose = itemView.findViewById(R.id.image_remove);
        EditText nameText = itemView.findViewById(R.id.edit_item_name);
        EditText paidText = itemView.findViewById(R.id.item_paid_amount);
        if(dto != null) {
            if(!isNullOrEmpty(dto.getItemName())) {
                nameText.setText(dto.getItemName());
            }
            if(dto.getPrice() != null) {
                paidText.setText(dto.getPrice().toString());
            }
            imageClose.setId(dto.getId());
        }
        imageClose.setOnClickListener(v -> {
            if(dto != null)
                groceryRepo.deleteARow(dto.getId());
            layoutListItem.removeView(itemView);
        });
        layoutListItem.addView(itemView);
    }

    private Boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    @SuppressLint("ResourceType")
    private boolean checkIfValidAndRead() {
        boolean result = true;
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < layoutListItem.getChildCount(); i++) {
            View itemView = layoutListItem.getChildAt(i);
            EditText nameText = itemView.findViewById(R.id.edit_item_name);
            EditText paidText = itemView.findViewById(R.id.item_paid_amount);
            String nameStr = nameText.getText().toString();
            String paidStr = paidText.getText().toString();
            if(isNullOrEmpty(nameStr)) {
                result = false;
                nameText.setError(getResources().getString(R.string.item_is_required));
            }
            if(isNullOrEmpty(paidStr)) {
                result = false;
                paidText.setError(getResources().getString(R.string.item_paid_is_required));
            }
            if(itemView.getId() > 0) {
                GroceryDTO dto = groceryRepo.getGroceryById(itemView.getId());
                dto.setItemName(nameStr);
                dto.setPrice(new BigDecimal(paidStr));
                groceryRepo.update(dto);
            } else {
                GroceryDTO dto = new GroceryDTO();
                dto.setItemName(nameStr);
                dto.setPrice(new BigDecimal(paidStr));
                dto.setMemberId(member.getId());
                int groceryId = groceryRepo.insert(dto);
                itemView.setId(groceryId);
            }
            total = total.add(new BigDecimal(paidStr));
        }
        member.setPaidAmount(total);
        memberRepo.update(member);
        return result;
    }

    private BigDecimal calculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < layoutListItem.getChildCount(); i++) {
            View itemView = layoutListItem.getChildAt(i);
            EditText paidText = itemView.findViewById(R.id.item_paid_amount);
            String paidStr = paidText.getText().toString();
            if (!isNullOrEmpty(paidStr)) {
                total = total.add(new BigDecimal(paidStr));
            }
        }
        return total;
    }

    private String getCurrencyFormat(BigDecimal n) {
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
        return df.format(n);
    }
}