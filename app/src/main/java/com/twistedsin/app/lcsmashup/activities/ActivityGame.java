package com.twistedsin.app.lcsmashup.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.twistedsin.app.api.messages.EventBusManager;
import com.twistedsin.app.api.models.Game.Game;
import com.twistedsin.app.api.models.Game.Player;
import com.twistedsin.app.api.models.Game.Vods;
import com.twistedsin.app.api.models.Match.Match;
import com.twistedsin.app.api.responses.GameResponseNotification;
import com.twistedsin.app.api.services.ApiServices;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.caching.Cache;
import com.twistedsin.app.lcsmashup.fragments.FragmentScheduleDay;
import com.twistedsin.app.lcsmashup.notifications.DataNotification;
import com.twistedsin.app.lcsmashup.notifications.ReceiverAlarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Filipe Oliveira on 10-07-2014.
 */
public class ActivityGame extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private AlarmManager am;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    public static String TAG = "ActivityGame";

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
    int value, gameNumber, totalGames;
    private RelativeLayout loading;
    private RelativeLayout nogame;
    private RelativeLayout spoilerLayout;
    boolean isValidGame = false;
    Switch spoilerSwitch;
    Match m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        loading = (RelativeLayout) findViewById(R.id.activity_game_loadinglayout);
        nogame = (RelativeLayout) findViewById(R.id.activity_game_nogame);
        spoilerLayout = (RelativeLayout) findViewById(R.id.activity_game_spoiler_layout);

        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        noGames = (TextView) findViewById(R.id.activity_game_no_games);
        noGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlert();
            }
        });

        if(C.spoilers)
            spoilerLayout.setVisibility(View.GONE);

        spoilerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spoilerLayout.setVisibility(View.GONE);
            }
        });

        value = getIntent().getExtras().getInt("index");
        gameNumber = getIntent().getExtras().getInt("gamenumber");

        m = FragmentScheduleDay.MatchList.get(value);

        if (m != null) {
            totalGames = Integer.valueOf(m.getResult().size());

            if (C.LOG_MODE) C.logI("TOTAL GAMES: " + totalGames);

            if (gameNumber <= totalGames) {
                nogame.setVisibility(View.GONE);
                isValidGame = true;
            }

        }

        getActionBar().setTitle(m.getName());
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        scrollView = (ScrollView) findViewById(R.id.activity_game_scrollview);


        matchDetailsLayout = (FrameLayout) findViewById(R.id.activity_game_details);

        BlueTeamName = (TextView) findViewById(R.id.activity_game_blueteam_name);
        RedTeamName = (TextView) findViewById(R.id.activity_game_redteam_name);

        BlueScore = (TextView) findViewById(R.id.activity_game_score_blueteam);
        RedScore = (TextView) findViewById(R.id.activity_game_score_redteam);

        BlueLogo = (ImageView) findViewById(R.id.activity_game_iv_blueteam);
        RedLogo = (ImageView) findViewById(R.id.activity_game_iv_redteam);

        blueteam = findViewById(R.id.activity_game_blueteam);
        LinearLayout b = (LinearLayout) blueteam.findViewById(R.id.activity_game_header);
        b.setBackgroundColor(Color.parseColor("#33B5E5"));
        redteam = findViewById(R.id.activity_game_redteam);
        LinearLayout r = (LinearLayout) redteam.findViewById(R.id.activity_game_header);
        r.setBackgroundColor(Color.parseColor("#FF4444"));

        if (m.getContestants() != null) {
            if (m.getContestants().getBlue() != null) {
                if (!m.getContestants().getBlue().getAcronym().isEmpty()) {
                    C.logW(m.getContestants().getBlue().getAcronym());
                    BlueTeamName.setText(m.getContestants().getBlue().getAcronym());
                } else
                    BlueTeamName.setText(m.getContestants().getBlue().getName());

                BlueScore.setText(m.getContestants().getBlue().getWins() + "W - " +
                        m.getContestants().getBlue().getLosses() + "L");

                Picasso.with(getApplicationContext()).load(C.BASE_URL + m.getContestants().getBlue().getLogoURL()).into(BlueLogo);
            }

            if (m.getContestants().getRed() != null) {
                if (!(m.getContestants().getRed().getAcronym().isEmpty()))
                    RedTeamName.setText(m.getContestants().getRed().getAcronym());
                else
                    RedTeamName.setText(m.getContestants().getRed().getName());

                RedScore.setText(m.getContestants().getRed().getWins() + "W - " +
                        m.getContestants().getRed().getLosses() + "L");

                Picasso.with(getApplicationContext()).load(C.BASE_URL + m.getContestants().getRed().getLogoURL()).into(RedLogo);
            }

            if (C.LOG_MODE) C.logE(m.getName());
        }
        if (m.getIsFinished().compareToIgnoreCase("0") == 0) {
            spoilerLayout.setVisibility(View.GONE);
            matchDetailsLayout.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.GONE);
            youTubeView.setVisibility(View.GONE);
            noGames.setVisibility(View.VISIBLE);
            if(m.getIsLive()){
                noGames.setText("This game is currently LIVE");
                nogame.setOnClickListener(null);
            }else
                noGames.setText("This game will be played on: " + m.getDateTime() + " (GMT)");
        } else {
            if (isValidGame) {
                ApiServices.getGame(m.getResult().get("game" + (gameNumber - 1)).getId());
                matchDetailsLayout.setVisibility(View.VISIBLE);
                noGames.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void onAlert(){

        if(m.getMatchId() != null) {
            DataNotification alert = Cache.alertsCache.getAlertByHash(m.getMatchId());
            Intent intent = new Intent(this, ReceiverAlarm.class);
            intent.setAction(C.ALARM_NAME);

            if (alert != null) {
                Cache.alertsCache.deleteAlert(alert.id);
                intent.putExtra("id", alert.id);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pendingIntent);
                Toast.makeText(this, R.string.Toast_Info_AlertRemoved, Toast.LENGTH_SHORT).show();
            } else {

                alert = new DataNotification();

                alert.hash = m.getMatchId();
                alert.title = m.getName();

                Date matchDate = new Date();

                try {
                    matchDate = format.parse(m.getDateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(matchDate);
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));


                int minutes = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                String time = hour+":"+minutes;

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                String date = day+"/"+month+"/"+year;

                if(C.LOG_MODE) C.logW("NOTIFICATION DATE TIME: "+date+ " - "+time);

                Calendar calTest = Calendar.getInstance();
                calTest.setTimeInMillis(System.currentTimeMillis());

                alert.startDate = calendar;
                alert.id = Cache.alertsCache.createAlert(alert);


                intent.putExtra("id", alert.id);
                intent.putExtra("title", alert.title);
                intent.putExtra("startDate", alert.startDate);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, alert.startDate.getTimeInMillis() - (5 * 60 * 1000),
                        pendingIntent);

                Toast.makeText(this,R.string.Toast_Info_AlertScheduled, Toast.LENGTH_SHORT)
                        .show();
            }



        }
        setResult(Activity.RESULT_OK);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_game_select_game_menu, menu);
        menu.getItem(gameNumber - 1).setChecked(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.game1:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    if (totalGames >= 1) {
                        Intent i = new Intent(getApplicationContext(), ActivityGame.class);
                        i.putExtra("gamenumber", 1);
                        i.putExtra("index", value);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        item.setChecked(true);
                    } else {
                        createToast("There is no Game " + 1);
                    }

                }
                break;
            case R.id.game2:
                if (item.isChecked()) item.setChecked(false);
                else {
                    if (C.LOG_MODE) C.logW("GAME 2 CALLED");
                    if (totalGames >= 2) {
                        if (C.LOG_MODE) C.logW("GAME 2 CALLED VALID");
                        Intent i = new Intent(getApplicationContext(), ActivityGame.class);
                        i.putExtra("gamenumber", 2);
                        i.putExtra("index", value);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        item.setChecked(true);
                    } else
                        createToast("There is no Game " + 2);


                }
                break;
            case R.id.game3:
                if (item.isChecked()) item.setChecked(false);
                else {
                    if (totalGames >= 3) {
                        Intent i = new Intent(getApplicationContext(), ActivityGame.class);
                        i.putExtra("gamenumber", 3);
                        i.putExtra("index", value);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        item.setChecked(true);
                    } else
                        createToast("There is no Game " + 3);

                }
                break;
            case R.id.game4:
                if (item.isChecked()) item.setChecked(false);
                else {
                    if (totalGames >= 4) {
                        Intent i = new Intent(getApplicationContext(), ActivityGame.class);
                        i.putExtra("gamenumber", 4);
                        i.putExtra("index", value);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        item.setChecked(true);
                    } else {
                        createToast("There is no Game " + 4);
                    }

                }
                break;
            case R.id.game5:
                if (item.isChecked()) item.setChecked(false);
                else {
                    if (totalGames >= 5) {
                        Intent i = new Intent(getApplicationContext(), ActivityGame.class);
                        i.putExtra("gamenumber", 5);
                        i.putExtra("index", value);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        item.setChecked(true);
                    } else
                        createToast("There is no Game " + 5);

                }
                break;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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

        if (g.getNoVods() == 0) {
            Vods v = g.getVods();
            if (v != null) {
                vodUrl = v.getVod().getURL().split("v=")[1];
                youTubeView.initialize("AIzaSyCAHG2RhyRuOIFJCo5purXDxwO57FPJSn0", this);

            } else
                youTubeView.setVisibility(View.GONE);
        }

        processGameData(g);

        if (C.LOG_MODE) C.logI("Got game: " + g.getPlayers().size());
    }


    public void processGameData(Game g) {

        int counter = 0;
        View v;
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

                    v = blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player1);

                    processItems(p, v);


                    break;

                case 1:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_cs);

                    v = blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player2);
                    processItems(p, v);
                    break;

                case 2:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_cs);
                    v = blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player3);
                    processItems(p, v);
                    break;
                case 3:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_cs);
                    v = blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player4);
                    processItems(p, v);
                    break;
                case 4:
                    pname = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_name);
                    pkda = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_kda);
                    pcs = (TextView) blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_cs);
                    v = blueteam.findViewById(R.id.activity_game_blueteam).findViewById(R.id.activity_game_player5);
                    processItems(p, v);
                    break;
                case 5:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1).findViewById(R.id.player_cs);

                    v = redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player1);
                    processItems(p, v);
                    break;
                case 6:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2).findViewById(R.id.player_cs);

                    v = redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player2);
                    processItems(p, v);
                    break;
                case 7:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3).findViewById(R.id.player_cs);

                    v = redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player3);
                    processItems(p, v);
                    break;
                case 8:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4).findViewById(R.id.player_cs);

                    v = redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player4);
                    processItems(p, v);
                    break;

                case 9:
                    pname = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_name);
                    pkda = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_kda);
                    pcs = (TextView) redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5).findViewById(R.id.player_cs);

                    v = redteam.findViewById(R.id.activity_game_redteam).findViewById(R.id.activity_game_player5);
                    processItems(p, v);
                    break;

            }
            if (pname != null & p.getName() != null)
                pname.setText(p.getName());
            else
                pname.setText("-");


            if (pcs != null && p.getMinionsKilled() != null)
                pcs.setText(p.getMinionsKilled().toString());
            else
                pcs.setText("-");


            if (pkda != null && (p.getKills() != null && p.getDeaths() != null && p.getAssists() != null))
                pkda.setText(p.getKills() + "/" + p.getDeaths() + "/" + p.getAssists());
            else
                pkda.setText("-/-/-");


            counter++;
        }

        loading.setVisibility(View.GONE);
    }

    private void processItems(Player p, View v) {

        ImageView champion = (ImageView) v.findViewById(R.id.champion);

        if (p.getChampionId() != null)
            Picasso.with(getApplicationContext()).load(C.ICON_CHAMPION_URL + p.getChampionId() + ".png").into(champion);

        ImageView item1 = (ImageView) v.findViewById(R.id.player_item1);
        ImageView item2 = (ImageView) v.findViewById(R.id.player_item2);
        ImageView item3 = (ImageView) v.findViewById(R.id.player_item3);
        ImageView item4 = (ImageView) v.findViewById(R.id.player_item4);
        ImageView item5 = (ImageView) v.findViewById(R.id.player_item5);
        ImageView item6 = (ImageView) v.findViewById(R.id.player_item6);

        if (!p.itemList.isEmpty() && p.itemList.size() != 1) {
            if (!p.itemList.get(0).equalsIgnoreCase("0"))
                Picasso.with(getApplicationContext()).load(C.ICON_ITEMS_URL + p.itemList.get(0) + ".png").into(item1);

            if (!p.itemList.get(1).equalsIgnoreCase("0"))
                Picasso.with(getApplicationContext()).load(C.ICON_ITEMS_URL + p.itemList.get(1) + ".png").into(item2);

            if (!p.itemList.get(2).equalsIgnoreCase("0"))
                Picasso.with(getApplicationContext()).load(C.ICON_ITEMS_URL + p.itemList.get(2) + ".png").into(item3);

            if (!p.itemList.get(3).equalsIgnoreCase("0"))
                Picasso.with(getApplicationContext()).load(C.ICON_ITEMS_URL + p.itemList.get(3) + ".png").into(item4);

            if (!p.itemList.get(4).equalsIgnoreCase("0"))
                Picasso.with(getApplicationContext()).load(C.ICON_ITEMS_URL + p.itemList.get(4) + ".png").into(item5);

            if (!p.itemList.get(5).equalsIgnoreCase("0"))
                Picasso.with(getApplicationContext()).load(C.ICON_ITEMS_URL + p.itemList.get(5) + ".png").into(item6);

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            getActionBar().hide();
//            if (youTubePlayer != null)
//                youTubePlayer.setFullscreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            getActionBar().show();
//            if (youTubePlayer != null)
//                youTubePlayer.setFullscreen(false);
        }
    }
}
