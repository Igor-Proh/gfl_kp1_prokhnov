package com.prokhnov.solution_kp_1.main.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Souvenir {
    private String name;
    private final List<Manufacture> manufacturers;
    private LocalDate releaseDate;
    private double price;

    public Souvenir(String name, LocalDate releaseDate, double price) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.price = price;
        this.manufacturers = new ArrayList<>();
        Manufacture manufacture = new Manufacture("None","Default");
        manufacturers.add(manufacture);

    }

    public Souvenir(String name,  LocalDate releaseDate, double price, List<Manufacture> manufacturers) {
        this.name = name;
        this.manufacturers = manufacturers;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addManufacturer(Manufacture manufacturer) {
        manufacturers.add(manufacturer);
    }

    public void removeManufacturer(Manufacture manufacturer) {
        manufacturers.remove(manufacturer);
    }

    public List<Manufacture> getManufacturers() {
        return new ArrayList<>(manufacturers);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Souvenir souvenir = (Souvenir) o;
        return Double.compare(souvenir.price, price) == 0 && Objects.equals(name, souvenir.name) && Objects.equals(manufacturers, souvenir.manufacturers) && Objects.equals(releaseDate, souvenir.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturers, releaseDate, price);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Release Date: ").append(releaseDate).append("\n");
        sb.append("Price: ").append(price).append("\n");
        sb.append("Manufacturers: ");
        for (Manufacture manufacturer : manufacturers) {
            sb.append(manufacturer.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("\n");
        return sb.toString();
    }
}
