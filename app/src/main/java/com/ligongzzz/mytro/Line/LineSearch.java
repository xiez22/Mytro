package com.ligongzzz.mytro.Line;
import java.math.*;
import net.sf.json.*;
import com.ligongzzz.mytro.Tool.*;

public class LineSearch
{
	//计算基本常量
	public double transferTime=20.0;
	public double defaultSpeed=8.0;
	public double stopTime=5.0;

	public String mapName="";

	public String[] lineName=new String[100];

	//整形初始化
	public void init(int[] shuzu, int num, int value)
	{
		for (int i = 0; i < num; i++)
			shuzu[i] = value;
	}
	//浮点型初始化
	public void initd(double[] shuzu, int num, double value)
	{
		for (int i = 0; i < num; i++)
			shuzu[i] = value;
	}

	public String[][] destination=new String[100][2];//线路，方向，站名

	//有向图车站列表
	public newstation[] nslist=new newstation[500];

    // 车站列表
	public station[] slist=new station[500];

	// 路线大集合
	public passenger[] ans=new passenger[10];

	public int create_sta(station[] slist, double sx, double sy, String name, int n)
	{
		slist[n].num = n;
		slist[n].sx = sx;
		slist[n].sy = sy;
		slist[n].name = name;
		slist[0].num = n > slist[0].num ? n: slist[0].num;
		return n;
	}

	// 线路大集合
	public int[][] line=new int[100][100];

    //有向图：S集与U集
	public int[] sji=new int[500];
	public int[] uji=new int[500];

	//------------------------------

	//算法寻找路线
	public int dsearch(passenger[] ans, int start, int end)
	{
		//是否抵达
		if (start == end)
		{

			//写入数据
			//倒序数组
			int[] temp=new int[500];
			temp[0] = 1;
			temp[1] = end;
			for (;;)
			{
				//加入数组
				int ci=++temp[0];
				temp[ci] = nslist[temp[ci - 1]].shortcur;
				if (nslist[temp[ci]].shortcur == 0)
					break;
			}
			//写入ans集合
			ans[1].st[0] = nslist[temp[temp[0]]].origin;
			ans[1].wl[0] = 0;
			ans[1].time = nslist[end].time;// + transferTime;
			for (int i=temp[0] - 1;i > 0;i--)
			{
				//判断是否改变了车站
				if (nslist[temp[i]].origin != nslist[temp[i + 1]].origin)
				{
					//将该车站写入
					ans[1].st[++ans[1].wl[0]] = nslist[temp[i]].origin;
					ans[1].wl[ans[1].wl[0]] = nslist[temp[i]].line;
					//判断隧道信息
					for (int m=1;m <= nslist[temp[i + 1]].tunnelsta[0];m++)
					{
						if (nslist[temp[i + 1]].tunnelsta[m] == temp[i])
						{
							ans[1].ds[ans[1].wl[0]] = nslist[temp[i + 1]].tunnelline[m];
							break;
						}
					}
				}
			}
			return 0;
		}
		sji[++sji[0]] = start;
		uji[start] = 0;
		//修改里程
		for (int i=1;i <= nslist[start].tunnelsta[0];i++)
		{
			double deltat=nslist[start].tunneltime[i];
			if (deltat + nslist[start].time < nslist[nslist[start].tunnelsta[i]].time)
			{
				//修改里程
				nslist[nslist[start].tunnelsta[i]].time = deltat + nslist[start].time;//修改父节点
				nslist[nslist[start].tunnelsta[i]].shortcur = start;
			}
		}
		int smallpoint=0;
//寻找最小U值
		for (int i=1;i <= uji[0];i++)
		{
			if ((smallpoint == 0 || nslist[i].time < nslist[smallpoint].time) && uji[i] != 0)
			{
				int q=uji[0];
				smallpoint = i;
			}
		}
//递归
		dsearch(ans, smallpoint, end);
		return 0;
	}

	//Create New Station From Slist
	public void dsearchnew(passenger[] ans, int start, int end)
	{
		for (int i=0;i <= slist[0].num;i++)
		{
			init(slist[i].newsta, 20, 0);
		}
		for (int i=0;i < 500;i++)
		{
			nslist[i].num = 0;
			nslist[i].time = 99999.0;
			nslist[i].line = 0;
			nslist[i].shortcur = 0;
			nslist[i].origin = 0;
			init(nslist[i].tunnelsta, 20, 0);
			init(nslist[i].tunnelline, 20, 0);
			initd(nslist[i].tunneltime, 20, 0.0);
		}
		for (int i=0;i < 10;i++)
		{
			ans[i].num = 0;
			ans[i].time = 0.0;
			init(ans[i].wl, 20, 0);
			init(ans[i].st, 20, 0);
			init(ans[i].ds, 20, 0);
		}
		//先列举
		for (int i=1;i <= line[0][0];i++)
		{
			for (int j=1;j <= line[i][0];j++)
			{
				int num=++nslist[0].num;
				nslist[num].num = num;
				nslist[num].origin = line[i][j];
				nslist[num].line = i;
				slist[line[i][j]].newsta[++slist[line[i][j]].newsta[0]] = num;
				if (j > 1)
				{
					nslist[num].tunnelsta[++nslist[num].tunnelsta[0]] = num - 1;
					nslist[num].tunnelline[nslist[num].tunnelsta[0]] = 0;
					int m=line[i][j];
					double d= Math.sqrt(Math.pow(slist[line[i][j - 1]].sx - slist[line[i][j]].sx, 2.0) + Math.pow(slist[line[i][j - 1]].sy - slist[line[i][j]].sy, 2.0));
					nslist[num].tunneltime[nslist[num].tunnelsta[0]] = d / defaultSpeed + stopTime;
				}
				if (j < line[i][0])
				{
					nslist[num].tunnelsta[++nslist[num].tunnelsta[0]] = num + 1;
					nslist[num].tunnelline[nslist[num].tunnelsta[0]] = 1;
					int m=line[i][j];
					double d= Math.sqrt(Math.pow(slist[line[i][j + 1]].sx - slist[line[i][j]].sx, 2.0) + Math.pow(slist[line[i][j + 1]].sy - slist[line[i][j]].sy, 2.0));
					nslist[num].tunneltime[nslist[num].tunnelsta[0]] = d / defaultSpeed + stopTime;
				}
			}
		}
		//建立换乘通道
		for (int i=1;i <= nslist[0].num;i++)
		{
			for (int j=1;j <= slist[nslist[i].origin].newsta[0];j++)
			{
				if (slist[nslist[i].origin].newsta[j] != i)
				{
					int curPos=slist[nslist[i].origin].newsta[j];
					//int numSta=nslist[0].num;
					nslist[i].tunnelsta[++nslist[i].tunnelsta[0]] = curPos;
					nslist[i].tunneltime[nslist[i].tunnelsta[0]] = transferTime;
					nslist[i].tunnelline[nslist[i].tunnelsta[0]] = -1;
//特殊情况
					if (start == nslist[i].origin || end == nslist[i].origin||
					nslist[i].line==nslist[curPos].line)
					{
						nslist[i].tunneltime[nslist[i].tunnelsta[0]] = 0.0;
					}
					//double lenTun=nslist[i].tunneltime[nslist[i].tunnelsta[0]];
					//continue;
				}
			}
		}
//初始化集合
		init(sji, 500, 0);
		for (int i=0;i < 500;i++)
		{
			uji[i] = i;
		}
		uji[0] = 499;
//初始化起点站时间
		nslist[slist[start].newsta[1]].time = 0.0;
//调用函数
		dsearch(ans, slist[start].newsta[1], slist[end].newsta[1]);
	}

//Find Station
	public int FindStation(String sta)
	{
		for (int i=1;i <= slist[0].num;i++)
		{
			if (slist[i].name.equals(sta))
				return i;
		}
		return 0;
	}

//-------------------------------

	public void Search(int startn, int endn)
	{
		dsearchnew(ans, startn, endn);
		dsearchnew(ans, startn, endn);
	}

	public LineSearch()
	{
		//init
		for (int i=0;i < 500;i++)
			nslist[i] = new newstation();
		for (int i=0;i < 10;i++)
			ans[i] = new passenger();
		for (int i=0;i < 500;i++)
			slist[i] = new station();
	}
	// 初始化车站
	/*slist[0].num = 0;
	 ans[0].wl[0]=0;
	 ans[0].st[0]=0;*/

	// 线路与车站
	//车站
	public int initlog(JSONObject myjson, int mode)
	{
		if (mode == 0)
		{
			try
			{
				try
				{
					//Statics
					defaultSpeed = myjson.getDouble("Speed");
					stopTime = myjson.getDouble("Stop Time");
					transferTime = myjson.getDouble("Transfer Time");

					mapName = myjson.getString("Name");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				//Stations
				JSONArray jsStation=myjson.getJSONArray("Station");
				for (int i=0;i < jsStation.size();i++)
				{
					JSONObject jsTemp=jsStation.getJSONObject(i);
					create_sta(slist, Double.parseDouble(jsTemp.getString("pointx")), Double.parseDouble(jsTemp.getString("pointy")), jsTemp.getString("name"),jsTemp.getInt("num"));
				}
				//Lines
				JSONArray jsLine=myjson.getJSONArray("Line");
				for (int i=0;i < jsLine.size();i++)
				{
					JSONObject jsTemp=jsLine.getJSONObject(i);
					int lineNum=jsTemp.getInt("num");
					destination[lineNum][0] = jsTemp.getString("destination0");
					destination[lineNum][1] = jsTemp.getString("destination1");
					lineName[lineNum] = jsTemp.getString("name");
					JSONArray jsArrayTemp=jsTemp.getJSONArray("station");
					for (int j=0;j < jsArrayTemp.size();j++)
					{
						line[lineNum][j] = jsArrayTemp.get(j);
					}
					//Change
					if (lineNum > line[0][0])
					{
						line[0][0] = lineNum;
					}
				}

				//生成车站表格
				for (int s=1;s <= slist[0].num;s++)
				{
					for (int i=1;i <= line[0][0];i++)
					{
						for (int j=1;j <= line[i][0];j++)
						{
							if (line[i][j] == s)
							{
								int n=slist[s].sl[0] + 1;
								slist[s].sl[n] = i;
								slist[s].ss[n] = j;
								slist[s].sl[0] = n;
							}	
						}
					}
				}
				return 0;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return 1;
			}
		}
		return 1;
	}
}

