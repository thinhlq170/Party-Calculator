package com.example.partycalculator;

import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;


public class DetailActivity extends Activity {

    Toolbar detailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailToolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            detailToolbar.setTitle(bundle.getString("PartyName"));
        }

    }
}