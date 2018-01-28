package com.jia.sample;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.jia.jsloader.JsLoader;
import com.jia.jsloader.async.AsyncHandler;
import com.jia.jsloader.async.AsyncUrlConnection;

public class MainActivity extends AppCompatActivity {

    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv1= (ImageView) findViewById(R.id.iv1);
        iv2= (ImageView) findViewById(R.id.iv2);
        iv3= (ImageView) findViewById(R.id.iv3);
        iv4= (ImageView) findViewById(R.id.iv4);


        JsLoader.with(this)
                .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=699359866,1092793192&fm=27&gp=0.jpg")
                .defaultImg(R.mipmap.ic_launcher)
                .into(iv1);

        JsLoader.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1516879221738&di=6197abb79657ae07358e91e5108386be&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd000baa1cd11728bdf999dd4c2fcc3cec2fd2c8b.jpg")
                .defaultImg(R.mipmap.ic_launcher)
                .into(iv2);

        JsLoader.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517132672037&di=1edc79cffce4799693f13b8c205f77d3&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205172039_wi35Z.jpeg")
                .defaultImg(R.mipmap.ic_launcher)
                .into(iv3);

        JsLoader.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517132672035&di=13ca407c18ba9ac3dea5924ba5fd6dd9&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205172252_kTASa.jpeg")
                .defaultImg(R.mipmap.ic_launcher)
                .into(iv4);


    }
}
