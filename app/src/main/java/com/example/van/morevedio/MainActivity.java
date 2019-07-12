package com.example.van.morevedio;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;



public class MainActivity extends AppCompatActivity {

    com.google.android.exoplayer2.ui.PlayerView main ,left,right;
    String url;
    String name1="front";
    String name2="left_repeater";
    String name3="right_repeater";

   String temp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0;i<1000;i++){
            temp=temp+i+";";
        }
        copy(temp);

        url=getIntent().getStringExtra("url");
        url=url.replaceAll(name1,"xiaoqiang");
        url=url.replaceAll(name2,"xiaoqiang");
        url=url.replaceAll(name3,"xiaoqiang");

        main=findViewById(R.id.video_main);
        left=findViewById(R.id.video_left);
        right=findViewById(R.id.video_right);

        main.setUseController(true);
        left.setUseController(false);
        right.setUseController(false);





        // 得到默认合适的带宽
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// 创建跟踪的工厂
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
// 创建跟踪器
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
// 创建player
        SimpleExoPlayer player1 = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        SimpleExoPlayer player2 = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        SimpleExoPlayer player3 = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
// 绑定player
        main.setPlayer(player1);
        left.setPlayer(player2);
        right.setPlayer(player3);




        // 生成通过其加载媒体数据的DataSource实例
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(MainActivity.this, "ExoPlayer"), bandwidthMeter);
// 创建要播放的媒体的MediaSource
        String url_left="file://"+url.replace("xiaoqiang",name1);

        Log.i("url_left",url+"");

        Log.i("url_left",url_left+"");

        String url_right="file://"+url.replace("xiaoqiang",name2);
        String url_front="file://"+url.replace("xiaoqiang",name3);
        Uri uri1=Uri.parse(url_left);
        Uri uri2=Uri.parse(url_right);
        Uri uri3=Uri.parse(url_front);


        MediaSource mediaSource1 = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri1);
        MediaSource mediaSource2 = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri2);
        MediaSource mediaSource3 = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri3);
// 准备播放器的MediaSource
        player1.prepare(mediaSource1, false, true);
        player2.prepare(mediaSource2, false, true);
        player3.prepare(mediaSource3, false, true);
        player1.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
               //kuaijin
                player2.seekTo(player1.getCurrentPosition());
                player3.seekTo(player1.getCurrentPosition());

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                player2.setPlayWhenReady(playWhenReady);
                player3.setPlayWhenReady(playWhenReady);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        player2.setPlayWhenReady(true);




    }


    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }









}
