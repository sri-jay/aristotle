package com.example.marvel.fast_potato;

/**
 * Created by Sriduth_2 on 22-12-2014.
 */
public class KnowledgeUnit extends KnowledgeTypes implements Knowledge {
    private String kID = null;

    private String knowledgeContent = null;
    private String knowledgeTitle = null;
    String mediaType = null;

    private Object mediaContent = null;

    private UserTelemetrics telemetrics = null;

    KnowledgeUnit(String uID, String uStm, String uTitle, String pProgress, Object media, String type) {
        kID = uID;
        knowledgeContent = uStm;
        knowledgeTitle = uTitle;
        pathProgress = pProgress;
        mediaContent = media;
        mediaType = type;
        telemetrics = new UserInteractionTelemetry.InformationTelemetry();
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
        return mediaContent;
    }

    @Override
    public UserTelemetrics getTelemetrics() {
        return telemetrics;
    }

    @Override
    public String getMediaType() { return mediaType; }
}
