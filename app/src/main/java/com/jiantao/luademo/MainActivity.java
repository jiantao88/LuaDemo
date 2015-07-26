package com.jiantao.luademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private android.widget.TextView luatv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.luatv = (TextView) findViewById(R.id.lua_tv);
        initLuaLib();
    }

    /**
     * 初始化luajava库
     */
    private void initLuaLib() {
        LuaState mluastate = LuaStateFactory.newLuaState();
        mluastate.openLibs();
        //从资源文件夹中获取的 Lua 脚本内容
        mluastate.LdoString(getStringForStream(getResources().openRawResource(R.raw.luademo)));
        //获得 Lua 脚本的函数 getData
        mluastate.getGlobal("getData");
        try {
            //参数传给 Lua 脚本函数 getData
            mluastate.pushObjectValue("world 测试向lua传数据");
        } catch (LuaException e) {
            e.printStackTrace();
        }
        // pcall 方法调用 Lua 脚本函数 getData，参数为上一步传入的 world
        mluastate.pcall(1, 1, 0);
        String text = mluastate.toString(-1);
        luatv.setText(text);
    }

    private String getStringForStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
