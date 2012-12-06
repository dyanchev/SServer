package com.example.sserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;



public class ServerActivity extends GenericActivity {

	EditText server;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_layout);
		server = (EditText) findViewById(R.id.server_ipaddress_et);
		SharedPreferences sp = this.getSharedPreferences(GenericActivity.SHARED_PREFERENCES, Context.MODE_WORLD_READABLE);
		String serverAddress = sp.getString("server", "10.0.2.2:8080");
		server.setText(serverAddress);
		doBindService();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences sp = this.getSharedPreferences(GenericActivity.SHARED_PREFERENCES, Context.MODE_WORLD_READABLE);
		sp.edit().putString("server", server.getText().toString()).commit();
		mBoundService.onServerChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
