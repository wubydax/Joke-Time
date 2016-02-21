package com.wubydax.laughtertime.free;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wubydax.laughtertime.FetchJoke;
import com.wubydax.laughtertime.R;

public class MainActivity extends AppCompatActivity {
    InterstitialAd interstitialAd;
    private AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                new FetchJoke(MainActivity.this).execute();
            }
        });
        adRequest = new AdRequest.Builder().build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adRequest != null) {
            interstitialAd.loadAd(adRequest);
        }
    }

    public void tellJoke(View view) {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            new FetchJoke(MainActivity.this).execute();
        }

    }


}
