package com.example.packard.spaceoptimizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ApplicationAdapter listadaptor = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("myapp->", "Burrrrrrrrrrrrrr111");

        packageManager = getPackageManager();
       // new files().getfilelist();
        new LoadApplications().execute();

    }


    private void displayAboutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.about_title));
        builder.setMessage(getString(R.string.about_desc));


        builder.setPositiveButton("Know More", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javatechig.com"));
                startActivity(browserIntent);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = applist.get(position);
        try {
            Intent intent = packageManager
                    .getLaunchIntentForPackage(app.packageName);

            if (null != intent) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    public void trimCache(Context context)
    {
        try
        {
            File dir = new File(context.getCacheDir().getAbsolutePath());
            if (dir != null && dir.isDirectory())
            {
                deleteDir(dir);
            }
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            // TODO: handle exception
        }
    }
    public Boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory())
        {
            File listFiles[] = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++)
            {

                Log.e("myapp->", "file1: " + listFiles[i].getName());
                Boolean success = deleteDir(listFiles[i]);
                if (!success)
                {
                    Log.e("myapp->", "not delete " + listFiles[i].getName());

                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        Log.e("myapp->", "Burrrrrrrrrrrrrr");

        for (ApplicationInfo info : list) {
            try {
                Log.e("myapp->", "Burr1");
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                    Context context = createPackageContext(info.packageName, Context.CONTEXT_IGNORE_SECURITY);
                    Log.e("myapp->", "Dir4 " + context.getCacheDir().getAbsolutePath() + " " + context.getCacheDir().getUsableSpace());

                    trimCache(context);
                   // items.add(p.applicationInfo.loadLabel(getPackageManager()).toString() + ": " + context.getCacheDir().getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadaptor = new ApplicationAdapter(MainActivity.this,
                    R.layout.snippet_list_row, applist);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}