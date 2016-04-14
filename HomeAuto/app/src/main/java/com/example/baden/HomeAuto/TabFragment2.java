package com.example.baden.HomeAuto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class TabFragment2 extends Fragment {
    public static View view;
    public static ImageButton WakeTime;
    public static ImageButton TimeOutput;
    public static ImageButton LightingLimit;
    public static ImageButton TempLimits;
    public static ImageButton Count;
    public static Button RequestUpdate;
    public static EditText countdown,UpTemp,LoTemp,TripVal,CurrentVal,HourON,MinuteON,HourOFF,MinuteOFF,WHour,WMinute;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tab_fragment2 , container, false);

        WakeTime = (ImageButton)view.findViewById(R.id.WakeTimeUpdate);
        WakeTime.setOnClickListener(WakeTimeUpdateListener);

        TimeOutput = (ImageButton)view.findViewById(R.id.WakeTimeUpdate);
        TimeOutput.setOnClickListener(TimeOutputUpdateListener);

        LightingLimit = (ImageButton)view.findViewById(R.id.WakeTimeUpdate);
        LightingLimit.setOnClickListener(LightingLimitUpdateListener);

        TempLimits = (ImageButton)view.findViewById(R.id.WakeTimeUpdate);
        TempLimits.setOnClickListener(TempLimitsUpdateListener);

        Count = (ImageButton)view.findViewById(R.id.WakeTimeUpdate);
        Count.setOnClickListener(CountUpdateListener);

        RequestUpdate = (Button)view.findViewById(R.id.RUpdate);
        RequestUpdate.setOnClickListener(RequestUpdateNow);

        countdown = (EditText)view.findViewById(R.id.countdown);
        UpTemp = (EditText)view.findViewById(R.id.UpTemp);
        LoTemp = (EditText)view.findViewById(R.id.LoTemp);
        TripVal = (EditText)view.findViewById(R.id.TripVal);
        CurrentVal = (EditText)view.findViewById(R.id.CurrentVal);
        HourON = (EditText)view.findViewById(R.id.HourON);
        MinuteON = (EditText)view.findViewById(R.id.MinuteON);
        HourOFF = (EditText)view.findViewById(R.id.HourOFF);
        MinuteOFF = (EditText)view.findViewById(R.id.MinuteOFF);
        WHour = (EditText)view.findViewById(R.id.WHour);
        WMinute = (EditText)view.findViewById(R.id.WMinute);

        return view;
    }

    public View.OnClickListener WakeTimeUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            byte SendBuf[] = {2};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };

    public View.OnClickListener TimeOutputUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
        }
    };

    public View.OnClickListener LightingLimitUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
        }
    };

    public View.OnClickListener TempLimitsUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
        }
    };

    public View.OnClickListener CountUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
        }
    };

    public View.OnClickListener RequestUpdateNow = new View.OnClickListener(){
        public void onClick(View v) {
            byte SendBuf[] ={2};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };

    public static void UpdatHints(byte[] values){
        countdown = (EditText)view.findViewById(R.id.countdown);
        countdown.setHint(Integer.toString(unsignedToBytes(values[1])));
        UpTemp = (EditText)view.findViewById(R.id.UpTemp);
        UpTemp.setHint(Integer.toString(unsignedToBytes(values[2])));
        LoTemp = (EditText)view.findViewById(R.id.LoTemp);
        LoTemp.setHint(Integer.toString(unsignedToBytes(values[3])));
        TripVal = (EditText)view.findViewById(R.id.TripVal);
        TripVal.setHint(Integer.toString(unsignedToBytes(values[4])));
        HourON = (EditText)view.findViewById(R.id.HourON);
        HourON.setHint(Integer.toString(unsignedToBytes(values[5])));
        MinuteON = (EditText)view.findViewById(R.id.MinuteON);
        MinuteON.setHint(Integer.toString(unsignedToBytes(values[6])));
        HourOFF = (EditText)view.findViewById(R.id.HourOFF);
        HourOFF.setHint(Integer.toString(unsignedToBytes(values[7])));
        MinuteOFF = (EditText)view.findViewById(R.id.MinuteOFF);
        MinuteOFF.setHint(Integer.toString(unsignedToBytes(values[8])));
        WHour = (EditText)view.findViewById(R.id.WHour);
        WHour.setHint(Integer.toString(unsignedToBytes(values[9])));
        WMinute = (EditText)view.findViewById(R.id.WMinute);
        WMinute.setHint(Integer.toString(unsignedToBytes(values[10])));
    };
    public static void UpdateLghtVal(int lightval){
        CurrentVal.setHint(Integer.toString(lightval));
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }
}