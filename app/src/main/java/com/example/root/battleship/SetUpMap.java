package com.example.root.battleship;

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
        TableRow.LayoutParams layout_params = new TableRow.LayoutParams(55, 55);
        layout_params.setMargins(5,5,5,5);
        View[] ships = new View[10];
        for (int i = 0; i < 10; i++) {
            ships[i] = new View(this);
            ships[i].setLayoutParams(layout_params);
            ships[i].setBackgroundColor(Color.RED);
        }
        TextView title = (TextView) findViewById(R.id.title);
        title.setTextSize(32);
        // TODO: set bold
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setTextColor(Color.rgb(150, 180, 255));
        TableLayout legend = (TableLayout) findViewById(R.id.legendLayout);
        int count = 0;
        for(int i = 0; i < 4 ; i++){
            TableRow row = (TableRow) legend.getChildAt(i);
            TextView txt = (TextView) row.getChildAt(0);
            txt.setTextSize(28);
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
        battleView = new BattleshipView(this,this, battle, (TableLayout) findViewById(R.id.map_layout), false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_btn:
                battle = new Battleship();
                battleView.set_up_map(battle);
                break;
            case R.id.ok_btn:
                openGameActivity();
                break;
        }
    }

    private void openGameActivity() {
        Intent gameIntent = new Intent(this, Game.class);
//        Bundle extras = new Bundle();
//        //TODO pass battle to Game
//        extras.putIntArray("map", battle);
        startActivity(gameIntent);
    }

    public Battleship getBattle(){
        return battle;
    }
}