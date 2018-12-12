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
    private BattleshipViewListener listener;

    public interface BattleshipViewListener{
        void onFieldDestroyed(int destroyStatus);
    }

    public void setBattleshipViewListener(BattleshipViewListener listener) {
        this.listener = listener;
    }

    public BattleshipView(Context context, Battleship battle, TableLayout map_layout){
        this(context, battle, map_layout, true);
    }

    public BattleshipView(Context context, Battleship battle,
                          TableLayout map_layout, boolean editable){
        this.context = context;
        this.battle = battle;
        this.map_layout = map_layout;
        this.editable = editable;
        create_map();
    }

    private void create_map(){
        //TODO: scale to right size not 50
        TableRow.LayoutParams layout_params = new TableRow.LayoutParams(40, 40);
        layout_params.setMargins(5,5,5,5);
        for(int i = 0; i < 10; i++) {
            rows[i] = new TableRow(context);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int k = 0; k < 10; k++) {
                view_map[i][k] = new View(context);
                view_map[i][k].setLayoutParams(layout_params);
                //TODO: both maps have first field id of 0
                if(editable) {
                    view_map[i][k].setId((i * 10) + k);
                }
                else{
                    view_map[i][k].setId((i * 1000) + k *100);
                }
                rows[i].addView(view_map[i][k]);
            }
            map_layout.addView(rows[i]);
        }
        set_up_map(battle);
    }

    public Battleship getBattle(){
        return battle;
    }

    public int destroyField(int y_pos, int x_pos){
        View vw = view_map[y_pos][x_pos];
        vw.setEnabled(false);
        int destroyStatus = battle.destroyField(y_pos, x_pos);
        switch (destroyStatus){
            case 0:
                vw.setBackgroundColor(Color.BLUE);
                break;
            case 1:
                vw.setBackgroundColor(Color.RED);
                break;
            case 2:
                destroy_full_ship(y_pos, x_pos);
                break;
            case 3:
                destroy_full_ship(y_pos, x_pos);
                show_full_map();
                break;
        }
        return destroyStatus;
    }

    private void destroy_full_ship(int y_pos, int x_pos){
        Integer[][] ship = battle.getShip(y_pos, x_pos);
        for (int i = 0; i < ship.length; i++) {
            if (ship[i][0] == -1) {
                break;
            }
            view_map[ship[i][0]][ship[i][1]].setBackgroundColor(Color.rgb(0, 200, 0));
        }
    }

    public void show_full_map(){
        for (int y_id = 0; y_id < 10; y_id++) {
            for (int x_id = 0; x_id < 10; x_id++) {
                set_unhit_color(y_id, x_id);
            }
        }
        disable_all();
    }

    public void set_up_map(Battleship n_battle){
        battle = n_battle;
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                if(editable) {
                    view_map[i][k].setBackgroundColor(Color.rgb(200, 220, 255));
                    view_map[i][k].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            int id = v.getId();
                            int destroyStatus = destroyField(id / 10, id % 10);
                            if (listener != null)
                                listener.onFieldDestroyed(destroyStatus);
                        }
                    });
                }
                else{
                    set_unhit_color(i, k);
                }
            }
        }
    }

    private void set_unhit_color(int y_id, int x_id){
        Integer[][] map = battle.get_map();
        if (map[y_id][x_id] == 0) {
            view_map[y_id][x_id].setBackgroundColor(Color.rgb(136, 175, 226));
        }
        else if (map[y_id][x_id] == 1) {
            view_map[y_id][x_id].setBackgroundColor(Color.rgb(255, 175, 84));
        }
    }

    public void set_hit_colors(Battleship battle){
        this.battle = battle;
        Integer[][] map = battle.get_map();
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                if (map[i][k] == 2) {
                    view_map[i][k].setEnabled(false);
                    view_map[i][k].setBackgroundColor(Color.BLUE);
                }
                else if (map[i][k] == 3) {
                    view_map[i][k].setEnabled(false);
                    if(battle.isDestroyed(i, k)){
                        view_map[i][k].setBackgroundColor(Color.rgb(0, 200, 0));
                    }
                    else{
                        view_map[i][k].setBackgroundColor(Color.RED);
                    }
                }
            }
        }

    }

    public void enable_all(){
        Integer[][] map = battle.get_map();
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                if(map[i][k] < 2){
                    view_map[i][k].setEnabled(true);
                }
            }
        }
    }

    public void disable_all(){
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                view_map[i][k].setEnabled(false);
            }
        }
    }
}
