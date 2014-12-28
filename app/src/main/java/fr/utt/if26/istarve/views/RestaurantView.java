package fr.utt.if26.istarve.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.utils.DialogUtil;

/**
 * Created by Fred-Dev on 28/12/2014.
 */
public class RestaurantView extends LinearLayout {

    public static interface ViewListener {
        public void onSubmitNewRating(int newRating);
        public void onSubmitNewComment(String title, String body);
    }

    private ViewListener viewListener;
    private EditText mNewCommentTitle;
    private EditText mNewCommentBody;
    private static final String TAG = RestaurantView.class.getSimpleName();

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public RestaurantView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RatingBar myRatingBar = (RatingBar)findViewById(R.id.my_rating_bar);
        mNewCommentTitle = (EditText)findViewById(R.id.comment_title);
        mNewCommentBody = (EditText)findViewById(R.id.comment_body);
        Button newCommentSubmitBtn = (Button)findViewById(R.id.submit_comment);
        myRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                viewListener.onSubmitNewRating((int)v);
            }
        });

        newCommentSubmitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mNewCommentTitle.getText().toString();
                String body = mNewCommentBody.getText().toString();
                viewListener.onSubmitNewComment(title, body);
            }
        });
    }

}
