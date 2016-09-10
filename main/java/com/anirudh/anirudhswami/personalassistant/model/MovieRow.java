package com.anirudh.anirudhswami.personalassistant.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Anirudh Swami on 07-07-2016.
 */
public class MovieRow {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String roll;

    @DatabaseField(canBeNull = false)
    String image;

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String title;

    @DatabaseField(canBeNull = false)
    String genre;

    @DatabaseField(canBeNull = false)
    String plot;

    @DatabaseField(canBeNull = false)
    String rating;

    @DatabaseField(canBeNull = false)
    String type;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public MovieRow() {

    }

    public MovieRow(String image, String title, String genre, String plot, String rating, String type,String roll) {
        this.image = image;
        this.title = title;
        this.genre = genre;
        this.plot = plot;
        this.rating = rating;
        this.type = type;
        this.roll = roll;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    @Override
    public String toString() {
        //return super.toString();
        return "MovieRow: [ id = " + id + " , Title = " + title + " , Plot = " + plot + " , Image = " + image + " , Genre = " + genre + "]";
    }
}
