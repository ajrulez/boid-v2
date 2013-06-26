package com.teamboid.twitter.fragments;

import android.content.Intent;
import com.teamboid.twitter.BoidApp;
import com.teamboid.twitter.ComposeActivity;
import com.teamboid.twitter.R;
import com.teamboid.twitter.adapters.StatusAdapter;
import com.teamboid.twitter.base.BoidAdapter;
import com.teamboid.twitter.base.FeedFragment;
import com.teamboid.twitter.utilities.Utils;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * A feed fragment that displays the current user's home timeline.
 */
public class TimelineFragment extends FeedFragment<Status> {

    public TimelineFragment() {
        super(true);
    }

    private StatusAdapter adapter;

    @Override
    public int getEmptyText() {
        return R.string.no_tweets;
    }

    @Override
    public BoidAdapter<Status> getAdapter() {
        if (adapter == null)
            adapter = new StatusAdapter(getActivity());
        return adapter;
    }

    @Override
    public void onItemClicked(int index) {
        //TODO
    }

    @Override
    public boolean onItemLongClicked(int index) {
        Status status = adapter.getItem(index);
        startActivity(new Intent(getActivity(), ComposeActivity.class)
                .putExtra("reply_to", Utils.serializeObject(status)));
        return true;
    }

    @Override
    public Status[] refresh() throws TwitterException {
        Paging paging = new Paging();
        paging.setCount(PAGE_LENGTH);
        if (getAdapter().getCount() > 0) {
            // Get tweets newer than the most recent tweet in the adapter
            paging.setSinceId(getAdapter().getItemId(0));
        }
        return BoidApp.get(getActivity()).getClient().getHomeTimeline(paging).toArray(new Status[0]);
    }

    @Override
    public Status[] paginate() throws TwitterException {
        Paging paging = new Paging();
        paging.setCount(PAGE_LENGTH);
        BoidAdapter adapt = getAdapter();
        if (adapt.getCount() > 0) {
            // Get tweets older than the oldest tweet in the adapter
            paging.setMaxId(adapt.getItemId(adapt.getCount() - 1) - 1);
        }
        return BoidApp.get(getActivity()).getClient().getHomeTimeline(paging).toArray(new Status[0]);
    }

    @Override
    public String getTitle() {
        return getString(R.string.timeline);
    }
}
