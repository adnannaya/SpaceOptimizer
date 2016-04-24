package com.example.packard.spaceoptimizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class files{

	private File root;
	private ArrayList<File> fileList = new ArrayList<File>();
	private File gpxfile;
	private FileWriter writer;
//	@Override
	/*protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		view = (LinearLayout) findViewById(R.id.view);

		//getting SDcard root path
		root = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		getfile(root);

		for (int i = 0; i < fileList.size(); i++) {
			TextView textView = new TextView(this);
			textView.setText(fileList.get(i).getName());
			textView.setPadding(5, 5, 5, 5);

			System.out.println(fileList.get(i).getName());

			if (fileList.get(i).isDirectory()) {
				textView.setTextColor(Color.parseColor("#FF0000"));
			}
			view.addView(textView);
		}

	}*/
public void generateNoteOnSD(String sFileName) {
	try {
		File root = new File(Environment.getExternalStorageDirectory(), "Notes");
		if (!root.exists()) {
			root.mkdirs();
		}
		gpxfile = new File(root, sFileName);
		writer = new FileWriter(gpxfile);
		/*writer.append(sBody);
		writer.flush();
		writer.close();*/
	} catch (IOException e) {
		e.printStackTrace();
	}
}

	public void appendmydatainfile(String sData) {
		try {


			writer.append(sData);
			writer.append("\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closemyfile() {
		try {

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getfilelist( ) {
		Log.e("myapp->", "burrrrrrrrrrrrrrrrr");
		//getting SDcard root path
		/*root = new File(Environment.getRootDirectory()
				.getAbsolutePath());*/
///storage/emulated/0/Notes/bur_list.txt
		root=new File("/");
		//Log.e("myapp->", "burrr" + root);
		generateNoteOnSD("bur_list.txt");
		getfile(root);
		closemyfile();
		Log.e("myapp->", "burrrrrr complete!!!");

	}

		public ArrayList<File> getfile(File dir) {

			//Log.e("myapp->", "Dir " + dir);
			appendmydatainfile( "Dir " + dir);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				appendmydatainfile(listFile[i].getName());
				if (listFile[i].isDirectory()) {
					fileList.add(listFile[i]);
					getfile(listFile[i]);

				} /*else {
					if (listFile[i].getName().endsWith(".png")
							|| listFile[i].getName().endsWith(".jpg")
							|| listFile[i].getName().endsWith(".jpeg")
							|| listFile[i].getName().endsWith(".gif"))

					{
						fileList.add(listFile[i]);
					}
				}*/

			}
		}
		return fileList;
	}

}
