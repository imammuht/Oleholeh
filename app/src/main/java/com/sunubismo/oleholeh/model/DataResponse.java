package com.sunubismo.oleholeh.model;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {
    private String status;
    private List<Toko> toko = new ArrayList<>();
    private User user;
    private List<Rating> rating = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Toko> getToko() {
        return toko;
    }

    public void setToko(List<Toko> toko) {
        this.toko = toko;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }
}