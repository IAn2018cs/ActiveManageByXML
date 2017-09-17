package cn.ian2018.activemanagebyxml.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.ian2018.activemanagebyxml.R;
import cn.ian2018.activemanagebyxml.model.Active;
import cn.ian2018.activemanagebyxml.utils.Constant;
import cn.ian2018.activemanagebyxml.utils.SpUtil;
import cn.ian2018.activemanagebyxml.utils.ToastUtil;
import cn.ian2018.activemanagebyxml.utils.XMLUtils;

public class ActiveDataActivity extends AppCompatActivity {

    private EditText et_active_name;
    private EditText et_active_time;
    private EditText et_active_location;
    private EditText et_active_image;
    private Button bt_submit;
    private int type;
    private String oldName = "";
    private String oldLocation = "";
    private String oldTime = "";
    private String oldImage = "";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_data);

        // 获取页面类型
        type = getIntent().getIntExtra("type", -1);

        // 初始化控件
        initView();

        // 如果是修改活动  就将数据填充
        if (type == 1) {
            Active active = (Active) getIntent().getSerializableExtra("active");
            initData(active);
        }

        // 按钮控制
        control();
    }

    // 按钮控制
    private void control() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_active_name.getText().toString().trim();
                String location = et_active_location.getText().toString().trim();
                String time = et_active_time.getText().toString().trim();
                String image = et_active_image.getText().toString().trim();

                switch (type) {
                    // 添加活动
                    case 0:
                        if (!name.equals("") && !location.equals("") && !time.equals("") && !image.equals("")) {
                            int id = SpUtil.getInt(Constant.ID, 5);
                            Active active = new Active();
                            active.setId(id);
                            active.setName(name);
                            active.setLocation(location);
                            active.setTime(time);
                            active.setImage(image);

                            boolean isAdd = XMLUtils.addActive(active);

                            if (isAdd) {
                                ToastUtil.showShort("添加成功");
                                id++;
                                SpUtil.putInt(Constant.ID, id);

                                et_active_name.setText("");
                                et_active_location.setText("");
                                et_active_time.setText("");
                                et_active_image.setText("");
                            } else {
                                ToastUtil.showShort("添加失败");
                            }
                        } else {
                            ToastUtil.showShort("请将信息填写完整");
                        }
                        break;
                    // 修改活动
                    case 1:
                        if (!name.equals(oldName) || !location.equals(oldLocation) || !time.equals(oldTime) || !image.equals(oldImage)) {
                            Active active = new Active();
                            active.setName(name);
                            active.setLocation(location);
                            active.setTime(time);
                            active.setImage(image);

                            boolean isChange = XMLUtils.changeXMLById(id, active);
                            if (isChange) {
                                ToastUtil.showShort("修改成功");
                            } else {
                                ToastUtil.showShort("修改失败");
                            }
                        } else {
                            ToastUtil.showShort("您没有做任何修改");
                        }
                        break;
                }
            }
        });
    }

    private void initData(Active active) {
        et_active_name.setText(active.getName());
        et_active_location.setText(active.getLocation());
        et_active_time.setText(active.getTime());
        et_active_image.setText(active.getImage());

        switch (type) {
            case 0:
                bt_submit.setText("添加活动");
                break;
            case 1:
                bt_submit.setText("修改活动");
                break;
        }

        id = active.getId();
        oldName = active.getName();
        oldLocation = active.getLocation();
        oldTime = active.getTime();
        oldImage = active.getImage();
    }

    private void initView() {
        et_active_name = (EditText) findViewById(R.id.et_active_name);
        et_active_time = (EditText) findViewById(R.id.et_active_time);
        et_active_location = (EditText) findViewById(R.id.et_active_location);
        et_active_image = (EditText) findViewById(R.id.et_active_image);

        bt_submit = (Button) findViewById(R.id.bt_submit);
    }
}
