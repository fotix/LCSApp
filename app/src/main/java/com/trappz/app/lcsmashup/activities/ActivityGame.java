package com.trappz.app.lcsmashup.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.trappz.app.api.messages.EventBusManager;
import com.trappz.app.api.models.Game.Game;
import com.trappz.app.api.models.Game.Player;
import com.trappz.app.api.models.Game.Vods;
import com.trappz.app.api.models.Match.Match;
import com.trappz.app.api.responses.GameResponseNotification;
import com.trappz.app.api.services.ApiServices;
import com.trappz.app.lcsmashup.C;
import com.trappz.app.lcsmashup.R;
import com.trappz.app.lcsmashup.fragments.FragmentScheduleDay;

/**
 * Created by Filipe Oliveira on 10-07-2014.
 */
public class ActivityGame extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

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
    boolean isValidGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        loading = (RelativeLayout) findViewById(R.id.activity_game_loadinglayout);
        nogame = (RelativeLayout) findViewById(R.id.activity_game_nogame);

        value = getIntent().getExtras().getInt("index");
        gameNumber = getIntent().getExtras().getInt("gamenumber");

        Match m = FragmentScheduleDay.MatchList.get(value);

        if (m != null) {
            totalGames = Integer.valueOf(m.getResult().size());
            Log.w(TAG, "TOTAL GAMES: " + totalGames);
            if (gameNumber <= totalGames) {
                nogame.setVisibility(View.GONE);
                isValidGame = true;
            }

        }

        getActionBar().setTitle(m.getName());
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

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
        LinearLayout b = (LinearLayout) blueteam.findViewById(R.id.activity_game_header);
        b.setBackgroundColor(Color.parseColor("#33B5E5"));
        redteam = findViewById(R.id.activity_game_redteam);
        LinearLayout r = (LinearLayout) redteam.findViewById(R.id.activity_game_header);
        r.setBackgroundColor(Color.parseColor("#FF4444"));

        if (m.getContestants() != null) {
            if (!m.getContestants().getBlue().getAcronym().isEmpty())
                BlueTeamName.setText(m.getContestants().getBlue().getAcronym());
            else
                BlueTeamName.setText(m.getContestants().getBlue().getName());

            if (!(m.getContestants().getRed().getAcronym().isEmpty()))
                RedTeamName.setText(m.getContestants().getRed().getAcronym());
            else
                RedTeamName.setText(m.getContestants().getRed().getName());


            BlueScore.setText(m.getContestants().getBlue().getWins() + "W - " +
                    m.getContestants().getBlue().getLosses() + "L");

            RedScore.setText(m.getContestants().getRed().getWins() + "W - " +
                    m.getContestants().getRed().getLosses() + "L");

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
            if (isValidGame) {
                ApiServices.getGame(m.getResult().get("game" + (gameNumber - 1)).getId());
                matchDetailsLayout.setVisibility(View.VISIBLE);
                noGames.setVisibility(View.INVISIBLE);
            }
        }
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
                    }else{
                        createToast("There is no Game "+1);
                    }

                }
                break;
            case R.id.game2:
                if (item.isChecked()) item.setChecked(false);
                else {
                    Log.w(TAG, "GAME 2 CALLED");
                    if (totalGames >= 2) {
                        Log.w(TAG, "GAME 2 CALLED VALID");
                        Intent i = new Intent(getApplicationContext(), ActivityGame.class);
                        i.putExtra("gamenumber", 2);
                        i.putExtra("index", value);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        item.setChecked(true);
                    }else
                        createToast("There is no Game "+2);


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
                    }else
                        createToast("There is no Game "+3);

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
                        createToast("There is no Game "+4);
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
                    }else
                        createToast("There is no Game "+5);

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
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
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

        Log.i("gameActivity", "Got game: " + g.getPlayers().size());
    }


    public void processGameData(Game g) {

        int counter = 0;
        View v;
        for (String key : g.getPlayers().keySet()) {
            Player p = g.getPlayers().get(key);

//            if(p.getItems().keySet().size() != 0){
//                for(String s:p.getItems().keySet()){
//                    Log.w(TAG,"ItemNUMBER: "+p.getItems().get(s));
//                }
//            }else
//                Log.w(TAG,"no items found");

//            if (!p.itemList.isEmpty()) {
//                for (String s : p.itemList)
//                    Log.w(TAG, s);
//            } else
//                Log.w(TAG, "no items found");

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
                pkda.setText(p.getKills() + "/" + p.getKills() + "/" + p.getAssists());
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

    }
}
