package com.cafedroid.android.cafecharge;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    GridView foodGrid;
    TextView cartView;
    Button buyButton;
    Double totalBill;
    DatabaseReference mRef;
    Double cardValue;
    ArrayList<FoodItem> orderedItems;
    String cardNo;
    boolean cardExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        foodGrid = findViewById(R.id.food_items);
        cartView = findViewById(R.id.cart_tv);
        buyButton = findViewById(R.id.buy_button);
        totalBill = 0.00;
        cartView.setText("Cart: \u20B9" + totalBill);
        mRef = FirebaseDatabase.getInstance().getReference();
        orderedItems = new ArrayList<>();
        cardExists = false;
        cardNo = getIntent().getStringExtra("card_no");

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChild(cardNo)) {
                    cardValue = Double.parseDouble(dataSnapshot.child(cardNo).getValue().toString());
                    cardExists = true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                cardValue = dataSnapshot.child(cardNo).getValue(Double.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ArrayList<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Noodles", 30.00, 0));
        foodItems.add(new FoodItem("Samosa", 10.00, 0));
        foodItems.add(new FoodItem("Maggi", 25.00, 0));
        foodItems.add(new FoodItem("Juice", 20.00, 0));
        foodItems.add(new FoodItem("Coffee", 40.00, 0));
        foodItems.add(new FoodItem("Pav Bhaji", 40.00, 0));
        foodItems.add(new FoodItem("Burger", 30.00, 0));
        foodItems.add(new FoodItem("Sandwich", 40.00, 0));

        FoodAdapter mAdapter = new FoodAdapter(this, 0, foodItems);
        foodGrid.setAdapter(mAdapter);
        foodGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodItem thisItem = foodItems.get(i);
                totalBill = totalBill + thisItem.getPrice();
                thisItem.increaseQuantity();
                orderedItems.add(thisItem);
                cartView.setText("Cart: \u20B9" + totalBill);
            }
        });

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: AlertDialog shows item in the cart and have buttons to change the quantity

                AlertDialog.Builder checkCartBuilder = new AlertDialog.Builder(MenuActivity.this)
                        .setTitle("Cart Summary").setMessage(orderedItems.toString());
                AlertDialog checkCart = checkCartBuilder.create();
                checkCart.show();
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardExists && totalBill > 0) {
                    if (totalBill > cardValue)
                        Toast.makeText(MenuActivity.this, "Balance not enough", Toast.LENGTH_SHORT).show();
                    else {
                        cardValue = cardValue - totalBill;
                        mRef.child("Cards").child(cardNo).setValue(cardValue);
                        Toast.makeText(MenuActivity.this, "Items ordered. Balance left is \u20B9" + cardValue, Toast.LENGTH_SHORT).show();
                        orderedItems.clear();
                        totalBill=0.00;
                        cartView.setText("Cart: \u20B9" + totalBill);
                        Intent goToBill=new Intent(MenuActivity.this,BillActivity.class);
                        startActivity(goToBill);
                    }
                } else if (totalBill <= 0) {
                    Toast.makeText(MenuActivity.this, "There is nothing in the cart to buy", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MenuActivity.this, "Your card is not registered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recharge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.recharge_button) {
            rechargeAttempt();
        }
        return true;
    }

    private void rechargeAttempt() {
        final EditText valueText = new EditText(this);
        valueText.setInputType(2);
        valueText.setBackground(getResources().getDrawable(android.R.drawable.editbox_background_normal));
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueText.setLayoutParams(params);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MenuActivity.this,android.R.style.Theme_Material_Light_Dialog_Alert)
                .setTitle("How much value do you want to put into your card?")
                .setView(valueText).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!valueText.getText().toString().isEmpty()){
                            Double value = Double.parseDouble(valueText.getText().toString());
                            cardValue=cardValue+value;
                            if (Integer.parseInt(valueText.getText().toString()) >= 50 && cardExists) {
                                mRef.child("Cards").child(cardNo).setValue(cardValue);
                                Toast.makeText(MenuActivity.this, "Recharge successful", Toast.LENGTH_SHORT).show();
                            } else if(!cardExists){
                                Toast.makeText(MenuActivity.this, "Your card is not registered", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(MenuActivity.this, "Minimum recharge amount is \u20B9 50", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MenuActivity.this, "Invalid value", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}
