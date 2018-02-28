package com.avaj.zhcettpo.model;

/**
 * Created by aakra on 7/27/2017.
 */

public class Model {

    private String image;
    private String name;
    private String desig;
    private String branch;

    public Model(String image, String name, String desig, String branch) {
        this.image = image;
        this.name = name;
        this.desig = desig;
        this.branch = branch;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesig() {
        return desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
