package com.example.rssprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    private ListView lvRss;
    private MenuItem menuItem;
    private final String CBC_URL_DEFAULT = "https://www.espn.com/espn/rss/news";
    //request code for startActivityForResult
    private final int STANDARD_REQUEST_CODE = 222;
    private RssAdapter rssAdapter;
    public static final String EXTRA_TEXT1 = "com.example.rssprocessing.EXTRA_TEXT1";
    public static final String EXTRA_TEXT2 = "com.example.rssprocessing.EXTRA_TEXT2";
    public static final String EXTRA_TEXT3 = "com.example.rssprocessing.EXTRA_TEXT3";
    public static final String EXTRA_TEXT4 = "com.example.rssprocessing.EXTRA_TEXT4";
    public TextView tvPubDate, tvTitle;

    private SharedPreferences sp;
    private final String PREF_FILE = "PREFS";
    private String urlSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SC", "onCreate()");
        setContentView(R.layout.activity_main);
        menuItem = findViewById(R.id.menu_refresh);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        MyAsyncTask myAsyncTask;
        sp = getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                return true;
            case R.id.world:
                String CBC_URL1 = "https://www.thesun.co.uk/news/worldnews/feed/";
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                editor.putString("url", CBC_URL1);
                editor.commit();
                return true;
            case R.id.soccer:
                String CBC_URL2 = "https://www.espn.com/espn/rss/soccer/news";
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                editor.putString("url", CBC_URL2);
                editor.commit();
                return true;
            case R.id.top_headlines:
                String CBC_URL3 = "https://www.espn.com/espn/rss/news";
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                editor.putString("url", CBC_URL3);
                editor.commit();
                return true;
            case R.id.settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Part 1
    class MyAsyncTask extends AsyncTask<Object, Object, Object> {

        private SAXParser saxParser;
        private URL[] url;
        private InputStream inputStream;
        private CBCHandler cbcHandler;

        //Part 2
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("SC", "onPreExecute()");
            Toast.makeText(MainActivity.this, "Processing RSS Feed", Toast.LENGTH_SHORT).show();
        }

        //Part 3
        @Override
        protected Object doInBackground(Object[] objects) {

            //Part 3A
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                saxParser = spf.newSAXParser();
                //create the handler
                cbcHandler = new CBCHandler();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            //Part 3B
            URL url = null;
            SharedPreferences sp = getSharedPreferences("PREFS", 0);

            String CBC_URL_NEW = sp.getString("url", CBC_URL_DEFAULT);
            try {
                url = new URL(CBC_URL_NEW);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Part 3C
            //inputStream - what to parse
            //cbcHandler (DefaultHandler) _ how to parse the data
            try {
                saxParser.parse(inputStream, cbcHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //Part 4
        //publish results of the task to the UI
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("SC", "onPostExecute()");

            for (SportNews title : cbcHandler.news) {
                Log.d("exam", String.valueOf(title));

            }
            //TODO: create and set the adapter -- can use android.R.layout.simple_list_item_1 for the textViewResouceId for the adapter
            //EU QUE FIZ - PARA POPULAR AS NOTICIAS
            lvRss = findViewById(R.id.lvRss);
            rssAdapter = new RssAdapter(MainActivity.this, R.layout.row, cbcHandler.news);
            lvRss.setAdapter(rssAdapter);

            lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    SportNews news = (SportNews) adapterView.getItemAtPosition(position);
                    Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                    //...and start it to get data back from OtherActivity
                    for (int i=0; i< adapterView.getCount(); i++ ){
                        if (position == i){
                            String textPubDate =  news.getPubDate();
                            String textTitle = news.getTitle();
                            String textLink = news.getLink();
                            String textDescription = news.getDescription();
                            
                            Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                            intent.putExtra(EXTRA_TEXT1, textPubDate);
                            intent.putExtra(EXTRA_TEXT2, textTitle);
                            intent.putExtra(EXTRA_TEXT3, textLink);
                            intent.putExtra(EXTRA_TEXT4, textDescription);
                            startActivityForResult(intent, STANDARD_REQUEST_CODE);
                        }
                    }


                }
            });
        }
    }

    //Part 7
    //A custom adapter for the ListView if you'd like to use it.
    private class RssAdapter extends ArrayAdapter<SportNews> {

        public RssAdapter(Context context, int textViewResourceId, List<SportNews> title) {
            super(context, textViewResourceId, title);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert vi != null;
                v = vi.inflate(R.layout.row, null);
            }

            SportNews news = getItem(position);
            if (news != null) {
                tvPubDate = v.findViewById(R.id.textView1);
                tvPubDate.setText(news.getPubDate());
                tvTitle = v.findViewById(R.id.textView2);
                tvTitle.setText(news.getTitle());
            }
            return v;
        }
    }

    //Part 5
    //a sax handler that is designed to parse a CBC news feed
    public class CBCHandler extends DefaultHandler {
        //Part 5A
        //flags to keep track of what element we are in
        private boolean inItem, inTitle, inLink, inPubDate, inDescription;
        //data structure(s) to store the data we want to parse out
        private List<String> title;
        private List<String> link;
        private List<String> pubDate;
        private List<String> description;
        private List<SportNews> news;
        //Use StringBuilder to build strings
        private StringBuilder stringBuilder;
        //Java initialization block
        {
            title = new ArrayList<String>(20);
            link = new ArrayList<String>(20);
            pubDate = new ArrayList<String>(10);
            description = new ArrayList<String>(100);
            news = new ArrayList<SportNews>(20);
        }

        //Part 5B
        //returns the title ArrayList
        public List<String> getTitles() {
            return title;
        }

        //Part 6
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            Log.d("SC", "startDocument");
        }

        //Part 7
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            Log.d("SC", "endDocument -- data in arraylist:");

            //let's see what we pulled out
            for(int i=0; i<title.size() -1; i++) {
                Log.d("SC", i + ": " + title.get(i));
                news.add(new SportNews(title.get(i), link.get(i), pubDate.get(i), description.get(i)));
            }
        }

        //Part 8
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Log.d("SC", "startElement: " + qName);
            if(qName.equals("item")) {
                inItem = true;
            }
            if(qName.equals("title")) {
                inTitle = true;
                stringBuilder = new StringBuilder(20);
            }
            if(qName.equals("link")) {
                inLink = true;
                stringBuilder = new StringBuilder(20);
            }
            if(qName.equals("pubDate")) {
                inPubDate = true;
                stringBuilder = new StringBuilder(10);
            }
            if(qName.equals("description")) {
                inDescription = true;
                stringBuilder = new StringBuilder(10);
            }
        }

        //Part 9
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            Log.d("SC", "endElement: " + qName);

            if(qName.equals("item")) {
                inItem = false;
            }
            else if(inItem && qName.equals("title")) {
                inTitle = false;
                //add the contents of the stringBuilder to the ArrayList
                title.add(stringBuilder.toString());
            }
            else if(inItem && qName.equals("link")) {
                inTitle = false;
                //add the contents of the stringBuilder to the ArrayList
                link.add(stringBuilder.toString());
            }
            else if(inItem && qName.equals("pubDate")) {
                inTitle = false;
                //add the contents of the stringBuilder to the ArrayList
                pubDate.add(stringBuilder.toString());
            }
            else if(inItem && qName.equals("description")) {
                inDescription = false;
                //add the contents of the stringBuilder to the ArrayList
                description.add(stringBuilder.toString());
            }
        }

        //Part 10
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            //String s = new String(ch, start, length);
            //Log.d("SC", "characters: " + s);

            //make sure we are in the title of an item element

            if (inItem && inTitle) {
                //build the string
                stringBuilder.append(ch, start, length);
            }
            else if (inItem && inLink) {
                //build the string
                stringBuilder.append(ch, start, length);
            }
            else if (inItem && inPubDate) {
                //build the string
                stringBuilder.append(ch, start, length);
            }
            else if (inItem && inDescription) {
                //build the string
                stringBuilder.append(ch, start, length);
            }

        }
    }

    //nested class for event handling
    class EventHandler implements View.OnClickListener, View.OnLongClickListener {
        @Override
        public void onClick(View view) {
            //what View (UI widget) was clicked?
            switch (view.getId()) {
                case R.id.menu_refresh:
                    Log.d("SC", "Refresh was clicked");
                    //create instance of our AsyncTask
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    //...and execute it!
                    myAsyncTask.execute();

                    //for(int i=0; i<999999; i++) {
                    //    Log.d("JG", "i = " + i);
                    //    //like downloading a giant file or some other big task
                    //}
                    break;
//                case R.id.btnOtherActivity:
//                    Log.d("SC", "clicked!!!!!!!!!!!!!!!");
//                    //create explicit Intent to start OtherActivity
//                    Intent i = new Intent(MainActivity.this, OtherActivity.class);
//                    //...and start it to get data back from OtherActivity
//                    startActivityForResult(i, STANDARD_REQUEST_CODE);
//                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("SC", "long clicked!!!!!!!!!!!!!!!");
            return true;
        }
    }

    class SportNews {
        private String title;
        private String link;
        private String pubDate;
        private String description;

        SportNews(String title, String link, String pubDate, String description) {
            this.title = title;
            this.link = link;
            this.pubDate = pubDate;
            this.description = description;
        }

        private String getLink() {
            return link;
        }
        private String getTitle() {
            return title;
        }
        private String getPubDate() {
            return pubDate;
        }
        private  String getDescription() {
            return description;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SC", "onStart()");
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SC", "onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SC", "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("SC", "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SC", "onDestroy()");
    }
}
