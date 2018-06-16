package dev.brian.com.eatout;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.brian.com.eatout.Database.Database;
import dev.brian.com.eatout.Model.Order;
import dev.brian.com.eatout.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    TextView txtTotalPrice;
    Button placeOrder;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
        txtTotalPrice = (TextView)findViewById(R.id.total);
        placeOrder = (Button)findViewById(R.id.btnPlaceOrder);
        loadListFood();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadListFood(){
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(Order order:cart){
            total+= (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }
        Locale locale = new Locale("en","US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(numberFormat.format(total));


    }
}
