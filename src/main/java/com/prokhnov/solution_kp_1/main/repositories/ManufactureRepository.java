package com.prokhnov.solution_kp_1.main.repositories;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;
import com.prokhnov.solution_kp_1.main.entity.Souvenir;
import com.prokhnov.solution_kp_1.main.exceptions.ManufacturerNotFoundException;
import com.prokhnov.solution_kp_1.main.formatter.ManufactureSaveSaveFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManufactureRepository implements Repository<Manufacture> {

    private final Set<Manufacture> manufactures = new HashSet<>();
    private final String filePath;

    public ManufactureRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void add(Manufacture manufacturer) {
        loadDataFromFile();
        if (manufactures.contains(manufacturer)) {
            System.out.println("Manufacture: " + manufacturer.getName() + " from country: " + manufacturer.getCountry() + " contains!!!");
        } else {
            manufactures.add(manufacturer);
        }
    }

    @Override
    public void edit(Manufacture manufacture) throws ManufacturerNotFoundException {
        loadDataFromFile();

        boolean found = false;
        for (Manufacture existingManufacture : manufactures) {
            if (existingManufacture.equals(manufacture)) {
                existingManufacture.setName(manufacture.getName());
                existingManufacture.setCountry(manufacture.getCountry());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ManufacturerNotFoundException("Manufacturer not found: " + manufacture.getName() + " (" + manufacture.getCountry() + ")");
        }

        saveDataToFile();
    }


    @Override
    public void delete(String name) throws ManufacturerNotFoundException {
        boolean removed = manufactures.removeIf(m -> m.getName().equals(name));
        if (!removed) {
            throw new ManufacturerNotFoundException("Manufacturer with name '" + name + "' not found.");
        }
    }

    @Override
    public void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            loadData(reader, manufactures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData(BufferedReader reader, Set<Manufacture> manufactures) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(":");
            if (fields.length == 2) {
                String name = fields[0];
                String country = fields[1];
                Manufacture manufacture = new Manufacture(name, country);
                if (manufactures.contains(manufacture)){
                    continue;
                }
                manufactures.add(manufacture);
            }
        }
    }

    @Override
    public void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ManufactureSaveSaveFormatter mf = new ManufactureSaveSaveFormatter();
            for (Manufacture manufacturer : manufactures) {
                writer.write(mf.save(manufacturer));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Manufacture> getListOfAllData() {
        return new ArrayList<>(manufactures);
    }

    public Manufacture getManufactureByName(String name) throws ManufacturerNotFoundException {
        Manufacture equationManufacture = null;

        for (Manufacture m : manufactures) {
            if (m.getName().equals(name)) {
                equationManufacture = m;
            }
        }

        if (equationManufacture == null) {
            throw new ManufacturerNotFoundException("Manufacture with name - " + name + "not found!!!");
        } else {
            return equationManufacture;
        }
    }

    public void showAllManufacturesAndTheirSouvenirs(SouvenirRepository souvenirRepository) {
        for (Manufacture m : manufactures) {
            System.out.println("===================================");
            System.out.println(m);
            System.out.println("===================================");
            System.out.println(m.getName() + " souvenirs:");
            for (Souvenir s : souvenirRepository.getSouvenirsByManufacturer(m.getName())) {
                System.out.println(s);
            }
            System.out.println("===================================");

        }
    }

}
