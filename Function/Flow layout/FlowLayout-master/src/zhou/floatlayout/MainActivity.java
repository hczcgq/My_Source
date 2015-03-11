package zhou.floatlayout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
    private String[] dates = new String[] { "Hello", "Hello World", "Android",
            "IOS", "Python Learn", "Java 8", "HTML CSS JS", "ImageView", "Git",
            "C", "C++", "Welcome" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        LayoutInflater inflater=LayoutInflater.from(this);
        FlowFrameLayout flowLayout = (FlowFrameLayout) findViewById(R.id.flow_layout);
        for (int i = 0; i < dates.length; i++) {
            TextView text = (TextView) inflater.inflate(R.layout.flow_textview, flowLayout,false);
            text.setText(dates[i]);
            text.setTextSize(20);
            text.setBackgroundColor(Color.DKGRAY);
            flowLayout.setupChild(text, i);
        }
        flowLayout.requestLayout();

        flowLayout.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                System.out.println(dates[arg2]);

            }
        });

    }

}
