package com.example.baden.HomeAuto;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MQTTService extends Service {

    private static final String TAG = "MQTTService";
    private static boolean hasWifi = false;
    private static boolean hasMmobile = false;
    private Thread thread;
    private ConnectivityManager mConnMan;
    public  static MqttClient mqttClient;
    private String deviceId;

    class MQTTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            IMqttToken token;
            boolean hasConnectivity = false;
            boolean hasChanged = false;
            NetworkInfo infos[] = mConnMan.getAllNetworkInfo();

            for (int i = 0; i < infos.length; i++){
                if (infos[i].getTypeName().equalsIgnoreCase("MOBILE")){
                    if((infos[i].isConnected() != hasMmobile)){
                        hasChanged = true;
                        hasMmobile = infos[i].isConnected();
                    }
                    Log.d(TAG, infos[i].getTypeName() + " is " + infos[i].isConnected());
                } else if ( infos[i].getTypeName().equalsIgnoreCase("WIFI") ){
                    if((infos[i].isConnected() != hasWifi)){
                        hasChanged = true;
                        hasWifi = infos[i].isConnected();
                    }
                    Log.d(TAG, infos[i].getTypeName() + " is " + infos[i].isConnected());
                }
            }

            hasConnectivity = hasMmobile || hasWifi;
            Log.v(TAG, "hasConn: " + hasConnectivity + " hasChange: " + hasChanged + " - "+(mqttClient == null || !mqttClient.isConnected()));
            if (hasConnectivity && hasChanged && (mqttClient == null || !mqttClient.isConnected())) {
                doConnect();
            } else if (!hasConnectivity && mqttClient != null && mqttClient.isConnected()) {
                Log.d(TAG, "doDisconnect()");
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public class MQTTBinder extends Binder {
        public MQTTService getService(){
            return MQTTService.this;
        }
    }

    @Override
    public void onCreate() {
        IntentFilter intentf = new IntentFilter();
        setClientID();
        intentf.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new MQTTBroadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged()");
        android.os.Debug.waitForDebugger();
        super.onConfigurationChanged(newConfig);

    }

    private void setClientID(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        deviceId = wInfo.getMacAddress();
        if(deviceId == null){
            deviceId = MqttAsyncClient.generateClientId();
        }
    }
    public static void PublishMsg(String topic, byte message[]){
        Log.d(TAG, "Publish()");
        MqttConnectOptions options = new MqttConnectOptions();
        MqttMessage message1 = new MqttMessage(message);
        try {
            mqttClient.publish(topic,message1);;
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            switch (e.getReasonCode()) {
                case MqttException.REASON_CODE_BROKER_UNAVAILABLE:
                case MqttException.REASON_CODE_CLIENT_TIMEOUT:
                case MqttException.REASON_CODE_CONNECTION_LOST:
                case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
                    Log.v(TAG, "c" +e.getMessage());
                    e.printStackTrace();
                    break;
                case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
                    Intent i = new Intent("RAISEALLARM");
                    i.putExtra("ALLARM", e);
                    Log.e(TAG, "b"+ e.getMessage());
                    break;
                default:
                    Log.e(TAG, "a" + e.getMessage());
            }
        }
    }

    private void doConnect(){
        Log.d(TAG, "doConnect()");
        MqttConnectOptions options = new MqttConnectOptions();
        //options.setCleanSession(true);
        char pass[] = {101, 101, 83, 112, 55, 52, 100, 88, 89, 82, 112, 83};
        options.setPassword(pass);
        options.setUserName("zhodqirz");
        try {
            mqttClient = new MqttClient("tcp://m21.cloudmqtt.com:11502", "android1", new MemoryPersistence());
            mqttClient.connect(options);
            Toast.makeText(getApplicationContext(), "Connected to broker!", Toast.LENGTH_LONG).show();
            mqttClient.setCallback(new MqttEventCallback());
            mqttClient.subscribe("d/0", 0);
            byte SendBuf[] ={1};
            MQTTService.PublishMsg("c/0", SendBuf);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            switch (e.getReasonCode()) {
                case MqttException.REASON_CODE_BROKER_UNAVAILABLE:

                case MqttException.REASON_CODE_CLIENT_TIMEOUT:
                case MqttException.REASON_CODE_CONNECTION_LOST:
                case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
                    Log.v(TAG, "c" +e.getMessage());
                    e.printStackTrace();
                    break;
                case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
                    Intent i = new Intent("RAISEALLARM");
                    i.putExtra("ALLARM", e);
                    Log.e(TAG, "b"+ e.getMessage());
                    break;
                default:
                    Log.e(TAG, "a" + e.getMessage());
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        return START_STICKY;
    }

    private class MqttEventCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable arg0) {
            doConnect();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }
        @Override

        @SuppressLint("NewApi")
        public void messageArrived(String topic, final MqttMessage msg) throws Exception {
            Handler h = new Handler(getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    /*
                    Intent launchA = new Intent(MQTTService.this, MainActivity.class);
                    launchA.putExtra("message", msg.getPayload());
                    //TODO write somethinkg that has some sense
                    if(Build.VERSION.SDK_INT >= 11){
                        launchA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    } /*else {
        		        launchA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		    }*/
                    /*startActivity(launchA);*/
                    //Toast.makeText(getApplicationContext(), "MQTT Message:\n" + new String(msg.getPayload()), Toast.LENGTH_SHORT).show();
                    //time
                    byte received[] = msg.getPayload();

                    if ((received.length == 17) && (received[0] == 1)) {
                        byte temp1 = DynamicVariables.hours;
                        byte temp2 = DynamicVariables.minutes;
                        byte temp3 = DynamicVariables.seconds;
                        DynamicVariables.hours = received[1];
                        DynamicVariables.minutes = received[2];
                        DynamicVariables.seconds = received[3];
                        if (temp1 != DynamicVariables.hours || temp2 != DynamicVariables.minutes || temp3 != DynamicVariables.seconds) {
                            TabFragment1.updateTime();
                        }
                        //date
                        temp1 = DynamicVariables.day;
                        temp2 = DynamicVariables.month;
                        temp3 = DynamicVariables.year;
                        DynamicVariables.day = received[4];
                        DynamicVariables.month = received[5];
                        DynamicVariables.year = received[6];
                        if (temp1 != DynamicVariables.day || temp2 != DynamicVariables.month || temp3 != DynamicVariables.year) {
                            TabFragment1.updateDate();
                        }
                        //temperature
                        temp1 = DynamicVariables.temperature1;
                        temp2 = DynamicVariables.temperature2;
                        DynamicVariables.temperature1 = received[7];
                        DynamicVariables.temperature2 = received[8];
                        if (temp1 != DynamicVariables.temperature1 || temp2 != DynamicVariables.temperature2) {
                            TabFragment1.updateTemp();
                        }
                        //settings & indicators
                        temp1 = DynamicVariables.WAKEMODE;
                        temp2 = DynamicVariables.BUZZERMODE;
                        temp3 = DynamicVariables.TEMPERATURECONTROL;
                        byte temp8 = DynamicVariables.TIMERCONTROL;
                        byte temp4 = DynamicVariables.WINDOW1;
                        byte temp5 = DynamicVariables.WINDOW2;
                        byte temp6 = DynamicVariables.DOOR;
                        byte temp7 = received[9];

                        //indicators
                        //window1
                        if ((temp7 & 1) == 1) {
                            DynamicVariables.WINDOW1 = 1;
                        } else {
                            DynamicVariables.WINDOW1 = 0;
                        }
                        if (temp4 != DynamicVariables.WINDOW1) {
                            TabFragment1.updateIndicators((byte) 1);
                        }
                        //window2
                        if ((temp7 & 2) == 2) {
                            DynamicVariables.WINDOW2 = 1;
                        } else {
                            DynamicVariables.WINDOW2 = 0;
                        }
                        if (temp5 != DynamicVariables.WINDOW2) {
                            TabFragment1.updateIndicators((byte) 2);
                        }
                        //door
                        if ((temp7 & 4) == 4) {
                            DynamicVariables.DOOR = 1;
                        } else {
                            DynamicVariables.DOOR = 0;
                        }
                        if (temp6 != DynamicVariables.DOOR) {
                            TabFragment1.updateIndicators((byte) 3);
                        }
                        //settings
                        //wakemode set
                        if ((temp7 & 8) == 8) {
                            DynamicVariables.WAKEMODE = 1;
                        } else {
                            DynamicVariables.WAKEMODE = 0;
                        }
                        if (temp1 != DynamicVariables.WAKEMODE) {
                            TabFragment1.updateSettings((byte) 1);
                        }
                        //buzzer set
                        if ((temp7 & 16) == 16) {
                            DynamicVariables.BUZZERMODE = 1;
                        } else {
                            DynamicVariables.BUZZERMODE = 0;
                        }
                        if (temp2 != DynamicVariables.BUZZERMODE) {
                            TabFragment1.updateSettings((byte) 2);
                        }
                        //temperature set
                        if ((temp7 & 32) == 32) {
                            DynamicVariables.TEMPERATURECONTROL = 1;
                        } else {
                            DynamicVariables.TEMPERATURECONTROL = 0;
                        }
                        if (temp3 != DynamicVariables.TEMPERATURECONTROL) {
                            TabFragment1.updateSettings((byte) 3);
                        }
                        //timer set
                        if ((temp7 & 64) == 64) {
                            DynamicVariables.TIMERCONTROL = 1;
                        } else {
                            DynamicVariables.TIMERCONTROL = 0;
                        }
                        if (temp8 != DynamicVariables.TIMERCONTROL) {
                            TabFragment1.updateSettings((byte) 4);
                        }
                        DynamicVariables.LED = received[10];
                        TabFragment1.UpdateLEDs();
                        DynamicVariables.lastsecond = received[11];
                        DynamicVariables.lastminute = received[12];
                        DynamicVariables.lasthour = received[13];
                        DynamicVariables.lastday = received[14];
                        DynamicVariables.lastmonth = received[15];
                        TabFragment1.UpdateMovement();
                        if (received[16] == 1) {
                            Toast.makeText(getApplicationContext(), "Command received!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if ((received.length == 11) && (received[0] == 2)) {
                        TabFragment2.UpdatHints(received);
                        Toast.makeText(getApplicationContext(), "Updated settings", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public String getThread(){
        return Long.valueOf(thread.getId()).toString();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return null;
    }

}