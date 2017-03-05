package kz.taxikz.ui;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected boolean visible = false;

    @Override
    protected void onResume() {
        super.onResume();
        visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
    }

    protected boolean isVisible() {
        return visible;
    }

}
