package com.makadown.top10downloader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by usuario on 26/02/2016.
 */
public class ParseApplications
{
    private String data;
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.data = xmlData;
        applications = new ArrayList<>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process()
    {
        boolean status = true;
        Application  currentRecord = null;
        boolean inEntry = false;
        String textValue  = "";
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader( this.data) );
            int eventType =  xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT )
            {
                String tagName = xpp.getName();
                switch(eventType )
                {
                    case XmlPullParser.START_TAG:
                       // Log.d("ParseApplication", "Starting tag for "  + tagName );
                        if (tagName.equalsIgnoreCase( "entry" ))
                        {
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
//                        Log.d("ParseApplication", "Ending tag for "  + tagName );
                        if (inEntry)
                        {
                            if( tagName.equalsIgnoreCase("entry") )
                            {
                                applications.add(currentRecord);
                                inEntry = false;
                            }
                            else if( tagName.equalsIgnoreCase("name") )
                            {
                                currentRecord.setName(textValue);
                            }
                            else if( tagName.equalsIgnoreCase("artist") )
                            {
                                currentRecord.setArtist(textValue);
                            }
                            else if( tagName.equalsIgnoreCase("releaseDate") )
                            {
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;
                    default:
                        //nothing
                }
                eventType = xpp.next();

            }

        }
        catch(Exception e)
        {
            status = false;
            e.printStackTrace();
        }

/*
        for( Application  app : applications)
        {
            Log.d("ParseApplication", "****************************************");
            Log.d("ParseApplication", "Name :"  +  app.getName() );
            Log.d("ParseApplication", "Artist:  "  + app.getArtist() );
            Log.d("ParseApplication", "Release Date: "  + app.getReleaseDate() );

        }
*/
        return status;
    }
}
