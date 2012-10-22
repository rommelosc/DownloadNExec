package com.rommelosc.downloadnexec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rommelosc.downloadnexec.listeners.DownloadListener;

public class MainActivity extends Activity {

	public static final int INVALID_URL = 0;

	private EditText urlEditText;
	private Button downloadButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		urlEditText = (EditText) findViewById(R.id.urlEditText);
		urlEditText.setHint("Escribe la URL");
		urlEditText.setSelectAllOnFocus(true);

		downloadButton = (Button) findViewById(R.id.downloadButton);
		DownloadListener downloadListener = new DownloadListener(this);
		downloadButton.setOnClickListener(downloadListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		switch (id) {
		case INVALID_URL:
			dialog.setTitle("URL inválida");
			dialog.setIcon(android.R.drawable.ic_dialog_alert);
			dialog.setMessage("Ocurrió un problema con la URL, intente escribirla de nuevo");
			dialog.setPositiveButton(" OK ",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							urlEditText.requestFocus();
						}
					});
			return dialog.create();

		}

		return super.onCreateDialog(id);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		String url = "";
		
		if (item.getItemId() == R.id.image_item) {
			url = "http://www.elandroidelibre.com/wp-content/uploads/2012/01/app.jpg";
		} else if (item.getItemId() == R.id.pdf_item) {
			url = "http://dl.dropbox.com/u/2029866/test.pdf";
		} else if (item.getItemId() == R.id.mp3_item) {
			url = "http://dl.dropbox.com/u/2029866/13.%20Jimi%20Hendrix%20-%20Let%20Me%20Go.mp3";
		} else if (item.getItemId() == R.id.apk_item) {
			url = "http://dl.dropbox.com/u/2029866/HolaMundo.apk";
		}
		
		Toast.makeText(this, "URL: " + url, Toast.LENGTH_LONG).show();
		urlEditText.setText(url);

		return super.onOptionsItemSelected(item);
	}

}
