package dev.brian.com.eatout;

import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.brian.com.eatout.Common.Common;
import dev.brian.com.eatout.Database.Database;
import dev.brian.com.eatout.Model.Order;
import dev.brian.com.eatout.Model.Request;
import dev.brian.com.eatout.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {
    CheckOutListener checkOutListener = new CheckOutListener();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;
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
        requests = firebaseDatabase.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
        txtTotalPrice = (TextView)findViewById(R.id.total);
        placeOrder = (Button)findViewById(R.id.btnPlaceOrder);
     //  placeOrder.setOnClickListener(checkOutListener);

        loadListFood();
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showAlertDialog();
            }
        });
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
    class CheckOutListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            showAlertDialog();

        }
    }
    public void showAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Enter Your Address");
        final EditText editAdress = new EditText(Cart.this);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editAdress.setLayoutParams(ip);
        alertDialog.setView(editAdress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editAdress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart);
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You, Order Placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
