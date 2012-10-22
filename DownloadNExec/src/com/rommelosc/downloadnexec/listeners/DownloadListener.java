package com.rommelosc.downloadnexec.listeners;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.rommelosc.downloadnexec.DownloadTask;
import com.rommelosc.downloadnexec.MainActivity;
import com.rommelosc.downloadnexec.R;

public class DownloadListener implements OnClickListener {

	private Activity activity;
	private String urlStr = null;

	public DownloadListener(Activity activity) {
		this.activity = activity;

	}

	@Override
	public void onClick(View v) {

		EditText urlEditTExt = (EditText) activity
				.findViewById(R.id.urlEditText);
		String url_tmp = urlEditTExt.getText().toString();

		// Validando URL
		if (!url_tmp.startsWith("http://")) {
			urlStr = "http://" + url_tmp;
		} else {
			urlStr = url_tmp;
		}

		if (!isValidURL(urlStr)) {
			activity.showDialog(MainActivity.INVALID_URL);
		}
		// Descarga
		else {

			 ProgressDialog progressDialog = new
			 ProgressDialog(v.getContext());
			 progressDialog.setCancelable(false);
			 progressDialog.setMessage("Descargando archivo ...");
			 progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			 progressDialog.setProgress(0);
			 progressDialog.setMax(100);
			 progressDialog.show();
			
			 DownloadTask downloadTask = new DownloadTask(activity,
			 progressDialog);
			 downloadTask.setUrlStr(urlStr);
			 downloadTask.execute();
		}

	}

	private boolean isValidURL(String aURL) {

		if (!URLUtil.isValidUrl(aURL)) {
			return false;
		}
		
		URL url = null;
		try {
			url = new URL(aURL);
		} catch (MalformedURLException e) {
			return false;
		}

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
		} catch (IOException e) {
			return false;
		}
		
		return true;

	}

}
