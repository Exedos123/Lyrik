package com.exedos.lyrik;

public class HomeListModel {
    private String songTitle;

    public HomeListModel(String songTitle) {

        this.songTitle = songTitle;
    }



    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }


}
