package com.example.macremote.demo_circlerefres;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private CircleRefreshLayout circleRefreshLayout ;
    private ListView listView ;
    private ArrayList<String> lists ;
    private ArrayAdapter<String> adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleRefreshLayout = (CircleRefreshLayout)findViewById(R.id.refresh_layout) ;
        listView = (ListView)findViewById(R.id.listView) ;
        lists = new ArrayList<String>() ;
        for(int i=0;i<10;i++)
        {
            lists.add(i+" ") ;
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lists) ;
        listView.setAdapter(adapter);

        //CircleRefreshLayout的监听事件
        circleRefreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
                //do something when completeRefresh
                Toast.makeText(MainActivity.this, "refresh has complete", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void refreshing() {
                //do something when Refreshing
                Toast.makeText(MainActivity.this, "refreshing", Toast.LENGTH_SHORT).show();
                //启动子线程，三秒后停止刷新
                MyRunnable myRunnable = new MyRunnable() ;
                new Thread(myRunnable).start();
            }
        });
    }
    //定义一个handle处理刷新
    private Handler myHandler = new Handler()
    {
        //接收子线程的信息
        public void handleMessage(Message msg)
        {
            switch(msg.what)
        {
            case 0:
                circleRefreshLayout.finishRefreshing();
                break ;
        }
        }
    } ;
    private class MyRunnable implements Runnable
    {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                lists.add(0,"100");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = new Message() ;
            message.what = 0 ;
            myHandler.sendMessage(message) ;
        }
    }
}
