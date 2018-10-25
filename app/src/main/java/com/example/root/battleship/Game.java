package com.example.root.battleship;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class Game extends AppCompatActivity implements View.OnClickListener{

    TextView title;
    Battleship battle = new Battleship();
    Button[][] btn_map = new Button[10][10];
    View[][] view_map = new View[10][10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);
        setContentView(R.layout.map_test);

        set_up_map();
        float size = new Button(this).getTextSize();
        Toast.makeText(this, "Textgröße" + size, Toast.LENGTH_LONG).show();
        title = (TextView)findViewById(R.id.textView);
        //Button red = (Button)findViewById(R.id.button_red);
        //Button blue = (Button)findViewById(R.id.button_blue);
        //Button end = (Button)findViewById(R.id.button_end);
        //red.setOnClickListener(this);
        //blue.setOnClickListener(this);
        //end.setOnClickListener(this);
    }

    private void set_up_map(){
        TableLayout layout = (TableLayout) findViewById(R.id.map_layout);
        System.out.println(layout);
        TableRow.LayoutParams layout_params = new TableRow.LayoutParams(50, 50);
        layout_params.setMargins(5,5,5,5);
        for(int i = 0; i < 10; i++){
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for(int k = 0; k < 10; k++){
                view_map[i][k] = new View(this);
                view_map[i][k].setId((i*10) + k);
                view_map[i][k].setBackgroundColor(Color.BLACK);
                view_map[i][k].setLayoutParams(layout_params);
                view_map[i][k].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int id = v.getId();
                        View vw = (View) findViewById(id);
                        Integer[][] map = battle.get_map();
                        if(map[id/10][id%10] == 0){
                            vw.setBackgroundColor(Color.BLUE);
                        }
                        else if(map[id/10][id%10] == 1){
                            vw.setBackgroundColor(Color.RED);
                        }
                        vw.setEnabled(false);
                        if(battle.destroyField(id/10, id%10)){
                            Integer[][] ship = battle.getShip(id/10, id%10);
                            for(int i = 0; i < ship.length; i++){
                                if(ship[i][0] == -1){
                                    break;
                                }
                                findViewById(ship[i][0] * 10 + ship[i][1]).setBackgroundColor(Color.GREEN);
                            }
                            //TODO: all boders blue
                        }
                    }
                });
                row.addView(view_map[i][k]);
            }
            layout.addView(row);
        }
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
