package com.example.team33.storyfinder;

import android.media.Image;

import java.util.UUID;


public class Story {

    private UUID mId;
    private String mCardNumber;
    private String mName;
    private String mDescription;
    private Image mImage;

    public Story(UUID id, String cardNumber, String name, String description, Image image) {
        mId = id;
        mCardNumber = cardNumber;
        mName = name;
        mDescription = description;
        mImage = image;
    }

    // using strings from strings XML will crash the app
    public Story() {
        mId = UUID.randomUUID();
        mName = "A random story";
        mDescription = "Suspendisse potenti. In hac habitasse platea dictumst. Donec tempor " +
                "ligula diam, tincidunt euismod ipsum scelerisque ut. Sed sed nibh rhoncus, " +
                "rhoncus mauris ut, pharetra magna. Morbi sit amet euismod purus";
    }


    public String getCardNumber() {
        return mCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        mCardNumber = cardNumber;
    }

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image image) {
        mImage = image;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }


}
