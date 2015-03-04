package com.huishen.edrive.demand;

import com.huishen.edrive.util.Const;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OrderFailBroadcastReceiver extends BroadcastReceiver {

	public OrderFailBroadcastReceiver() {
	}

   @Override
   public void onReceive(Context context, Intent intent) {
		Intent i =  new Intent(context ,FailDialogActivity.class);
		i.putExtra("tempOrderId", intent.getIntExtra(Const.USER_LAST_ORDER_ID, 0));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
   }
}
