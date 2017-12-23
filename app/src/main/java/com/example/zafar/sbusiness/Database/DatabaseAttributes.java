package com.example.zafar.sbusiness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import com.example.zafar.sbusiness.Models.ProductAttributes;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAttributes extends SQLiteAssetHelper {
    private static final String DB_NAME="ProductAttribute.db";
    private static final int DB_VER = 1;
    public DatabaseAttributes(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }
    public List<ProductAttributes> getAttributes() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ID","ProductId","AttributeName","AttributeValue"};
        String sqlTable = "product_attributes";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        final List<ProductAttributes> result = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                result.add(new ProductAttributes(c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("AttributeName")),
                        c.getString(c.getColumnIndex("AttributeValue"))
                ));
            }while (c.moveToNext());
        }
        c.close();
        return result;
    }
    //Adding Attribute
    public void addToPA(ProductAttributes pa) {
        SQLiteDatabase db = getReadableDatabase();
        String sqlTable = "product_attributes";
        String query = String.format("INSERT INTO product_attributes(ProductId,AttributeName,AttributeValue) VALUES('%s','%s','%s');",
                pa.getProductId(),
                pa.getAttributeName(),
                pa.getAttributeValue()
        );
        db.execSQL(query);
    }
    //Update removing items
    public void removeFromPA(ProductAttributes pa) {
        SQLiteDatabase db = getReadableDatabase();
        String sqlTable = "product_attributes";
        Log.d("YOOODLT",String.valueOf(pa.getAttributeId()));
        String query = "DELETE FROM product_attributes where ID = " + pa.getAttributeId();
        db.execSQL(query);
    }
    //Update Option
    public void updateOptionPA(ProductAttributes pa) {
        SQLiteDatabase db = getReadableDatabase();
        String sqlTable = "product_attributes";
        String Query = "Select * from product_attributes where ID = " + pa.getAttributeId();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        ContentValues data=new ContentValues();
        String abc = pa.getAttributeName() ;
        Log.d("YOOOCome",pa.getAttributeName());
        data.put("AttributeName",abc);
        db.update(sqlTable, data, "ID=" + pa.getAttributeId(), null);
    }
    //Update Value
    public void updateValuePA(ProductAttributes pa) {
        SQLiteDatabase db = getReadableDatabase();
        String sqlTable = "product_attributes";
        String Query = "Select * from product_attributes where ID = " + pa.getAttributeId();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        ContentValues data=new ContentValues();
        String abc = pa.getAttributeName() ;
        Log.d("YOOOCome",pa.getAttributeName());
        data.put("AttributeValue",abc);
        db.update(sqlTable, data, "ID=" + pa.getAttributeId(), null);
    }
    // Delete Cart
    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM product_attributes");
        db.execSQL(query);
    }
}