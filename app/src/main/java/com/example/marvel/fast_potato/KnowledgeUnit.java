package com.example.marvel.fast_potato;

/**
 * Created by Sriduth_2 on 22-12-2014.
 */
public class KnowledgeUnit extends KnowledgeTypes implements Knowledge {
    private String kID = null;

    private String knowledgeContent = null;
    private String knowledgeTitle = null;

    KnowledgeUnit(String uID, String uStm, String uTitle, String pProgress) {
        kID = uID;
        knowledgeContent = uStm;
        knowledgeTitle = uTitle;
        pathProgress = pProgress;
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
    public String getKnowledge() {
        return knowledgeContent;
    }
}
