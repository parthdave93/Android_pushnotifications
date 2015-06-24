package com.bugle.pushnotification_types;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.normal)
    Button normal;
    @InjectView(R.id.bigtext)
    Button bigtext;
    @InjectView(R.id.WebFetched)
    Button WebFetched;
    @InjectView(R.id.bigimagebanner)
    Button bigimagebanner;
    @InjectView(R.id.inbox)
    Button inbox;

    Bitmap bigimagebannerBitmap, webfetchedIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        toolbar.setTitle("Push Notification Demo");
        setSupportActionBar(toolbar);

        normal.setOnClickListener(this);
        bigimagebanner.setOnClickListener(this);
        bigtext.setOnClickListener(this);
        WebFetched.setOnClickListener(this);
        inbox.setOnClickListener(this);

        new post_data().execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal:

               /* NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification(R.mipmap.ic_launcher,"Title Text Will be like this!!!!",System.currentTimeMillis());
                PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

                notify.setLatestEventInfo(getApplicationContext(),"Subject of the text will be like this","Body of the message will be this",pending);
                notif.notify(0, notify);*/

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(this, MainActivity.class);

                mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);

                mBuilder.setTicker("My notification");

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(0, mBuilder.build());

                break;

            case R.id.inbox:
                mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Inbox tracker")
                        .setContentText("Messages received");

                mBuilder.setTicker("Inbox notifications");

                mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);

                NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle();
                String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
                inboxStyle.setBigContentTitle("Event tracker details:");
                for (int i = 0; i < events.length; i++) {
                    events[i] = "Event " + i + " Text....... Message";
                }
// Moves events into the expanded layout
                for (int i = 0; i < events.length; i++) {
                    inboxStyle.addLine(events[i]);
                }
// Moves the expanded layout object into the notification object.

                mBuilder.setStyle(inboxStyle);

                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(1, mBuilder.build());

                break;
            case R.id.bigimagebanner:

                new fetch_bigimage_bitmap().execute();

                break;
            case R.id.bigtext:

                mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("BigTextStyle")
                        .setContentText("BigTextStyle received");

                mBuilder.setTicker("BigTextStyle notifications");

                mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);

                NotificationCompat.BigTextStyle bigTextStyle =
                        new NotificationCompat.BigTextStyle();

                bigTextStyle.bigText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries.");
                bigTextStyle.setBigContentTitle("BigTextStyle");

                mBuilder.setStyle(bigTextStyle);

                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(3, mBuilder.build());
                break;
            case R.id.WebFetched:

                new fetch_webIconimage_bitmap().execute();
                break;
        }
    }


    private class fetch_bigimage_bitmap extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://thefilmreview.com/wp-content/uploads/2013/05/iron-man-review-landscape-thumb.jpeg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bigimagebannerBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("bigPictureStyle")
                    .setContentText("bigPictureStyle received");

            mBuilder.setTicker("bigPictureStyle notifications");

            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationCompat.BigPictureStyle bigPictureStyle =
                    new NotificationCompat.BigPictureStyle();

            bigPictureStyle.setSummaryText("bigPictureStyle Summery");
            bigPictureStyle.bigPicture(bigimagebannerBitmap);


            mBuilder.setStyle(bigPictureStyle);

            Intent intent = new Intent(MainActivity.this, MainActivity.class);

            PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            mBuilder.addAction(R.mipmap.ic_launcher, "Action", pIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(2, mBuilder.build());
        }
    }

    private class fetch_webIconimage_bitmap extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://majorspoilers.com/wp-content/uploads/2012/04/IronMan-THUMB.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                webfetchedIcon = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(webfetchedIcon)
                            .setContentTitle("My webIconimage notification")
                            .setContentText("webIconimage World!");

            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);

            mBuilder.setTicker("My webIconimage");
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(4, mBuilder.build());
        }
    }

    public Bitmap getBitmap(String urlstr) {
        Bitmap b;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            b = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
        return b;
    }


    public class post_data extends AsyncTask<Void, Void, Void> {

        linklist link_list;


        @Override
        protected Void doInBackground(Void... voids) {

            String jsondata = "{\"class\": \"OrderItemListDto\",orderItemList\": [{\"class\": \"OrderItemDto\",\"orderId\": 24,\"itemId\": 1,\"quantity\": 2,\"status\":\"NEW\",\"sRequest\": \"none\"},{\"class\": \"OrderItemDto\",\"orderId\": 24,\"itemId\": 2,\"quantity\": 2,\"status\": \"NEW\",\"sRequest\": \"none\"}]}";

            String resp = callJson("http://192.168.1.70/sagar/sufirecipe/api/v1/product/1");

            link_list = new linklist();

            System.out.println("Json:" + jsondata);

            if (resp != null)
                if (resp.length() > 0) {

                    System.out.println("--------------------------------------------------------------------------------------------------------------");

                    analyse_in_tree(resp);


                    System.out.println("--------------------------------------------------------------------------------------------------------------");
                }
            return null;
        }

        public String callJson(String link) {

            String response = null;

            try {
                String result = null;
                // 1. create HttpClient
                HttpResponse httpResponse = null;

                String jsonUser = "";

                jsonUser = link;

                System.out.println("json Called " + jsonUser);

                HttpGet httpGet = new HttpGet(jsonUser);

                @SuppressWarnings("unused")
                StringEntity seUser = null;


                @SuppressWarnings("unused")
                HttpParams httpParameters = new BasicHttpParams();

                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpResponse = httpClient.execute(httpGet);

                try {
                    seUser = new StringEntity(jsonUser);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println("json sending " + jsonUser);

                try {

                    // make GET request to the given URL


                    // receive response as inputStream
                    InputStream is = httpResponse.getEntity().getContent();

                    // convert inputstream to string
                    if (is != null) {
                        result = convertInputStreamToString(is);
                        //System.out.println("Result From The Vanya = "+result);
                    } else {
                        result = "Did not work!";
                        //	System.out.println("Result From The Vanya = "+result);
                    }

                    response = result;

                    //				JSONObject jResponseObj = new JSONObject(result);//Json Object For user details

                    //				displayName = jResponseObj.getString("display_name");
                    //				System.out.println("display name"+displayName);
                    //				profilePicName = jResponseObj.getString("image");
                    //				coverImgName = jResponseObj.getString("image");////temp change for coverimage

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (Exception e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;

        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void analyse_in_tree(String jsondata) {
        nodes main_node = new nodes();

//            String jsondata = "{\"class\": \"OrderItemListDto\",\"orderItemList\": [{\"class\": \"OrderItemDto\",\"orderId\": 24,\"itemId\": 1,\"quantity\": 2,\"status\":\"NEW\",\"sRequest\": \"none\"},{\"class\": \"OrderItemDto\",\"orderId\": 24,\"itemId\": 2,\"quantity\": 2,\"status\": \"NEW\",\"sRequest\": \"none\"}]}";

        System.out.println("Json:" + jsondata);

        try {
            if (jsondata.length() > 0) {
                if (jsondata.charAt(0) == '{') {
                    main_node.setCurrent_node_name("main_json_object");
                    main_node.setCurrent_node_type("JSONObject");
                    main_node.setParent_node_name("main_json_object");
                    main_node.setParent_node_type("JSONObject");

                    parse_this_jsonObject_nodes(main_node.getCurrent_node_name(), new JSONObject(jsondata), main_node);

                } else if (jsondata.charAt(0) == '[') {
                    main_node.setCurrent_node_name("main_json_array");
                    main_node.setCurrent_node_type("JSONArray");
                    main_node.setParent_node_type("JSONArray");
                    main_node.setParent_node_name("main_json_array");
                    parse_this_jsonArray_nodes(main_node.getCurrent_node_name(), new JSONArray(jsondata), main_node);
                } else {
                    Log.e("Json Error", "Json can not starting with string must have { or [ tags");
                }


            } else {
                Log.e("Json Error", "Json Size 0");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        generate_file(main_node, "generated_async");

        Log.e("nodes:", main_node.toString());
    }

    public void parse_this_jsonObject_nodes(String jsonobject_name, JSONObject jsonObject, nodes node) {
        Iterator<?> keys = jsonObject.keys();
        try {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {

                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("JSONObject");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        parse_this_jsonObject_nodes(key, jsonObject, temp_node);
                        node.getSub_nodes().add(temp_node);

                    }


                } else if (jsonObject.get(key) instanceof JSONArray) {

                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("JSONArray");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        parse_this_jsonArray_nodes(key, (JSONArray) jsonObject.get(key), temp_node);

                        node.getSub_nodes().add(temp_node);

                    }


                } else if (jsonObject.get(key) instanceof String) {
                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("String");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        node.getSub_nodes().add(temp_node);

                    }
                } else if (jsonObject.get(key) instanceof Integer) {
                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("Integer");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        node.getSub_nodes().add(temp_node);
                    }
                } else if (jsonObject.get(key) instanceof Float) {
                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("Float");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        node.getSub_nodes().add(temp_node);
                    }
                } else if (jsonObject.get(key) instanceof Double) {
                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("Double");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        node.getSub_nodes().add(temp_node);
                    }
                } else if (jsonObject.get(key) instanceof Long) {
                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("Long");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        node.getSub_nodes().add(temp_node);
                    }
                } else if (jsonObject.get(key) instanceof Boolean) {
                    boolean contains = false;
                    if (node.getSub_nodes() != null)
                        for (nodes subnode : node.getSub_nodes()) {
                            if (subnode.getCurrent_node_name().equals(key)) {
                                contains = true;
                                break;
                            }
                        }
                    if (!contains) {
                        nodes temp_node = new nodes();
                        temp_node.setCurrent_node_name(key);
                        temp_node.setCurrent_node_type("Boolean");
                        temp_node.setParent_node_name(jsonobject_name);
                        temp_node.setParent_node_type(node.getCurrent_node_type());
                        node.getSub_nodes().add(temp_node);
                    }
                } else {
                    Log.e("Json Error", "Error: JSON FORMAT EXCEPTION please validate json on www.jsonlint.com");
                }
            }
        } catch (JSONException e) {
            Log.e("Json Error", "Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void parse_this_jsonArray_nodes(String jsonobject_name, JSONArray jsonArray, nodes node) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object jsonObject = jsonArray.get(i);
                if (jsonObject instanceof JSONObject) {
                    parse_this_jsonObject_nodes(jsonobject_name, (JSONObject) jsonObject, node);
                } else if (jsonObject instanceof JSONArray) {
                    parse_this_jsonArray_nodes(jsonobject_name, (JSONArray) jsonObject, node);
                } else {
                    Log.e("Json Error", "Error: JSON FORMAT EXCEPTION please validate json on www.jsonlint.com");
                }

            } catch (JSONException e) {
                Log.e("Json Error", "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    String jsoncode = "";

    public void file(nodes firstchild) {
        if (firstchild.getCurrent_node_type().equals("JSONObject")) {
            jsoncode += get_tabs(2) + "JSONObject " + firstchild.getCurrent_node_name() + " = new JSONObject(jsonstr);\n";
        } else {
            jsoncode += get_tabs(2) + "JSONArray " + firstchild.getCurrent_node_name() + " = new JSONArray(jsonstr);\n";
        }
        for (int i = 0; i < firstchild.getSub_nodes().size(); i++)
            write_to_file(firstchild.getSub_nodes().get(i), 0, 2);
    }

    public String get_tabs(int tabs) {
        String returning = "";
        for (int i = 0; i < tabs; i++)
            returning += "\t";
        return returning;
    }

    Set<String> vriables = new HashSet<>();

    public void write_to_file(nodes node, int level, int tab_level) {
        String objcontcat = "obj_";
        boolean main_json = false;
        if (node.getCurrent_node_name().contains("main"))
            main_json = true;
        if (node.getCurrent_node_type().equals("JSONObject")) {

            String object_name = ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            jsoncode += get_tabs(tab_level) + "JSONObject " + object_name + " = " + node.getParent_node_name() + ".optJSONObject(\"" + node.getCurrent_node_name() + "\");\n";

            jsoncode += get_tabs(tab_level) + "for(int i_" + level + " = 0; i_" + level + " < " + ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name() + ".length(); i_" + level + "++)" + get_tabs(tab_level) + "{\n";
            for (int i = 0; i < node.getSub_nodes().size(); i++)
                write_to_file(node.getSub_nodes().get(i), level + 1, tab_level + 1);
            jsoncode += get_tabs(tab_level) + "}\n";
        }
        if (node.getCurrent_node_type().equals("JSONArray")) {

            String object_name = ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }


            jsoncode += get_tabs(tab_level) + "JSONArray " + object_name + " = " + node.getParent_node_name() + ".optJSONArray(\"" + node.getCurrent_node_name() + "\");\n";

            jsoncode += get_tabs(tab_level) + "for(int i_" + level + " = 0; i_" + level + " < " + ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name() + ".length(); i_" + level + "++)\n" + get_tabs(tab_level) + "{\n";
            for (int i = 0; i < node.getSub_nodes().size(); i++)
                write_to_file(node.getSub_nodes().get(i), level + 1, tab_level + 1);
            jsoncode += "\n" + get_tabs(tab_level) + "}\n";
        }
        if (node.getCurrent_node_type().equals("String")) {
            String object_name = "obj_" + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
//                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            if (!node.getParent_node_type().equals("JSONArray"))
                jsoncode += "\n" + get_tabs(tab_level) + "String " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optString(\"" + node.getCurrent_node_name() + "\",\"\");\n";
            else
                jsoncode += "\n" + get_tabs(tab_level) + "String " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optJSONObject(i_" + (level - 1) + ").optString(\"" + node.getCurrent_node_name() + "\",\"\");\n";
        }
        if (node.getCurrent_node_type().equals("Integer")) {

            String object_name = "obj_" + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
//                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            if (!node.getParent_node_type().equals("JSONArray"))
                jsoncode += "\n" + get_tabs(tab_level) + "Integer " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optInt(\"" + node.getCurrent_node_name() + "\",0);\n";
            else
                jsoncode += "\n" + get_tabs(tab_level) + "Integer " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optJSONObject(i_" + (level - 1) + ").optInt(\"" + node.getCurrent_node_name() + "\",0);\n";
        }
        if (node.getCurrent_node_type().equals("Float")) {
            String object_name = "obj_" + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
//                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            if (!node.getParent_node_type().equals("JSONArray"))
                jsoncode += "\n" + get_tabs(tab_level) + "Float " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optFloat(\"" + node.getCurrent_node_name() + "\",0);\n";
            else
                jsoncode += "\n" + get_tabs(tab_level) + "Float " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optJSONObject(i_" + (level - 1) + ").optFloat(\"" + node.getCurrent_node_name() + "\",0);\n";
        }
        if (node.getCurrent_node_type().equals("Double")) {

            String object_name = ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
//                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            if (!node.getParent_node_type().equals("JSONArray"))
                jsoncode += "\n" + get_tabs(tab_level) + "Double " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optDouble(\"" + node.getCurrent_node_name() + "\",0);\n";
            else
                jsoncode += "\n" + get_tabs(tab_level) + "Double " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optJSONObject(i_" + (level - 1) + ").optDouble(\"" + node.getCurrent_node_name() + "\",0);\n";
        }
        if (node.getCurrent_node_type().equals("Long")) {

            String object_name = ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
//                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            if (!node.getParent_node_type().equals("JSONArray"))
                jsoncode += "\n" + get_tabs(tab_level) + "Long " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optLong(\"" + node.getCurrent_node_name() + "\",0);\n";
            else
                jsoncode += "\n" + get_tabs(tab_level) + "Long " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optJSONObject(i_" + (level - 1) + ").optLong(\"" + node.getCurrent_node_name() + "\",0);\n";
        }
        if (node.getCurrent_node_type().equals("Boolean")) {

            String object_name = ((main_json == true) ? "obj_" : "") + node.getCurrent_node_name();

            int r = 0;
            if (vriables.contains(object_name))
                while (vriables.contains(object_name)) {
                    if (!vriables.contains(object_name + "_" + ++r)) {
                        object_name += "_" + r;
                        vriables.add(object_name);
//                        node.setCurrent_node_name(object_name);
                        break;
                    }
                }
            else {
                vriables.add(object_name);
            }

            if (!node.getParent_node_type().equals("JSONArray"))
                jsoncode += "\n" + get_tabs(tab_level) + "Boolean " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optBoolean(\"" + node.getCurrent_node_name() + "\",\"false\");\n";
            else
                jsoncode += "\n" + get_tabs(tab_level) + "Boolean " + object_name + " = " + ((main_json == true) ? "obj_" : "") + node.getParent_node_name() + ".optJSONObject(i_" + (level - 1) + ").optBoolean(\"" + node.getCurrent_node_name() + "\",false);\n";
        }

    }


    public void generate_file(nodes firstnode, String async_task_name) {
        file(firstnode);

        String format = "public class " + async_task_name + " extends AsyncTask<Void, Void, Void> {\n" +
                "\n" +
                "\n" +
                "\n" +
                "\t@Override\n" +
                "\tprotected Void doInBackground(Void... voids) {\n" + jsoncode + "\n\t\treturn null;\n" +
                "\t}\n" +
                "}";

        try {
            File myFile = new File("/sdcard/myjsonfile.txt");
            if (myFile.exists())
                myFile.delete();
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(format);
            myOutWriter.close();
            fOut.close();
            System.out.printf("format:%s", format);
            ;
            Log.e("File Write Status:", "Sucess");
//                Toast.makeText(getBaseContext(),"Done writing SD 'mysdfile.txt'",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("File Write Status:", "Fail");
            e.printStackTrace();
        }

    }




}
