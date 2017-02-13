package com.tiagoespinha.popmovee.model;

import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideo;

/**
 * Created by TiagoEspinha on 12/02/2017.
 */

public class MovieTrailerMetadata {
    private String id;
    private String key;

    public MovieTrailerMetadata(String id, String key, String name, String site) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    private String name;
    private String site;

    public static MovieTrailerMetadata parseFromTMDBMovieVideo(TMDBMovieVideo tmdbMovieVideo) {
        return new MovieTrailerMetadata(tmdbMovieVideo.getId(),
                tmdbMovieVideo.getKey(),
                tmdbMovieVideo.getName(),
                tmdbMovieVideo.getSite());
    }
}
