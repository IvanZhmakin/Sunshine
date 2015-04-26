package com.example.zhmakin.sunshine.app;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.zhmakin.sunshine.R;


public class Detail extends ActionBarActivity {

    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String text = this.getIntent().getStringExtra(Intent.EXTRA_TEXT);

        TextView textView = (TextView)findViewById(R.id.textView_detail);
        textView.setText(text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        // Get the menu item.
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        //mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        TextView textView = (TextView)findViewById(R.id.textView_detail);
        String strShare = textView.getText().toString()+"#sunShine";
        mShareActionProvider.setShareIntent(createShareForecastIntent(strShare));

        // Set history different from the default before getting the action
        // view since a call to MenuItem.getActionView() calls
        // onCreateActionView() which uses the backing file name. Omit this
        // line if using the default share history file is desired.
        //mShareActionProvider.setShareHistoryFileName(SHARE_TEXT_HISTORY_FILE_NAME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent detailIntent = new Intent(this, SettingsActivity.class);
            startActivity(detailIntent);
            return true;
        }
        /*else if (id == R.id.action_share) {

            //ShareActionProvider shareActionProvider = new ShareActionProvider();

            TextView textView = (TextView)findViewById(R.id.textView_detail);
            String strShare = textView.getText().toString()+"#sunShine";

            Intent intent = new Intent(Intent.ACTION_MEDIA_SHARED);
           // intent.setData(strShare);
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                startActivity(intent);
            }
        }*/
        return super.onOptionsItemSelected(item);
    }

    // Somewhere in the application.
    public void doShare(Intent shareIntent) {
        // When you want to share set the share intent.
        mShareActionProvider.setShareIntent(shareIntent);
    }

    private Intent createShareForecastIntent(String shareTxt){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTxt);
        return shareIntent;
    }
}
