package com.example.baden.HomeAuto;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


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

    public static TextView Reconnects;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        creatItems();

        zerovariables();

        setupbutton();

        return view;
    }

    void creatItems(){
        TEMPERATURE = (TextView) view.findViewById(R.id.Temp);
        DATE = (TextView) view.findViewById(R.id.Date);
        TIME = (TextView) view.findViewById(R.id.Time);

        Wakemode = (ImageButton)view.findViewById(R.id.wakemode);
        Buzzer = (ImageButton)view.findViewById(R.id.buzzer);
        Temperature = (ImageButton)view.findViewById(R.id.tempControl);
        Timer = (ImageButton)view.findViewById(R.id.TimerControl);

        WINDOW1 = (ImageView) view.findViewById(R.id.window1);
        WINDOW2 = (ImageView) view.findViewById(R.id.window2);
        DOOR = (ImageView) view.findViewById(R.id.door);

        LED1 = (ImageButton)view.findViewById(R.id.LED1);
        LED2 = (ImageButton)view.findViewById(R.id.LED2);
        LED3 = (ImageButton)view.findViewById(R.id.LED3);
        LED4 = (ImageButton)view.findViewById(R.id.LED4);
        LED5 = (ImageButton)view.findViewById(R.id.LED5);
        LED6 = (ImageButton)view.findViewById(R.id.LED6);

        LastMove = (TextView) view.findViewById(R.id.LastMove);

        Reconnects = (TextView) view.findViewById(R.id.Reconnects);
    }

    void zerovariables(){
        DynamicVariables.BUZZERMODE = 0;
        DynamicVariables.DOOR = 0;
        DynamicVariables.TEMPERATURECONTROL = 0;
        DynamicVariables.TIMERCONTROL = 0;
        DynamicVariables.WAKEMODE = 0;
        DynamicVariables.WINDOW1 = 0;
        DynamicVariables.WINDOW2 = 0;
    }

    void setupbutton(){
        Wakemode.setOnClickListener(WakeModeListener);
        Buzzer.setOnClickListener(BuzzerListen);
        Temperature.setOnClickListener(TemperatureListener);

        Timer.setOnClickListener(TimerListener);;
        LED1.setOnClickListener(LED1Listener);
        LED2.setOnClickListener(LED2Listener);
        LED3.setOnClickListener(LED3Listener);
        LED4.setOnClickListener(LED4Listener);
        LED5.setOnClickListener(LED5Listener);
        LED6.setOnClickListener(LED6Listener);
    }

    public View.OnClickListener WakeModeListener = new View.OnClickListener(){
        public void onClick(View v) {
            byte SendBuf[] = {0, 0};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener BuzzerListen = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {0, 1};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener TimerListener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {0, 2};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener TemperatureListener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {0,  3};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener LED1Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 32};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener LED2Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 16};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener LED3Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 8};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener LED4Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 4};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener LED5Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 2};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public View.OnClickListener LED6Listener = new View.OnClickListener() {
        public void onClick(View v) {
            byte SendBuf[] = {1, 1};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };
    public static void UpdateView(byte values[]){
        try {
            String TempTime = printtwodigits(values[1]) + ":" + printtwodigits(values[2]) + ":" + printtwodigits(values[3]);
            TIME.setText(TempTime);

            String TempDate = printtwodigits(values[4]) + "/" + printtwodigits(values[5]) + "/20" + printtwodigits(values[6]);
            DATE.setText(TempDate);

            String TempTemperature = printtwodigits(values[7]) + "." + printtwodigits(values[8])
                    + "°C";
            TEMPERATURE.setText(TempTemperature);
            byte Indicator = values[9];
            if ((Indicator & 1) == 1 && DynamicVariables.WINDOW1 == 0) {
                DynamicVariables.WINDOW1 = 1;
                WINDOW1.setImageResource(R.drawable.windowopen);
            } else if((Indicator & 1) == 0 && DynamicVariables.WINDOW1 == 1){
                DynamicVariables.WINDOW1 = 0;
                WINDOW1.setImageResource(R.drawable.windowdefault);
            }
            //window2
            if ((Indicator & 2) == 2 && DynamicVariables.WINDOW2 == 0) {
                DynamicVariables.WINDOW2 = 1;
                WINDOW2.setImageResource(R.drawable.windowopen);
            } else if((Indicator & 2) == 0 && DynamicVariables.WINDOW2 == 1){
                DynamicVariables.WINDOW2 = 0;
                WINDOW2.setImageResource(R.drawable.windowopen);
            }
            //door
            if ((Indicator & 4) == 4 && DynamicVariables.DOOR == 0) {
                DynamicVariables.DOOR = 1;
                DOOR.setImageResource(R.drawable.dooropen);
            } else if((Indicator & 4) == 4 && DynamicVariables.DOOR == 1){
                DynamicVariables.DOOR = 0;
                DOOR.setImageResource(R.drawable.doordefault);
            }
            //settings
            //wakemode set
            if ((Indicator & 8) == 8 && DynamicVariables.WAKEMODE == 0) {
                DynamicVariables.WAKEMODE = 1;
                Wakemode.setImageResource(R.drawable.sunpressed);
                MainActivity.ChangeBackground((byte) 1);
            } else if((Indicator & 8) == 0 && DynamicVariables.WAKEMODE == 1){
                DynamicVariables.WAKEMODE = 0;
                Wakemode.setImageResource(R.drawable.sundefault);
                MainActivity.ChangeBackground((byte) 0);
            }
            //buzzer set
            if ((Indicator & 16) == 16 && DynamicVariables.BUZZERMODE == 0) {
                DynamicVariables.BUZZERMODE = 1;
                Buzzer.setImageResource(R.drawable.buzzerpressed);
            } else if((Indicator & 16) == 0 && DynamicVariables.BUZZERMODE == 1){
                DynamicVariables.BUZZERMODE = 0;
                Buzzer.setImageResource(R.drawable.buzzerdefault);
            }
            //temperature set
            if ((Indicator & 32) == 32 && DynamicVariables.TEMPERATURECONTROL == 0) {
                DynamicVariables.TEMPERATURECONTROL = 1;
                Temperature.setImageResource(R.drawable.temperaturepressed);
            } else if((Indicator & 32) == 0 && DynamicVariables.TEMPERATURECONTROL == 1) {
                DynamicVariables.TEMPERATURECONTROL = 0;
                Temperature.setImageResource(R.drawable.temperaturedefault);
            }
            //timer set
            if ((Indicator & 64) == 64 && DynamicVariables.TIMERCONTROL == 0) {
                DynamicVariables.TIMERCONTROL = 1;
                Timer.setImageResource(R.drawable.timerpressed);
            } else if((Indicator & 64) == 0 && DynamicVariables.TIMERCONTROL == 1) {
                DynamicVariables.TIMERCONTROL = 0;
                Timer.setImageResource(R.drawable.timerdefault);
            }
            byte LEDS = values[10];
            if((DynamicVariables.LED & 32) == 0 && (LEDS & 32) == 32){
                LED1.setImageResource(R.drawable.ledon);
            }else if((DynamicVariables.LED & 32) == 32 && (LEDS & 32) == 0){
                LED1.setImageResource(R.drawable.leddefault);
            }
            if((DynamicVariables.LED & 16) == 0 && (LEDS & 16) == 16){
                LED2.setImageResource(R.drawable.ledon);
            }else if((DynamicVariables.LED & 16) == 16 && (LEDS & 16) == 0){
                LED2.setImageResource(R.drawable.leddefault);
            }
            if((DynamicVariables.LED & 8) == 0 && (LEDS & 8) == 8){
                LED3.setImageResource(R.drawable.ledon);
            }else if((DynamicVariables.LED & 8) == 8 && (LEDS & 8) == 0){
                LED3.setImageResource(R.drawable.leddefault);
            }
            if((DynamicVariables.LED & 4) == 0 && (LEDS & 4) == 4){
                LED4.setImageResource(R.drawable.ledon);
            }else if((DynamicVariables.LED & 4) == 4 && (LEDS & 4) == 0){
                LED4.setImageResource(R.drawable.leddefault);
            }
            if((DynamicVariables.LED & 2) == 0 && (LEDS & 2) == 2) {
                LED5.setImageResource(R.drawable.ledon);
            }else if((DynamicVariables.LED & 2) == 2 && (LEDS & 2) == 0){
                LED5.setImageResource(R.drawable.leddefault);
            }
            if((DynamicVariables.LED & 1) == 0 && (LEDS & 1) == 1){
                LED6.setImageResource(R.drawable.ledon);
            }else if((DynamicVariables.LED & 1) == 1 && (LEDS & 1) == 0){
                LED6.setImageResource(R.drawable.leddefault);
            }
            DynamicVariables.LED = LEDS;

            String LastMovement = "Last Movement: " + printtwodigits(values[13]) + ":"
                    + printtwodigits(values[12]) + ":" + printtwodigits(values[11])
                    + " on " + printtwodigits(values[14]) + "/" + printtwodigits(values[15]);
            LastMove.setText(LastMovement);

            String Reconnected = "Device has Reconnected " + Integer.toString((values[19]&0xFF)) + " times";
            Reconnects.setText(Reconnected);
        }catch(Exception e){
            //do nothing
        }
    }

    static String printtwodigits(byte digit){
        String output = "";
        if(digit < 10){
            output += "0";
        }
        output += Byte.toString(digit);
        return output;
    }
}

