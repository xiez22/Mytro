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

public class EditorSub3Activity extends Activity
{
	//Constant
	int currentLine=-1;
	int selectStation=-1;
	JSONArray jsonList=new JSONArray();
	int sjtu=0;
	JSONObject jsonObject;

	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editorsub3);

		//获取信息
		Intent intent =getIntent();
        Bundle bundle= intent.getExtras();
        sjtu = bundle.getInt("sjtu");

		if (fileutil.read("Mytro/lines.json") != null)
		{
			jsonList = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0)).getJSONArray("Line").getJSONObject(sjtu).getJSONArray("station");
		}

	    //Init
		try
		{
			ListView listView = (ListView) findViewById(R.id.editorsub3ListView1);
			jsonObject = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
			final JSONArray jsonArray=jsonList;
			ArrayList names=new ArrayList();
			for (int i=1;i < jsonArray.size();i++)
			{
				String name="";
			    JSONArray jsonSta=jsonObject.getJSONArray("Station");
				for (int j=0;j < jsonSta.size();j++)
				{
					if (jsonArray.getInt(i) == jsonSta.getJSONObject(j).getInt("num"))
					{
						name = jsonSta.getJSONObject(j).getString("name");
					}
				}
				names.add(name);
			}
			//列表统一显示器
			ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
			listView.setAdapter(adapter1);
			//列表统一控制器
			listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> l, View v, int position, long id)
					{
						currentLine = position;
						String name="";
						JSONArray jsonSta=jsonObject.getJSONArray("Station");
						for (int j=0;j < jsonSta.size();j++)
						{
							if (jsonArray.getInt(position + 1) == jsonSta.getJSONObject(j).getInt("num"))
							{
								name = jsonSta.getJSONObject(j).getString("name");
							}
						}
						((TextView)findViewById(R.id.editorsub3TextView1)).setText(name);
					}
				});

			//Spinner
			ArrayList<String> allSta=new ArrayList<String>();
			allSta.add("轻按选择");
			JSONArray jsonSta=jsonObject.getJSONArray("Station");
			for (int i=0;i < jsonSta.size();i++)
			{
				allSta.add(jsonSta.getJSONObject(i).getString("name"));
			}
			SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allSta);
			//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			Spinner spinner = (Spinner) findViewById(R.id.editorsub3Spinner1);
			spinner.setAdapter(adapter);

			//设置动作
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> l, View v, int position, long id)
					{
						ArrayAdapter<String> adapter = (ArrayAdapter<String>) l.getAdapter();
						selectStation = position - 1;
					}
					//没有选中时的处理
					public void onNothingSelected(AdapterView<?> parent)
					{
					}
				});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	public void onClickEditorSub3Add(View view)
	{
		if (selectStation < 0||currentLine<0)
		{
			Toast.makeText(this, "请在下拉框中和列表中都选择一个车站!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			jsonList.set(0, jsonList.getInt(0) + 1);
			jsonList.add(currentLine+1, jsonObject.getJSONArray("Station").getJSONObject(selectStation).getInt("num"));

			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub3ListView1);
				jsonObject = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
				final JSONArray jsonArray=jsonList;
				ArrayList names=new ArrayList();
				for (int i=1;i < jsonArray.size();i++)
				{
					String name="";
					JSONArray jsonSta=jsonObject.getJSONArray("Station");
					for (int j=0;j < jsonSta.size();j++)
					{
						if (jsonArray.getInt(i) == jsonSta.getJSONObject(j).getInt("num"))
						{
							name = jsonSta.getJSONObject(j).getString("name");
						}
					}
					names.add(name);
				}
				//列表统一显示器
				ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
				listView.setAdapter(adapter1);
				((TextView)findViewById(R.id.editorsub3TextView1)).setText(names.get(currentLine).toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	public void onClickEditorSub3AddBehind(View view)
	{
		if (selectStation < 0)
		{
			Toast.makeText(this, "请在下拉框中选择一个车站!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			jsonList.set(0, jsonList.getInt(0) + 1);
			jsonList.add(currentLine+2, jsonObject.getJSONArray("Station").getJSONObject(selectStation).getInt("num"));

			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub3ListView1);
				jsonObject = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
				final JSONArray jsonArray=jsonList;
				ArrayList names=new ArrayList();
				for (int i=1;i < jsonArray.size();i++)
				{
					String name="";
					JSONArray jsonSta=jsonObject.getJSONArray("Station");
					for (int j=0;j < jsonSta.size();j++)
					{
						if (jsonArray.getInt(i) == jsonSta.getJSONObject(j).getInt("num"))
						{
							name = jsonSta.getJSONObject(j).getString("name");
						}
					}
					names.add(name);
				}
				//列表统一显示器
				ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
				listView.setAdapter(adapter1);
				currentLine++;
				((TextView)findViewById(R.id.editorsub3TextView1)).setText(names.get(currentLine).toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	public void onClickEditorSub3Delete(View view)
	{
		if (currentLine < 0)
		{
			Toast.makeText(this, "请选择要删除的车站!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			jsonList.set(0, jsonList.getInt(0)-1);
			jsonList.remove(currentLine+1);

			//Refresh
			currentLine = -1;
			
			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub3ListView1);
				jsonObject = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
				final JSONArray jsonArray=jsonList;
				ArrayList names=new ArrayList();
				for (int i=1;i < jsonArray.size();i++)
				{
					String name="";
					JSONArray jsonSta=jsonObject.getJSONArray("Station");
					for (int j=0;j < jsonSta.size();j++)
					{
						if (jsonArray.getInt(i) == jsonSta.getJSONObject(j).getInt("num"))
						{
							name = jsonSta.getJSONObject(j).getString("name");
						}
					}
					names.add(name);
				}
				//列表统一显示器
				ListAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
				listView.setAdapter(adapter1);
				((TextView)findViewById(R.id.editorsub3TextView1)).setText("");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	public void onClickEditorSub3Save(View view){
		JSONArray jsonLines=jsonObject.getJSONArray("Line");
		JSONObject jsonLine=jsonLines.getJSONObject(sjtu);
		jsonLine.put("station",jsonList);
		jsonLines.set(sjtu,jsonLine);
		jsonObject.put("Line",jsonLines);
		
		fileutil.write(jsonObject.toString(),"Mytro/lines.json",1);
		Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
		
		Intent intent=new Intent();
		setResult(2, intent);
	}
}
