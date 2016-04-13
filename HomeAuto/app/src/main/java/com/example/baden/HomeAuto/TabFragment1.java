package com.example.baden.HomeAuto;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

import java.sql.Time;


public class TabFragment1 extends Fragment {
    private static final String TAG = "FRAGEMNT 1";
    public static View view;
    public static ImageButton Wakemode;
    public static ImageButton Buzzer;
    public static ImageButton Temperature;
    public static ImageButton Timer;
    public static TextView TIME;
    public static TextView DATE;
    public static TextView TEMPERATURE;
    public static ImageView WINDOW1;
    public static ImageView WINDOW2;
    public static ImageView DOOR;
    public static ImageButton LED1;
    public static ImageButton LED2;
    public static ImageButton LED3;
    public static ImageButton LED4;
    public static ImageButton LED5;
    public static ImageButton LED6;
    public static TextView LastMove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        Wakemode = (ImageButton)view.findViewById(R.id.wakemode);
        Wakemode.setOnClickListener(WakeModeListener);

        Buzzer = (ImageButton)view.findViewById(R.id.buzzer);
        Buzzer.setOnClickListener(BuzzerListen);


        Temperature = (ImageButton)view.findViewById(R.id.tempControl);
        Temperature.setOnClickListener(TemperatureListener);

        Timer = (ImageButton)view.findViewById(R.id.TimerControl);
        Timer.setOnClickListener(TimerListener);

        LED1 = (ImageButton)view.findViewById(R.id.LED1);
        LED1.setOnClickListener(LED1Listener);

        LED2 = (ImageButton)view.findViewById(R.id.LED2);
        LED2.setOnClickListener(LED2Listener);

        LED3 = (ImageButton)view.findViewById(R.id.LED3);
        LED3.setOnClickListener(LED3Listener);

        LED4 = (ImageButton)view.findViewById(R.id.LED4);
        LED4.setOnClickListener(LED4Listener);

        LED5 = (ImageButton)view.findViewById(R.id.LED5);
        LED5.setOnClickListener(LED5Listener);

        LED6 = (ImageButton)view.findViewById(R.id.LED6);
        LED6.setOnClickListener(LED6Listener);


        return view;
    }

    public View.OnClickListener WakeModeListener = new View.OnClickListener(){
        public void onClick(View v) {
            byte SendBuf[] = {0, 0};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener BuzzerListen = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {0, 1};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener TimerListener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {0, 2};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener TemperatureListener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {0,  3};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener LED1Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 32};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener LED2Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 16};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener LED3Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 8};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener LED4Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 4};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener LED5Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 2};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };
    public View.OnClickListener LED6Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 1};
            MQTTService.PublishMsg("c/0",SendBuf);
        }
    };

    //update the time indicator of the fragment
    public static void updateTime (){
        TIME = (TextView) view.findViewById(R.id.Time);
        String TempTime = printtwodigits(DynamicVariables.hours) + ":" + printtwodigits(DynamicVariables.minutes) + ":" + printtwodigits(DynamicVariables.seconds);
        TIME.setText(TempTime);
    }
    //update the date indicator of the fragment
    public static void updateDate (){
        DATE = (TextView) view.findViewById(R.id.Date);
        String TempDate = printtwodigits(DynamicVariables.day) + "/" + printtwodigits(DynamicVariables.month) + "/20" + printtwodigits(DynamicVariables.year);
        DATE.setText(TempDate);
    }

    //update the temperature indicator on the fragment
    public static void updateTemp(){
        TEMPERATURE = (TextView) view.findViewById(R.id.Temp);
        String TempTemperature = printtwodigits(DynamicVariables.temperature1) + "." + printtwodigits(DynamicVariables.temperature2)
                + "°C";
        TEMPERATURE.setText(TempTemperature);
    }

    //set settings indicators
    public static void updateSettings(byte noCase){
        switch(noCase){
            case 1:
                if(DynamicVariables.WAKEMODE == 1){
                    Wakemode.setImageResource(R.drawable.sunpressed);
                    MainActivity.ChangeBackground((byte)1);
                }else{
                    Wakemode.setImageResource(R.drawable.sundefault);
                    MainActivity.ChangeBackground((byte)0);
                }
                break;
            case 2:
                if(DynamicVariables.BUZZERMODE == 1){
                    Buzzer.setImageResource(R.drawable.buzzerpressed);
                }else{
                    Buzzer.setImageResource(R.drawable.buzzerdefault);
                }
                break;
            case 3:
                if(DynamicVariables.TEMPERATURECONTROL == 1){
                    Temperature.setImageResource(R.drawable.temperaturepressed);
                }else{
                    Temperature.setImageResource(R.drawable.temperaturedefault);
                }
                break;
            case 4:
                if(DynamicVariables.TIMERCONTROL == 1){
                    Timer.setImageResource(R.drawable.timerpressed);
                }else{
                    Timer.setImageResource(R.drawable.timerdefault);
                }
                break;
            default:
                break;
        }
    }

    public static void updateIndicators(byte noCase){
        switch(noCase){
            case 1:
                WINDOW1 = (ImageView) view.findViewById(R.id.window1);
                if(DynamicVariables.WINDOW1 == 1){
                    WINDOW1.setImageResource(R.drawable.windowopen);
                }else{
                    WINDOW1.setImageResource(R.drawable.windowdefault);
                }
                break;
            case 2:
                WINDOW2 = (ImageView) view.findViewById(R.id.window2);
                if(DynamicVariables.WINDOW2 == 1){
                    WINDOW2.setImageResource(R.drawable.windowopen);
                }else{
                    WINDOW2.setImageResource(R.drawable.windowdefault);
                }
                break;
            case 3:
                DOOR = (ImageView) view.findViewById(R.id.door);
                if(DynamicVariables.DOOR == 1){
                    DOOR.setImageResource(R.drawable.dooropen);
                }else{
                    DOOR.setImageResource(R.drawable.doordefault);
                }
                break;

            default:
                break;
        }
    }

    public static void UpdateLEDs(){
        LED1 = (ImageButton) view.findViewById(R.id.LED1);
        LED2 = (ImageButton) view.findViewById(R.id.LED2);
        LED3 = (ImageButton) view.findViewById(R.id.LED3);
        LED4 = (ImageButton) view.findViewById(R.id.LED4);
        LED5 = (ImageButton) view.findViewById(R.id.LED5);
        LED6 = (ImageButton) view.findViewById(R.id.LED6);
        if((DynamicVariables.LED & 32) == 32){
            LED1.setImageResource(R.drawable.ledon);
        }else LED1.setImageResource(R.drawable.leddefault);
        if((DynamicVariables.LED & 16) == 16){
            LED2.setImageResource(R.drawable.ledon);
        }else LED2.setImageResource(R.drawable.leddefault);
        if((DynamicVariables.LED & 8) == 8){
            LED3.setImageResource(R.drawable.ledon);
        }else LED3.setImageResource(R.drawable.leddefault);
        if((DynamicVariables.LED & 4) == 4){
            LED4.setImageResource(R.drawable.ledon);
        }else LED4.setImageResource(R.drawable.leddefault);
        if((DynamicVariables.LED & 2) == 2) {
            LED5.setImageResource(R.drawable.ledon);
        }else LED5.setImageResource(R.drawable.leddefault);
        if((DynamicVariables.LED & 1) == 1){
            LED6.setImageResource(R.drawable.ledon);
        }else LED6.setImageResource(R.drawable.leddefault);
    }

    public static void UpdateMovement(){
        LastMove = (TextView) view.findViewById(R.id.LastMove);
        String LastMovement = "Last Movement: " + printtwodigits(DynamicVariables.lasthour) + ":"
                + printtwodigits(DynamicVariables.lastminute) + ":" + printtwodigits(DynamicVariables.lastsecond)
                + " on " + printtwodigits(DynamicVariables.lastday) + "/" + printtwodigits(DynamicVariables.lastmonth);
        LastMove.setText(LastMovement);
    }

    static String printtwodigits(byte digit){
        String output = "";
        if(digit < 10){
            output += "0";
        }
        output += Integer.toString(digit);
        return output;
    }
}

