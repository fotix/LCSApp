package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.models.Game.Game;
import com.trappz.lcsmashup.api.models.Game.Player;
import com.trappz.lcsmashup.api.models.Game.Vod;
import com.trappz.lcsmashup.api.models.Game.Vods;
import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.api.responses.GameResponseNotification;
import com.trappz.lcsmashup.api.responses.ProgrammingBlockResponseNotification;
import com.trappz.lcsmashup.api.services.ApiServices;
import com.trappz.lcsmashup.api.services.ApiServicesInterface;
import com.trappz.lcsmashup.lcsmashup.C;
import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.fragments.FragmentScheduleDay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Filipe Oliveira on 10-07-2014.
 */
public class ActivityGame extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public TextView BlueScore;
    public TextView RedScore;
    public TextView BlueTeamName;
    public TextView RedTeamName;

    public ImageView BlueLogo;
    public ImageView RedLogo;

    public TextView vodsButton;

    public View blueteam, redteam;

    String vodUrl = null;
    YouTubePlayerView youTubeView;
    TextView noGames;
    FrameLayout matchDetailsLayout;
    ScrollView scrollView;

    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getActionBar().setDisplayHomeAsUpEnabled(true);


        loading = (RelativeLayout)findViewById(R.id.activity_game_loadinglayout);

        int value = getIntent().getExtras().getInt("index");

        Match m = FragmentScheduleDay.MatchList.get(value);


        youTubeView  = (YouTubePlayerView)findViewById(R.id.youtube_view);

        scrollView = (ScrollView) findViewById(R.id.activity_game_scrollview);


        noGames = (TextView) findViewById(R.id.activity_game_no_games);
        matchDetailsLayout = (FrameLayout) findViewById(R.id.activity_game_details);

        BlueTeamName = (TextView) findViewById(R.id.activity_game_blueteam_name);
        RedTeamName = (TextView) findViewById(R.id.activity_game_redteam_name);

        BlueScore = (TextView) findViewById(R.id.activity_game_score_blueteam);
        RedScore = (TextView) findViewById(R.id.activity_game_score_redteam);

        BlueLogo = (ImageView) findViewById(R.id.activity_game_iv_blueteam);
        RedLogo = (ImageView) findViewById(R.id.activity_game_iv_redteam);

        blueteam = findViewById(R.id.activity_game_blueteam);
         LinearLayout b =  (LinearLayout) blueteam.findViewById(R.id.activity_game_header);
        b.setBackgroundColor(Color.parseColor("#33B5E5"));
        redteam = findViewById(R.id.activity_game_redteam);
        LinearLayout r =  (LinearLayout) redteam.findViewById(R.id.activity_game_header);
        r.setBackgroundColor(Color.parseColor("#FF4444"));

        if(m.getContestants() != null) {
            if (!m.getContestants().getBlue().getAcronym().isEmpty())
                BlueTeamName.setText(m.getContestants().getBlue().getAcronym());
            else
                BlueTeamName.setText(m.getContestants().getBlue().getName());

            if (!(m.getContestants().getRed().getAcronym().isEmpty()))
                RedTeamName.setText(m.getContestants().getRed().getAcronym());
            else
                RedTeamName.setText(m.getContestants().getRed().getName());


            BlueScore.setText(m.getContestants().getBlue().getWins()+"W - "+
                    m.getContestants().getBlue().getLosses()+"L");

            RedScore.setText(m.getContestants().getRed().getWins()+"W - "+
                    m.getContestants().getRed().getLosses()+"L");

            Picasso.with(getApplicationContext()).load(C.BASE_URL + m.getContestants().getBlue().getLogoURL()).into(BlueLogo);
            Picasso.with(getApplicationContext()).load(C.BASE_URL + m.getContestants().getRed().getLogoURL()).into(RedLogo);

            Log.e("LCSGAME", m.getName());
        }
        if (m.getIsFinished().compareToIgnoreCase("0") == 0) {
            matchDetailsLayout.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.GONE);
            youTubeView.setVisibility(View.GONE);
            noGames.setVisibility(View.VISIBLE);
            noGames.setText("This game will be played on: " + m.getDateTime());
        } else {
            ApiServices.getGame(m.getResult().get("game0").getId());
            matchDetailsLayout.setVisibility(View.VISIBLE);
            noGames.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult error) {
        Toast.makeText(this, "Oh no! " + error.toString(),
                Toast.LENGTH_LONG).show();
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        player.loadVideo(vodUrl);
    }


    @Override
    public void onPause() {

        super.onPause();
        EventBusManager.unregister(this);
    }

    @Override
    public void onResume() {

        super.onResume();
        EventBusManager.register(this);
    }

    @Subscribe
    public void processGameResponseNotification(GameResponseNotification grn) {
        Game g = grn.data;

        if(g.getNoVods() == 0) {
            Vods v = g.getVods();
            if(v!=null){
               vodUrl= v.getVod().getURL().split("v=")[1];
               youTubeView.initialize("AIzaSyCAHG2RhyRuOIFJCo5purXDxwO57FPJSn0", this);

            }else
                youTubeView.setVisibility(View.GONE);
        }

        processGameData(g);

        Log.i("gameActivity", "Got game: "+g.getPlayers().size());
    }


    public void processGameData(Game g) {

        int counter = 0;
        for (String key : g.getPlayers().keySet()) {
            Player p = g.getPlayers().get(key);
            TextView pname = new TextView(getApplicationContext());
            TextView pkda = new TextView(getApplicationContext());
            TextView pcs = new TextView(getApplicationContext());

            switch (counter) {
                case 0:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_cs);
                    break;

                case 1:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_cs);
                    break;

                case 2:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_cs);
                    break;
                case 3:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_cs);
                    break;
                case 4:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_cs);
                    break;
                case 5:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_cs);
                    break;
                case 6:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_cs);
                    break;
                case 7:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_cs);
                    break;
                case 8:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_cs);
                    break;

                case 9:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_cs);
                    break;

            }
            if (pname != null & p.getName() != null)
                pname.setText(p.getName());
            else
                pname.setText("-");


            if(pcs != null && p.getMinionsKilled() != null)
                pcs.setText(p.getMinionsKilled().toString());
            else
                pcs.setText("-");


            if(pkda != null && (p.getKills() != null && p.getDeaths() != null && p.getAssists() !=null))
                pkda.setText(p.getKills()+"/"+p.getKills()+"/"+p.getAssists());
            else
                pkda.setText("-/-/-");

            counter++;
        }

        loading.setVisibility(View.GONE);
    }
}
