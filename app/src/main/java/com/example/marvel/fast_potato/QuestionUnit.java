package com.example.marvel.fast_potato;

/**
 * Created by Sriduth_2 on 22-12-2014.
 */
public class QuestionUnit extends KnowledgeTypes implements Knowledge {
    private String questionID = null;
    private String questionStatement = null;
    private String questionData = null;
    private String[] answerOptions = null;

    private String answer = null;

    QuestionUnit(String qId, String qStm, String qData, String[] qOptions, String pProgress) {
        questionID = qId;
        questionStatement = qStm;
        questionData = qData;
        answerOptions = qOptions;
        pathProgress = pProgress;
    }

    @Override
    public String getUniqueHash() {
        return questionID;
    }

    @Override
    public String getKnowledgeType() {
        return KNOWLEDGE_TYPE_UNIT;
    }

    @Override
    public  String getPathProgress() {
        return pathProgress;
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
}
