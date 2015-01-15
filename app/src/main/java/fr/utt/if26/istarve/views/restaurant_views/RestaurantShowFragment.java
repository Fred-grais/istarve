package fr.utt.if26.istarve.views.restaurant_views;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.models.RestaurantComment;
import fr.utt.if26.istarve.utils.ConnexionUtils;

/**
 * View holding the fragment logic and capturing the user events for the restaurant show fragment
 */
public class RestaurantShowFragment extends android.support.v4.app.Fragment {

    private static final String TAG = RestaurantShowFragment.class.getSimpleName();

    private TextView mRestaurantName;
    private TextView mRestaurantAddress;
    private RatingBar mAverageRatingsBar;
    private ListView mCommentsList;
    private RestaurantActivity mActivity;
    private RestaurantMenuFragment.ViewListener mListener;
    private Switch mFavoriteSwitch;

    public RestaurantShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View ShowView = inflater.inflate(R.layout.fragment_restaurant_show, container, false);
        mRestaurantName = (TextView) ShowView.findViewById(R.id.restaurant_name);
        mRestaurantAddress = (TextView) ShowView.findViewById(R.id.restaurant_address);
        mAverageRatingsBar = (RatingBar) ShowView.findViewById(R.id.average_ratings_bar);
        mCommentsList =(ListView)ShowView.findViewById(R.id.comments_list);
        mFavoriteSwitch = (Switch)ShowView.findViewById(R.id.favorite_switch);
        mActivity = ((RestaurantActivity) getActivity());
        mListener = mActivity.getViewListener();
        Restaurant currentRestaurant = ((RestaurantActivity) getActivity()).getCurrentRestaurant();
        mRestaurantName.setText(currentRestaurant.getmName());
        mRestaurantAddress.setText(currentRestaurant.getmAddress());
        mAverageRatingsBar.setRating(currentRestaurant.getmRatingsAverage());

        mActivity.getFavoriteState();

        return ShowView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * Called each time the fragment become visible for the user
     * Allow to refresh the comment list
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(new ConnexionUtils(getActivity().getBaseContext()).isOnline()) {

                mActivity.getRestaurantComments();

            }
        }
    }

    /**
     * Called by the controller to update the comments list on the view
     */
    public void updateCommentsList() {
        final ArrayList<RestaurantComment> commentsList = mActivity.getCurrentRestaurant().getmCommentsList();
        ArrayAdapter adapter = new ArrayAdapter(this.mActivity,
                android.R.layout.simple_list_item_2, android.R.id.text1, commentsList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(commentsList.get(position).getHeader());
                text2.setText(commentsList.get(position).getmBody());
                return view;
            }
        };
        mCommentsList.setAdapter(adapter);
    }

    /**
     * Called by the controller to update the Favorite Switch state
     * @param state
     *  True of false, returned by the API
     */
    public void updateFavoriteSwitchState(boolean state){
        mFavoriteSwitch.setOnCheckedChangeListener(null);
        mFavoriteSwitch.setChecked(state);
        mFavoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                mListener.onManageUserFavorite();

            }
        });
    }
}
