package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

        if (position == DEFAULT_POSITION){
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);

        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null){
            closeOnError();
            return;
        }

        populateUI( sandwich );
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI( Sandwich sandwich) {
        TextView descriptionTextView, alsoKnownAsTextView, placeOfOriginTextView, ingredientsTextView;

        if( sandwich.getMainName() != null && !sandwich.getMainName().equals("") ) {
            descriptionTextView = findViewById(R.id.description_tv);
            descriptionTextView.setText(sandwich.getDescription());
        }

        if( sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() != 0 ) {

            alsoKnownAsTextView = findViewById(R.id.also_known_tv);

            String alsoKnownConcat = "";
            for (String name : sandwich.getAlsoKnownAs()) {
                alsoKnownConcat += name + ",  ";
            }

            alsoKnownConcat = alsoKnownConcat.substring(0, alsoKnownConcat.length() - 3);

            alsoKnownAsTextView.setText(alsoKnownConcat);
        }

        if( sandwich.getPlaceOfOrigin() != null && !sandwich.getPlaceOfOrigin().equals("") ){
            placeOfOriginTextView = findViewById(R.id.origin_tv);

            placeOfOriginTextView.setText( sandwich.getPlaceOfOrigin() );
        }

        if( sandwich.getIngredients() != null && sandwich.getIngredients().size() != 0 ){
            ingredientsTextView = findViewById(R.id.ingredients_tv);

            String ingredientsConcat = "";

            for( String item: sandwich.getIngredients() ){
                ingredientsConcat += item + ",  ";
            }

            ingredientsConcat = ingredientsConcat.substring(0, ingredientsConcat.length() - 3);

            ingredientsTextView.setText(ingredientsConcat);
        }
    }
}
