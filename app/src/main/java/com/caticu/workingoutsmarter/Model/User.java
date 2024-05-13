package com.caticu.workingoutsmarter.Model;

public class User
{
    private static User instance;

    public String name;

    private long Id;
    private double Height;
    private double Weight;
    private String PictureUrl;

    private String EmailAddress;
    private User() {

    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        this.Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        this.Weight = weight;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.EmailAddress = emailAddress;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.PictureUrl = pictureUrl;
    }

    // toString method to print the object
    @Override
    public String toString() {
        return "Name: " + name + ", Height: " + Height + "m, Weight: " + Weight + "kg, Email Address: " + EmailAddress + ", Picture URL: " + PictureUrl;
    }

    public User(String name, double height, double weight, String emailAddress, String pictureUrl) {
        this.name = name;
        this.Height = height;
        this.Weight = weight;
        this.EmailAddress = emailAddress;
        this.PictureUrl = pictureUrl;
    }
}
