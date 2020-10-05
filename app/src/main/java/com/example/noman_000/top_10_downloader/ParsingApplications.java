package com.example.noman_000.top_10_downloader;

import android.util.Log;

import com.example.noman_000.top_10_downloader.FeedEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ParsingApplications {
    private ArrayList<FeedEntry> applications;
    private String feedTitle;

    public ParsingApplications(){
        applications = new ArrayList<>();
    }

    public boolean parseXMl(String xMlData){
        Log.d("par", "parseXMl: ");
        boolean status = true;
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xMlParser = factory.newPullParser();
            xMlParser.setInput(new StringReader(xMlData));
            int eventType = xMlParser.getEventType();
            FeedEntry currentFeed = null;
            boolean inEntry = false;
            boolean inTitle = false;
            String tagNaMe;
            String textValue = null;
            while(eventType != XmlPullParser.END_DOCUMENT){
                tagNaMe = xMlParser.getName();
                Log.d("pari", "parseXMl: ");
                switch(eventType){
                    case XmlPullParser.START_TAG :
                        if(!inEntry && "title".equalsIgnoreCase(tagNaMe)){
                            inTitle = true;
                        }
                        else if("entry".equalsIgnoreCase(tagNaMe)){
                            inEntry = true;
                            currentFeed = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT :
                        if(inTitle){
                            feedTitle = xMlParser.getText();
                            inTitle = false;
                        }
                        else if(inEntry) {
                            textValue = xMlParser.getText();
                        }
                        break;

                    case XmlPullParser.END_TAG :
                        if(inEntry) {
                            if ("entry".equalsIgnoreCase(tagNaMe)) {
                                inEntry = false;
                                applications.add(currentFeed);
                            } else if ("name".equalsIgnoreCase(tagNaMe)) {
                                currentFeed.setNaMe(textValue);
                            } else if ("artist".equalsIgnoreCase(tagNaMe)) {
                                currentFeed.setArtist(textValue);
                            } else if ("releasedate".equalsIgnoreCase(tagNaMe)) {
                                currentFeed.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(tagNaMe)) {
                                currentFeed.setSuMMary(textValue);
                            } else if ("image".equalsIgnoreCase(tagNaMe)) {
                                currentFeed.setiMageUrl(textValue);
                            }
                            break;
                        }
                }
                eventType = xMlParser.next();
            }
            for(FeedEntry feed : applications){
                Log.d(TAG, "***********");
                feed.toString();
            }
        }
        catch (XmlPullParserException e){
            status = false;
            Log.e(TAG, "parseXMl: " + e.getMessage());
        }
        catch (NullPointerException e){
            status = false;
            e.printStackTrace();
        }
        catch (IOException e){
            status = false;
            e.printStackTrace();
        }
        return status;
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public String getFeedTitle() {
        return feedTitle;
    }
}
