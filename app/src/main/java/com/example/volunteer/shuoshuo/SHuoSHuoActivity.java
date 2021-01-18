package com.example.volunteer.shuoshuo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import com.example.volunteer.R;
import com.example.volunteer.stack.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
public class SHuoSHuoActivity extends AppCompatActivity {
private RecyclerView recyclerView;
List<Content> contentList=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    contentList= (List<Content>) msg.obj;
                    ContentAdapter  adapter=new ContentAdapter(contentList,SHuoSHuoActivity.this);
                    recyclerView.setAdapter(adapter);
break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
        setContentView(R.layout.activity_shuo_shuo);
        recyclerView= (RecyclerView) findViewById(R.id.recleView);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Bmob.initialize(this, "00957be3d6e96745186f6a436b6fe2a2");
        requestData();
Log.d("MainActivity", contentList.equals(null) + "==" + contentList.size());
FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "into addShuoshuo");
                Intent intent=new Intent(SHuoSHuoActivity.this, AddShuoShuo.class);
                startActivity(intent);
                finish();
}
        });
        findViewById(R.id.backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
}
@Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenManager.getScreenManager().popActivity(this);
    }
private void requestData(){//利用handler.setMessage()方法传出讯息
        final BmobQuery<Content> query=new BmobQuery<Content>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Content>() {
            @Override
            public void done(List<Content> list, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = list;//查询出的lsit
                    handler.sendMessage(message);
                }
            }
        });
    }
}
