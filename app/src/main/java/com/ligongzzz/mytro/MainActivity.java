package com.ligongzzz.mytro;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import java.util.Random;
import android.widget.*;
import android.view.animation.*;
import java.util.*;
import android.*;
import com.ligongzzz.mytro.Tool.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.*;
import android.content.pm.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	public void onClickButton1(View view)
	{
		Intent intent = new Intent(this, Sub2Activity.class);
    	startActivity(intent);
	}

	public void onClickButton2(View view)
	{
		if (fileutil.write("", "Mytro/lines.json", 2) == 0)
		{
			CreateDialogFragment f = new CreateDialogFragment();
			f.show(getFragmentManager(), "mydialog");
		}
		else
		{
			Intent intent = new Intent(this, EditorActivity.class);
			startActivity(intent);
		}
	}
	
	public void onClickButtonAbout(View view)
	{
			AboutDialogFragment f = new AboutDialogFragment();
			f.show(getFragmentManager(), "mydialog");
	}

	//Request Storage Power
	public void requestPower()
	{
		//判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{
			//如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
																	Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            }
			else
			{
				//申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }
    }

	class CreateDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("创建新的地图");
			builder.setMessage("没有检测到现有的地图，是否创建新的地图?地图的保存地址为/storage/emulated/0/Mytro/lines.json。");
			builder.setPositiveButton("创建，并开始编辑", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						fileutil.write("{\"Stop Time\":5,\"Transfer Time\":20,\"Speed\":8,\"Name\":\"自定义名称\",\"Line\":[],\"Station\":[]}", "Mytro/lines.json", 1);
						Intent intent = new Intent(MainActivity.this, EditorActivity.class);
						startActivity(intent);
					}
				});
			builder.setNegativeButton("不需要", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method

					}
				});

			return builder.create();
		}
	}
	
	class AboutDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("关于Mytro");
			builder.setMessage("Mytro V1.0\nMytro线路查询与编辑器由@ligongzzz开发，如果您有任何问题或建议，请联系作者:\n邮箱:xiezhe20001128@sjtu.edu.cn\nGitHub:github.com/ligongzzz");
			builder.setPositiveButton("好", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
					}
				});

			return builder.create();
		}
	}
}
