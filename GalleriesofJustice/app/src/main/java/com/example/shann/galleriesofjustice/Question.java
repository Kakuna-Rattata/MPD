package com.example.shann.galleriesofjustice;

import java.io.Serializable;

/**
 * Created by shann on 16/03/2017.
 *
 * Some Question and Quiz feature code adapted from tutorial https://www.youtube.com/watch?v=016QnvN5x4s
 */

public class Question implements Serializable {

    private int ID;

    private String questionText;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String correctAnswer;

    private boolean creditGiven;

    public Question() {

    }

    public Question(String questionText, String choiceA, String choiceB, String choiceC, String correctAnswer) {

        this.questionText = questionText;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.correctAnswer = correctAnswer;

        this.creditGiven = false;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    public boolean isCorrectAnswer(String selectedAnswer) {
        return (selectedAnswer.equals(correctAnswer));
    }

    @Override
    public String toString() {
        return questionText;
    }
}
