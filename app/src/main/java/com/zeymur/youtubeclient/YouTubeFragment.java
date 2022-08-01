package com.zeymur.youtubeclient;

import com.zeymur.youtubeclient.R;
import com.zeymur.youtubeclient.RequestState;
import com.zeymur.youtubeclient.model.Item;
import com.zeymur.youtubeclient.ErrorDialogFragment;
import com.zeymur.youtubeclient.viewmodel.ItemViewModel;
import com.zeymur.youtubeclient.viewmodel.ItemViewModelFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class YouTubeFragment extends Fragment {
    private YouTubeAdapter adapter;
    private ItemViewModel itemViewModel;

    private static final String ARG_ID = "id";
    private static final String ARG_ID_TYPE = "id-type";
    private static final String ARG_KEY = "key";

    private String id;
    private String key;
    private YOUTUBE_IDTYPE idType;

    public YouTubeFragment() { }

    @SuppressWarnings("unused")
    public static YouTubeFragment newInstance(String id, YOUTUBE_IDTYPE idType, String key) {
        YouTubeFragment fragment = new YouTubeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putString(ARG_KEY, key);
        args.putInt(ARG_ID_TYPE, idType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            idType = YOUTUBE_IDTYPE.values()[getArguments().getInt(ARG_ID_TYPE)];
            key = getArguments().getString(ARG_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        final Context context = view.getContext();
        final RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        //final Listener listener = (item) -> #InterstitialHelperShow#{
        final Listener listener = (item) -> {
            Intent intent = new Intent(context, YouTubePlayerActivity.class);
            intent.putExtra("video_id", item.getContentDetails().getVideoId());
            intent.putExtra("key", key);
            getActivity().startActivity(intent);
        };

        itemViewModel = ViewModelProviders.of(this,
                new ItemViewModelFactory(getActivity(), id, idType, key)).get(ItemViewModel.class);
        adapter = new YouTubeAdapter(getContext(), listener);

        itemViewModel.itemPagedList.observe(getViewLifecycleOwner(), (items) -> {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(items);
        });

        recyclerView.setAdapter(adapter);

        itemViewModel.getNetworkState().observe(getViewLifecycleOwner(), requestState -> {
            if (requestState.getState() == RequestState.STATE.RUNNING)
                swipeRefreshLayout.setRefreshing(true);
            else
                swipeRefreshLayout.setRefreshing(false);

            if (requestState.getState() == RequestState.STATE.FAILED)
                getActivity().runOnUiThread(() -> {
                    ErrorDialogFragment errorFragment = ErrorDialogFragment.newInstance(requestState.getMessage(), ()-> requestState.getRetry().retry());
                    ErrorDialogFragment.show(getChildFragmentManager(), errorFragment);
                });
        });

        swipeRefreshLayout.setOnRefreshListener(() -> itemViewModel.refresh());

        return view;
    }

    public interface Listener {
        void onYouTubeItemClicked(Item item);
    }
}
