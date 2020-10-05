package com.example.noman_000.top_10_downloader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQ_CODE = 12;
    private ListView list;
    private TextView feedTitle;
    private AlertDialog.Builder builder;
    private int feedLiMit = 10;
    private int MenuiteMIndex;
    private boolean isMenuGroupIteMSelected;
    private String cachedData;
    private String cachedUrl;
    private ParsingApplications parsingApplications;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    static int MenuSize;

    private void forMatUrlAndDownload() {
        Log.d("feedUrl", feedUrl);
        Log.d("feedLiMit", ""+feedLiMit);
        if (!feedUrl.equalsIgnoreCase(cachedUrl)) {
            downloadUrl(String.format(feedUrl, feedLiMit));
            cachedUrl = feedUrl;
        } else {
            builder.show().
                    setCanceledOnTouchOutside(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_list);
        list = (ListView) findViewById(R.id.parseList);
        feedTitle = (TextView) findViewById(R.id.FeedTitle);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Refresh").
                setMessage("Do you want to download data again").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadUrl(String.format(feedUrl, feedLiMit));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        if (savedInstanceState == null) {
            forMatUrlAndDownload();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast.makeText(this, "onCreateOptionsMenu", Toast.LENGTH_LONG).show();
        getMenuInflater().inflate(R.menu.list_menu, menu);
        MenuSize = menu.size();
        if (isMenuGroupIteMSelected) {
            menu.getItem(MenuiteMIndex).setChecked(true);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("iteMIndex", MenuiteMIndex);
        outState.putBoolean("MenuiteMSelected", isMenuGroupIteMSelected);
        outState.putString("cachedData", cachedData);
        outState.putString("feedUrl", feedUrl);
        outState.putString("feedTitle", feedTitle.getText().toString());
        if (feedLiMit == 25) {
            outState.putInt("feedLiMit", feedLiMit);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        MenuiteMIndex = savedInstanceState.getInt("iteMIndex");
        isMenuGroupIteMSelected = savedInstanceState.getBoolean("MenuiteMSelected");
        cachedData = savedInstanceState.getString("cachedData");
        feedUrl = savedInstanceState.getString("feedUrl");
        feedTitle.setText(savedInstanceState.getString("feedTitle"));
        cachedUrl = feedUrl;
        setData(cachedData);
        int feedLiMit = savedInstanceState.getInt("feedLiMit", 10);
        if (feedLiMit > 10) {
            this.feedLiMit = feedLiMit;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.MenuFree:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.paid:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.MenuSongs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.MenuAlbuMs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=%d/xml";
                break;
            case R.id.MnuMovies:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
                if(!feedUrl.equalsIgnoreCase(cachedUrl)) {
                    cachedUrl = feedUrl;
                    downloadUrl(feedUrl);
                }
                else {
                    builder.show().setCanceledOnTouchOutside(false);
                }
                return true;

            case R.id.Mnu10:
                if(!feedUrl.equalsIgnoreCase("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml")) {
                    MenuiteMIndex = IteMsIndexOfMenu.iteMTen.getIteMIndex();
                    if (feedLiMit == 25) {
                        cachedUrl = null;
                    }
                    feedLiMit = 10;
                }
                else {
                    return true;
                }
                break;
            case R.id.Mnu25:
                if(!feedUrl.equalsIgnoreCase("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml")) {
                    MenuiteMIndex = IteMsIndexOfMenu.iteMTwentyFive.getIteMIndex();
                    if (feedLiMit == 10) {
                        cachedUrl = null;
                    }
                    feedLiMit = 25;
                    Toast.makeText(this, "" + feedLiMit, Toast.LENGTH_LONG).show();
                }
                else {
                    return true;
                }
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        if (id == R.id.Mnu10 || id == R.id.Mnu25) {
            if (!isMenuGroupIteMSelected) {
                isMenuGroupIteMSelected = true;
            }
            item.setChecked(true);
        }
        forMatUrlAndDownload();
        return true;
    }

    private void downloadUrl(String feedUrl) {
        DownloadData downloadData = new DownloadData();
        downloadData.execute(feedUrl);
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: " + strings[0]);
            String rssFeed = downloadXMl(strings[0]);
            if (rssFeed != null) {
                Log.d(TAG, "doInBackground: " + rssFeed);
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cachedData = s;
            Log.d(TAG, "onPostExecute: ");
            setData(s);
        }

        private String downloadXMl(String urlPath) {
            StringBuilder rssFeed = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    char[] inputBuffer = new char[500];
                    int charsRead = bufferedReader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    } else if (charsRead > 0) {
                        rssFeed.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXMl: invalid url");
            } catch (IOException e) {
                Log.e(TAG, "downloadXMl: with in application soMething went wrong in input / output operation " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXMl: Security exception " + e.getMessage());
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "downloadXMl: with in application soMething went wrong in input / output operation " + e.getMessage());
                } catch (NullPointerException e) {

                }
            }
            return rssFeed.toString();
        }
    }

    private void setData(String data) {
        parsingApplications = new ParsingApplications();
        boolean parseResult = parsingApplications.parseXMl(data);
        if (parseResult) {
            ApplicationsAdapter applicationsAdapter = new ApplicationsAdapter(MainActivity.this
                    , R.layout.list_record, parsingApplications.getApplications());

            feedTitle.setText(parsingApplications.getFeedTitle());
            list.setAdapter(applicationsAdapter);
        } else {
            Toast.makeText(MainActivity.this, "Unfortunately app crashed", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private String processData(){
        if(parsingApplications != null){
            StringBuilder result = new StringBuilder();
            for(FeedEntry entry : parsingApplications.getApplications()){
                result.append(entry.toString()).append("\n\n");
            }
            return result.toString();
        }
        return null;
    }
    public void sendEMail(View v){
        String proceesedData = processData();
        Intent eMailIntent = new Intent(Intent.ACTION_SEND);
        eMailIntent.setType("message/rfc822");
        eMailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"daniyalghani@hotmail.com"});
        eMailIntent.putExtra(Intent.EXTRA_SUBJECT, "apple rss feed");
        eMailIntent.putExtra(Intent.EXTRA_TEXT, proceesedData);
        startActivity(Intent.createChooser(eMailIntent, "Send eMail"));
    }

}

enum IteMsIndexOfMenu {
    iteMTen(MainActivity.MenuSize - 2), iteMTwentyFive(MainActivity.MenuSize - 1);
    private final int iteMIndex;

    IteMsIndexOfMenu(int position) {
        iteMIndex = position;
    }

    int getIteMIndex() {
        return iteMIndex;
    }
}
