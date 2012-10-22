package com.rommelosc.downloadnexec;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExecActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exec_activity);

		final Bundle extras = getIntent().getExtras();
		String[] info = extras.getStringArray("info");

		ListView listView = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, info);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				if( position == 0 ){
					
					String extension = extras.getString("extension");
					String path = extras.getString("path");
					File file = new File(path);
					
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					MimeTypeMap mime = MimeTypeMap.getSingleton();
					String type = mime.getMimeTypeFromExtension(extension);
					intent.setDataAndType(Uri.fromFile(file), type);
					startActivity(intent);					
				}
				
			}
		});
	}

}
