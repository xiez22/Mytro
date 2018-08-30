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

public class EditorSub1Activity extends Activity
{
	//Constant
	int currentLine=-1;
	JSONArray jsonList=new JSONArray();

	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editorsub1);

		if (fileutil.read("Mytro/lines.json") != null)
		{
			jsonList = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0)).getJSONArray("Line");
		}

	    //Init
		try
		{
			ListView listView = (ListView) findViewById(R.id.editorsub1ListView1);
			//JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
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
						currentLine = position;
						((EditText)findViewById(R.id.editorsub1EditText1)).setText(jsonArray.getJSONObject(position).getString("name"));
						((EditText)findViewById(R.id.editorsub1EditText2)).setText(jsonArray.getJSONObject(position).getString("destination0"));
						((EditText)findViewById(R.id.editorsub1EditText3)).setText(jsonArray.getJSONObject(position).getString("destination1"));
					}
				});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}

	public void onClickEditorSub1Add(View view)
	{
		JSONObject addObject=new JSONObject();
		JSONArray addSta=new JSONArray();
		addSta.add(0);
		addObject.put("name", "X号线");
		addObject.put("destination0", "起点");
		addObject.put("destination1", "终点");
		addObject.put("station",addSta);
		
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
				//Toast.makeText(this, Integer.toString(i), Toast.LENGTH_SHORT).show();
				break;
			}
		}

		jsonList.add(addObject);
		//Refresh
		currentLine = jsonList.size() - 1;
		((EditText)findViewById(R.id.editorsub1EditText1)).setText(jsonList.getJSONObject(jsonList.size() - 1).getString("name"));
		((EditText)findViewById(R.id.editorsub1EditText2)).setText(jsonList.getJSONObject(jsonList.size() - 1).getString("destination0"));
		((EditText)findViewById(R.id.editorsub1EditText3)).setText(jsonList.getJSONObject(jsonList.size() - 1).getString("destination1"));

		try
		{
			ListView listView = (ListView) findViewById(R.id.editorsub1ListView1);
			//JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
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

	public void onClickEditorSub1Delete(View view)
	{
		if (currentLine < 0)
		{
			Toast.makeText(this, "请选择要删除的线路!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			jsonList.remove(currentLine);

			//Refresh
			currentLine = -1;
			((EditText)findViewById(R.id.editorsub1EditText1)).setText("");
			((EditText)findViewById(R.id.editorsub1EditText2)).setText("");
			((EditText)findViewById(R.id.editorsub1EditText3)).setText("");

			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub1ListView1);
				//JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
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

	public void onClickEditorSub1Apply(View view)
	{
		if (currentLine < 0)
		{
			Toast.makeText(this, "请选择一条线路!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			JSONObject addObject=new JSONObject();
			addObject.put("name", ((EditText)findViewById(R.id.editorsub1EditText1)).getText().toString());
			addObject.put("destination0", ((EditText)findViewById(R.id.editorsub1EditText2)).getText().toString());
			addObject.put("destination1", ((EditText)findViewById(R.id.editorsub1EditText3)).getText().toString());
			addObject.put("num", jsonList.getJSONObject(currentLine).getInt("num"));
			addObject.put("station", jsonList.getJSONObject(currentLine).getJSONArray("station"));

			jsonList.set(currentLine, addObject);

			//Refresh
			/*((EditText)findViewById(R.id.editorsub2EditText1)).setText("");
			 ((EditText)findViewById(R.id.editorsub2EditText2)).setText("");
			 ((EditText)findViewById(R.id.editorsub2EditText3)).setText("");*/

			try
			{
				ListView listView = (ListView) findViewById(R.id.editorsub1ListView1);
				//JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
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

	public void onClickEditorSub1Save(View view)
	{
		//Change
		JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
		jsonObject.put("Line", jsonList);

		//Write
		fileutil.write(jsonObject.toString(), "Mytro/lines.json", 1);
		Toast.makeText(this, "成功保存!", Toast.LENGTH_SHORT).show();
	}

	public void onClickEditorSub1Stations(View view)
	{
		if (currentLine < 0)
		{
			Toast.makeText(this, "请选择一条线路!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			//Change
			JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
			jsonObject.put("Line", jsonList);

			//Write
			fileutil.write(jsonObject.toString(), "Mytro/lines.json", 1);
			
			Intent intent = new Intent(this, EditorSub3Activity.class);
			intent.putExtra("sjtu", currentLine);
			startActivityForResult(intent,2);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		//Toast.makeText(this,"Result!",Toast.LENGTH_SHORT).show();
		jsonList = JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0)).getJSONArray("Line");
	}

}

