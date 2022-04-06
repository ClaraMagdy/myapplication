package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Socket client;
    private PrintWriter printwriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private EditText EditText;
    private TextView textView;
    private Button Send;
    private Button Connect;
    private String message;
    private String received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText = (EditText) findViewById(R.id.editext);
        Send = (Button) findViewById(R.id.send);
        Connect = (Button) findViewById(R.id.connect);
        textView = (TextView) findViewById(R.id.textview);
        Send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                message = EditText.getText().toString();
                new Thread(new ClientThread(message)).start();
            }
        });
        Connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new ConnectingThread()).start();
            }
        });
    }
    class ClientThread implements Runnable {
        private final String message;

        ClientThread(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            try {
                printwriter = new PrintWriter(client.getOutputStream(),true);
                printwriter.println(message);
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                received = bufferedReader.readLine();
                textView.setText(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditText.setText("");
                }
            });
        }
    }
    class ConnectingThread implements Runnable {
        @Override
        public void run() {
            try {
                client = new Socket("192.168.1.10", 12346);

            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditText.setText("");
                }
            });
        }
    }
}