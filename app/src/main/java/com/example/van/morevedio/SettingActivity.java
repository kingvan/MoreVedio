package com.example.van.morevedio;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {

    private String vedio;
    private List<String> list=new ArrayList<>();
    private boolean isClick=false;
    private ListView listview;
    private AboutAdapter adapter;
    private LinearLayout main;
    String temp="";
    String test="1279029_6B789C12";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        for (int i=0;i<10000;i++){
            temp=temp+i+test+";";
        }
        copy(temp);

        listview=findViewById(R.id.listview);
        main=findViewById(R.id.main);
        adapter=new AboutAdapter(this);

        listview.setAdapter(adapter);
        findViewById(R.id.sel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 62);
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(vedio)){
                    Toast.makeText(SettingActivity.this,"路径没有设置",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(SettingActivity.this,MainActivity.class).putExtra("url",vedio));
            }
        });

        findViewById(R.id.sel2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick){

                    Toast.makeText(SettingActivity.this,"寻找找到路径中，请稍后",Toast.LENGTH_SHORT).show();
                    return;
                }
                isClick=true;
                LocalFileTool.readFile(LocalFileTool.aviType, SettingActivity.this, new LocalFileTool.IReadCallBack() {
                    @Override
                    public void callBack(List<String> localPath) {
                        for (String path : localPath) {
                            if (path.contains("left_repeater")) {
                                list.add(path);
                                break;
                            }
                        }
                        if(list!=null&&list.size()>0){
                            adapter.setData(list);
                            main.setVisibility(View.GONE);
                            listview.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingActivity.this,"找到路径",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SettingActivity.this,"没有找到路径",Toast.LENGTH_SHORT).show();
                        }
                        isClick=false;
                    }
                });
            }
        });


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 33);
            }
        }







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 33) {

        }


        if (requestCode == 62) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                /** 数据库查询操作。
                 * 第一个参数 uri：为要查询的数据库+表的名称。
                 * 第二个参数 projection ： 要查询的列。
                 * 第三个参数 selection ： 查询的条件，相当于SQL where。
                 * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
                 * 第四个参数 sortOrder ： 结果排序。
                 */
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // 视频ID:MediaStore.Audio.Media._ID
                        int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        // 视频名称：MediaStore.Audio.Media.TITLE
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        // 视频路径：MediaStore.Audio.Media.DATA
                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                        vedio=videoPath;
                        // 视频时长：MediaStore.Audio.Media.DURATION
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        // 视频大小：MediaStore.Audio.Media.SIZE
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        // 视频缩略图路径：MediaStore.Images.Media.DATA
                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                    }
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public class AboutAdapter extends BaseAdapter{

        private Context context;
        private List<String> list;
        public AboutAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list!=null?list.size():0;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.item,null);
                holder.tv_title = (TextView) view.findViewById(R.id.tv);
                holder.tv_1 = (TextView) view.findViewById(R.id.tv_1);


                holder.btn = (Button) view.findViewById(R.id.btn);
                holder.im = (ImageView) view.findViewById(R.id.im);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tv_title.setText(getname(list.get(i)));

            holder.tv_1.setText(list.get(i));

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vedio=list.get(i);
                    Toast.makeText(SettingActivity.this,"已选择视频",Toast.LENGTH_SHORT).show();
                    listview.setVisibility(View.GONE);
                    main.setVisibility(View.VISIBLE);
                }
            });
            GlideFileVideo(SettingActivity.this,list.get(i),holder.im,R.mipmap.ic_launcher);


            return view;
        }

        public String  getname(String tem){

            try {
                String[] split = tem.split("/");
                String s = split[split.length - 1];
                int left = s.indexOf("left");
                return s.substring(0,left);
            }catch (Exception e){
                return tem;
            }



        }


        public void setData(List<String> aboutlist) {

            this.list = aboutlist;
            notifyDataSetChanged();
        }

        class ViewHolder{
            TextView tv_title,tv_1;
            Button btn;
            ImageView im;
        }
    }

    public static void GlideFileVideo(Context context, String fileUrl, ImageView imageView , int placeholderImage){
        if(fileUrl==null||fileUrl.length()<5){
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(fileUrl).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .crossFade().centerCrop().placeholder(R.mipmap.ic_launcher).into(new GlideDrawableImageViewTarget(imageView));
        }

    }

    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
