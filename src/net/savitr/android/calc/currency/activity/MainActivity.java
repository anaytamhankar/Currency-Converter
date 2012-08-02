package net.savitr.android.calc.currency.activity;

import net.savitr.android.calc.currency.R;
import net.savitr.android.calc.currency.data.CurrencyManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

	private static int FINISH_ACTIVITY = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				return CurrencyManager.initialize();
			}

			protected void onPostExecute(Boolean result) {
				if (result) {
					final Intent intent = new Intent(MainActivity.this,
							CalculatorActivity.class);
					startActivityForResult(intent, FINISH_ACTIVITY);
				} else {
					showErrorDialog();
				}
			}
		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FINISH_ACTIVITY) {
			finish();
		}
	}

	private void showErrorDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				MainActivity.this).create();
		alertDialog.setTitle(R.string.warning);
		alertDialog.setMessage(getResources().getString(R.string.alert_dialog_warning));
		alertDialog.setButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.cancel();
						MainActivity.this.finish();
					}
				});
		alertDialog.show();
	}
}
