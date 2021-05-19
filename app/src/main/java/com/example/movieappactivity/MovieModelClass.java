package com.example.movieappactivity;

public class MovieModelClass {
    String name;
    String description;
    String img;
    String id;

    String email;

    public MovieModelClass( String id,String name, String description, String img) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.id = id;


    }
    public MovieModelClass(String id,String name,String email) {

        this.email = email;
        this.id=id;
        this.name = name;
    }
    public MovieModelClass() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
