package com.example.root.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TurnDisplay extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn_display);
        Intent intent = getIntent();
        TextView turn_text = (TextView) findViewById(R.id.turn_text);
        turn_text.setText(intent.getStringExtra("turn_text"));
        Button start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }
}
