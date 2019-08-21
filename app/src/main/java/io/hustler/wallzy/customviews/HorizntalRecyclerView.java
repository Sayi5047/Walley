package io.hustler.wallzy.customviews;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HorizntalRecyclerView extends RecyclerView {
    LayoutManager layoutManager;

    public HorizntalRecyclerView(@NonNull Context context) {

        super(context);

    }

    public HorizntalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizntalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void intialize(RecyclerView.Adapter adapter) {
        layoutManager = new LinearLayoutManager(getContext(), HORIZONTAL, false);
        setLayoutManager(layoutManager);

        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                android.os.Handler mhandler = new Handler();
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int sidePadding = (getWidth() / 4) - getChildAt(0).getWidth() / 4;
                        setPadding(sidePadding, 0, sidePadding, 0);
                        scrollToPosition(0);
                        onScrollSchanged();
                        addOnScrollListener(new OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                onScrollSchanged();
                            }
                        });
                    }
                });

            }


        });

        setAdapter(adapter);
    }

    private void onScrollSchanged() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    int childCenterX = (child.getLeft() + child.getRight()) / 2;
                    float scaleValue = getGussianScale(childCenterX, 1f, 0.1f, 250d);
                    child.setScaleX(scaleValue);
                    child.setScaleY(scaleValue);


                }
            }
        });

    }

    private float getGussianScale(int childCenterX, float minScaleOffset, float scalefactor, double spreadFactor) {
        /*FORMULAE is f(x)= ae ^ ( ((x-b) ^ 2) / 2c ^ 2 )
         * a= scale factor
         * c= spread factor
         * x= childs center
         * b= recyclerviewCenter
         * e= exp val i.e 2.7
         * */

        int recyclerviewCenter = (getLeft() + getRight()) / 2;
        return (float) (Math.pow
                (Math.E,
                        -Math.pow(childCenterX - recyclerviewCenter, 2)
                                /
                                (2
                                        *
                                        Math.pow(spreadFactor, 2)))
                *
                scalefactor + minScaleOffset);


    }
}
