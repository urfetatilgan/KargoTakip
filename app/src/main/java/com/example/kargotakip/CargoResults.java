package com.example.kargotakip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CargoResults {
    public List<CargoResults> data = new ArrayList();
    @SerializedName("cargo_id")
    @Expose
    public Integer cargoId;
    @SerializedName("cargo_name")
    @Expose
    public String cargoName;
    @SerializedName("cargo_no")
    @Expose
    public String cargoNo;
    @SerializedName("cargo_date")
    @Expose
    public String cargoDate;
    @SerializedName("cargo_status")
    @Expose
    public String cargoStatus;

    public CargoResults(String cargoName, String cargoNo, String cargoStatus) {
        this.cargoName = cargoName;
        this.cargoNo = cargoNo;
        this.cargoStatus= cargoStatus;
    }
    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoNo() {
        return cargoNo;
    }

    public void setCargoNo(String cargoNo) {
        this.cargoNo = cargoNo;
    }

    public String getCargoDate() {
        return cargoDate;
    }

    public void setCargoDate(String cargoDate) {
        this.cargoDate = cargoDate;
    }

    public String getCargoStatus() {
        return cargoStatus;
    }

    public void setCargoStatus(String cargoStatus) {
        this.cargoStatus = cargoStatus;
    }

}