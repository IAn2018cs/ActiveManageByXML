package cn.ian2018.activemanagebyxml.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import cn.ian2018.activemanagebyxml.R;
import cn.ian2018.activemanagebyxml.adapter.ActiveRecyclerAdapter;
import cn.ian2018.activemanagebyxml.model.Active;
import cn.ian2018.activemanagebyxml.utils.ToastUtil;
import cn.ian2018.activemanagebyxml.utils.XMLUtils;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText et_name;
    private ImageView iv_search;
    private ActiveRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (!name.equals("")) {
                    List<Active> activeList = XMLUtils.selectXMLByName(name);
                    if (activeList.size() == 0) {
                        ToastUtil.showShort("没有该活动");
                    } else {
                        setData(activeList);
                    }
                } else {
                    ToastUtil.showShort("请先输入活动名称哦");
                }
            }
        });
    }

    // 设置数据
    private void setData(final List<Active> activeList) {
        mAdapter = new ActiveRecyclerAdapter(activeList);
        recyclerView.setAdapter(mAdapter);

        // 设置RecyclerView条目点击事件
        mAdapter.setItemClickListener(new ActiveRecyclerAdapter.OnRecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, ActiveDataActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("active",activeList.get(position));
                startActivity(intent);
            }
        });

        // 删除
        mAdapter.setDeleteClickListener(new ActiveRecyclerAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                showConfirmDialog(activeList, position);
            }
        });
    }

    // 显示确认对话框
    protected void showConfirmDialog(final List<Active> activeList, final int position) {
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
}
