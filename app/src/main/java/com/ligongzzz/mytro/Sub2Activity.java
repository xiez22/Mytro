package com.ligongzzz.mytro;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;
import java.math.*;
import com.ligongzzz.mytro.Line.*;
import android.widget.AdapterView.*;
import java.util.*;
import android.view.animation.*;
import com.ligongzzz.mytro.Tool.*;
import net.sf.json.*;

public class Sub2Activity extends Activity 
{
	//全局变量
	int downloadsta=0;
	ProgressDialog dialog=null;
	CheckDialogFragment checkf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub2);
		//Set Spinner
		LineSearch lineSearch=new LineSearch();
		//JSON
		JSONObject myjson=null;
		try{
		myjson=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0).toString());
		}
		catch(Exception e){
			e.printStackTrace();
			checkf = new CheckDialogFragment();
			checkf.show(getFragmentManager(), "mydialog");
			checkf.setCancelable(false);
		}
		if (lineSearch.initlog(myjson,0) == 1&&checkf==null)
		{
			checkf = new CheckDialogFragment();
			checkf.show(getFragmentManager(), "mydialog");
			checkf.setCancelable(false);
		}
		List spinnerlist=new ArrayList();
		spinnerlist.add("车站选择");
		for (int i=1;i <= lineSearch.slist[0].num;i++)
		{
			if(!lineSearch.slist[i].name.equals(""))
			spinnerlist.add(lineSearch.slist[i].name);
		}
		SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerlist);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        Spinner spinner = (Spinner) findViewById(R.id.sub2Spinner1);
		spinner.setAdapter(adapter);
		Spinner spinner2 = (Spinner) findViewById(R.id.sub2Spinner2);
		spinner2.setAdapter(adapter);

		//设置动作
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> l, View v, int position, long id)
				{
					ArrayAdapter<String> adapter = (ArrayAdapter<String>) l.getAdapter();
					//tvResult.setText(adapter.getItem(position));
					//String s=(String)l.getItemAtPosition(position);
					//Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
					EditText editText1=(EditText) findViewById(R.id.sub2EditText1);
					if (adapter.getItem(position).equals("车站选择") == false)
						editText1.setText(adapter.getItem(position));
				}
				//没有选中时的处理
				public void onNothingSelected(AdapterView<?> parent)
				{
				}
			});
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> l, View v, int position, long id)
				{
					ArrayAdapter<String> adapter = (ArrayAdapter<String>) l.getAdapter();
					//tvResult.setText(adapter.getItem(position));
					//String s=(String)l.getItemAtPosition(position);
					//Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
					EditText editText2=(EditText) findViewById(R.id.sub2EditText2);
					if (adapter.getItem(position).equals("车站选择") == false)
						editText2.setText(adapter.getItem(position));
				}
				//没有选中时的处理
				public void onNothingSelected(AdapterView<?> parent)
				{
				}
			});
		//Loading
		int sjtu=0;
		try
		{
			Intent intent =getIntent();
			Bundle bundle= intent.getExtras();
			sjtu = bundle.getInt("sjtu");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (sjtu == 1)
			{
				SharedPreferences sp = getSharedPreferences("temp", Activity.MODE_PRIVATE);
				String startstation= sp.getString("startstation", "");
				EditText editText1=(EditText) findViewById(R.id.sub2EditText1);
				editText1.setText(startstation);
				String endstation= sp.getString("endstation", "");
				EditText editText2=(EditText) findViewById(R.id.sub2EditText2);
				editText2.setText(endstation);
			}
		}
    }

	//输出内容
	String op2="";

	//查询状态
	int request_statue=0;

	public void onSub2Button1Click(View view)
	{
		//Toast.makeText(this,"该功能尚未启用，敬请期待！",Toast.LENGTH_SHORT).show();
		//Get Input
		int err=0;
		request_statue = 0;
		EditText editText1=(EditText) findViewById(R.id.sub2EditText1);
		EditText editText2=(EditText) findViewById(R.id.sub2EditText2);
		String start=editText1.getText().toString();
		String end=editText2.getText().toString();
		String output="";op2 = "";
		String[] oplist=new String[20];
		for (int i=0;i < 20;i++) oplist[i] = "";
		List alist=new ArrayList();
		output = "以下为您显示从" + start + "站到" + end + "站的最快线路:\n";

		//Calculate
		LineSearch lineSearch=new LineSearch();
		//JSON
		JSONObject myjson=null;
		try{
			myjson=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0).toString());
		}
		catch(Exception e){
			e.printStackTrace();
			checkf = new CheckDialogFragment();
			checkf.show(getFragmentManager(), "mydialog");
			checkf.setCancelable(false);
		}
		if (lineSearch.initlog(myjson,0) == 1&&checkf==null)
		{
			checkf = new CheckDialogFragment();
			checkf.show(getFragmentManager(), "mydialog");
			checkf.setCancelable(false);
		}
		else if(checkf==null){
		int startn=lineSearch.FindStation(start);
		int endn=lineSearch.FindStation(end);
		if (startn * endn == 0)
		{
			err = 1;
			output = "您输入的车站不存在或还没启用，请重新确认后再试一次!";
		}
		else if (startn == endn)
		{
			err = 1;
			output = "起始站与到达站不能相同!";
		}
		else
		{
			request_statue = 1;
			lineSearch.Search(startn, endn);
		    int listn=0;
			op2 += "以下是Mytro为您推荐的由" + start + "站到" + end + "站的最快线路:\n";
			op2 += "起始站:" + lineSearch.slist[lineSearch.ans[1].st[0]].name + "。\n";
			oplist[listn++] = "起始站:" + lineSearch.slist[lineSearch.ans[1].st[0]].name + "";
			alist.add(oplist[listn - 1]);
			int cp = 0;
			for (int i = 1; i <= lineSearch.ans[1].wl[0]; i++)
			{
				if (i == lineSearch.ans[1].wl[0])
				{
					int cn=i - cp;
					op2 += "乘坐" + lineSearch.lineName[lineSearch.ans[1].wl[i]] + "(" + lineSearch.destination[lineSearch.ans[1].wl[i]][lineSearch.ans[1].ds[i]] + "方向)" + cn + "站至:" + lineSearch.slist[lineSearch.ans[1].st[i]].name;
                    oplist[listn++] = "乘坐" + lineSearch.lineName[lineSearch.ans[1].wl[i]] + "("  + lineSearch.destination[lineSearch.ans[1].wl[i]][lineSearch.ans[1].ds[i]] + "方向)" + cn + "站至:" + lineSearch.slist[lineSearch.ans[1].st[i]].name;
					alist.add(oplist[listn - 1]);
				}
				else if (lineSearch.ans[1].wl[i] != lineSearch.ans[1].wl[i + 1])
				{
					int cn=i - cp;
					cp = i;
					op2 += "乘坐" + lineSearch.lineName[lineSearch.ans[1].wl[i]] + "("  + lineSearch.destination[lineSearch.ans[1].wl[i]][lineSearch.ans[1].ds[i]] + "方向)" + cn + "站至:" + lineSearch.slist[lineSearch.ans[1].st[i]].name + "，换乘" + lineSearch.lineName[lineSearch.ans[1].wl[i + 1]] + "。\n";
					oplist[listn++] = "乘坐" + lineSearch.lineName[lineSearch.ans[1].wl[i]] + "("  + lineSearch.destination[lineSearch.ans[1].wl[i]][lineSearch.ans[1].ds[i]] + "方向)" + cn + "站至:" + lineSearch.slist[lineSearch.ans[1].st[i]].name + "，换乘" + lineSearch.lineName[lineSearch.ans[1].wl[i + 1]];
					alist.add(oplist[listn - 1]);
				}
			}
			int tz=(int)(lineSearch.ans[1].time / 60 + 0.5);
			if (tz == 0)
				tz = 1;
			op2 += "。\n到达终点，全程共" + lineSearch.ans[1].wl[0] + "站，预计时间:" + tz + "分钟。\n";
			oplist[listn++] = "到达终点，全程共" + lineSearch.ans[1].wl[0] + "站，预计时间:" + tz + "分钟";
			alist.add(oplist[listn - 1]);
		}
		//输出模块
		TextView textView1=(TextView) findViewById(R.id.sub2TextView1);
		textView1.setText(output);

		//List
		ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alist);
        ListView listView = (ListView) findViewById(R.id.sub2ListView1);
		listView.setAdapter(adapter1);
		}
	}

	public void onClickSub2ButtonMap(View view)
	{
		LineSearch lineSearch=new LineSearch();
		//JSON
		JSONObject myjson=null;
		try{
			myjson=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0).toString());
		}
		catch(Exception e){
			e.printStackTrace();
			checkf = new CheckDialogFragment();
			checkf.show(getFragmentManager(), "mydialog");
			checkf.setCancelable(false);
		}
		if (lineSearch.initlog(myjson,0) == 1&&checkf==null)
		{
			checkf = new CheckDialogFragment();
			checkf.show(getFragmentManager(), "mydialog");
			checkf.setCancelable(false);
		}
		else if(checkf==null)
		{
			Intent intent = new Intent(this, SubListActivity.class);
			intent.putExtra("sjtu", 1);
			SharedPreferences.Editor editor= getSharedPreferences("temp", MODE_WORLD_WRITEABLE).edit();
			EditText editText1=(EditText) findViewById(R.id.sub2EditText1);
			EditText editText2=(EditText) findViewById(R.id.sub2EditText2);
			String start=editText1.getText().toString();
			String end=editText2.getText().toString();
			editor.putString("startstation", start);
			editor.putString("endstation", end);
			editor.commit();
			startActivity(intent);
		}
	}


	public void onClickSub2Text(View view)
	{
		SecretDialogFragment f = new SecretDialogFragment();
		f.show(getFragmentManager(), "mydialog");
	}

	public void onClickSub2ButtonBack(View view)
	{
		finish();
	}


	public void onClickSub2ButtonShare(View view)
	{
		if (request_statue == 1)
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, op2);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(this, "没有可供分享的路线信息!", Toast.LENGTH_SHORT).show();
		}
	}

	public static void stastart()
	{
		int a=0;
		Message message = new Message(); 
		message.what = 1; 
		// handler.sendMessage(message);
	}

	

	Handler handler_finish= new Handler() { 
		public void handleMessage(Message msg)
		{ 
		    dialog.dismiss();
		    if (downloadsta == 1)
			{
				//刷新
				//Set Spinner
				LineSearch lineSearch=new LineSearch();
				//JSON
				JSONObject myjson=null;
				try{
					myjson=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0).toString());
				}
				catch(Exception e){
					e.printStackTrace();
					checkf = new CheckDialogFragment();
					checkf.show(getFragmentManager(), "mydialog");
					checkf.setCancelable(false);
				}
				if (lineSearch.initlog(myjson,0) == 1&&checkf==null)
				{
					checkf = new CheckDialogFragment();
					checkf.show(getFragmentManager(), "mydialog");
					checkf.setCancelable(false);
				}
				List spinnerlist=new ArrayList();
				spinnerlist.add("车站选择");
				for (int i=1;i <= lineSearch.slist[0].num;i++)
				{
					spinnerlist.add(lineSearch.slist[i].name);
				}
				SpinnerAdapter adapter = new ArrayAdapter<String>(Sub2Activity.this, android.R.layout.simple_spinner_dropdown_item, spinnerlist);
				//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
				Spinner spinner = (Spinner) findViewById(R.id.sub2Spinner1);
				spinner.setAdapter(adapter);
				Spinner spinner2 = (Spinner) findViewById(R.id.sub2Spinner2);
				spinner2.setAdapter(adapter);
				
				OkDialogFragment f = new OkDialogFragment();
				f.show(getFragmentManager(), "mydialog");
			}
			else
			{
				FailDialogFragment f = new FailDialogFragment();
				f.show(getFragmentManager(), "mydialog");
				f.setCancelable(false);
			}
		}
	};

	class OkDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("下载成功");
			builder.setMessage("在线地铁线路图下载并更新成功，您可以开始使用新的线路图啦！");
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

	class FailDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("下载出现问题:(");
			builder.setMessage("很抱歉，我们的下载没能成功。请检查一下您的网络连接再试吧！");
			builder.setPositiveButton("再试一次", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						//startDownload();
					}
				});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
					}
				});

			return builder.create();
		}

	}

	class CheckDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("需要线路图");
			builder.setMessage("您还没有地铁线路图或现有的线路图已损坏，请返回首页创建新的线路图或者把线路图数据保存在/storage/emulated/0/Mytro/lines.json。");
			builder.setPositiveButton("好的，我明白了", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						finish();
					}
				});

			return builder.create();
		}

	}

}

class SecretDialogFragment extends DialogFragment
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("关于地铁查询");
		builder.setMessage("        地铁查询由作者本人独立开发，采用改进版的Dijkstra算法获得最快路径。本软件的全部源代码已上传GitHub。如有改进方案，请与作者联系:\n邮箱:xiezhe20001128@sjtu.edu.cn");
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
