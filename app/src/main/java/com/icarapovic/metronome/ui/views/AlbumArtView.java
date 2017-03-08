package com.icarapovic.metronome.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class AlbumArtView extends AppCompatImageView {
    public AlbumArtView(Context context) {
        this(context, null);
    }

    public AlbumArtView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumArtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int w, int h) {
        int size = w > h ? w : h;
        super.onMeasure(size, size);
    }
}
