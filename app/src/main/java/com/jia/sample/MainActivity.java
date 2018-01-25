package com.jia.sample;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.jia.jsloader.async.AsyncHandler;
import com.jia.jsloader.async.AsyncUrlConnection;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);


        AsyncUrlConnection.getBitmap(50000, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1516879221738&di=6197abb79657ae07358e91e5108386be&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd000baa1cd11728bdf999dd4c2fcc3cec2fd2c8b.jpg", new AsyncHandler() {
            @Override
            public void onSuccess(Object result) {
                Log.e("TAG", "onSuccess: "+result.toString() );
                iv.setImageBitmap((Bitmap) result);
            }

            @Override
            public void onFail(String str) {
                Log.e("TAG", "onFail: "+str );
            }
        });
    }
}
