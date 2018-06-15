package dev.brian.com.eatout.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import dev.brian.com.eatout.Model.Order;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME ="EatItDB.db";
    private static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(){
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String [] sqlSelect ={"ProductName","ProductId","Quantity","Price","Discount"};
        String sqlTable ="OrderDetail" ;
        queryBuilder.setTables(sqlTable);
        Cursor cursor = queryBuilder.query(database,sqlSelect,null,null,null,null,null);
        final List<Order> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductId")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount"))));
            }while(cursor.moveToNext());
        }
        return result;
    }
    public void addToCart(Order order){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s'," +
                "'%s','%s','%s','%s');",order.getProductId(),
                order.getProductName(),order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        sqLiteDatabase.execSQL(query);

    }
}
