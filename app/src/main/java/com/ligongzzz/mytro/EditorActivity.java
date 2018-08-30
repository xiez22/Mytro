package com.ligongzzz.mytro;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;
import com.ligongzzz.mytro.Tool.*;
import net.sf.json.*;

public class EditorActivity extends Activity
{
	protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
		
		//初始化
		try{
		JSONObject jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
		
		EditText editText1=(EditText)findViewById(R.id.editorEditText1);
		EditText editText2=(EditText)findViewById(R.id.editorEditText2);
		EditText editText3=(EditText)findViewById(R.id.editorEditText3);
		EditText editText4=(EditText)findViewById(R.id.editorEditText4);
		
		editText1.setText(jsonObject.getString("Name"));
		editText2.setText(jsonObject.getString("Speed"));
		editText3.setText(jsonObject.getString("Stop Time"));
		editText4.setText(jsonObject.getString("Transfer Time"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onClickEditorSave(View view){
		JSONObject jsonObject=null;
		try{
			jsonObject=JSONObject.fromObject(fileutil.read("Mytro/lines.json").get(0));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		jsonObject.put("Name",((EditText)findViewById(R.id.editorEditText1)).getText().toString());
		jsonObject.put("Speed",((EditText)findViewById(R.id.editorEditText2)).getText().toString());
		jsonObject.put("Stop Time",((EditText)findViewById(R.id.editorEditText3)).getText().toString());
		jsonObject.put("Transfer Time",((EditText)findViewById(R.id.editorEditText4)).getText().toString());
		
		fileutil.write(jsonObject.toString(),"Mytro/lines.json",1);
		
		Toast.makeText(this,"成功保存!",Toast.LENGTH_SHORT).show();
	}
	
	public void onClickEditorButton1(View view)
	{
		Intent intent = new Intent(this, EditorSub1Activity.class);
    	startActivity(intent);
	}
	
	public void onClickEditorButton2(View view)
	{
		Intent intent = new Intent(this, EditorSub2Activity.class);
    	startActivity(intent);
	}
}
