package dima.sabor.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class AddRecipeSquareImageView extends android.support.v7.widget.AppCompatImageView {
    public AddRecipeSquareImageView(Context context) {
        super(context);
    }

    public AddRecipeSquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddRecipeSquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
