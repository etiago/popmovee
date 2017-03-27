package com.tiagoespinha.popmovee;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieReview;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieReviewResultSet;

import java.util.Arrays;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class ReviewListFragment extends Fragment implements Consumer<TMDBMovieReviewResultSet> {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.movie_review_list_view, container, false);
        ButterKnife.bind(this, linearLayout);

        return linearLayout;
    }

    @Override
    public void accept(TMDBMovieReviewResultSet tmdbMovieReviewResultSet) throws Exception {
        for (TMDBMovieReview tmdbMovieReview : tmdbMovieReviewResultSet.getMovieReviewList()) {
//            TextView reviewTextView = new TextView(getContext());
//            reviewTextView.setText(tmdbMovieReview.getContent());
//            ((LinearLayout)getView()).addView(reviewTextView);

            View child = LinearLayout.inflate(getContext(), R.layout.review_list_item, null);

            ((TextView)child.findViewById(R.id.tv_review_author))
                    .setText(String.format(getString(R.string.review_by), tmdbMovieReview.getAuthor()));
            ((TextView)child.findViewById(R.id.tv_review_snippet))
                    .setText(tmdbMovieReview.getContent());

//            child
//                    .findViewById(R.id.iv_play_trailer)
//                    .setOnClickListener(v -> startPlayYoutubeVideo(tmdbMovieVideo.getKey()));
            ((LinearLayout)getView()).addView(child);
        }
    }
}
