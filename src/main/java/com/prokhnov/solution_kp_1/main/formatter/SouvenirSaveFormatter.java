package com.prokhnov.solution_kp_1.main.formatter;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;
import com.prokhnov.solution_kp_1.main.entity.Souvenir;

import java.util.List;

public class SouvenirSaveFormatter implements SaveFormatter<Souvenir> {
    @Override
    public String save(Souvenir souvenir) {
        StringBuilder sbManufacturers = new StringBuilder();
        StringBuilder sbSouvenir = new StringBuilder();
        List<Manufacture> manufacturers = souvenir.getManufacturers();

        for (Manufacture m: manufacturers) {

            sbManufacturers
                    .append(m.getName())
                    .append(";")
                    .append(m.getCountry())
                    .append("|");
        }

        sbSouvenir
                .append(souvenir.getName())
                .append("_")
                .append(souvenir.getReleaseDate())
                .append("_")
                .append(souvenir.getPrice())
                .append("_");
        if (sbManufacturers.length() != 0){
            return sbSouvenir.append(sbManufacturers.deleteCharAt(sbManufacturers.length()-1)).toString();
        } else {
            return sbSouvenir.toString();
        }


    }
}
