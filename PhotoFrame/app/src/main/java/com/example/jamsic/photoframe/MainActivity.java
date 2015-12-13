package com.example.jamsic.photoframe;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText insertCode = (EditText) findViewById(R.id.editText);
        final Button getCodeButton = (Button) findViewById(R.id.getcode_button);
        final Button submitCodeButton = (Button) findViewById(R.id.submitcode_button);
        final TextView answerView = (TextView) findViewById(R.id.answer_view);

        getCodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCode();
            }
        });

        submitCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSubmittedCode(answerView, insertCode);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getCode() {
        String url = "https://oauth.yandex.ru/authorize?response_type=code&client_id=3b983eab2470421bb5247159f6b3c2a8";
        Uri address = Uri.parse(url);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);
        //openlink.getData();
    }

    public void sendSubmittedCode(TextView answerView, EditText insertCode) {
        String val = insertCode.getText().toString();

        // Set desired text in answerView TextView
        //answerView.setText("The answer to life, the universe and everything is:\n\n" + val);
        getToken(val, answerView);

    }

    public void getToken(String code, TextView answerView)  {
        RetrieveFeedTask f = new RetrieveFeedTask();
        f.execute(code);
        String res = "";
        try {
            res = f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        answerView.setText(res);
    }

    public void getToken2(String code, TextView answerView) {

    }
}

class RetrieveFeedTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String code = params[0];
        ///*
        String address = "https://oauth.yandex.ru/token";
        ///*
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(address);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        //pairs.add(new BasicNameValuePair("HOST", "oauth.yandex.ru"));
        pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
        pairs.add(new BasicNameValuePair("code", code));
        pairs.add(new BasicNameValuePair("client_id", "3b983eab2470421bb5247159f6b3c2a8"));
        pairs.add(new BasicNameValuePair("client_secret", "a1faa2ef2df64fd7b6f6344834c0e220"));
        //String PostData = k1+"=authorization_code&code=" + code + "client_id=3b983eab2470421bb5247159f6b3c2a8" +
        //        "&client_secret=a1faa2ef2df64fd7b6f6344834c0e220";
        post.addHeader("HOST", "https://oauth.yandex.ru");
        //post.setHeader("Content-Length", new Integer(PostData.length()).toString());
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        String text = "response";
        UrlEncodedFormEntity u = null;
        try {
            u = new UrlEncodedFormEntity(pairs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long s = u.getContentLength();
        //post.setHeader("Content-Length", String.valueOf(s));
        post.setEntity(u);
        HttpResponse response = null;
        try {
            response = client.execute(post);
            //response.
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
            text = code + "   " + response.toString() + "successfulresponse" + result;
        } catch (IOException e) {
            e.printStackTrace();
            text = "client.execute(post)";
            //text += e.toString();
            text += e.getStackTrace().toString();
            text += e.getMessage();
        }
        //response = client.execute(post);
        //text = response.toString();

        //answerView.setText("response");
        //text = "response";
        return text;
        //*/

        /*
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)new URL(address).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
// Http Method becomes POST
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("HOST", "https://oauth.yandex.ru");

// Encode according to application/x-www-form-urlencoded specification
        String content =
                "grant_type=" + URLEncoder.encode("authorization_code") +
                        "&code=" + URLEncoder.encode (code) +
                        "&client_id=" + URLEncoder.encode ("3b983eab2470421bb5247159f6b3c2a8") +
                        "&client_secret=" + URLEncoder.encode ("a1faa2ef2df64fd7b6f6344834c0e220");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

// Try this should be the length of you content.
// it is not neccessary equal to 48.
// content.getBytes().length is not neccessarily equal to content.length() if the String contains non ASCII characters.
        connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));

        String text = code + "   ";

        // Write body
        try {
            /*
            OutputStream output = connection.getOutputStream();
            output.write(content.getBytes());
            output.close();
            */

        /*
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (content);
            wr.flush();
            wr.close();

            //InputStream in = connection.getInputStream();
            //in.read();
            //connection.getResponseCode();
            //text = output.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        try {
            //connection.connect();
            //connection.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        //Get Response
        InputStream is = null;
        try {
            is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            text = response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

        //return text;

        //InputStream in = connection.getInputStream();
        //in.read()
        //in.write(content.getBytes());
        //output.close();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // TODO: check this.exception
        // TODO: do something with the feed
    }

}

