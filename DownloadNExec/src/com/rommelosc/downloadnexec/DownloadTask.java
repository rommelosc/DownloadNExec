package com.rommelosc.downloadnexec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<String, Void, Void> {

	private static final String EXTENSION_SEPARATOR = ".";

	private Activity activity;
	private ProgressDialog progressBar;
	private String urlStr = null;
	private String fileName = null;
	private String fileNameWithoutExtn = null;
	private String extension = null;
	private boolean successfulDownload;
	private File file; 

	public DownloadTask(Activity activity, ProgressDialog progressBar) {
		this.activity = activity;
		this.progressBar = progressBar;
//		this.progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected Void doInBackground(String... params) {
		int progress = 0;
		int max = 100;

		URL url;
		try {
			url = new URL(urlStr);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			// Información del archivo a descargar
			fileName = url.toString().substring(
					url.toString().lastIndexOf('/') + 1,
					url.toString().length());

			fileNameWithoutExtn = fileName.substring(0,
					fileName.lastIndexOf('.'));

			int dot = url.toString().lastIndexOf(EXTENSION_SEPARATOR);
			extension = url.toString().substring(dot + 1);

			// Descargando archivo a la SD
			File SDCardRoot = Environment.getExternalStorageDirectory();
			file = new File(SDCardRoot, fileName);

			FileOutputStream fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();

			int totalSize = urlConnection.getContentLength();
			int downloadedSize = 0;
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			
			max = totalSize;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				progress += bufferLength;
				progressBar.setProgress((int) (progress * 100 / max));
			}
			fileOutput.close();

			if (downloadedSize == totalSize) {
				successfulDownload = true;
			}

		} catch (MalformedURLException e) {
			activity.showDialog(MainActivity.INVALID_URL);
		} catch (IOException e) {
			activity.showDialog(MainActivity.INVALID_URL);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		progressBar.dismiss();
		Toast.makeText(activity.getApplicationContext(), "Descarga terminada",
				Toast.LENGTH_SHORT).show();
		
		if( !successfulDownload )
			return;
		
		String [] info = {"Nombre: " + fileName,
						  "Extensión: " + extension,
						  "URL: " + urlStr
					     };
		

		Bundle extras = new Bundle();
		extras.putStringArray("info", info);
		extras.putString("extension", extension);
		extras.putString("path", file.getAbsolutePath());
		
		Intent intentForward = new Intent(((MainActivity)activity).getApplicationContext(), ExecActivity.class);
		intentForward.putExtras(extras);
		((MainActivity)activity).startActivity(intentForward);
		
//		((MainActivity)activity).go2ExecActivity();
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileNameWithoutExtn() {
		return fileNameWithoutExtn;
	}

	public String getExtension() {
		return extension;
	}

	public boolean isSuccessfulDownload() {
		return successfulDownload;
	}

	public String getUrlStr() {
		return urlStr;
	}

}
