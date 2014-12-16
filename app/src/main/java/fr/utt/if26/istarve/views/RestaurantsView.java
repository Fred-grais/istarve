package fr.utt.if26.istarve.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import fr.utt.if26.istarve.R;

/**
 * Created by Thomas on 16/12/2014.
 */
public class RestaurantsView extends LinearLayout{

    private ViewListener viewListener;

    public RestaurantsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static interface ViewListener {

    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public ViewListener getViewListener(){
        return viewListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
