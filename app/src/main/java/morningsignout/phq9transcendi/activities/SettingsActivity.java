package morningsignout.phq9transcendi.activities;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import morningsignout.phq9transcendi.PHQApplication;
import morningsignout.phq9transcendi.R;
import morningsignout.phq9transcendi.HelperClasses.NotificationReceiver;
import morningsignout.phq9transcendi.HelperClasses.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static morningsignout.phq9transcendi.HelperClasses.NotificationReceiver.FREQ_PREF;

public class SettingsActivity extends AppCompatActivity {
    Context context;

    //For notifications
    final static String NOTIF_PREF = "Notification_Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this, Utils.GetTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        //populate the spinner with frequencies of notification
        final Spinner notification_spinner = (Spinner) findViewById(R.id.notification_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notifications_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notification_spinner.setAdapter(adapter);

        //set the default spinner text to the saved preferences
        SharedPreferences prefs = getSharedPreferences(NOTIF_PREF, MODE_PRIVATE);
        int savedFreq = prefs.getInt(FREQ_PREF, 0);
        if (savedFreq != 0) { //now change spinner
            notification_spinner.setSelection(savedFreq);
        }
        //set up the textbox that shows the next time the alarm will go off.
        setNextAlarmText();
        //Set up notification Button and button listener (ok)
        Button notification_button = (Button) findViewById(R.id.notification_button);
        notification_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int frequency = notification_spinner.getSelectedItemPosition();
                NotificationReceiver.changeAlarmSettings(context, frequency);
                setNextAlarmText();

            }
        });


        // Themes spinner
        final Spinner themes_spinner = (Spinner) findViewById(R.id.themes_spinner);
        ArrayAdapter<CharSequence> themesadapter = ArrayAdapter.createFromResource(this,
                R.array.themes, android.R.layout.simple_spinner_item);
        themesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themes_spinner.setAdapter(themesadapter);

        //set the default spinner text to the saved preferences
        int savedTheme = Utils.GetTheme(this);
        if (savedTheme != 0) { //now change spinner
            themes_spinner.setSelection(savedTheme);
        }

        //Set up notification Button and button listener (ok)
        Button themes_button = (Button) findViewById(R.id.themes_button);
        themes_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int theme = themes_spinner.getSelectedItemPosition();

            //Set the theme
            Utils.SaveTheme(Utils.THEME, theme, SettingsActivity.this);
            Utils.changeToTheme(SettingsActivity.this);
            updateThemePref();
            }
        });
    }

    public void setNextAlarmText(){
        String nextAlarm;
        SharedPreferences prefs = context.getSharedPreferences(NOTIF_PREF, Context.MODE_PRIVATE);
        nextAlarm = prefs.getString("Next alarm", "");
        TextView nextAlarmTime = (TextView) findViewById(R.id.next_notification_time);
        nextAlarmTime.setText(nextAlarm);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));   // For custom Rubik font
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

    //Themes are stored in firebase
    private void updateThemePref() {
        String userID = FirebaseAuth.getInstance(PHQApplication.getFirebaseAppInstance())
                .getCurrentUser().getUid();
        DatabaseReference themePref = FirebaseDatabase.getInstance(PHQApplication.getFirebaseAppInstance())
                .getReference("users/" + userID + "/themePreference");
        themePref.setValue(Utils.THEME_NAMES[Utils.GetTheme(this)]);
    }
}
