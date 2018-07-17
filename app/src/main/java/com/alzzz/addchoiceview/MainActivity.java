package com.alzzz.addchoiceview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AddChoiceView addChoiceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addChoiceView = findViewById(R.id.addchoiceview);
    }

    public void onClick(View view){
        addChoiceView.addChoice();
    }
}
