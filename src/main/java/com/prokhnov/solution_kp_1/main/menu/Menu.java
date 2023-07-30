package com.prokhnov.solution_kp_1.main.menu;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;
import com.prokhnov.solution_kp_1.main.entity.Souvenir;
import com.prokhnov.solution_kp_1.main.exceptions.ManufacturerNotFoundException;
import com.prokhnov.solution_kp_1.main.exceptions.SouvenirNotFoundException;
import com.prokhnov.solution_kp_1.main.repositories.ManufactureRepository;
import com.prokhnov.solution_kp_1.main.repositories.SouvenirRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final SouvenirRepository souvenirRepository;
    private final ManufactureRepository manufactureRepository;

    public Menu(String souvenirDataPath, String manufactureDataPath) {
        manufactureRepository = new ManufactureRepository(manufactureDataPath);
        souvenirRepository = new SouvenirRepository(souvenirDataPath, manufactureRepository);
    }

    public void run() {
        manufactureRepository.loadDataFromFile();
        souvenirRepository.loadDataFromFile();

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("Souvenir Menu:");
            System.out.println("1. Add New Souvenir");
            System.out.println("2. Edit Souvenir");
            System.out.println("3. Delete Souvenir by Name");
            System.out.println("4. Show All Souvenirs");
            System.out.println("5. Show All Souvenirs by Manufacturer Name");
            System.out.println("6. Show All Souvenirs by Manufacturer Country");
            System.out.println("7. Show Manufacturers with Souvenirs Under a Max Price");
            System.out.println("8. Show Souvenirs by Manufacturer and Year");
            System.out.println("9. Group Souvenirs by Year");

            System.out.println("\nManufacture Menu:");
            System.out.println("10. Add New Manufacture");
            System.out.println("11. Edit Manufacture");
            System.out.println("12. Delete Manufacture by Name");
            System.out.println("13. Show All Manufactures");
            System.out.println("14. Show All Manufactures and Their Souvenirs");

            System.out.println("\nOther:");
            System.out.println("15. Exit");

            System.out.print("Enter your choice (1-15): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addNewSouvenir();
                    break;
                case 2:
                    editSouvenir();
                    break;
                case 3:
                    deleteSouvenirByName();
                    break;
                case 4:
                    showAllSouvenirs();
                    break;
                case 5:
                    showSouvenirsByManufacturerName();
                    break;
                case 6:
                    showSouvenirsByManufacturerCountry();
                    break;
                case 7:
                    showManufacturersByMaxPrice();
                    break;
                case 8:
                    showSouvenirsByManufacturerAndYear();
                    break;
                case 9:
                    groupSouvenirsByYear();
                    break;

                case 10:
                    addNewManufacture();
                    break;
                case 11:
                    editManufacture();
                    break;
                case 12:
                    deleteManufactureByName();
                    break;
                case 13:
                    showAllManufactures();
                    break;
                case 14:
                    showAllManufacturesAndTheirSouvenirs();
                    break;

                case 15:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }


    private void addNewSouvenir() {
        System.out.println("Enter the name of the souvenir: ");
        String name = scanner.nextLine();

        System.out.println("Enter the release date (YYYY-MM-DD): ");
        LocalDate releaseDate = LocalDate.parse(scanner.nextLine());

        System.out.println("Enter the price of the souvenir: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Souvenir souvenir = new Souvenir(name, releaseDate, price);
        souvenirRepository.add(souvenir);
        souvenirRepository.saveDataToFile();
        System.out.println("Souvenir added successfully.");
    }


    private void deleteSouvenirByName() {
        System.out.println("Enter the name of the souvenir to delete: ");
        String name = scanner.nextLine();
        try {
            souvenirRepository.delete(name);
            souvenirRepository.saveDataToFile();
        } catch (SouvenirNotFoundException e) {
            System.out.println("Souvenir with name - " + name + "not found.");
        }
        souvenirRepository.saveDataToFile();

    }

    private void showSouvenirsByManufacturerName() {
        System.out.println("Enter the manufacturer name: ");
        String manufacturerName = scanner.nextLine();
        List<Souvenir> souvenirs = souvenirRepository.getSouvenirsByManufacturer(manufacturerName);
        if (!souvenirs.isEmpty()) {
            for (Souvenir souvenir : souvenirs) {
                System.out.println(souvenir);
            }
        } else {
            System.out.println("No souvenirs found for manufacturer: " + manufacturerName);
        }
    }

    private void showSouvenirsByManufacturerCountry() {
        System.out.println("Enter the manufacturer country: ");
        String country = scanner.nextLine();
        List<Souvenir> souvenirs = souvenirRepository.getSouvenirsByCountry(country);
        if (!souvenirs.isEmpty()) {
            for (Souvenir souvenir : souvenirs) {
                System.out.println(souvenir);
            }
        } else {
            System.out.println("No souvenirs found for country: " + country);
        }
    }

    private void showManufacturersByMaxPrice() {
        System.out.println("Enter the maximum price: ");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine();

        List<Manufacture> manufacturers = souvenirRepository.getManufacturersByMaxPrice(maxPrice);
        if (!manufacturers.isEmpty()) {
            for (Manufacture manufacturer : manufacturers) {
                System.out.println(manufacturer);
            }
        } else {
            System.out.println("No manufacturers found with souvenirs under the maximum price: " + maxPrice);
        }
    }

    private void showSouvenirsByManufacturerAndYear() {
        System.out.println("Enter the manufacturer name: ");
        String manufacturerName = scanner.nextLine();

        System.out.println("Enter the year: ");
        String year = scanner.nextLine();

        List<Souvenir> souvenirs = souvenirRepository.getSouvenirsByManufacturerAndYear(manufacturerName, year);
        if (!souvenirs.isEmpty()) {
            for (Souvenir souvenir : souvenirs) {
                System.out.println(souvenir);
            }
        } else {
            System.out.println("No souvenirs found for manufacturer: " + manufacturerName + " and year: " + year);
        }
    }

    private void groupSouvenirsByYear() {
        System.out.println("Souvenirs grouped by year:");
        for (Integer year : souvenirRepository.groupSouvenirsByYear().keySet()) {
            System.out.println("Year: " + year);
            for (Souvenir souvenir : souvenirRepository.groupSouvenirsByYear().get(year)) {
                System.out.println(souvenir);
            }
        }
    }

    private void showAllManufacturesAndTheirSouvenirs() {
        manufactureRepository.showAllManufacturesAndTheirSouvenirs(souvenirRepository);
    }

    private void deleteManufactureByName() {
        System.out.println("Enter the name of the manufacture to delete: ");
        String name = scanner.nextLine();

        try {
            manufactureRepository.delete(name);
            souvenirRepository.deleteManufacture(name);
            manufactureRepository.saveDataToFile();
            souvenirRepository.saveDataToFile();
            System.out.println("Manufacture and associated souvenirs deleted successfully.");
        } catch (ManufacturerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAllSouvenirs() {
        List<Souvenir> souvenirs = souvenirRepository.getListOfAllData();
        if (!souvenirs.isEmpty()) {
            System.out.println("===== All Souvenirs =====");
            for (Souvenir souvenir : souvenirs) {
                System.out.println(souvenir);
            }
        } else {
            System.out.println("No souvenirs found.");
        }
    }

    private void showAllManufactures() {
        List<Manufacture> manufactures = manufactureRepository.getListOfAllData();
        if (!manufactures.isEmpty()) {
            System.out.println("===== All Manufactures =====");
            for (Manufacture manufacture : manufactures) {
                System.out.println(manufacture);
            }
        } else {
            System.out.println("No manufactures found.");
        }
    }

    //TODO
    private void editSouvenir() {
        System.out.println("Enter the name of the souvenir to edit: ");
        String name = scanner.nextLine();


        Souvenir souvenirToEdit = null;
        try {
            souvenirToEdit = souvenirRepository.getSouvenirByName(name);
        } catch (SouvenirNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Enter new name of the souvenir: ");

        String newName = scanner.nextLine();
        System.out.println("Enter new release date of the souvenir(yyyy-mm-dd): ");

        String newReleaseDate = scanner.nextLine();
        if (newReleaseDate.isBlank()){
            newReleaseDate = souvenirToEdit.getReleaseDate().toString();
        }

        System.out.println("Enter new price of the souvenir(0.0): ");
        String newPrice = scanner.nextLine();

        if (newName != null && newReleaseDate != null && newPrice != null) {
            assert souvenirToEdit != null;
            souvenirToEdit.setName(newName);
            souvenirToEdit.setReleaseDate(LocalDate.parse(newReleaseDate));
            souvenirToEdit.setPrice(Double.parseDouble(newPrice));
        }
        if (souvenirToEdit != null) {
            try {
                souvenirRepository.edit(souvenirToEdit);
            } catch (SouvenirNotFoundException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Souvenir edited successfully.");
            souvenirRepository.saveDataToFile();
        }
    }

    private void editManufacture() {
        System.out.println("Enter the name of the manufacture to edit: ");
        String name = scanner.nextLine();

        Manufacture manufactureToEdit = null;
        try {
            manufactureToEdit = manufactureRepository.getManufactureByName(name);
        } catch (ManufacturerNotFoundException e) {
            System.out.println("Manufacture with name '" + name + "' not found.");
        }

        System.out.println("Enter new name of the manufacture: ");
        String newName = scanner.nextLine();
        System.out.println("Enter country of the manufacture: ");
        String newCountry = scanner.nextLine();

        if (newName.isBlank()) {
            System.out.println("incorrect input!");
        } else if (manufactureToEdit != null) {
            manufactureToEdit.setName(newName);
            manufactureToEdit.setCountry(newCountry);
        }

        try {
            manufactureRepository.edit(manufactureToEdit);
        } catch (ManufacturerNotFoundException e) {
            System.out.println("Manufacture with name '" + name + "' not found.");
        }

        manufactureRepository.saveDataToFile();
        System.out.println("Manufacture edited successfully.");

    }

    private void addNewManufacture() {
        System.out.println("Enter the name of the new manufacture: ");
        String name = scanner.nextLine();

        System.out.println("Enter the country of the new manufacture: ");
        String country = scanner.nextLine();

        Manufacture newManufacture = new Manufacture(name, country);
        manufactureRepository.add(newManufacture);
        manufactureRepository.saveDataToFile();

        System.out.println("New manufacture added successfully.");
    }


}
