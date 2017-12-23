package com.example.zafar.sbusiness.Models;

public class ProductAttributes {
    private int ID;
    private String ProductId;
    private String AttributeName;
    private String AttributeValue;

    public ProductAttributes(int id) {
        ID = id;
    }
    public ProductAttributes(int id, String productId, String attributeName, String attributeValue) {
        ID = id;
        ProductId = productId;
        AttributeName = attributeName;
        AttributeValue = attributeValue;
    }
    public ProductAttributes(String productId, String attributeName, String attributeValue) {
        ProductId = productId;
        AttributeName = attributeName;
        AttributeValue = attributeValue;
    }
    public ProductAttributes(Integer finalI, String o) {
        ID = finalI;
        AttributeName = o;
    }
    public int getAttributeId() {return ID;}
    public String getProductId() {
        return ProductId;
    }
    public String getAttributeName() {
        return AttributeName;
    }
    public String getAttributeValue() {
        return AttributeValue;
    }
}