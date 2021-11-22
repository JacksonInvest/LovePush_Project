package com.lovepushapp.quickBloxTest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/*import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;*/
import com.lovepushapp.R;
import com.lovepushapp.quickBloxTest.utils.ResourceUtils;
import com.squareup.picasso.Picasso;

public class AttachmentImageActivity extends BaseActivity {

    private static final String EXTRA_URL = "url";
    private static final int PREFERRED_IMAGE_SIZE_FULL = ResourceUtils.dpToPx(320);

    private ImageView imageView;
    private ProgressBar progressBar;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AttachmentImageActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        initUI();
        loadImage();
    }

    private void initUI() {
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        imageView = findViewById(R.id.image_full_view);
        progressBar = findViewById(R.id.progress_bar_show_image);
    }

    private void loadImage() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.ic_error_white);
            return;
        }

        progressBar.setVisibility(View.GONE);
        /*Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListenerImpl())
                .error(R.drawable.ic_error_white)
                .dontTransform()
                .override(PREFERRED_IMAGE_SIZE_FULL, PREFERRED_IMAGE_SIZE_FULL)
                .into(imageView);*/

        /*Glide.with(this).load(url).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                //if you want to convert the drawable to ImageView
                Bitmap bitmapImage  = ((BitmapDrawable) resource).getBitmap();

                progressBar.setVisibility(View.GONE);


                return false;
            }
        }).into(imageView);*/

        //Picasso.get().load(url).resize(ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size), ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size)).into(imageView);

        Picasso.get().load(url).into(imageView);
    }


}