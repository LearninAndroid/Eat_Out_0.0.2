package dev.brian.com.eatout;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import dev.brian.com.eatout.Database.Database;
import dev.brian.com.eatout.Model.Food;
import dev.brian.com.eatout.Model.Order;

public class FoodDetail extends AppCompatActivity {
    TextView food_nam,food_price,food_description;
    ImageView food_ima;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String foodId ="";
    FirebaseDatabase database;
    DatabaseReference foods;
    FloatingButtonListener floatingButtonListener = new FloatingButtonListener();
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        food_description = (TextView)findViewById(R.id.food_description);
        food_nam = (TextView)findViewById(R.id.food_name);
        food_price =(TextView)findViewById(R.id.food_price);
        food_ima = (ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        
        if(getIntent()!= null){
            foodId = getIntent().getStringExtra("FoodId");
        }
        if(!foodId.isEmpty()){
            getDetailFood(foodId);
        }
        btnCart.setOnClickListener(floatingButtonListener);
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 food = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(food.getImage()).into(food_ima);
                collapsingToolbarLayout.setTitle(food.getName());

                food_price.setText(food.getPrice());
                food_nam.setText(food.getName());
                food_description.setText(food.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class FloatingButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            new Database(getBaseContext()).addToCart(new Order(foodId,
                    food.getName(),numberButton.getNumber(),food.getPrice(),food.getDiscount()));
            Toast.makeText(FoodDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();
        }
    }
}
