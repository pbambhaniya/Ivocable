package com.multipz.dictionary.Model;

/**
 * Created by Admin on 19-07-2017.
 */

public class SpinnerModel {

    private String type,formula_type_id,subject_id;

    public SpinnerModel(String type,String formula_type_id,String subject_id){

        this.type = type;
        this.formula_type_id = formula_type_id;
        this.subject_id = subject_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormula_type_id() {
        return formula_type_id;
    }

    public void setFormula_type_id(String formula_type_id) {
        this.formula_type_id = formula_type_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
