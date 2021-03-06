package morningsignout.phq9transcendi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import morningsignout.phq9transcendi.R;
import morningsignout.phq9transcendi.HelperClasses.BlinkScrollView;
import morningsignout.phq9transcendi.HelperClasses.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Stella on 3/20/2016.
 */
public class DemographicsIntroActivity extends AppCompatActivity {
    Button continue_button;
    Button skip_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this, Utils.GetTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_intro);

        // Contine and Skip button for Demographics
        continue_button = (Button) findViewById(R.id.button_continue_demo);
        skip_button = (Button) findViewById(R.id.button_skip_demo);

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

    // When activity is visible to user: Continue button will have same width as skip.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Make both buttons the same width for aesthetic
        continue_button.setWidth(skip_button.getMeasuredWidth());

        // Blink scrollbar to indicate scrolling is possible
        BlinkScrollView container = (BlinkScrollView) findViewById(R.id.container_demo_intro);
        
        if (container.canScrollVertically())
            container.blinkScrollBar();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));   // For custom Rubik font
    }
}
