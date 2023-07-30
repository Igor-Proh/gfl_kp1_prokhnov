package com.prokhnov.solution_kp_1.main.repositories;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;
import com.prokhnov.solution_kp_1.main.entity.Souvenir;
import com.prokhnov.solution_kp_1.main.exceptions.ManufacturerNotFoundException;
import com.prokhnov.solution_kp_1.main.exceptions.SouvenirNotFoundException;
import com.prokhnov.solution_kp_1.main.formatter.SouvenirSaveFormatter;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SouvenirRepository implements Repository<Souvenir> {

    private final List<Souvenir> souvenirs = new ArrayList<>();
    private final String filePath;
    private final ManufactureRepository manufactureRepository;

    public SouvenirRepository(String filePath, ManufactureRepository manufactureRepository) {
        this.filePath = filePath;
        this.manufactureRepository = manufactureRepository;
    }

    @Override
    public void add(Souvenir souvenir) {
        loadDataFromFile();
        if (souvenirs.contains(souvenir)) {
            System.out.println("Souvenir: " + souvenir.getName() + " contains!!!");
        } else {
            souvenirs.add(souvenir);
        }
    }

    @Override
    public void edit(Souvenir souvenir) throws SouvenirNotFoundException {
        loadDataFromFile();

        Souvenir existingSouvenir = getSouvenirByName(souvenir.getName());

        if (existingSouvenir.equals(souvenir)) {
            existingSouvenir.setName(souvenir.getName());
            existingSouvenir.setReleaseDate(souvenir.getReleaseDate());
            existingSouvenir.setPrice(souvenir.getPrice());
        }

        saveDataToFile();
    }


    @Override
    public void delete(String name) throws SouvenirNotFoundException {
        boolean removed = souvenirs.removeIf(s -> s.getName().equals(name));
        if (!removed) {
            throw new SouvenirNotFoundException("Souvenir with name - " + name + " not found.");
        }
    }

    @Override
    public void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("_");
                if (fields.length >= 3) {

                    List<Manufacture> manufactures = new ArrayList<>();

                    String name = fields[0];
                    LocalDate releaseDate = LocalDate.parse(fields[1]);
                    double price = Double.parseDouble(fields[2]);

                    if (fields.length == 4) {
                        String[] souvenirManufactures = fields[3].split("\\|");

                        for (String souvenirManufacture : souvenirManufactures) {
                            String[] manufacture = souvenirManufacture.split(";");
                            manufactures.add(new Manufacture(manufacture[0], manufacture[1]));
                        }

                    } else {
                        Manufacture manufacture = new Manufacture("None", "Default");
                        manufactures.add(manufacture);
                    }

                    Souvenir souvenir = new Souvenir(name, releaseDate, price, manufactures);
                    if (souvenirs.contains(souvenir)){
                        continue;
                    }
                    souvenirs.add(souvenir);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            SouvenirSaveFormatter sf = new SouvenirSaveFormatter();

            for (Souvenir souvenir : souvenirs) {
                writer.write(sf.save(souvenir));
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Souvenir> getListOfAllData() {
        return new ArrayList<>(souvenirs);
    }

    public void deleteManufacture(String name) throws ManufacturerNotFoundException {

        List<Souvenir> souvenirList = souvenirs;

        for (int i = 0; i < souvenirList.size(); i++) {
            List<Manufacture> manufactures = souvenirList.get(i).getManufacturers();

            if (manufactures.size() == 1 && manufactures.get(0).getName().equals(name)) {
                souvenirs.remove(i);
                i--;
            }

            if (manufactures.size() > 1) {
                souvenirList.get(i).removeManufacturer(manufactureRepository.getManufactureByName(name));
            }
        }

        try {
            getManufactureRepository().delete(name);
        } catch (ManufacturerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public Souvenir getSouvenirByName(String name) throws SouvenirNotFoundException {
        List<Souvenir> listOfAllSouvenirs = getListOfAllData();
        Souvenir equationSouvenir = null;
        for (Souvenir souvenir : listOfAllSouvenirs) {
            if (souvenir.getName().equals(name)) {
                equationSouvenir = souvenir;
            }
        }

        if (equationSouvenir == null) {
            throw new SouvenirNotFoundException("Souvenir with name - " + name + "not found!!!");
        } else {
            return equationSouvenir;
        }

    }

    public List<Souvenir> getSouvenirsByManufacturer(String manufacturerName) {
        return souvenirs.stream()
                .filter(souvenir -> souvenir.getManufacturers().stream()
                        .anyMatch(manufacturer -> manufacturer.getName().equals(manufacturerName)))
                .collect(Collectors.toList());
    }

    public List<Souvenir> getSouvenirsByCountry(String country) {
        return souvenirs.stream()
                .filter(souvenir -> souvenir.getManufacturers().stream()
                        .anyMatch(manufacturer -> manufacturer.getCountry().equals(country)))
                .collect(Collectors.toList());
    }

    public List<Manufacture> getManufacturersByMaxPrice(double maxPrice) {
        return souvenirs.stream()
                .filter(souvenir -> souvenir.getPrice() < maxPrice)
                .map(Souvenir::getManufacturers)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Souvenir> getSouvenirsByManufacturerAndYear(String manufacturerName, String year) {
        return souvenirs.stream()
                .filter(souvenir -> souvenir.getManufacturers().stream()
                        .anyMatch(manufacturer -> manufacturer.getName().equals(manufacturerName)))
                .filter(souvenir -> souvenir.getReleaseDate().getYear() == Integer.parseInt(year))
                .collect(Collectors.toList());
    }

    public Map<Integer, List<Souvenir>> groupSouvenirsByYear() {
        Map<Integer, List<Souvenir>> souvenirsByYear = new HashMap<>();

        for (Souvenir souvenir : souvenirs) {
            int year = souvenir.getReleaseDate().getYear();
            souvenirsByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(souvenir);
        }

        return souvenirsByYear;
    }

    public ManufactureRepository getManufactureRepository() {
        return manufactureRepository;
    }

}
