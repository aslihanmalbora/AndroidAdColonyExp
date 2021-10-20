package com.c1ctech.adcolonydemo;

import androidx.appcompat.app.AppCompatActivity;

import com.adcolony.sdk.*;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String APP_ID = "ENTER_YOUR_APP_ID";
    private final static String BANNER_ZONE_ID = "ENTER_YOUR_BANNER_ZONE_ID";
    private final static String INTERSTITIAL_ZONE_ID = "ENTER_YOUR_INTERSTITIAL_ZONE_ID";
    private final static String REWARD_ZONE_ID = "ENTER_YOUR_REWARD_ZONE_ID";
    public final static String[] AD_UNIT_Zone_Ids = new String[]{BANNER_ZONE_ID, INTERSTITIAL_ZONE_ID, REWARD_ZONE_ID};

    private LinearLayout bannerContainer;
    private AdColonyAdView bannerAdColony;

    private AdColonyInterstitial interstitialAdColony;
    private AdColonyInterstitialListener interstitialListener;
    private AdColonyAdOptions interstitialAdOptions;

    private AdColonyInterstitial rewardAdColony;
    private AdColonyInterstitialListener rewardListener;
    private AdColonyAdOptions rewardAdOptions;

    private static boolean isInterstitialLoaded;
    private static boolean isRewardLoaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initColonySdk();
        initBannerAd();
        initInterstitialAd();
        initRewardedAd();
    }

    private void initColonySdk() {
        // Construct optional app options object to be sent with configure
        // setKeepScreenOn: set a flag on our Activity's window to keep the display from going to sleep.
        AdColonyAppOptions appOptions = new AdColonyAppOptions().setKeepScreenOn(true);

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(getApplication(), appOptions, APP_ID, AD_UNIT_Zone_Ids);
    }

    private void initBannerAd() {
        bannerContainer = (LinearLayout) findViewById(R.id.colony);

        // Set up listener for banner ad callbacks.
        // Implement only the needed callbacks.
        AdColonyAdViewListener bannerListener = new AdColonyAdViewListener() {

            // Code to be executed when an ad request is filled
            // or when an ad finishes loading.
            // get AdColonyAdView object from adcolony Ad Server.
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {

                //Remove previous ad view if present.
                if (bannerContainer.getChildCount() > 0) {
                    bannerContainer.removeView(bannerAdColony);
                }
                bannerContainer.addView(adColonyAdView);
                bannerAdColony = adColonyAdView;
            }

            // Code to be executed when an ad request is not filled
            //or when an ad is not loaded.
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
            }

            //Code to be executed when an ad opens
            @Override
            public void onOpened(AdColonyAdView ad) {
                super.onOpened(ad);
            }

            //Code to be executed when user closed an ad
            @Override
            public void onClosed(AdColonyAdView ad) {
                super.onClosed(ad);
            }

            // Code to be executed when the user clicks on an ad.
            @Override
            public void onClicked(AdColonyAdView ad) {
                super.onClicked(ad);
            }

            // called after onAdOpened(), when a user click opens another app
            // (such as the Google Play), backgrounding the current app
            @Override
            public void onLeftApplication(AdColonyAdView ad) {
                super.onLeftApplication(ad);
            }
        };

        // Optional Ad specific options to be sent with request
        AdColonyAdOptions adOptions = new AdColonyAdOptions();

        //Request Ad
        AdColony.requestAdView(BANNER_ZONE_ID, bannerListener, AdColonyAdSize.BANNER, adOptions);
    }

    private void initInterstitialAd() {

        // Set up listener for interstitial ad callbacks.
        // Implement only the needed callbacks.
        interstitialListener = new AdColonyInterstitialListener() {

            // Code to be executed when an ad request is filled.
            // get AdColonyInterstitial object from adcolony Ad Server.
            @Override
            public void onRequestFilled(AdColonyInterstitial adIn) {
                // Ad passed back in request filled callback, ad can now be shown
                interstitialAdColony = adIn;
                isInterstitialLoaded = true;
            }

            // Code to be executed when an ad request is not filled
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
            }

            //Code to be executed when an ad opens
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
            }

            //Code to be executed when user closed an ad
            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();

                //request new Interstitial Ad on close
                AdColony.requestInterstitial(INTERSTITIAL_ZONE_ID, interstitialListener, interstitialAdOptions);
            }

            // Code to be executed when the user clicks on an ad.
            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
            }

            // called after onAdOpened(), when a user click opens another app
            // (such as the Google Play), backgrounding the current app
            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }

            // Code to be executed when an ad expires.
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };

        // Optional Ad specific options to be sent with request
        interstitialAdOptions = new AdColonyAdOptions();
        AdColony.requestInterstitial(INTERSTITIAL_ZONE_ID, interstitialListener, interstitialAdOptions);
    }

    private void initRewardedAd() {

        //setRewardListener: set the AdColonyRewardListener for global reward callbacks for the app.
        AdColony.setRewardListener(new AdColonyRewardListener() {

            @Override
            public void onReward(AdColonyReward reward) {

                if (reward.success()) {
                    Toast.makeText(getApplicationContext(), "Reward Earned", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Reward Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up listener for reward ad callbacks.
        // Implement only the needed callbacks.
        rewardListener = new AdColonyInterstitialListener() {

            // Code to be executed when an ad request is filled.
            // get AdColonyInterstitial Reward object from adcolony Ad Server.
            @Override
            public void onRequestFilled(AdColonyInterstitial adReward) {
                rewardAdColony = adReward;
                isRewardLoaded = true;
            }

            // Code to be executed when an ad request is not filled
            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
            }

            //Code to be executed when an ad opens
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
            }

            //Code to be executed when user closed an ad
            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();

                //request new reward on close
                AdColony.requestInterstitial(REWARD_ZONE_ID, rewardListener, rewardAdOptions);
            }

            // Code to be executed when the user clicks on an ad.
            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
            }

            // called after onAdOpened(), when a user click opens another app
            // (such as the Google Play), backgrounding the current app
            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }

            // Code to be executed when an ad expires.
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };

        // Ad specific options to be sent with request
        rewardAdOptions = new AdColonyAdOptions()
                .enableConfirmationDialog(false)
                .enableResultsDialog(false);
        AdColony.requestInterstitial(REWARD_ZONE_ID, rewardListener, rewardAdOptions);


    }

    //method call on click of showInterstitialAd button
    public void showInterstitialAd(View view) {
        if (interstitialAdColony != null && isInterstitialLoaded) {
            interstitialAdColony.show();
            isInterstitialLoaded = false;
        } else {
            Toast.makeText(getApplicationContext(), "Interstitial Ad Is Not Loaded Yet or Request Not Filled", Toast.LENGTH_SHORT).show();
        }
    }

    //method call on click of showRewardVideoAd button
    public void showRewardVideoAd(View view) {
        if (rewardAdColony != null && isRewardLoaded) {
            rewardAdColony.show();
            isRewardLoaded = false;
        } else {
            Toast.makeText(getApplicationContext(), "Reward Ad Is Not Loaded Yet or Request Not Filled", Toast.LENGTH_SHORT).show();
        }
    }

}