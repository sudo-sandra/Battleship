package com.example.root.schiffeversenken;

import java.util.ArrayList;
import java.util.Random;

public class Battleship {
    private Integer[][] map = new Integer[10][10];

    public Battleship(){
        for(int i =0; i<10; i++){
            for(int k=0;k<10;k++){
                map[i][k] = 0;
            }
        }
        setShips(1, 4);
        setShips(2, 3);
        setShips(3, 2);
        setShips(2, 1);
        print_map();
    }

    public void print_map(){
        for(int i =0; i<10; i++){
            for(int k=0;k<10;k++){
                System.out.print(map[i][k] + " ");
            }
            System.out.println("");
        }
    }

    public Integer[][] get_map(){
        return map;
    }

    public boolean destroyField(int y, int x){
        map[y][x] += 2;
        return isDestroyed(y, x);
    }

    private boolean isDestroyed(int y, int x){
        Integer[][] ship = getShip(y, x);
        for(int i = 0; i < ship.length; i++){
            if(ship[i][0] == -1){
                break;
            }
            if(map[ship[i][0]][ship[i][1]] != 3){
                return false;
            }
        }
        return true;
    }

    public Integer[][] getShip(int y, int x){
        Integer[][] ship = new Integer[4][2];
        for(int i = 0; i < 4; i++){
            ship[i][0] = -1;
            ship[i][1] = -1;
        }
        if(map[y][x] == 1 || map[y][x] == 3){
            ship[0][0] = y;
            ship[0][1] = x;
            int k = 0;
            for(int i = -3; i <= 3; i+=2){
                int n_y = y;
                int n_x = x;
                while(true){
                    if(i%3==0){
                        n_x += 1 * Math.signum(i);
                        if(n_x < 0 || n_x > 9){
                            break;
                        }
                    }
                    else {
                        n_y += 1 * Math.signum(i);
                        if(n_y < 0 || n_y > 9){
                            break;
                        }
                    }
                    if(map[n_y][n_x] == 1 || map[n_y][n_x] == 3){
                        k++;
                        ship[k][0] = n_y;
                        ship[k][1] = n_x;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        return ship;
    }

    private void setShips(int num, int size){
        Random random = new Random();
        for(int i = 0; i < num; i++){
            boolean ok = false;
            ArrayList<Integer> positions = new ArrayList<>();
            while(!ok){
                int x_pos = random.nextInt(10);
                int y_pos = random.nextInt(10);
                if(random.nextInt(2) == 1){
                    for(int k = 0; k < size; k++) {
                        positions.add(x_pos+k);
                        positions.add(y_pos);
                    }
                }
                else{
                    for(int k = 0; k < size; k++) {
                        positions.add(x_pos);
                        positions.add(y_pos+k);
                    }
                }
                if(isValidPlace(positions)){
                    ok = true;
                }
                else{
                    positions.clear();
                }

            }
            for(int k = 0; k < size*2; k += 2){
                map[positions.get(k)][positions.get(k+1)] = 1;
            }
        }
    }

    private boolean isValidPlace(ArrayList<Integer> positions){
        for(int i = 0; i < positions.size(); i+=2){
            if(!isValidShipPlace(positions.get(i), positions.get(i+1))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidShipPlace(int x_pos, int y_pos){
        if(x_pos < 0 || x_pos > 9 || y_pos < 0 || y_pos > 9){
            return false;
        }
        try {
            if(isShip(map,x_pos,y_pos)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos-1,y_pos-1)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos+1,y_pos+1)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos+1,y_pos-1)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos-1,y_pos+1)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos-1,y_pos)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos,y_pos-1)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos+1,y_pos)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        try {
            if(isShip(map,x_pos,y_pos+1)){
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException exception) {}
        return true;
    }

    private boolean isShip(Integer[][] map, int x_pos, int y_pos){
        return(map[x_pos][y_pos] == 1);
    }
}
