package com.example.baden.HomeAuto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TabFragment2 extends Fragment {
    public static View view;
    public static ImageButton WakeTime;
    public static ImageButton TimeOutput;
    public static ImageButton LightingLimit;
    public static ImageButton TempLimits;
    public static ImageButton Count;
    public static Button RequestUpdate;
    public static EditText countdown,UpTemp,LoTemp,TripVal,HourON,MinuteON,HourOFF,MinuteOFF,WHour,WMinute;
    public static TextView CurrentVal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tab_fragment2 , container, false);

        WakeTime = (ImageButton)view.findViewById(R.id.WakeTimeUpdate);
        WakeTime.setOnClickListener(WakeTimeUpdateListener);

        TimeOutput = (ImageButton)view.findViewById(R.id.TimeOutputUpdate);
        TimeOutput.setOnClickListener(TimeOutputUpdateListener);

        LightingLimit = (ImageButton)view.findViewById(R.id.LightingLimitUpdate);
        LightingLimit.setOnClickListener(LightingLimitUpdateListener);

        TempLimits = (ImageButton)view.findViewById(R.id.TempLimitsUpdate);
        TempLimits.setOnClickListener(TempLimitsUpdateListener);

        Count = (ImageButton)view.findViewById(R.id.CountUpdate);
        Count.setOnClickListener(CountUpdateListener);

        RequestUpdate = (Button)view.findViewById(R.id.RUpdate);
        RequestUpdate.setOnClickListener(RequestUpdateNow);

        countdown = (EditText)view.findViewById(R.id.countdown);
        UpTemp = (EditText)view.findViewById(R.id.UpTemp);
        LoTemp = (EditText)view.findViewById(R.id.LoTemp);
        TripVal = (EditText)view.findViewById(R.id.TripVal);
        CurrentVal = (TextView)view.findViewById(R.id.CurrentVal);
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
            String warning = "Enter a numbers less than 256";
            String newWHour = WHour.getText().toString();
            if(newWHour.matches("")){
                newWHour = WHour.getHint().toString();
            }
            String newWMinute = WMinute.getText().toString();
            if(newWMinute.matches("")){
                newWMinute = WMinute.getHint().toString();
            }
            if((newWHour != WHour.getHint().toString()) || (newWMinute != WMinute.getHint().toString())) {
                try {
                    int checkint1 = Integer.parseInt(newWHour);
                    int checkint2 = Integer.parseInt(newWMinute);
                    if(checkint1 < 24 && checkint2 < 60 && checkint1 > 0 && checkint2 > 0) {
                        byte newWHourByte = (byte) (checkint1);
                        byte newWminuteByte = (byte) (checkint2);
                        byte SendBuf[] = {7, newWHourByte,newWminuteByte};
                        MQTTService.PublishMsg("c/0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Fill in atleast one field");
            }
        }
    };

    public View.OnClickListener TimeOutputUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            String warning = "Enter a numbers less than 256";
            String newHourOn = HourON.getText().toString();
            if(newHourOn.matches("")){
                newHourOn = HourON.getHint().toString();
            }
            String newMinuteOn = MinuteON.getText().toString();
            if(newMinuteOn.matches("")) {
                newMinuteOn = MinuteON.getHint().toString();
            }
            String newHourOff = HourOFF.getText().toString();
            if(newHourOff.matches("")) {
                newHourOff = HourOFF.getHint().toString();
            }
            String newMinuteOff = MinuteOFF.getText().toString();
            if(newMinuteOff.matches("")) {
                newMinuteOff = MinuteOFF.getHint().toString();
            }
            if((newHourOn != HourON.getHint().toString()) || (newMinuteOn != MinuteON.getHint().toString()) || (newHourOff != HourOFF.getHint().toString()) || (newMinuteOff != MinuteOFF.getHint().toString())) {
                try {
                    int checkint1 = Integer.parseInt(newHourOn);
                    int checkint2 = Integer.parseInt(newMinuteOn);
                    int checkint3 = Integer.parseInt(newHourOff);
                    int checkint4 = Integer.parseInt(newMinuteOff);
                    if(checkint1 < 24 && checkint2 < 60 && checkint3 < 24 && checkint4 < 60 && checkint1 > 0 && checkint2 > 0 && checkint3 > 0 && checkint4 > 0) {
                        byte newHourOnByte = (byte) (checkint1);
                        byte newMinuteOnByte = (byte) (checkint2);
                        byte newHourOffByte = (byte) (checkint3);
                        byte newMinuteOffByte = (byte) (checkint4);
                        byte SendBuf[] = {6, newHourOnByte, newMinuteOnByte, newHourOffByte, newMinuteOffByte};
                        MQTTService.PublishMsg("c/0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Fill in atleast one field");
            }
        }
    };

    public View.OnClickListener LightingLimitUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            String warning = "Enter a numbers less than 256";
            String newLimt = TripVal.getText().toString();
            if(newLimt.matches("")){
                newLimt = TripVal.getHint().toString();
            }
            if(newLimt != TripVal.getHint().toString()) {
                try {
                    int checkint = Integer.parseInt(newLimt);
                    if(checkint < 256 && checkint > 0) {
                        byte newTripValByte = (byte) (checkint);
                        byte SendBuf[] = {5, newTripValByte};
                        MQTTService.PublishMsg("c/0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Enter new value");
            }
        }
    };

    public View.OnClickListener TempLimitsUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            String warning = "Enter a numbers less than 256";
            String newUpTemp = UpTemp.getText().toString();
            if(newUpTemp.matches("")){
                newUpTemp = UpTemp.getHint().toString();
            }
            String newLoTemp = LoTemp.getText().toString();
            if(newLoTemp.matches("")){
                newLoTemp = LoTemp.getHint().toString();
            }
            if((newUpTemp != UpTemp.getHint().toString()) || (newLoTemp != LoTemp.getHint().toString())) {
                try {
                    int checkint1 = Integer.parseInt(newUpTemp);
                    int checkint2 = Integer.parseInt(newLoTemp);
                    if(checkint1 < 256 && checkint2 < 256 && checkint1 > 0 && checkint2 > 0) {
                        byte newUpTempByte = (byte) (checkint1);
                        byte newLoTempByte = (byte) (checkint2);
                        byte SendBuf[] = {4, newUpTempByte,newLoTempByte};
                        MQTTService.PublishMsg("c/0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Fill in atleast one field");
            }
        }
    };

    public View.OnClickListener CountUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            String warning = "Enter a numbers less than 256";
            String newCount = countdown.getText().toString();
            if(newCount.matches("")){
                newCount = countdown.getHint().toString();
            }
            if(newCount != countdown.getHint().toString()) {
                try {
                    int checkint = Integer.parseInt(newCount);
                    if(checkint < 256 && checkint > 0) {
                        byte newCountByte = (byte) (checkint);
                        byte SendBuf[] = {3, newCountByte};
                        MQTTService.PublishMsg("c/0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Enter new value");
            }
        }
    };

    private void makeToast(String ToastText){
        Toast.makeText(getActivity(), ToastText, Toast.LENGTH_SHORT).show();
    }

    public View.OnClickListener RequestUpdateNow = new View.OnClickListener(){
        public void onClick(View v) {
            byte SendBuf[] ={2};
            MQTTService.PublishMsg("c/0", SendBuf);
        }
    };

    public static void UpdatHints(byte[] values){
        try {
            countdown = (EditText) view.findViewById(R.id.countdown);
            countdown.setHint(Integer.toString(unsignedToBytes(values[1])));
            UpTemp = (EditText) view.findViewById(R.id.UpTemp);
            UpTemp.setHint(Integer.toString(unsignedToBytes(values[2])));
            LoTemp = (EditText) view.findViewById(R.id.LoTemp);
            LoTemp.setHint(Integer.toString(unsignedToBytes(values[3])));
            TripVal = (EditText) view.findViewById(R.id.TripVal);
            TripVal.setHint(Integer.toString(unsignedToBytes(values[4])));
            HourON = (EditText) view.findViewById(R.id.HourON);
            HourON.setHint(Integer.toString(unsignedToBytes(values[5])));
            MinuteON = (EditText) view.findViewById(R.id.MinuteON);
            MinuteON.setHint(Integer.toString(unsignedToBytes(values[6])));
            HourOFF = (EditText) view.findViewById(R.id.HourOFF);
            HourOFF.setHint(Integer.toString(unsignedToBytes(values[7])));
            MinuteOFF = (EditText) view.findViewById(R.id.MinuteOFF);
            MinuteOFF.setHint(Integer.toString(unsignedToBytes(values[8])));
            WHour = (EditText) view.findViewById(R.id.WHour);
            WHour.setHint(Integer.toString(unsignedToBytes(values[9])));
            WMinute = (EditText) view.findViewById(R.id.WMinute);
            WMinute.setHint(Integer.toString(unsignedToBytes(values[10])));
        }catch(Exception e){
            //do nothing
        }
    };
    public static void UpdateLghtVal(int lightval){
        try {
            CurrentVal.setText(Integer.toString(lightval));
        }catch(Exception e){

        }
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }
}