package com.prokhnov.solution_kp_1.main;

import com.prokhnov.solution_kp_1.main.exceptions.ManufacturerNotFoundException;
import com.prokhnov.solution_kp_1.main.exceptions.SouvenirNotFoundException;
import com.prokhnov.solution_kp_1.main.menu.Menu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ManufacturerNotFoundException, SouvenirNotFoundException {
        String souvenirDataPath = "SouvenirData.txt";
        String manufactureDataPath = "ManufactureData.txt";
        Menu menu = new Menu(souvenirDataPath, manufactureDataPath);
        menu.run();
    }
}
