package com.tiagoespinha.popmovee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideo;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideoResultSet;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class TrailerListFragment extends Fragment implements Consumer<TMDBMovieVideoResultSet> {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.movie_trailer_list_view, container, false);
        ButterKnife.bind(this, linearLayout);

        return linearLayout;
    }

    @Override
    public void accept(TMDBMovieVideoResultSet tmdbMovieVideoResultSet) throws Exception {
        for (TMDBMovieVideo tmdbMovieVideo : tmdbMovieVideoResultSet.getMovieVideoList()){
            if (tmdbMovieVideo.getSite().toLowerCase().compareTo("youtube") != 0) {
                // Ignore non-YT videos.
                continue;
            }

            View child = LinearLayout.inflate(getContext(), R.layout.trailer_list_item, null);

            ((TextView)child.findViewById(R.id.tv_trailer_name))
                    .setText(String.format(getString(R.string.trailer_prefix), tmdbMovieVideo.getName()));
            child
                    .findViewById(R.id.iv_play_trailer)
                    .setOnClickListener(v -> startPlayYoutubeVideo(tmdbMovieVideo.getKey()));
            ((LinearLayout)getView()).addView(child);
        }
    }

    private void startPlayYoutubeVideo(String youtubeKey) {
        Intent intent =
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(String.format(getString(R.string.youtube_base_uri), youtubeKey)));

        startActivity(intent);
    }
}
