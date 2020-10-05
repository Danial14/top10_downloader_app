package com.example.noman_000.top_10_downloader;


public class FeedEntry {
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSuMMary() {
        return suMMary;
    }

    public void setSuMMary(String suMMary) {
        this.suMMary = suMMary;
    }

    public String getiMageUrl() {
        return iMageUrl;
    }

    public void setiMageUrl(String iMageUrl) {
        this.iMageUrl = iMageUrl;
    }

    private String naMe;
    private String artist;
    private String releaseDate;
    private String suMMary;
    private String iMageUrl;

    public String getNaMe() {
        return naMe;
    }

    public void setNaMe(String naMe) {
        this.naMe = naMe;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "naMe : " + naMe + "\nartist : " + artist + "\nsuMMary" + suMMary + "\nreleaseDate" + releaseDate
                +"\niMageUrl" + iMageUrl;
    }
}

