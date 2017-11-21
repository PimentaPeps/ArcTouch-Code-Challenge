package com.code.arctouch.arctouchcodechallenge.upcomingmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.code.arctouch.arctouchcodechallenge.Injection;
import com.code.arctouch.arctouchcodechallenge.R;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.Api;
import com.code.arctouch.arctouchcodechallenge.data.source.remote.model.Movie;
import com.code.arctouch.arctouchcodechallenge.moviedetail.MovieDetailFragment;
import com.code.arctouch.arctouchcodechallenge.moviedetail.MovieDetailPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Movie}s. User can choose to view all, active or completed upcomingMovies.
 */
public class UpcomingMoviesFragment extends Fragment implements UpcomingMoviesContract.View {

    private UpcomingMoviesContract.Presenter mPresenter;
    /**
     * Listener for clicks on upcomingMovies in the ListView.
     */
    UpcomingMovieItemListener mItemListener = new UpcomingMovieItemListener() {
        @Override
        public void onUpcomingMovieClick(Movie clickedMovie) {
            mPresenter.openUpcomingMovieDetails(clickedMovie);
        }
    };
    private MovieDetailPresenter mMovieDetailPresenter;
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
        mListAdapter = new UpcomingMoviesAdapter(new ArrayList<Movie>(0), mItemListener, getContext());
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
        RecyclerView recyclerView = root.findViewById(R.id.upcoming_movies_list);
        recyclerView.setAdapter(mListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //add ItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
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
        swipeRefreshLayout.setScrollUpChild(recyclerView);

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
    public void showUpcomingMovies(List<Movie> movies) {
        mListAdapter.replaceData(movies);

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

//        MovieDetailFragment movieDetailFragment =
//                (MovieDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.upcoming_movies_container);
//        if (movieDetailFragment == null) {
//            // Create the fragment
//            movieDetailFragment = MovieDetailFragment.newInstance();
//            ActivityUtils.addFragmentToActivity(
//                    getActivity().getSupportFragmentManager(), movieDetailFragment, R.id.upcoming_movies_container);
//        }

        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance();
        movieDetailFragment.show((getActivity()).getSupportFragmentManager().beginTransaction(), "MyDialogFragment");

        // Create the presenter
        mMovieDetailPresenter = new MovieDetailPresenter(
                Injection.provideUseCaseHandler(),
                movieDetailFragment,
                Injection.provideGetMoviesDetail(getActivity().getApplicationContext()),
                UpcomingMovieId
        );
    }

    @Override
    public void showLoadingUpcomingMoviesError() {
        showMessage(getString(R.string.loading_upcoming_movies_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public interface UpcomingMovieItemListener {
        void onUpcomingMovieClick(Movie clickedMovie);
    }

    private static class UpcomingMoviesAdapter extends RecyclerView.Adapter<UpcomingMoviesAdapter.MyViewHolder> {
        private final Context mContext;
        private List<Movie> mMovies;
        private UpcomingMovieItemListener mItemListener;

        public UpcomingMoviesAdapter(List<Movie> movies, UpcomingMovieItemListener itemListener, Context context) {
            setList(movies);
            mItemListener = itemListener;
            mContext = context;
        }

        public void replaceData(List<Movie> movies) {
            setList(movies);
            notifyDataSetChanged();
        }

        private void setList(List<Movie> movies) {
            mMovies = checkNotNull(movies);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcoming_movie_item, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Movie movie = mMovies.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onUpcomingMovieClick(mMovies.get(position));
                }
            });
            if (movie.getBackdropPath() != null)
                Picasso.with(mContext)
                        .load(Api.createImageUrl(movie.getBackdropPath(), "w500").toString())
                        .into(holder.mUpcomingMovieImage);
            holder.mUpcomingMovieTitle.setText(movie.getTitle());
            holder.mUpcomingMovieGenre.setText(movie.getGenresString());
            holder.mUpcomingMovieReleaseDate.setText(movie.getReleaseDate());
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView mUpcomingMovieImage;
            public TextView mUpcomingMovieTitle;
            public TextView mUpcomingMovieGenre;
            public TextView mUpcomingMovieReleaseDate;

            public MyViewHolder(View v) {
                super(v);
                mUpcomingMovieTitle = v.findViewById(R.id.upcoming_movie_title);
                mUpcomingMovieGenre = v.findViewById(R.id.upcoming_movie_genre);
                mUpcomingMovieReleaseDate = v.findViewById(R.id.upcoming_movie_release_date);
                mUpcomingMovieImage = v.findViewById(R.id.upcoming_movie_backdrop_image);
            }
        }
    }

}
