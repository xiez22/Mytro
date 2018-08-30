package com.ligongzzz.mytro;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;
import com.ligongzzz.mytro.Tool.*;
import net.sf.json.*;
import java.util.*;
import android.widget.AdapterView.*;

public class EditorSub2Activity extends Activity
{
	//Constant
	JSONArray jsonList=new JSONArray();
	int currentStation=-1;

	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editorsub2);

		if (fileutil.read("Mytro/lines.json") != null)
		{
			jsonList = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0)).getJSONArray("Station");
		}

	    //Init
		try
		{
			ListView listView = (ListView) findViewById(R.id.editorsub2ListView1);
			JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
			final JSONArray jsonArray=jsonList;
			ArrayList names=new ArrayList();
			for (int i=0;i < jsonArray.size();i++)
			{
				JSONObject jsonTemp=jsonArray.getJSONObject(i);
				names.add(jsonTemp.getString("name"));
			}
			//列表统一显示器
			ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
			listView.setAdapter(adapter1);
			//列表统一控制器
			listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> l, View v, int position, long id)
					{
						currentStation = position;
						((EditText)findViewById(R.id.editorsub2EditText1)).setText(jsonArray.getJSONObject(position).getString("name"));
						((EditText)findViewById(R.id.editorsub2EditText2)).setText(jsonArray.getJSONObject(position).getString("pointx"));
						((EditText)findViewById(R.id.editorsub2EditText3)).setText(jsonArray.getJSONObject(position).getString("pointy"));
					}
				});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	public void onClickEditorSub2Add(View view)
	{
		JSONObject addObject=new JSONObject();
		addObject.put("name", "未命名");
		addObject.put("pointx", "0.0");
		addObject.put("pointy", "0.0");
		//num
		for (int i=1;;i++)
		{
			boolean f=false;
			for (int j=0;j < jsonList.size();j++)
			{
				if (jsonList.getJSONObject(j).getInt("num") == i)
				{
					f = true;
					break;
				}
			}
            if (!f)
			{
				addObject.put("num", i);
				//Toast.makeText(this,Integer.toString(i),Toast.LENGTH_SHORT).show();
				break;
			}
		}

		jsonList.add(addObject);
		//Refresh
		currentStation = jsonList.size() - 1;
		((EditText)findViewById(R.id.editorsub2EditText1)).setText(jsonList.getJSONObject(jsonList.size() - 1).getString("name"));
		((EditText)findViewById(R.id.editorsub2EditText2)).setText(jsonList.getJSONObject(jsonList.size() - 1).getString("pointx"));
		((EditText)findViewById(R.id.editorsub2EditText3)).setText(jsonList.getJSONObject(jsonList.size() - 1).getString("pointy"));

		try
		{
			ListView listView = (ListView) findViewById(R.id.editorsub2ListView1);
			JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
			final JSONArray jsonArray=jsonList;
			ArrayList names=new ArrayList();
			for (int i=0;i < jsonArray.size();i++)
			{
				JSONObject jsonTemp=jsonArray.getJSONObject(i);
				names.add(jsonTemp.getString("name"));
			}
			//列表统一显示器
			ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
			listView.setAdapter(adapter1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	public void onClickEditorSub2Delete(View view)
	{
		if (currentStation < 0)
		{
			Toast.makeText(this, "请选择要删除的车站!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			RemoveDialogFragment f = new RemoveDialogFragment();
			f.show(getFragmentManager(), "mydialog");
			f.setCancelable(false);
		}
	}

	public void onClickEditorSub2Apply(View view)
	{
		if (currentStation < 0)
		{
			Toast.makeText(this, "请选择一个车站!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			JSONObject addObject=new JSONObject();
			addObject.put("name", ((EditText)findViewById(R.id.editorsub2EditText1)).getText().toString());
			addObject.put("pointx", ((EditText)findViewById(R.id.editorsub2EditText2)).getText().toString());
			addObject.put("pointy", ((EditText)findViewById(R.id.editorsub2EditText3)).getText().toString());
			addObject.put("num", jsonList.getJSONObject(currentStation).getInt("num"));

			jsonList.set(currentStation, addObject);

			//Refresh
			/*((EditText)findViewById(R.id.editorsub2EditText1)).setText("");
			 ((EditText)findViewById(R.id.editorsub2EditText2)).setText("");
			 ((EditText)findViewById(R.id.editorsub2EditText3)).setText("");*/

			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub2ListView1);
				JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
				final JSONArray jsonArray=jsonList;
				ArrayList names=new ArrayList();
				for (int i=0;i < jsonArray.size();i++)
				{
					JSONObject jsonTemp=jsonArray.getJSONObject(i);
					names.add(jsonTemp.getString("name"));
				}
				//列表统一显示器
				ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
				listView.setAdapter(adapter1);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}

	public void onClickEditorSub2Save(View view)
	{
		//Explore
		boolean[] stationExist=new boolean[500];
		for (int i=0;i < 500;i++)
		{
			stationExist[i] = false;
		}
		for (int i=0;i < jsonList.size();i++)
		{
			stationExist[jsonList.getJSONObject(i).getInt("num")] = true;
		}

		//Change
		JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
		JSONArray jsonLine=jsonObject.getJSONArray("Line");
		for (int i=0;i < jsonLine.size();i++)
		{
			JSONObject jsonL=jsonLine.getJSONObject(i);
			JSONArray jsonS=jsonL.getJSONArray("station");
			for (int j=1;j < jsonS.size();j++)
			{
				if (!stationExist[jsonS.getInt(j)])
				{
					jsonS.set(0, jsonS.getInt(0) - 1);
					jsonS.remove(j);
				}
			}
			//Package
			jsonL.put("station", jsonS);
			jsonLine.set(i, jsonL);
		}
		jsonObject.put("Line", jsonLine);
		jsonObject.put("Station", jsonList);

		//Write
		fileutil.write(jsonObject.toString(), "Mytro/lines.json", 1);
		Toast.makeText(this, "成功保存!", Toast.LENGTH_SHORT).show();
	}

	//Handlers
	Handler handler= new Handler() { 
		public void handleMessage(Message msg)
		{ 
		    jsonList.remove(currentStation);

			//Refresh
			currentStation = -1;
			((EditText)findViewById(R.id.editorsub2EditText1)).setText("");
			((EditText)findViewById(R.id.editorsub2EditText2)).setText("");
			((EditText)findViewById(R.id.editorsub2EditText3)).setText("");

			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub2ListView1);
				JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
				final JSONArray jsonArray=jsonList;
				ArrayList names=new ArrayList();
				for (int i=0;i < jsonArray.size();i++)
				{
					JSONObject jsonTemp=jsonArray.getJSONObject(i);
					names.add(jsonTemp.getString("name"));
				}
				//列表统一显示器
				ListAdapter adapter1 = new ArrayAdapter<String>(EditorSub2Activity.this, android.R.layout.simple_list_item_1, names);
				listView.setAdapter(adapter1);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	};

	class RemoveDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("删除车站");
			builder.setMessage("删除该车站，所有途径线路中的该车站也将被删除。");
			builder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						Toast.makeText(EditorSub2Activity.this, "删除成功!", Toast.LENGTH_SHORT).show();
						Message message = new Message(); 
						handler.sendMessage(message);
					}
				});
			builder.setNegativeButton("不删啦", new DialogInterface.OnClickListener() {

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
