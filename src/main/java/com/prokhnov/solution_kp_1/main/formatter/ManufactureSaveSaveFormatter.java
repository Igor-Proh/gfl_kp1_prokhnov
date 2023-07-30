package com.prokhnov.solution_kp_1.main.formatter;

import com.prokhnov.solution_kp_1.main.entity.Manufacture;

public class ManufactureSaveSaveFormatter implements SaveFormatter<Manufacture> {

    @Override
    public String save(Manufacture manufacture) {
        return manufacture.getName() + ":" + manufacture.getCountry();
    }
}
