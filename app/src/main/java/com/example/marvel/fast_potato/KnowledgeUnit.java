package com.example.marvel.fast_potato;

import android.graphics.Bitmap;

/**
 * Created by Sriduth_2 on 22-12-2014.
 */
public class KnowledgeUnit extends KnowledgeTypes implements Knowledge {
    private String kID = null;

    private String knowledgeContent = null;
    private String knowledgeTitle = null;

    private Bitmap imageContent = null;

    KnowledgeUnit(String uID, String uStm, String uTitle, String pProgress, Bitmap bmp) {
        kID = uID;
        knowledgeContent = uStm;
        knowledgeTitle = uTitle;
        pathProgress = pProgress;
        imageContent = bmp;
    }

    @Override
    public String getUniqueHash() {
        return kID;
    }

    @Override
    public String getKnowledgeType() {
        return KNOWLEDGE_TYPE_UNIT;
    }

    @Override
    public  String getPathProgress() {
        return pathProgress;
    }

    @Override
    public String getKnowledgeTitle() {
        return knowledgeTitle;
    }

    @Override
    public Object getKnowledgeContent() {
        return knowledgeContent;
    }

    @Override
    public Object getMultimediaContent() {
        return imageContent;
    }
}
