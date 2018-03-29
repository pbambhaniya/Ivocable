package com.multipz.dictionary.Model;

public class MathsWordModel {
    String subject_name, word, word_desc_id, word_id, subject_id, definition, definition_desc, html_string, definition_image,is_favourite, formula, formula_image, example, example_image, created_date, modified_date, is_status, is_delete;

    public MathsWordModel(String definition, String html_string, String definition_desc,String subject_id,String word_desc_id,String is_favourite) {
        this.definition = definition;
        this.html_string = html_string;
        this.definition_desc = definition_desc;
        this.subject_id = subject_id;
        this.word_desc_id = word_desc_id;
        this.is_favourite = is_favourite;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public boolean setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
        return false;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_desc_id() {
        return word_desc_id;
    }

    public void setWord_desc_id(String word_desc_id) {
        this.word_desc_id = word_desc_id;
    }

    public String getWord_id() {
        return word_id;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDefinition_desc() {
        return definition_desc;
    }

    public void setDefinition_desc(String definition_desc) {
        this.definition_desc = definition_desc;
    }

    public String getHtml_string() {
        return html_string;
    }

    public void setHtml_string(String html_string) {
        this.html_string = html_string;
    }

    public String getDefinition_image() {
        return definition_image;
    }

    public void setDefinition_image(String definition_image) {
        this.definition_image = definition_image;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFormula_image() {
        return formula_image;
    }

    public void setFormula_image(String formula_image) {
        this.formula_image = formula_image;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExample_image() {
        return example_image;
    }

    public void setExample_image(String example_image) {
        this.example_image = example_image;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}
