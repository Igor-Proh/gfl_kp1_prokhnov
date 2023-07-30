package com.prokhnov.solution_kp_1.main.repositories;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;
import com.prokhnov.solution_kp_1.main.exceptions.ManufacturerNotFoundException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ManufactureRepositoryTest {
    private ManufactureRepository manufactureRepository;

    @BeforeClass
    public void inti() {
        String testDataFilePath = "testManufactureData.txt";
        manufactureRepository = new ManufactureRepository(testDataFilePath);
    }

    @BeforeMethod
    public void beforeMethod() {
        manufactureRepository.loadDataFromFile();
    }

    @AfterMethod
    public void afterMethod() {

        recreateTestFile();
    }

    @Test
    public void testAdd() throws ManufacturerNotFoundException {
        Manufacture manufacture = new Manufacture("Test Manufacturer", "Test Country");
        manufactureRepository.add(manufacture);

        Manufacture addedManufacture = manufactureRepository.getManufactureByName("Test Manufacturer");

        Assert.assertNotNull(addedManufacture);
        Assert.assertEquals(addedManufacture.getName(), "Test Manufacturer");
        Assert.assertEquals(addedManufacture.getCountry(), "Test Country");

    }


    @Test
    public void testDelete() throws ManufacturerNotFoundException {
        Manufacture manufacture = new Manufacture("Test Manufacturer", "Test Country");
        manufactureRepository.add(manufacture);

        Assert.assertEquals(manufactureRepository.getListOfAllData().size(), 1);
        manufactureRepository.delete("Test Manufacturer");

        Assert.assertEquals(manufactureRepository.getListOfAllData().size(), 0);
    }

    @Test
    public void testGetListOfAllData() {
        Manufacture m1 = new Manufacture("Manufacturer 1", "Country 1");
        Manufacture m2 = new Manufacture("Manufacturer 2", "Country 2");
        manufactureRepository.add(m1);
        manufactureRepository.add(m2);

        List<Manufacture> allManufactures = manufactureRepository.getListOfAllData();

        Assert.assertFalse(allManufactures.isEmpty());
        Assert.assertTrue(allManufactures.contains(m1));
        Assert.assertTrue(allManufactures.contains(m2));
    }

    @Test
    public void testGetManufactureByName() {
    }

    private void recreateTestFile() {
        File file = new File("testManufactureData.txt");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}