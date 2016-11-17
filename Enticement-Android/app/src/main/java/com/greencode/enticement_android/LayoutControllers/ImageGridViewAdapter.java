package com.greencode.enticement_android.LayoutControllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.greencode.enticement_android.Helpers.ScreenUtils;
import com.greencode.enticement_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jasonnguyen on 11/16/16.
 * Credits goes to InstaMaterial
 */

public class ImageGridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PHOTO_ANIMATION_DELAY = 600;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();


    private final Context context;
    private final int cellSize;

    private final List<String> photos;

    private boolean lockedAnimations = false;
    private int lastAnimatedItem = -1;

    public ImageGridViewAdapter(Context context) {
        this.context = context;
        this.cellSize = ScreenUtils.getScreenWidth(context) / 3;
        this.photos = Arrays.asList(context.getResources().getStringArray(R.array.user_photos));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.squared_image_view, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindPhoto((PhotoViewHolder) holder, position);
    }

    private void bindPhoto(final PhotoViewHolder holder, int position) {
        Picasso.with(context)
                .load(photos.get(position))
                .resize(cellSize, cellSize)
                .centerCrop()
                .into(holder.framePhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        animatePhoto(holder);
                    }

                    @Override
                    public void onError() {

                    }
                });
        if (lastAnimatedItem < position) lastAnimatedItem = position;
    }

    private void animatePhoto(PhotoViewHolder viewHolder) {
        if (!lockedAnimations) {
            if (lastAnimatedItem == viewHolder.getPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = PHOTO_ANIMATION_DELAY + viewHolder.getPosition() * 30;

            viewHolder.frameRoot.setScaleY(0);
            viewHolder.frameRoot.setScaleX(0);

            viewHolder.frameRoot.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameRoot;
        ImageView framePhoto;

        public PhotoViewHolder(View view) {
            super(view);
            frameRoot = (FrameLayout) view.findViewById(R.id.squarephoto_layout);
            framePhoto = (ImageView) view.findViewById(R.id.squarephoto_photo);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }
}