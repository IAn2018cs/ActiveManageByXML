package cn.ian2018.activemanagebyxml.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.ian2018.activemanagebyxml.R;
import cn.ian2018.activemanagebyxml.adapter.ActiveRecyclerAdapter;
import cn.ian2018.activemanagebyxml.model.Active;
import cn.ian2018.activemanagebyxml.utils.Constant;
import cn.ian2018.activemanagebyxml.utils.SpUtil;
import cn.ian2018.activemanagebyxml.utils.ToastUtil;
import cn.ian2018.activemanagebyxml.utils.XMLUtils;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ActiveRecyclerAdapter mAdapter;
    private List<Active> activeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 检查权限
        checkPermission();

        // 将xml文件写入sd卡中
        writeToSD();

        // 初始化控件
        initView();

        // 设置数据
        setData();
    }

    // 设置数据
    private void setData() {
        XMLUtils.readXMLFile(activeList);
        mAdapter = new ActiveRecyclerAdapter(activeList);
        recyclerView.setAdapter(mAdapter);

        // 设置RecyclerView条目点击事件
        mAdapter.setItemClickListener(new ActiveRecyclerAdapter.OnRecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转到修改活动页面
                Intent intent = new Intent(MainActivity.this, ActiveDataActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("active",activeList.get(position));
                startActivity(intent);
            }
        });

        // 删除
        mAdapter.setDeleteClickListener(new ActiveRecyclerAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                showConfirmDialog(position);
            }
        });

        swipeRefresh.setRefreshing(false);
    }

    // 初始化控件
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        // 配置swipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        // 设置刷新事件
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                XMLUtils.readXMLFile(activeList);
                mAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                if (activeList.size() == 0) {
                    ToastUtil.showShort("没有数据");
                }
            }
        });

        swipeRefresh.setRefreshing(true);
    }

    private void writeToSD() {
        // 第一次打开应用 将xml文件写入sd卡中
        if (SpUtil.getBoolean(Constant.FIRST_READ_XML, true)) {
            // 获取手机文件目录
            File file = getExternalFilesDir("xml");
            try {
                // 打开资源文件
                AssetManager assets = getAssets();
                InputStream inputStream = assets.open("active.xml");

                // 将流转换为字节
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                inputStream.close();
                bos.close();

                // 写入手机文件中
                FileOutputStream fos = new FileOutputStream(file.getPath() + "/" + "active.xml");
                fos.write(bos.toByteArray());

                SpUtil.putBoolean(Constant.FIRST_READ_XML, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 显示确认对话框
    protected void showConfirmDialog(final int position) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        // 设置对话框左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置不能取消
        builder.setCancelable(false);
        // 设置对话框标题
        builder.setTitle("删除活动");
        // 设置对话框内容
        builder.setMessage("您确认删除该活动？");
        // 设置积极的按钮
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除活动
                int id = activeList.get(position).getId();
                boolean isDelete = XMLUtils.deleteXMLById(id);
                if (isDelete) {
                    activeList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    ToastUtil.showShort("删除成功");
                } else {
                    ToastUtil.showShort("删除失败");
                }
                dialog.dismiss();
            }
        });
        // 设置消极的按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        XMLUtils.readXMLFile(activeList);
        mAdapter.notifyDataSetChanged();
        if (activeList.size() == 0) {
            ToastUtil.showShort("没有数据");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 搜索
        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        // 添加活动
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, ActiveDataActivity.class);
            intent.putExtra("type",0);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 检查权限
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            ToastUtil.showShort("请您同意所有权限再使用");
                            finish();
                        }
                    }
                } else {
                    ToastUtil.showShort("出了个小错误");
                    finish();
                }
                break;
        }
    }
}
