package com.example.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity  {
    public TextView lbl_title, lbl_description;
    public String title, content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        lbl_title = findViewById(R.id.lbl_title);
        lbl_description = findViewById(R.id.lbl_description);

        lbl_title.setText(title);
        lbl_description.setText(content);

    }
    public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}