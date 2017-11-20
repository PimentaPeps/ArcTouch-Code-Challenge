package com.code.arctouch.arctouchcodechallenge.upcomingmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.code.arctouch.arctouchcodechallenge.R;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.UpcomingMovie;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link UpcomingMovie}s. User can choose to view all, active or completed upcomingMovies.
 */
public class UpcomingMoviesFragment extends Fragment implements UpcomingMoviesContract.View {

    private UpcomingMoviesContract.Presenter mPresenter;
    /**
     * Listener for clicks on upcomingMovies in the ListView.
     */
    UpcomingMovieItemListener mItemListener = new UpcomingMovieItemListener() {
        @Override
        public void onUpcomingMovieClick(UpcomingMovie clickedUpcomingMovie) {
            mPresenter.openUpcomingMovieDetails(clickedUpcomingMovie);
        }
    };

    private UpcomingMoviesAdapter mListAdapter;
    private View mNoUpcomingMoviesView;
    private ImageView mNoUpcomingMovieIcon;
    private TextView mNoUpcomingMovieMainView;
    private LinearLayout mUpcomingMoviesView;
    private TextView mFilteringLabelView;

    public UpcomingMoviesFragment() {
        // Requires empty public constructor
    }

    public static UpcomingMoviesFragment newInstance() {
        return new UpcomingMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new UpcomingMoviesAdapter(new ArrayList<UpcomingMovie>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull UpcomingMoviesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.upcoming_movies_frag, container, false);

        // Set up upcomingMovies view
        ListView listView = root.findViewById(R.id.upcoming_movies_list);
        listView.setAdapter(mListAdapter);
        mFilteringLabelView = root.findViewById(R.id.filtering_label);
        mUpcomingMoviesView = root.findViewById(R.id.upcoming_movies_ll);

        // Set up  no upcomingMovies view
        mNoUpcomingMoviesView = root.findViewById(R.id.no_upcoming_movies);
        mNoUpcomingMovieIcon = root.findViewById(R.id.no_upcoming_movies_icon);
        mNoUpcomingMovieMainView = root.findViewById(R.id.no_upcoming_movies_main);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadUpcomingMovies(true);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
//            case R.id.menu_refresh:
//                mPresenter.loadUpcomingMovies(true);
//                break;
        }
        return true;
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_upcoming_movies, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.filter_item_pop:
                        mPresenter.setFiltering(UpcomingMoviesFilterType.POPULARITY_UPCOMINGMOVIES);
                        break;
                    case R.id.filter_item_title_asc:
                        mPresenter.setFiltering(UpcomingMoviesFilterType.TITLE_ASC_UPCOMINGMOVIES);
                        break;
                    case R.id.filter_item_title_desc:
                        mPresenter.setFiltering(UpcomingMoviesFilterType.TITLE_DESC_UPCOMINGMOVIES);
                        break;
                    default:
                        mPresenter.setFiltering(UpcomingMoviesFilterType.RELEASE_DATE_UPCOMINGMOVIES);
                        break;
                }
                mPresenter.loadUpcomingMovies(false);
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showUpcomingMovies(List<UpcomingMovie> upcomingMovies) {
        mListAdapter.replaceData(upcomingMovies);

        mUpcomingMoviesView.setVisibility(View.VISIBLE);
        mNoUpcomingMoviesView.setVisibility(View.GONE);
    }

    @Override
    public void showNoUpcomingMovies() {
        showNoUpcomingMoviesViews(
                getResources().getString(R.string.no_upcoming_movies_all),
                R.drawable.ic_movie_black_24dp,
                false
        );
    }

    private void showNoUpcomingMoviesViews(String mainText, int iconRes, boolean showAddView) {
        mUpcomingMoviesView.setVisibility(View.GONE);
        mNoUpcomingMoviesView.setVisibility(View.VISIBLE);

        mNoUpcomingMovieMainView.setText(mainText);
        mNoUpcomingMovieIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }

    @Override
    public void showPopularityFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_popularity));
    }

    @Override
    public void showTitleAscFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_title_asc));
    }

    @Override
    public void showTitleDescFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_title_desc));
    }

    @Override
    public void showReleaseDateFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_release_date));
    }

    @Override
    public void showUpcomingMovieDetailsUi(String UpcomingMovieId) {
        //Call detail fragment
        //TO-DO
//        Intent intent = new Intent(getContext(), UpcomingMovieDetailActivity.class);
//        intent.putExtra(UpcomingMovieDetailActivity.EXTRA_UpcomingMovie_ID, UpcomingMovieId);
//        startActivity(intent);
    }

    @Override
    public void showLoadingUpcomingMoviesError() {
        showMessage(getString(R.string.loading_upcoming_movies_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public interface UpcomingMovieItemListener {
        void onUpcomingMovieClick(UpcomingMovie clickedUpcomingMovie);
    }

    private static class UpcomingMoviesAdapter extends BaseAdapter {

        private List<UpcomingMovie> mUpcomingMovies;
        private UpcomingMovieItemListener mItemListener;

        public UpcomingMoviesAdapter(List<UpcomingMovie> upcomingMovies, UpcomingMovieItemListener itemListener) {
            setList(upcomingMovies);
            mItemListener = itemListener;
        }

        public void replaceData(List<UpcomingMovie> upcomingMovies) {
            setList(upcomingMovies);
            notifyDataSetChanged();
        }

        private void setList(List<UpcomingMovie> upcomingMovies) {
            mUpcomingMovies = checkNotNull(upcomingMovies);
        }

        @Override
        public int getCount() {
            return mUpcomingMovies.size();
        }

        @Override
        public UpcomingMovie getItem(int i) {
            return mUpcomingMovies.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.upcoming_movie_item, viewGroup, false);
            }

            final UpcomingMovie upcomingMovie = getItem(i);

            TextView titleTV = rowView.findViewById(R.id.title);
            titleTV.setText(upcomingMovie.getTitle());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onUpcomingMovieClick(upcomingMovie);
                }
            });

            return rowView;
        }
    }

}
