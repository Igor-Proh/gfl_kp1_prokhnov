package com.prokhnov.solution_kp_1.main.repositories;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;
import com.prokhnov.solution_kp_1.main.entity.Souvenir;
import com.prokhnov.solution_kp_1.main.exceptions.ManufacturerNotFoundException;
import com.prokhnov.solution_kp_1.main.exceptions.SouvenirNotFoundException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.*;

public class SouvenirRepositoryTest {
    private static final String MANUFACTURERS_DATA_FILE_PATH = "testManufactureData.txt";
    private static final String SOUVENIRS_DATA_FILE_PATH = "testSouvenirData.txt";
    private SouvenirRepository souvenirRepository;
    private List<Souvenir> souvenirs;

    @BeforeMethod
    public void init() {
        ManufactureRepository manufactureRepository = new ManufactureRepository(MANUFACTURERS_DATA_FILE_PATH);
        souvenirRepository = new SouvenirRepository(SOUVENIRS_DATA_FILE_PATH, manufactureRepository);
    }

    @AfterMethod
    public void destroy() {
        recreateTestFile();
        souvenirs = null;
    }

    @Test
    public void testAdd() throws SouvenirNotFoundException {
        Souvenir souvenir = new Souvenir("TestSouvenir", LocalDate.now(), 2.99, List.of(new Manufacture("TestManufacturer", "TestCountry")));
        souvenirRepository.add(souvenir);

        Souvenir retrievedSouvenir = souvenirRepository.getSouvenirByName("TestSouvenir");
        assertEquals(retrievedSouvenir, souvenir);
    }

    @Test
    public void testDelete() throws SouvenirNotFoundException {
        Souvenir souvenir = new Souvenir("ToDelete", LocalDate.now(), 10.0, List.of(new Manufacture("Manufacturer", "Country")));
        souvenirRepository.add(souvenir);

        souvenirRepository.delete("ToDelete");

        boolean souvenirExists = souvenirRepository.getListOfAllData().stream().anyMatch(s -> s.getName().equals("ToDelete"));
        assertFalse(souvenirExists);
    }

    @Test
    public void testLoadDataFromFile() {
        String testData = "TestSouvenir1_2023-07-29_15.99_Manufacturer1;Country1|Manufacturer2;Country2\n"
                + "TestSouvenir2_2023-07-29_9.99_Manufacturer3;Country3\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SOUVENIRS_DATA_FILE_PATH))) {
            writer.write(testData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        souvenirRepository.loadDataFromFile();

        souvenirs = souvenirRepository.getListOfAllData();
        assertEquals(souvenirs.size(), 2);

        Souvenir souvenir1 = souvenirs.get(0);
        assertEquals(souvenir1.getName(), "TestSouvenir1");
        assertEquals(souvenir1.getReleaseDate(), LocalDate.of(2023, 7, 29));
        assertEquals(souvenir1.getPrice(), 15.99);
        List<Manufacture> manufactures1 = souvenir1.getManufacturers();
        assertEquals(manufactures1.size(), 2);
        assertEquals(manufactures1.get(0).getName(), "Manufacturer1");
        assertEquals(manufactures1.get(0).getCountry(), "Country1");
        assertEquals(manufactures1.get(1).getName(), "Manufacturer2");
        assertEquals(manufactures1.get(1).getCountry(), "Country2");

        Souvenir souvenir2 = souvenirs.get(1);
        assertEquals(souvenir2.getName(), "TestSouvenir2");
        assertEquals(souvenir2.getReleaseDate(), LocalDate.of(2023, 7, 29));
        assertEquals(souvenir2.getPrice(), 9.99);
        List<Manufacture> manufactures2 = souvenir2.getManufacturers();
        assertEquals(manufactures2.size(), 1);
        assertEquals(manufactures2.get(0).getName(), "Manufacturer3");
        assertEquals(manufactures2.get(0).getCountry(), "Country3");
    }

    @Test
    public void testGetListOfAllData() {
        List<Manufacture> manufactures1 = List.of(new Manufacture("Manufacturer1", "Country1"));
        List<Manufacture> manufactures2 = List.of(new Manufacture("Manufacturer2", "Country2"));

        Souvenir souvenir1 = new Souvenir("TestSouvenir1", LocalDate.of(2023, 7, 29), 15.99, manufactures1);
        Souvenir souvenir2 = new Souvenir("TestSouvenir2", LocalDate.of(2023, 7, 30), 20.49, manufactures2);

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);

        souvenirs = souvenirRepository.getListOfAllData();

        souvenirRepository.saveDataToFile();

        assertEquals(souvenirs.size(), 2);

        assertEquals(souvenirs.get(0).getName(), "TestSouvenir1");
        assertEquals(souvenirs.get(0).getReleaseDate(), LocalDate.of(2023, 7, 29));
        assertEquals(souvenirs.get(0).getPrice(), 15.99);
        assertEquals(souvenirs.get(0).getManufacturers(), manufactures1);

        assertEquals(souvenirs.get(1).getName(), "TestSouvenir2");
        assertEquals(souvenirs.get(1).getReleaseDate(), LocalDate.of(2023, 7, 30));
        assertEquals(souvenirs.get(1).getPrice(), 20.49);
        assertEquals(souvenirs.get(1).getManufacturers(), manufactures2);
    }

    @Test
    public void testDeleteManufacturer() throws ManufacturerNotFoundException {
        List<Manufacture> manufactures1 = List.of(new Manufacture("Manufacturer1", "Country1"));
        List<Manufacture> manufactures2 = List.of(new Manufacture("Manufacturer2", "Country2"));
        List<Manufacture> manufactures3 = List.of(new Manufacture("Manufacturer3", "Country3"));

        Souvenir souvenir1 = new Souvenir("Test1", LocalDate.of(2022, 1, 1), 10.0, manufactures1);
        Souvenir souvenir2 = new Souvenir("Test2", LocalDate.of(2023, 7, 30), 20.0, manufactures2);
        Souvenir souvenir3 = new Souvenir("Test3", LocalDate.of(2023, 7, 31), 15.0, manufactures3);

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);
        souvenirRepository.add(souvenir3);

        String manufacturerName = "Manufacturer2";
        souvenirRepository.deleteManufacture(manufacturerName);

        List<Souvenir> updatedSouvenirs = souvenirRepository.getListOfAllData();
        assertEquals(updatedSouvenirs.size(), 2);

    }

    @Test
    public void testGetSouvenirByName() throws SouvenirNotFoundException {

        List<Manufacture> manufactures1 = List.of(new Manufacture("Manufacturer1", "Country1"));
        List<Manufacture> manufactures2 = List.of(new Manufacture("Manufacturer2", "Country2"));

        Souvenir souvenir1 = new Souvenir("TestSouvenir1", LocalDate.of(2023, 7, 29), 15.99, manufactures1);
        Souvenir souvenir2 = new Souvenir("TestSouvenir2", LocalDate.of(2023, 7, 30), 20.49, manufactures2);

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);

        Souvenir souvenir = souvenirRepository.getSouvenirByName("TestSouvenir2");
        assertEquals(souvenir.getName(), "TestSouvenir2");
        assertEquals(souvenir.getReleaseDate(), LocalDate.of(2023, 7, 30));
        assertEquals(souvenir.getPrice(), 20.49);
        assertEquals(souvenir.getManufacturers(), manufactures2);
    }

    @Test
    public void testGetSouvenirsByManufacturer() {
        Souvenir souvenir1 = new Souvenir("Test1", LocalDate.now(), 10.0, List.of(new Manufacture("Manufacturer1", "Country1")));
        Souvenir souvenir2 = new Souvenir("Test2", LocalDate.now(), 20.0, List.of(new Manufacture("Manufacturer2", "Country2")));

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);

        List<Souvenir> souvenirsByManufacturer = souvenirRepository.getSouvenirsByManufacturer("Manufacturer1");
        assertEquals(souvenirsByManufacturer.size(), 1);
        assertEquals(souvenirsByManufacturer.get(0), souvenir1);

    }

    @Test
    public void testGetSouvenirsByCountry() {
        Souvenir souvenir1 = new Souvenir("Test1", LocalDate.now(), 10.0, List.of(new Manufacture("Manufacturer1", "Country1")));
        Souvenir souvenir2 = new Souvenir("Test2", LocalDate.now(), 20.0, List.of(new Manufacture("Manufacturer2", "Country2")));
        Souvenir souvenir3 = new Souvenir("Test3", LocalDate.now(), 20.0, List.of(new Manufacture("Manufacturer2", "Country2")));

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);
        souvenirRepository.add(souvenir3);

        List<Souvenir> souvenirsByCountry = souvenirRepository.getSouvenirsByCountry("Country2");

        assertEquals(souvenirsByCountry.size(), 2);

        assertEquals(souvenirsByCountry.get(0).getName(), "Test2");
        assertEquals(souvenirsByCountry.get(0).getReleaseDate(), LocalDate.now());
        assertEquals(souvenirsByCountry.get(0).getPrice(), 20.0);

        assertEquals(souvenirsByCountry.get(1).getName(), "Test3");
        assertEquals(souvenirsByCountry.get(1).getReleaseDate(), LocalDate.now());
        assertEquals(souvenirsByCountry.get(1).getPrice(), 20.0);
    }

    @Test
    public void testGetManufacturersByMaxPrice() {
        List<Manufacture> manufactures1 = List.of(new Manufacture("Manufacturer1", "Country1"));
        List<Manufacture> manufactures2 = List.of(new Manufacture("Manufacturer2", "Country2"));

        Souvenir souvenir1 = new Souvenir("Test1", LocalDate.now(), 10.0, manufactures1);
        Souvenir souvenir2 = new Souvenir("Test2", LocalDate.now(), 20.0, manufactures2);
        Souvenir souvenir3 = new Souvenir("Test3", LocalDate.now(), 15.0, manufactures2);

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);
        souvenirRepository.add(souvenir3);

        double maxPrice = 16.0;
        List<Manufacture> manufacturersByMaxPrice = souvenirRepository.getManufacturersByMaxPrice(maxPrice);

        assertEquals(manufacturersByMaxPrice.size(), 2);
    }

    @Test
    public void testGetSouvenirsByManufacturerAndYear() {
        List<Manufacture> manufactures1 = List.of(new Manufacture("Manufacturer1", "Country1"));
        List<Manufacture> manufactures2 = List.of(new Manufacture("Manufacturer2", "Country2"));

        Souvenir souvenir1 = new Souvenir("Test1", LocalDate.of(2022, 1, 1), 10.0, manufactures1);
        Souvenir souvenir2 = new Souvenir("Test2", LocalDate.of(2023, 7, 30), 20.0, manufactures2);
        Souvenir souvenir3 = new Souvenir("Test3", LocalDate.of(2023, 7, 31), 15.0, manufactures2);

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);
        souvenirRepository.add(souvenir3);

        String manufacturerName = "Manufacturer2";
        String year = "2023";
        List<Souvenir> souvenirsByManufacturerAndYear = souvenirRepository.getSouvenirsByManufacturerAndYear(manufacturerName, year);

        assertEquals(souvenirsByManufacturerAndYear.size(), 2);
        assertEquals(souvenirsByManufacturerAndYear.get(0), souvenir2);
        assertEquals(souvenirsByManufacturerAndYear.get(1), souvenir3);
    }

    @Test
    public void testGetManufactureRepository() {
        ManufactureRepository obtainedManufactureRepository = souvenirRepository.getManufactureRepository();
        assertEquals(obtainedManufactureRepository, souvenirRepository.getManufactureRepository());

    }

    private void recreateTestFile(){
        File file = new File("testSouvenirData.txt");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveDataToFile() {
        List<Manufacture> manufactures1 = List.of(new Manufacture("Manufacturer1", "Country1"));
        List<Manufacture> manufactures2 = List.of(new Manufacture("Manufacturer2", "Country2"));

        Souvenir souvenir1 = new Souvenir("TestSouvenir1", LocalDate.of(2023, 7, 29), 15.99, manufactures1);
        Souvenir souvenir2 = new Souvenir("TestSouvenir2", LocalDate.of(2023, 7, 30), 20.49, manufactures2);

        souvenirRepository.add(souvenir1);
        souvenirRepository.add(souvenir2);

        souvenirRepository.saveDataToFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(SOUVENIRS_DATA_FILE_PATH))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                switch (lineNumber) {
                    case 1:
                        assertEquals(line, "TestSouvenir1_2023-07-29_15.99_Manufacturer1;Country1");
                        break;
                    case 2:
                        assertEquals(line, "TestSouvenir2_2023-07-30_20.49_Manufacturer2;Country2");
                        break;
                    default:
                        assert false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}