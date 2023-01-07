package com.example.kargotakip;

public class Cargo {
    private String cargo_id, cargo_name, cargo_no, cargo_date, cargo_status;

    public Cargo(String cargo_id, String cargo_name, String cargo_no, String cargo_status) {
        this.cargo_id = cargo_id;
        this.cargo_name = cargo_name;
        this.cargo_no = cargo_no;
        this.cargo_status = cargo_status;
    }

    public String getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(String cargo_id) {
        this.cargo_id = cargo_id;
    }

    public String getCargo_name() {
        return cargo_name;
    }

    public void setCargo_name(String cargo_name) {
        this.cargo_name = cargo_name;
    }

    public String getCargo_no() {
        return cargo_no;
    }

    public void setCargo_no(String cargo_no) {
        this.cargo_no = cargo_no;
    }

    public String getCargo_date() {
        return cargo_date;
    }

    public void setCargo_date(String cargo_date) {
        this.cargo_date = cargo_date;
    }

    public String getCargo_status() {
        return cargo_status;
    }

    public void setCargo_status(String cargo_status) {
        this.cargo_status = cargo_status;
    }
}
