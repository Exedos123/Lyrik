package com.exedos.lyrik;

public class HomeListModel {
    private String songTitle;
    private String songId;


    public HomeListModel(String songTitle, String songId) {

        this.songTitle = songTitle;
        this.songId = songId;
    }




    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle ) {
        this.songTitle = songTitle;
    }

    public String getSongId() {
        return songId;
    }
}
