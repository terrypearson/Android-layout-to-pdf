package xyz.easyeats.androidlayouttopdf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bClickMe = (Button) findViewById(R.id.bClickMe);
        //Listener Order Queue button
        bClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamplePdfMaker receipt = new ExamplePdfMaker(MainActivity.this);
                String pdfFilePath=receipt.buildPdf();
            }
        });

    }

}
