package com.example.marvel.fast_potato.knowledge;

import com.example.marvel.fast_potato.telemetrics.UserInteractionTelemetry;
import com.example.marvel.fast_potato.telemetrics.UserTelemetrics;

/**
 * Created by Sriduth_2 on 22-12-2014.
 */
public class QuestionUnit extends KnowledgeTypes implements Knowledge {
    private String questionID = null;
    private String questionStatement = null;
    private String questionData = null;
    private String[] answerOptions = null;
    private Object mediaContent = null;
    private String mediaType = null;

    private String answer = null;

    private UserTelemetrics telemetrics = null;

    QuestionUnit(String qId, String qStm, String qData, String[] qOptions, String pProgress, Object media, String type) {
        questionID = qId;
        questionStatement = qStm;
        questionData = qData;
        answerOptions = qOptions;
        pathProgress = pProgress;
        mediaContent = media;
        mediaType = type;
        telemetrics = new UserInteractionTelemetry.QuestionTelemetry();
    }

    @Override
    public String getUniqueHash() {
        return questionID;
    }

    @Override
    public String getKnowledgeType() {
        return KNOWLEDGE_TYPE_QUESTION;
    }

    @Override
    public String getPathProgress() {
        return pathProgress;
    }

    @Override
    public String getKnowledgeTitle() {
        return getQuestionStatement();
    }

    @Override
    public String[] getKnowledgeContent() {
        return getAnswerOptions();
    }

    public String getQuestionStatement() {
        return questionStatement;
    }

    public String getQuestionData() {
        return questionData;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerValue(String aVal) {
        answer = aVal;
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
