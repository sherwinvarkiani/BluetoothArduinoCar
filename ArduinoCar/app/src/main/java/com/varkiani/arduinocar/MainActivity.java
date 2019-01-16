package com.varkiani.arduinocar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button startButton, upButton, downButton, rightButton, leftButton;
    TextView textView;
    private OutputStream outputStream;
    private InputStream inputStream;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upButton = (Button) findViewById(R.id.up);
        downButton = (Button) findViewById(R.id.down);
        leftButton = (Button) findViewById(R.id.left);
        rightButton = (Button) findViewById(R.id.right);
        startButton = (Button) findViewById(R.id.start);

        View.OnClickListener startListener = new View.OnClickListener() {
            public void onClick(android.view.View view) {
                String buttonText = startButton.getText().toString();
                String start = "START";
                String stop = "STOP";
                if (buttonText.equals(start)) {
                    enableButtons(true);
                    startButton.setText("STOP");
                    if (bluetoothSearch() == true) {
                        try {
                            connectBluetooth();
                        } catch (IOException ex) {}
                        //if (connectBluetooth() == true) {
                            //textView.append("Connection Opened!\n");
                            //readData();
                        //}
                    }

                } else if (buttonText.equals(stop)) {
                    enableButtons(false);
                    startButton.setText("START");
                    try {
                        outputStream.close();
                        inputStream.close();
                        socket.close();
                    } catch (IOException e) {

                    }
                }
            }
        };
        startButton.setOnClickListener(startListener);

        //When the up button is being pressed down
        View.OnClickListener upListener = new View.OnClickListener() { //we were experimenting with onclick vs ontouch when the arduino stopped working
            public void onClick(android.view.View view) {

                String direction = "u"; //since we are reading a char in the arduino code we will send the first letter of up down left or right
                //direction.concat("\n");
                try { //sends the up direction to the arduino
                    outputStream.write(direction.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        upButton.setOnClickListener(upListener);




        /*upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //if the button is being pressed down send the instruction to the arduino
                    //upButton.setText("bye");
                    String direction = "u"; //since we are reading a char in the arduino code we will send the first letter of up down left or right
                    direction.concat("\n");
                    try { //sends the up direction to the arduino
                        outputStream.write(direction.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP){ //if the button is released stop sending the instructions
                    //upButton.setText("hi");
                }
                return true;
            }
        });*/

        //When the down button is being held down
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //if the button is being pressed down send the instruction to the arduino
                    String direction = "d"; //since we are reading a char in the arduino code we will send the first letter of up down left or right
                    try { //sends the down direction to the arduino
                        outputStream.write(direction.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    //downButton.setText("hi");
                }
                return true;
            }
        });

        //When the left button is being held down
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //if the button is being pressed down send the instruction to the arduino
                    String direction = "l"; //since we are reading a char in the arduino code we will send the first letter of up down left or right
                    try { //sends the up direction to the arduino
                        outputStream.write(direction.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //leftButton.setText("hi");
                }
                return true;
            }
        });

        //When the right button is being held down
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //if the button is being pressed down send the instruction to the arduino
                    String direction = "r"; //since we are reading a char in the arduino code we will send the first letter of up down left or right
                    try { //sends the up direction to the arduino
                        outputStream.write(direction.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //rightButton.setText("hi");
                }
                return true;
            }
        });

    }

    public void enableButtons (boolean bool) { //function to enable and disable buttons
        upButton.setEnabled(bool);
        downButton.setEnabled(bool);
        leftButton.setEnabled(bool);
        rightButton.setEnabled(bool);
    }

    void connectBluetooth() throws IOException {
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(PORT_UUID); //connects the socket to the bluetooth port address
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {
                //BluetoothAdapter.cancelDiscovery();
                try {
                    socket.connect();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException exception2) {
                        exception2.printStackTrace();
                    }
                }
            }
        });
        connection.start();
        //socket.connect();
        try {
            outputStream = socket.getOutputStream(); //sets up the output
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            inputStream = socket.getInputStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public boolean bluetoothSearch() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) { //if no bluetooth adapter is found display a message saying so
            Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (bluetoothAdapter.isEnabled() == false) { //if bluetooth isn't enabled enable it
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //intent to enable bluetooth adapter
            startActivityForResult(enableAdapter, 0); //request code will be returned at the end of the application
            try {
                Thread.sleep(2000); //gives it time to connect to the bluetooth module
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        Set<BluetoothDevice> connectedDevices = bluetoothAdapter.getBondedDevices();
        if (connectedDevices.isEmpty() == true) { //if there is no device connected
            Toast.makeText(getApplicationContext(), "Please pair a Device to the bluetooth module first", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
