package com.lovepushapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.lovepushapp.R;
import com.lovepushapp.helper.TouchImageView;
import com.squareup.picasso.Picasso;


public class FullImage extends AppCompatActivity /*implements View.OnTouchListener*/ {
    String imagepath;
    TouchImageView fullimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image);


        imagepath = getIntent().getStringExtra("ImagePath");
        Log.e("imagePath"," "+imagepath);
        // Toast.makeText(this, "path "+imagepath, Toast.LENGTH_SHORT).show();
        fullimage = findViewById(R.id.fullimageview);
        //Glide.with(FullImage.this).load(imagepath).into(fullimage);

        Picasso.get()
                .load(imagepath)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(fullimage);
       // fullimage.setOnTouchListener(new Touch(1f,8f));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
