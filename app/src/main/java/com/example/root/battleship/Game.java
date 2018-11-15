package com.example.root.battleship;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;


public class Game extends AppCompatActivity implements View.OnClickListener{

    TextView title;
    Battleship battle = new Battleship();
    Button[][] btn_map = new Button[10][10];
    View[][] view_map = new View[10][10];
    BattleshipView battleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);
        setContentView(R.layout.game);

        set_up_map();
        float size = new Button(this).getTextSize();
        //Toast.makeText(this, "Textgröße" + size, Toast.LENGTH_LONG).show();
        title = (TextView)findViewById(R.id.textView);
    }

    private void set_up_map(){
        battleView = new BattleshipView(this, this, battle, (TableLayout)
                findViewById(R.id.map_layout));
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button_red:
                title.setBackgroundColor(Color.RED);
                battle.print_map();
                break;
            case R.id.button_blue:
                title.setBackgroundColor(Color.BLUE);
                break;
            case R.id.button_end:
                finish();
                break;
        }
    }
}
