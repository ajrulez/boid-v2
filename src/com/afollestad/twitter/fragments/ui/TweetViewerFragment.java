package com.afollestad.twitter.fragments.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.afollestad.silk.caching.OnReadyCallback;
import com.afollestad.silk.caching.SilkCache;
import com.afollestad.silk.fragments.SilkFragment;
import com.afollestad.silk.images.Dimension;
import com.afollestad.silk.images.SilkImageManager;
import com.afollestad.silk.utilities.TimeUtils;
import com.afollestad.silk.views.image.SilkImageView;
import com.afollestad.twitter.BoidApp;
import com.afollestad.twitter.R;
import com.afollestad.twitter.columns.Column;
import com.afollestad.twitter.columns.Columns;
import com.afollestad.twitter.ui.ComposeActivity;
import com.afollestad.twitter.ui.ProfileActivity;
import com.afollestad.twitter.utilities.TweetUtils;
import com.afollestad.twitter.utilities.text.TextUtils;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Displays information about a specific tweet, contained in the {@link com.afollestad.twitter.ui.TweetViewerActivity} on phones.
 *
 * @author Aidan Follestad (afollestad)
 */
public class TweetViewerFragment extends SilkFragment {

    private Status mTweet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        processArgs();
    }

    private void reloadTweet() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Twitter client = BoidApp.get(getActivity()).getClient();
                try {
                    mTweet = client.showStatus(mTweet.getId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateCaches();
                            displayTweet();
                        }
                    });
                } catch (final TwitterException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BoidApp.showAppMsgError(getActivity(), e);
                        }
                    });
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    private void processArgs() {
        mTweet = (Status) getArguments().getSerializable("tweet");
        if (mTweet.isRetweet())
            mTweet = mTweet.getRetweetedStatus();
        displayTweet();
        // Reload to update if the tweet was outdated in the cache
        reloadTweet();
    }

    private void displayTweet() {
        View v = getView();
        if (v == null) return;
        SilkImageView profilePic = (SilkImageView) v.findViewById(R.id.profilePic);
        profilePic.setFitView(false).setImageURL(BoidApp.get(getActivity()).getImageLoader(), mTweet.getUser().getProfileImageURL());
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class)
                        .putExtra("user", mTweet.getUser()));
            }
        });
        ((TextView) v.findViewById(R.id.fullname)).setText(mTweet.getUser().getName());
        ((TextView) v.findViewById(R.id.source)).setText("via " + Html.fromHtml(mTweet.getSource()).toString());

        if (mTweet.getFavoriteCount() == 0 && mTweet.getRetweetCount() == 0) {
            v.findViewById(R.id.infoFrame).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.infoFrame).setVisibility(View.VISIBLE);
            TextView favoriteCount = (TextView) v.findViewById(R.id.favoriteCount);
            favoriteCount.setText(mTweet.getFavoriteCount() + "\n" + getString(R.string.favorites));
            if (mTweet.getFavoriteCount() > 0) {
                favoriteCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
            }
            TextView retweetCount = (TextView) v.findViewById(R.id.retweetCount);
            retweetCount.setText(mTweet.getRetweetCount() + "\n" + getString(R.string.retweets));
            if (mTweet.getRetweetCount() > 0) {
                retweetCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
            }
        }

        TextView content = (TextView) v.findViewById(R.id.content);
        TextUtils.linkifyText(getActivity(), content, mTweet, true, true);

        Calendar time = new GregorianCalendar();
        time.setTime(mTweet.getCreatedAt());
        ((TextView) v.findViewById(R.id.timestamp)).setText(TimeUtils.toStringLong(time));

        final SilkImageView media = (SilkImageView) v.findViewById(R.id.media);
        final String mediaUrl = TweetUtils.getTweetMediaURL(mTweet, true);
        if (mediaUrl != null) {
            media.setVisibility(View.VISIBLE);
            media.setImageURL(BoidApp.get(getActivity()).getImageLoader(), mediaUrl);
        } else media.setVisibility(View.GONE);

        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SilkImageManager image = BoidApp.get(getActivity()).getImageLoader();
                Uri uri = Uri.fromFile(image.getCacheFile(mediaUrl, new Dimension(v)));
                startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "image/*"));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        User me = BoidApp.get(getActivity()).getProfile();
        boolean isMe = me.getId() == mTweet.getUser().getId();
        inflater.inflate(isMe ? R.menu.fragment_tweet_viewer_me : R.menu.fragment_tweet_viewer, menu);
        if (!isMe) {
            MenuItem retweet = menu.findItem(R.id.retweet);
            retweet.setVisible(!mTweet.getUser().isProtected());
            if (mTweet.getCurrentUserRetweetId() > 0) {
                retweet.setTitle(R.string.unretweet);
                retweet.setIcon(R.drawable.ic_unretweet);
            }
        }
        MenuItem favorite = menu.findItem(R.id.favorite);
        int favIcon;
        if (mTweet.isFavorited()) {
            favorite.setTitle(R.string.unfavorite);
            favIcon = R.attr.favoritedIcon;
        } else {
            favorite.setTitle(R.string.favorite);
            favIcon = R.attr.unfavoritedIcon;
        }
        TypedArray ta = getActivity().obtainStyledAttributes(new int[]{favIcon});
        favIcon = ta.getResourceId(0, 0);
        ta.recycle();
        favorite.setIcon(favIcon);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reply:
                startActivity(new Intent(getActivity(), ComposeActivity.class).putExtra("reply_to", mTweet));
                return true;
            case R.id.retweet:
                if (mTweet.getCurrentUserRetweetId() > 0) {
                    confirmDelete();
                } else showRetweetDialog();
                return true;
            case R.id.favorite:
                toggleFavorite();
                return true;
            case R.id.share:
                performShare();
                return true;
            case R.id.delete:
                confirmDelete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCaches() {
        List<Column> columns = Columns.getAll(getActivity(), Status.class);
        for (Column col : columns) {
            new SilkCache<Status>(getActivity(), col.toString(), new OnReadyCallback<Status>() {
                @Override
                public void onReady(SilkCache<Status> cache) {
                    cache.update(mTweet);
                    if (cache.isChanged()) {
                        cache.commit(new SilkCache.SimpleCommitCallback() {
                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            });
        }
    }

    private void toggleFavorite() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Twitter cl = BoidApp.get(getActivity()).getClient();
                    if (mTweet.isFavorited()) {
                        cl.destroyFavorite(mTweet.getId());
                        mTweet.setIsFavorited(false);
                    } else {
                        cl.createFavorite(mTweet.getId());
                        mTweet.setIsFavorited(true);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().invalidateOptionsMenu();
                            updateCaches();
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BoidApp.showAppMsgError(getActivity(), e);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    private void showRetweetDialog() {
        new AlertDialog.Builder(getActivity()).setItems(R.array.retweet_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    default:
                        performRetweet();
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), ComposeActivity.class)
                                .putExtra("content", "\"@" + mTweet.getUser().getScreenName() + ": " + mTweet.getText() + "\" "));
                        break;
                }
            }
        }).show();
    }

    private void performRetweet() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Twitter cl = BoidApp.get(getActivity()).getClient();
                    cl.retweetStatus(mTweet.getId());
                } catch (final TwitterException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BoidApp.showAppMsgError(getActivity(), e);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    private void performShare() {
        String shareBody = "@" + mTweet.getUser().getScreenName() + ": " +
                TextUtils.expandURLs(mTweet.getText(), true, mTweet.getURLEntities(), mTweet.getMediaEntities()) + "\n\n" +
                "https://twitter.com/" + mTweet.getUser().getScreenName() + "/status/" + mTweet.getId();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain").putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    private void confirmDelete() {
        boolean unretweetMode = mTweet.getCurrentUserRetweetId() > 0;
        new AlertDialog.Builder(getActivity())
                .setTitle(unretweetMode ? R.string.unretweet : R.string.delete)
                .setMessage(unretweetMode ? R.string.confirm_unretweet : R.string.confirm_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        performDelete();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void performDelete() {
        final boolean unretweetMode = mTweet.getCurrentUserRetweetId() > 0;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Twitter cl = BoidApp.get(getActivity()).getClient();
                    cl.destroyStatus(unretweetMode ? mTweet.getCurrentUserRetweetId() : mTweet.getId());
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            BoidApp.showAppMsgError(getActivity(), e);
                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tweet_viewer;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tweet);
    }
}