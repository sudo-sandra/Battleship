package com.example.root.battleship;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SetUpMap extends AppCompatActivity implements View.OnClickListener{

    BattleshipView battleView;
    Battleship battle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_map);
        TableRow.LayoutParams layout_params = new TableRow.LayoutParams(40, 40);
        layout_params.setMargins(5,5,5,5);
        View[] ships = new View[10];
        for (int i = 0; i < 10; i++) {
            ships[i] = new View(this);
            ships[i].setLayoutParams(layout_params);
            ships[i].setBackgroundColor(Color.rgb(255, 175, 84));
        }
        TableLayout legend = (TableLayout) findViewById(R.id.legendLayout);
        int count = 0;
        for(int i = 0; i < 4 ; i++){
            TableRow row = (TableRow) legend.getChildAt(i);
            TextView txt = (TextView) row.getChildAt(0);
            txt.setTextSize(20);
            for(int k = Math.abs(i - 4); k > 0; k--){
                row.addView(ships[count]);
                count++;
            }
        }
        Button auto_btn = (Button)findViewById(R.id.auto_btn);
        auto_btn.setOnClickListener(this);
        Button ok_btn = (Button)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(this);
        create_battle();
    }

    private void create_battle(){
        battle = new Battleship();
        battleView = new BattleshipView(this, battle, (TableLayout) findViewById(R.id.map_layout), false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_btn:
                battle = new Battleship();
                battleView.set_up_map(battle);
                break;
            case R.id.ok_btn:
                returnBattleToGame();
                break;
        }
    }

    private void returnBattleToGame() {
        Intent gameIntent = new Intent();
        gameIntent.putExtra("battle", battle);
        setResult(Activity.RESULT_OK, gameIntent);
        finish();
    }

    public Battleship getBattle(){
        return battle;
    }
}