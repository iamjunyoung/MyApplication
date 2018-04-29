package com.bbeaggoo.myapplication.datas;

public class ItemObjects {
    //PrimaryKey
    private int id;

    private String name;
    private int photo;
    private int height = -1;

    public ItemObjects(int id, String name, int photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getHeight() { return height;}

    public void setHeight(int height) { this.height = height; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemObjects that = (ItemObjects) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
