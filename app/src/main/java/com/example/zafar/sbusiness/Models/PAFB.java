package com.example.zafar.sbusiness.Models;

public class PAFB {
    private String ID;
    private String ProductId;
    private String AttributeName;
    private String AttributeValue;

    public PAFB(){}
    public PAFB(String id, String productId, String attributeName, String attributeValue) {
        ID = id;
        ProductId = productId;
        AttributeName = attributeName;
        AttributeValue = attributeValue;
    }

//    Getters
    public String getProductId() {
        return ProductId;
    }

    public String getAttributeName() {
        return AttributeName;
    }

    public String getAttributeValue() {
        return AttributeValue;
    }

    public String getID() {
        return ID;
    }

//    Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public void setAttributeName(String attributeName) {
        AttributeName = attributeName;
    }

    public void setAttributeValue(String attributeValue) {
        AttributeValue = attributeValue;
    }
}