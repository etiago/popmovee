package com.tiagoespinha.popmovee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tiagoespinha.popmovee.model.MovieMetadata;
import com.tiagoespinha.popmovee.model.MovieTrailerListDto;

import butterknife.ButterKnife;

/**
 * Created by TiagoEspinha on 11/02/2017.
 */

public class TrailerListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.movie_trailer_list_view, container, false);
        ButterKnife.bind(this, linearLayout);

        return linearLayout;
    }

    public void setMovieTrailerMetadata(MovieTrailerListDto movieTrailerListDto) {
        LinearLayout linearLayout = (LinearLayout) getView();

        linearLayout.removeAllViews();
        View child = linearLayout.inflate(getContext(), R.layout.trailer_list_item, null);
        linearLayout.addView(child);
        child = linearLayout.inflate(getContext(), R.layout.trailer_list_item, null);
        linearLayout.addView(child);
    }
}
