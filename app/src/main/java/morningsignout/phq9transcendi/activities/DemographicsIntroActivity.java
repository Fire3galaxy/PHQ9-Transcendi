package morningsignout.phq9transcendi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import morningsignout.phq9transcendi.R;

/**
 * Created by Stella on 3/20/2016.
 */
public class DemographicsIntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this, Utils.GetTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demographics_intro);

        // Contine and Skip button for Demographics
        Button continue_button = (Button) findViewById(R.id.button_continue_demo);
        Button skip_button = (Button) findViewById(R.id.button_skip_demo);

        // Continue - Do demographics questions
        continue_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DemographicsIntroActivity.this, DemographicsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Skip - Move on to quiz
        skip_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DemographicsIntroActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // When activity is visible to user: Continue button will have width
    // Note: Used when the text was "continue" (longer) and "skip" (shorter), and side by side
    // buttons. Now deprecated.
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        // Make both buttons the same width for aesthetic
//        skip_button.setWidth(continue_button.getMeasuredWidth());
//    }
}
