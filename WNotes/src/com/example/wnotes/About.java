package com.example.wnotes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class About extends Activity {
	private TextView tvAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		tvAbout = (TextView) findViewById(R.id.tvAbout);
		String about = "���±���Android�棩1.1\n\n";
		about += "author��XXX\n\n";
		about += "����΢����XXX\n\n";
		about += "Email��XXX@126.com";
		tvAbout.setText(about);
	}
}

