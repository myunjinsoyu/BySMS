package com.soyu.bysms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        switch (intent.getAction()) {

        }
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            // SMS 메시지를 파싱합니다.
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.e("문자 수신 시간", curDate.toString());

            // SMS 발신 번호 확인
            String origNumber = smsMessage[0].getOriginatingAddress();

            // SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            Log.e("문자 내용", "발신자 : " + origNumber + ", 내용 : " + message);


            SQLHelper sql = new SQLHelper(context, "bysms", null, 1);
            SQLiteDatabase db = sql.getReadableDatabase();
            Cursor c = sql.selectData(db, origNumber);
            if (c.getCount() > 0) {

                while (c.moveToNext()) {
                    String str = c.getString(1) + " => " + c.getString(2);
                    Log.e("!!!", "DB str = " + str);

                    if (origNumber.equals(c.getString(1))) {
                        //  SMS Send
                        sendSMS(c.getString(2), message);
                    }


                }

            }

        }
    }

    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";
    private int MAX_SMS_MESSAGE_LENGTH = 160;

    private void sendSMS(String phoneNumber, String message) {
        PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0, new Intent(DELIVERED), 0);
        SmsManager smsManager = SmsManager.getDefault();

        int length = message.length();
        if (length > MAX_SMS_MESSAGE_LENGTH) {
            ArrayList<String> messagelist = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist, null, null);
        } else {
            smsManager.sendTextMessage(phoneNumber, null, message, piSent, piDelivered);
        }
    }
}
