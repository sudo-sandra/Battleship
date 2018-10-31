package com.example.root.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

public class SetUpMap extends AppCompatActivity implements View.OnClickListener{

    BattleshipView battleView;
    Battleship battle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_map);
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
        Intent loginIntent = new Intent(this, Game.class);
        startActivity(loginIntent);
    }

    public Battleship getBattle(){
        return battle;
    }
}