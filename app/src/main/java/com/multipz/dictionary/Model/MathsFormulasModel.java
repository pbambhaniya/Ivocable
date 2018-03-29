package com.multipz.dictionary.Model;

/**
 * Created by Admin on 10-07-2017.
 */

public class MathsFormulasModel {

    String subject_id,type_id,type_name,sub_type,sub_type_id,sub_type_name,formulas_title,formulas_img,desc_title,desc_images,html_string,formula_desc_id,is_favourite;

    public MathsFormulasModel(String type_id,String type_name,String sub_type_name,String html_string,String formulas_title,String formula_desc_id,String is_favourite,String subject_id) {
        this.type_id=type_id;
        this.type_name=type_name;
        this.sub_type_name=sub_type_name;
        this.html_string=html_string;
        this.subject_id=subject_id;
        this.formulas_title=formulas_title;
        this.formula_desc_id=formula_desc_id;
        this.is_favourite=is_favourite;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getHtml_string() {
        return html_string;
    }

    public void setHtml_string(String html_string) {
        this.html_string = html_string;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getSub_type_id() {
        return sub_type_id;
    }

    public void setSub_type_id(String sub_type_id) {
        this.sub_type_id = sub_type_id;
    }

    public String getSub_type_name() {
        return sub_type_name;
    }

    public void setSub_type_name(String sub_type_name) {
        this.sub_type_name = sub_type_name;
    }

    public String getFormulas_title() {
        return formulas_title;
    }

    public void setFormulas_title(String formulas_title) {
        this.formulas_title = formulas_title;
    }

    public String getFormulas_img() {
        return formulas_img;
    }

    public void setFormulas_img(String formulas_img) {
        this.formulas_img = formulas_img;
    }

    public String getDesc_title() {
        return desc_title;
    }

    public void setDesc_title(String desc_title) {
        this.desc_title = desc_title;
    }

    public String getDesc_images() {
        return desc_images;
    }

    public void setDesc_images(String desc_images) {
        this.desc_images = desc_images;
    }

    public String getFormula_desc_id() {
        return formula_desc_id;
    }

    public void setFormula_desc_id(String formula_desc_id) {
        this.formula_desc_id = formula_desc_id;
    }
}
