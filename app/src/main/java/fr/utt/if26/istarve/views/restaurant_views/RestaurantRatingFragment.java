package fr.utt.if26.istarve.views.restaurant_views;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.models.Restaurant;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RestaurantRatingFragment extends android.support.v4.app.Fragment {

    private EditText mCommentTitle;
    private EditText mCommentBody;
    private RatingBar mMyRatingBar;
    private Button mSubmitCommentButton;
    private RestaurantActivity mActivity;
    private RestaurantMenuFragment.ViewListener mListener;

    public RestaurantRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RatingView = inflater.inflate(R.layout.fragment_restaurant_rating, container, false);

        mCommentTitle = (EditText) RatingView.findViewById(R.id.comment_title);
        mCommentBody = (EditText) RatingView.findViewById(R.id.comment_body);
        mMyRatingBar = (RatingBar) RatingView.findViewById(R.id.my_rating_bar);
        mSubmitCommentButton = (Button) RatingView.findViewById(R.id.submit_comment);
        mActivity = ((RestaurantActivity) getActivity());
        mListener = mActivity.getViewListener();

        mMyRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b){
                    mListener.onSubmitNewRating((int) v);
                }
            }
        });

        mSubmitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mCommentTitle.getText().toString();
                String body = mCommentBody.getText().toString();
                mListener.onSubmitNewComment(title, body);
            }
        });
        mActivity.getUserRatingAndComments();

        return RatingView;
    }


    public void updateRatingBar(int rating) {
        mMyRatingBar.setRating(rating);
    }

    public void updateCommentFields(String title, String body) {
        mCommentTitle.setText(title);
        mCommentBody.setText(body);
    }
}
