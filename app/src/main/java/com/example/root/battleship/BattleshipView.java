package com.example.root.battleship;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.content.Context;
import android.app.Activity;

public class BattleshipView{

    private Battleship battle;
    private View[][] view_map = new View[10][10];
    private TableLayout map_layout;
    private boolean editable;
    private Context context;
    private TableRow[] rows = new TableRow[10];
    private Activity activity;

    public BattleshipView(Activity activity, Context context, Battleship battle, TableLayout map_layout){
        this(activity, context, battle, map_layout, true);
    }

    public BattleshipView(Activity activity, Context context, Battleship battle,
                          TableLayout map_layout, boolean editable){
        this.activity = activity;
        this.context = context;
        this.battle = battle;
        this.map_layout = map_layout;
        this.editable = editable;
        create_map();
    }

    private void create_map(){
        TableRow.LayoutParams layout_params = new TableRow.LayoutParams(50, 50);
        layout_params.setMargins(5,5,5,5);
        for(int i = 0; i < 10; i++) {
            rows[i] = new TableRow(context);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int k = 0; k < 10; k++) {
                view_map[i][k] = new View(context);
                view_map[i][k].setLayoutParams(layout_params);
                if(editable) {
                    view_map[i][k].setId((i * 10) + k);
                }
                rows[i].addView(view_map[i][k]);
            }
            map_layout.addView(rows[i]);
        }
        set_up_map(battle);
    }

    public void set_up_map(Battleship n_battle){
        battle = n_battle;
        Integer[][] map = battle.get_map();
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                if(editable) {
                    view_map[i][k].setBackgroundColor(Color.BLACK);
                    view_map[i][k].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            int id = v.getId();
                            View vw = (View) activity.findViewById(id);
                            Integer[][] map = battle.get_map();
                            if (map[id / 10][id % 10] == 0) {
                                vw.setBackgroundColor(Color.BLUE);
                            } else if (map[id / 10][id % 10] == 1) {
                                vw.setBackgroundColor(Color.RED);
                            }
                            vw.setEnabled(false);
                            int destroyStatus = battle.destroyField(id / 10, id % 10);
                            if (destroyStatus > 1) {
                                Integer[][] ship = battle.getShip(id / 10, id % 10);
                                for (int i = 0; i < ship.length; i++) {
                                    if (ship[i][0] == -1) {
                                        break;
                                    }
                                    activity.findViewById(ship[i][0] * 10 + ship[i][1]).
                                            setBackgroundColor(Color.GREEN);
                                }
                                //TODO: all boders blue
                            }
                            if (destroyStatus == 3) {
                                for (int m = 0; m < 10; m++) {
                                    for (int n = 0; n < 10; n++) {
                                        if (map[m][n] == 0) {
                                            view_map[m][n].setBackgroundColor(Color.BLUE);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                else{
                    if (map[i][k] == 0) {
                        view_map[i][k].setBackgroundColor(Color.BLUE);
                    }
                    else if (map[i][k] == 1){
                        view_map[i][k].setBackgroundColor(Color.RED);
                    }
                }
            }
        }
    }
}
