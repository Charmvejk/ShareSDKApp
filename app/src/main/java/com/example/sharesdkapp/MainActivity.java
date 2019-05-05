package com.example.sharesdkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;

/*
 * weishuyong
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {
    private TextView tvWeChat;
    private TextView tvQQ;
    private TextView tvShareAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvQQ = findViewById(R.id.tv_grant_authorization);
        tvShareAll = findViewById(R.id.tv_one_share);
        tvWeChat = findViewById(R.id.tv_share_qq);
        tvWeChat.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvShareAll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //一键分享
            case R.id.tv_one_share:
                showShare("");
                break;
            //分享到qq
            case R.id.tv_share_qq:
                Platform plat1 = ShareSDK.getPlatform(QQ.NAME);
                showShare(plat1.getName());
                break;
            //用户qq授权此app
            case R.id.tv_grant_authorization:
                showShareQQ();
                break;
        }
    }

    private void showShare(String name) {

        String URL = "http://ganjiang.top:1005" + "/dn";
        OnekeyShare oks = new OnekeyShare();
        if (name != null&&!"".equals(name)) {
            oks.setPlatform(name);
        }
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("干将");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(URL);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(URL);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // text是分享文本，所有平台都需要这个字段
        oks.setText("发票网址：");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(URL);
        // 启动分享GUI
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

            }
        });
        oks.show(this);
    }

    private void showShareQQ() {
        Platform plat = ShareSDK.getPlatform(QQ.NAME);
        plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
        if (plat.isClientValid()) {
            //判断是否存在授权凭条的客户端，true是有客户端，false是无
        }
        if (plat.isAuthValid()) {
//判断是否已经存在授权状态，可以根据自己的登录逻辑设置
            Toast.makeText(this, "已经授权过了", 0).show();
            return;
        }
        ShareSDK.setActivity(this);//抖音登录适配安卓9.0
        plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
