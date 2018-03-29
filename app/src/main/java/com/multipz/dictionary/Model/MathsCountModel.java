package com.multipz.dictionary.Model;

/**
 * Created by Admin on 12-07-2017.
 */

public class MathsCountModel {

    String subject_id, subject_name, is_status, word_count, formula_count;

    public MathsCountModel(String subject_id, String subject_name, String is_status, String word_count, String formula_count) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.is_status = is_status;
        this.word_count = word_count;
        this.formula_count = formula_count;
    }

    public MathsCountModel(String subject_id, String subject_name, String word_count, String formula_count) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.is_status = is_status;
        this.word_count = word_count;
        this.formula_count = formula_count;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    public String getWord_count() {
        return word_count;
    }

    public void setWord_count(String word_count) {
        this.word_count = word_count;
    }

    public String getFormula_count() {
        return formula_count;
    }

    public void setFormula_count(String formula_count) {
        this.formula_count = formula_count;
    }
}
