package com.anirudh.anirudhswami.personalassistant.model;

/**
 * Created by Anirudh Swami on 07-07-2016.
 */
public class MovieGrid {

    String titleG;
    String plotG;
    String genreG;
    String typeG;
    String posterG;
    String ratingG;

    public MovieGrid(String typeG, String titleG, String ratingG, String posterG, String plotG, String genreG) {
        this.typeG = typeG;
        this.titleG = titleG;
        this.ratingG = ratingG;
        this.posterG = posterG;
        this.plotG = plotG;
        this.genreG = genreG;
    }

    public String getGenreG() {
        return genreG;
    }

    public void setGenreG(String genreG) {
        this.genreG = genreG;
    }

    public String getPlotG() {
        return plotG;
    }

    public void setPlotG(String plotG) {
        this.plotG = plotG;
    }

    public String getPosterG() {
        return posterG;
    }

    public void setPosterG(String posterG) {
        this.posterG = posterG;
    }

    public String getRatingG() {
        return ratingG;
    }

    public void setRatingG(String ratingG) {
        this.ratingG = ratingG;
    }

    public String getTitleG() {
        return titleG;
    }

    public void setTitleG(String titleG) {
        this.titleG = titleG;
    }

    public String getTypeG() {
        return typeG;
    }

    public void setTypeG(String typeG) {
        this.typeG = typeG;
    }
}
