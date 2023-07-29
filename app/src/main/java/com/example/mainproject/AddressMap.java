package com.example.mainproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressMap {
    private Map<String, List<String>> divisionDistrictMap;

    public AddressMap() {
        initializeDivisionDistrictMap();
    }

    private void initializeDivisionDistrictMap() {
        divisionDistrictMap = new HashMap<>();

        // Add division-district mappings for Dhaka division
        List<String> dhakaDistricts = new ArrayList<>();
        dhakaDistricts.add("Dhaka");
        dhakaDistricts.add("Gazipur");
        dhakaDistricts.add("Narayanganj");
        dhakaDistricts.add("Narsingdi");
        divisionDistrictMap.put("Dhaka", dhakaDistricts);

        // Add division-district mappings for Chittagong division
        List<String> chittagongDistricts = new ArrayList<>();
        chittagongDistricts.add("Chittagong");
        chittagongDistricts.add("Cox's Bazar");
        chittagongDistricts.add("Comilla");
        chittagongDistricts.add("Feni");
        divisionDistrictMap.put("Chittagong", chittagongDistricts);

        // Add division-district mappings for other divisions
        List<String> rajshahiDistricts = new ArrayList<>();
        rajshahiDistricts.add("Rajshahi");
        rajshahiDistricts.add("Pabna");
        rajshahiDistricts.add("Bogura");
        divisionDistrictMap.put("Rajshahi", rajshahiDistricts);

        List<String> khulnaDistricts = new ArrayList<>();
        khulnaDistricts.add("Khulna");
        khulnaDistricts.add("Jessore");
        khulnaDistricts.add("Satkhira");
        divisionDistrictMap.put("Khulna", khulnaDistricts);

        List<String> barisalDistricts = new ArrayList<>();
        barisalDistricts.add("Barisal");
        barisalDistricts.add("Patuakhali");
        barisalDistricts.add("Bhola");
        divisionDistrictMap.put("Barisal", barisalDistricts);

        // Add more divisions and districts as needed
    }

    public Map<String, List<String>> getDivisionDistrictMap() {
        return divisionDistrictMap;
    }
}
