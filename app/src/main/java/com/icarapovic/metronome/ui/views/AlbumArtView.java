package com.icarapovic.metronome.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AlbumArtView extends ImageView {

    public AlbumArtView(Context context) {
        this(context, null);
    }

    public AlbumArtView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumArtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int w, int h) {
        int size = w > h ? w : h;
        super.onMeasure(size, size);
    }
}
