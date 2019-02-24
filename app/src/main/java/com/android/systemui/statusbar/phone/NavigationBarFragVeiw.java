package com.android.systemui.statusbar.phone;


import com.android.systemui.R;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.adayo.adayosource.AdayoSource;
import com.adayo.proxy.sourcemngproxy.Beans.AppConfigType;
import com.adayo.proxy.sourcemngproxy.Beans.SourceInfo;
import com.adayo.proxy.sourcemngproxy.Control.SrcMngSwitchProxy;




public class NavigationBarFragVeiw extends RelativeLayout implements OnClickListener{

    private String TAG = this.getClass().getSimpleName();

    private TextView mHomeBtn,mHomeRadioBtn,mHomeMediaBtn,mHomePhoneBtn,mHomeVoiceassistantBtn,
                   mHomeEnergyBtn,mHomeNaviBtn,mHomeSettingBtn,mHomePhoneLinkBtn,mHomeBtnApp,
                   mHomeClimateBtn;
    private ArrayList<TextView> mBtnGroup = new ArrayList<TextView>();
    private Context mContext;
    private ActivityManager am;
    private String packagename;
    private int count = 0;
    private Handler mHandler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            Log.i(TAG, "sxf run: cn.getPackageName() = "+cn.getPackageName()+" packagename = "+packagename);
            if(packagename.equals(cn.getPackageName())){
                SetHightLight();
            }else {
                Log.i(TAG, "sxf run: count  ="+count);
                count ++;
                if(count <10){
                    mHandler.postDelayed(runnable,3 * 100);
                }else {
                    mHandler.removeCallbacks(runnable);
                    count = 0;
                }

            }
        }
    };

    private  void SetHightLight(){
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.i(TAG, " sxf onReceive:    cn.getPackageName() = "+cn.getPackageName());
        if(cn.getPackageName().toLowerCase().contains("launcher")){
            if(cn.getClassName().toLowerCase().contains("allapp")){
                setFocusBtn(R.id.home_btn_app);
            }else {
                setFocusBtn(R.id.home_btn);
            }
        }else if(cn.getPackageName().toLowerCase().contains("radio")){
            setFocusBtn(R.id.home_radio_btn);
        } else if(cn.getPackageName().toLowerCase().contains("media")){
            setFocusBtn(R.id.home_media_btn);
        }else if(cn.getPackageName().toLowerCase().contains("setting")){
            setFocusBtn(R.id.home_btn_setting);
        }else if (cn.getPackageName().toLowerCase().contains("bluetooth")){
            setFocusBtn(R.id.home_phone_btn);
        }
    }


    public NavigationBarFragVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGERNAME_NAR");
        mContext.registerReceiver(mIntentReceiver, filter, null, null);
         am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            packagename = intent.getStringExtra("packagename");
            if (action.equals("android.intent.action.PACKAGERNAME_NAR") ){
                Log.i(TAG, " sxf onReceive: action = "+action+"  packagename = "+packagename);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                Log.i(TAG, " sxf onReceive: cn.getPackageName() = "+ cn.getPackageName()+"  packagename = "+packagename);
                if(packagename.equals(cn.getPackageName())){
                    SetHightLight();
                }else {
                    mHandler.postDelayed(runnable,3 * 100);
                }

            }
        }
    };

    private void initView() {

        mHomeBtn = (TextView)findViewById(R.id.home_btn);
        mHomeRadioBtn = (TextView)findViewById(R.id.home_radio_btn);
        mHomeMediaBtn = (TextView)findViewById(R.id.home_media_btn);
        mHomePhoneBtn = (TextView)findViewById(R.id.home_phone_btn);
        mHomeVoiceassistantBtn = (TextView)findViewById(R.id.home_voiceassistant_btn);
        mHomeEnergyBtn = (TextView)findViewById(R.id.home_energy_btn);
        mHomeNaviBtn = (TextView)findViewById(R.id.home_navi_btn);
        mHomeSettingBtn = (TextView)findViewById(R.id.home_btn_setting);
        mHomePhoneLinkBtn = (TextView)findViewById(R.id.home_phonelink_btn);
        mHomeBtnApp = (TextView)findViewById(R.id.home_btn_app);
        mHomeClimateBtn = (TextView)findViewById(R.id.home_climate_btn);

        mHomeBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeBtn);

        mHomeRadioBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeRadioBtn);

        mHomeMediaBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeMediaBtn);

        mHomePhoneBtn.setOnClickListener(this);
        mBtnGroup.add(mHomePhoneBtn);

        mHomeVoiceassistantBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeVoiceassistantBtn);

        mHomeEnergyBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeEnergyBtn);

        mHomeNaviBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeNaviBtn);

        mHomeSettingBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeSettingBtn);

        mHomePhoneLinkBtn.setOnClickListener(this);
        mBtnGroup.add(mHomePhoneLinkBtn);

        mHomeBtnApp.setOnClickListener(this);
        mBtnGroup.add(mHomeBtnApp);

        mHomeClimateBtn.setOnClickListener(this);
        mBtnGroup.add(mHomeClimateBtn);

        setFocusBtn(R.id.home_btn);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "view.getId():" + view.getId());
        if (view.getId() == R.id.home_btn) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            mContext.startActivity(i);
            //openApp("com.adayo.launcher");
        } else if (view.getId() == R.id.home_radio_btn) {
           // openApp("com.adayo.radio","com.adayo.radio.activity.RadioActivity");
		   UIAudioRequest(AdayoSource.ADAYO_SOURCE_RADIO);
        } else if (view.getId() == R.id.home_media_btn) {
	    	UIAudioRequest(AdayoSource.ADAYO_SOURCE_USB);
            openApp("com.adayo.media");
        } else if (view.getId() == R.id.home_phone_btn) {
	    	UIAudioRequest(AdayoSource.ADAYO_SOURCE_BT_PHONE);
            openApp("com.adayo.bluetooth");
        } else if (view.getId() == R.id.home_voiceassistant_btn) {
            Toast.makeText(getContext(), "view.getId() = "+view.getId()+"R.id.home_voiceassistant_btn = "+R.id.home_voiceassistant_btn
                    ,Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.home_energy_btn) {
            Toast.makeText(getContext(), "view.getId() = "+view.getId()+"R.id.home_energy_btn = "+R.id.home_energy_btn
                    ,Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.home_navi_btn) {
            Toast.makeText(getContext(), "view.getId() = "+view.getId()+"R.id.home_navi_btn = "+R.id.home_navi_btn
                    ,Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.home_btn_setting) {
	     	UIRequest(AdayoSource.ADAYO_SOURCE_SETTING);
            openApp("com.adayo.app.settings");
        } else if (view.getId() == R.id.home_phonelink_btn) {
			UIRequest(AdayoSource.ADAYO_SOURCE_HOME);
            Toast.makeText(getContext(), "view.getId() = "+view.getId()+"R.id.home_phonelink_btn = "+R.id.home_phonelink_btn
                    ,Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.home_btn_app) {
            openApp("com.adayo.launcher","com.adayo.launcher.ui.activity.AllAppActivity");
        } else if (view.getId() == R.id.home_climate_btn) {
		    openApp("com.adayo.app.air","com.adayo.app.air.AirActivity");
        }
        setFocusBtn(view.getId());
    }

    // 设置焦点按钮
    private void setFocusBtn(int id) {
        mHandler.removeCallbacks(runnable);
        for (int i = 0; i < mBtnGroup.size();i++) {

            TextView mTv =mBtnGroup.get(i);
            if (mTv.getId() == id) {
                Log.i(TAG, "Botton_ControlBar,setFocusBtn,i="+i+",id="+id);
                mTv.setSelected(true);
                mTv.setEnabled(false);
            } else {
                mTv.setSelected(false);
                mTv.setEnabled(true);
            }
        }

    }
	
    private void UIAudioRequest(String typeSource){
        SourceInfo info = new SourceInfo(typeSource,
                AppConfigType.SourceSwitch.APP_ON.getValue(),
                AppConfigType.SourceType.UI_AUDIO.getValue());
//        SrcMngSwitchProxy.getInstance().onRequest(info);
    }
	
	private void UIRequest(String typeSource){
        SourceInfo info = new SourceInfo(typeSource,
                AppConfigType.SourceSwitch.APP_ON.getValue(),
                AppConfigType.SourceType.UI.getValue());
//        SrcMngSwitchProxy.getInstance().onRequest(info);
    }
    private void openApp(String mPackagename,String mClassName) {
		try{
			Intent mIntent = new Intent();
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ComponentName comp = new ComponentName(mPackagename,mClassName);
			mIntent.setComponent(comp);
			mContext.startActivity(mIntent);
		}catch(Exception e){
			  Toast.makeText(getContext(), "e = "+e,Toast.LENGTH_SHORT).show();
		}

    }
    private void openApp(String mPackagename) {
		try{
			Intent mIntent = mContext.getPackageManager().getLaunchIntentForPackage(
					mPackagename);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(mIntent);
		}catch (Exception e){
			Toast.makeText(getContext(), "e2 = "+e,Toast.LENGTH_SHORT).show();
		}
    }
    /**
     * 设置高亮显示
     * @param value
     */
    public void enterTabType(int value) {
        View view = null;

        Log.v(TAG, "enterTabType:" + value);
        switch (value) {

            case 0:
                view = mHomeBtn;
                break;
            case 1:
                view = mHomeRadioBtn;
                break;
            case 2:
                view = mHomeMediaBtn;
                break;
            case 3:
                view = mHomePhoneBtn;
                break;
            case 4:
                view = mHomeVoiceassistantBtn;
                break;
            case 5:
                view = mHomeEnergyBtn;
                break;
            case 6:
                view = mHomeNaviBtn;
                break;
            case 7:
                view = mHomeSettingBtn;
                break;
            case 8:
                view = mHomePhoneLinkBtn;
                break;
            case 9:
                view = mHomeBtnApp;
                break;
            case 10:
                view = mHomeClimateBtn;
                break;
            default:
                break;
        }
        if (view != null) {
            setFocusBtn(view.getId());
        }
    }
        @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
