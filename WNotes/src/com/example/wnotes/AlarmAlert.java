package com.example.wnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

/**
 * ��ʾ�Ի���
 * 
 */
public class AlarmAlert extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// ��ʾ��Ϣ
		String remindMsg = bundle.getString("remindMsg");
		if (bundle.getBoolean("ring")) {
			// ��������
			Main.mediaPlayer = MediaPlayer.create(this, R.raw.ring);
			try {
				Main.mediaPlayer.setLooping(true);
				Main.mediaPlayer.prepare();
			} catch (Exception e) {
				setTitle(e.getMessage());
			}
			Main.mediaPlayer.start();// ��ʼ����
		}
		if (bundle.getBoolean("shake")) {
			Main.vibrator = (Vibrator) getApplication().getSystemService(
					Service.VIBRATOR_SERVICE);
			Main.vibrator.vibrate(new long[] { 1000, 100, 100, 1000 }, -1);
		}
		new AlertDialog.Builder(AlarmAlert.this)
				.setIcon(R.drawable.clock)
				.setTitle("����")
				.setMessage(remindMsg)
				.setPositiveButton("�� ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								AlarmAlert.this.finish();
								// �ر����ֲ�����
								if (Main.mediaPlayer != null)
									Main.mediaPlayer.stop();
								if (Main.vibrator != null)
									Main.vibrator.cancel();
							}
						}).setCancelable(false).show();

	}
}
