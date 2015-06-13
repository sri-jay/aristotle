package com.example.marvel.fast_potato.knowledge;

import com.example.marvel.fast_potato.telemetrics.UserTelemetrics;

/**
 * Created by Sriduth_2 on 22-12-2014.
 */
public interface Knowledge {
    public String getUniqueHash();
    public String getKnowledgeType();
    public String getPathProgress();

    public String getKnowledgeTitle();
    public Object getKnowledgeContent();

    public Object getMultimediaContent();

    public UserTelemetrics getTelemetrics();

    public String getMediaType();
}
