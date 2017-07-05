package com.example.baden.HomeAuto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TabFragment3 extends Fragment {
    public static View view;
    public static ImageButton KeepToggle;
    public static ImageButton RelaySwitch;
    public static ImageButton UpdateTempLimit;
    public static ImageButton OnTimeUpdate;
    public static EditText TempLimit,WHour,WMinute,WSecond;
    public static TextView TIME,Temp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tab_fragment3 , container, false);

        KeepToggle = (ImageButton)view.findViewById(R.id.KeepToggle);
        KeepToggle.setOnClickListener(KeepToggleUpdateListener);

        RelaySwitch = (ImageButton)view.findViewById(R.id.RelaySwitch);
        RelaySwitch.setOnClickListener(RelaySwitchUpdateListener);
        RelaySwitch.setOnLongClickListener(LongRelaySwitchUpdateListener);

        UpdateTempLimit = (ImageButton)view.findViewById(R.id.UpdateTempLimit);
        UpdateTempLimit.setOnClickListener(UpdateTempLimitUpdateListener);

        OnTimeUpdate = (ImageButton)view.findViewById(R.id.OnTimeUpdate);
        OnTimeUpdate.setOnClickListener(OnTimeUpdateListener);

        TempLimit = (EditText)view.findViewById(R.id.TempLimit);
        WHour = (EditText)view.findViewById(R.id.WHour);
        WMinute = (EditText)view.findViewById(R.id.WMinute);
        WSecond = (EditText)view.findViewById(R.id.WSecond);

        TIME = (TextView) view.findViewById(R.id.Time1);
        Temp = (TextView) view.findViewById(R.id.Temp);

        return view;
    }

    public View.OnClickListener KeepToggleUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            try {
                byte newTempLimit;
                if(DynamicVariables.KEEP != 0) newTempLimit = 0;
                else newTempLimit = 2;
                byte SendBuf[] = {3, newTempLimit};
                MQTTService.PublishMsg("s/kettle0", SendBuf);
            }catch(Exception e){
            }
        }
    };

    public View.OnClickListener RelaySwitchUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            byte SendBuf[] = {1};
            if(DynamicVariables.RELAY != 0) SendBuf[0] = 0;
            MQTTService.PublishMsg("s/kettle0", SendBuf);
        }
    };

    public View.OnLongClickListener LongRelaySwitchUpdateListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            byte SendBuf[] = {2};
            MQTTService.PublishMsg("s/kettle0", SendBuf);
            return true;
        }
    };

    public View.OnClickListener UpdateTempLimitUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            String warning = "Enter a valid number";
            String newWHour = TempLimit.getText().toString();
            if(newWHour.matches("")){
                newWHour = TempLimit.getHint().toString();
            }
            if(newWHour != TempLimit.getHint().toString()) {
                try {
                    int checkint1 = Integer.parseInt(newWHour);
                    if(checkint1 < 120 && checkint1 > 20 ) {
                        byte newTempLimit = (byte) (checkint1 & 0xFF);
                        byte SendBuf[] = {5, newTempLimit};
                        MQTTService.PublishMsg("s/kettle0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Fill in the field");
            }
            TempLimit.setText("");
        }
    };

    public View.OnClickListener OnTimeUpdateListener = new View.OnClickListener(){
        public void onClick(View v) {
            String warning = "Enter a valid number";
            String newWHour = WHour.getText().toString();
            if(newWHour.matches("")){
                newWHour = WHour.getHint().toString();
            }
            String newWMinute = WMinute.getText().toString();
            if(newWMinute.matches("")){
                newWMinute = WMinute.getHint().toString();
            }
            String newWSecond = WSecond.getText().toString();
            if(newWSecond.matches("")){
                newWSecond = WSecond.getHint().toString();
            }
            if((newWHour != WHour.getHint().toString()) || (newWMinute != WMinute.getHint().toString()) || (newWSecond != WSecond.getHint().toString())) {
                try {
                    int checkint1 = Integer.parseInt(newWHour);
                    int checkint2 = Integer.parseInt(newWMinute);
                    int checkint3 = Integer.parseInt(newWSecond);
                    if(checkint1 < 24 && checkint2 < 60 && checkint3 < 60 && checkint1 > -1 && checkint2 > -1 && checkint3 > -1) {
                        byte newWHourByte = (byte) (checkint1 & 0xFF);
                        byte newWminuteByte = (byte) (checkint2 & 0xFF);
                        byte newWsecondByte = (byte) (checkint3 & 0xFF);
                        byte SendBuf[] = {4, newWHourByte,newWminuteByte,newWsecondByte};
                        MQTTService.PublishMsg("s/kettle0", SendBuf);
                    }else{
                        makeToast(warning);
                    }
                }catch(Exception e){
                    makeToast(warning);
                }
            } else {
                makeToast("Fill in atleast one field");
            }
            WHour.setText("");
            WMinute.setText("");
            WSecond.setText("");
        }
    };

    private void makeToast(String ToastText){
        Toast.makeText(getActivity(), ToastText, Toast.LENGTH_SHORT).show();
    }

    public static void UpdateView(byte[] values){
        try {
            String TempTime = printtwodigits(values[2]) + ":" + printtwodigits(values[3]) + ":" + printtwodigits(values[4]);
            TIME.setText(TempTime);

            String TempTemperature = printtwodigits(values[5]) + "." + printtwodigits(values[6])
                    + "°C";
            Temp.setText(TempTemperature);

            if(values[7] == 1){
                RelaySwitch.setImageResource(R.drawable.ledon);
                DynamicVariables.RELAY = 1;
            }else if(values[7]  == 0){
                RelaySwitch.setImageResource(R.drawable.leddefault);
                DynamicVariables.RELAY = 0;
            }

            TempLimit.setHint(Integer.toString(values[8]));

            if(values[9] == 1){
                KeepToggle.setImageResource(R.drawable.ledon);
                DynamicVariables.KEEP = 1;
            }else if(values[9]  == 0){
                KeepToggle.setImageResource(R.drawable.leddefault);
                DynamicVariables.KEEP = 0;
            }else if(values[9] == 2){
                KeepToggle.setImageResource(R.drawable.ledyellow);
                DynamicVariables.KEEP = 2;
            }

            WHour.setHint(Integer.toString(values[10]));
            WMinute.setHint(Integer.toString(values[11]));
            WSecond.setHint(Integer.toString(values[12]));
        }catch(Exception e){
            //do nothing
        }
    };

    static String printtwodigits(byte digit){
        String output = "";
        if(digit < 10){
            output += "0";
        }
        output += Byte.toString(digit);
        return output;
    }
}