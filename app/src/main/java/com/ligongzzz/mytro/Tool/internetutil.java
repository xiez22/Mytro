package com.ligongzzz.mytro.Tool;
import java.net.*;
import java.io.*;
import android.util.*;
import android.os.*;
import android.app.*;
import android.webkit.*;
import android.net.*;

public class internetutil
{
	String line=null;
    StringBuffer strBuffer=new StringBuffer();
    BufferedReader bufferReader=null;

    //下载小型的文档文件，返回文档的String字符串
    public String downloadFiles(String urlStr){
        try {
            InputStream inputStream=getInputStreamFromUrl(urlStr);
            bufferReader=new BufferedReader(new InputStreamReader(inputStream));
            while((line=bufferReader.readLine())!=null){
                strBuffer.append(line+'\n');
            }
        } catch (Exception e) {
            strBuffer.append("something is wrong!!");
            System.out.println("读取数据异常:"+e);
        } finally{
            try {
                bufferReader.close();
            } catch (Exception e) {
                strBuffer.append("something is wrong!!");
                e.printStackTrace();
            }
        }
        return strBuffer.toString();
    }

    //可以下载任意文件，例如MP3，并把文件存储在制定目录（-1：下载失败，0：下载成功，1：文件已存在）
    public int downloadFiles(String urlStr,String path,String fileName){
        try {
            fileutil fileUtils=new fileutil();
            if(fileUtils.isFileExist(fileName,path)) return 1;//判断文件是否存在
            else{
                InputStream inputStream=getInputStreamFromUrl(urlStr);
                File resultFile=fileUtils.write2SDFromInput(fileName,path,inputStream);
                if(resultFile==null) return -1;
            }
        } catch (Exception e) {
            System.out.println("读写数据异常:"+e);
            return -1;
        }
        return 0;
    }

    public InputStream getInputStreamFromUrl(String urlStr)throws IOException {
        //创建一个URL对象
        URL url=new URL(urlStr);
        //创建一个HTTP链接
        HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
        //使用IO流获取数据
        InputStream inputStream=urlConn.getInputStream();
        return inputStream;
    }
	
	
	//-------------------------------
	
	//下载具体操作
    public static int download(String downloadUrl,String dir,String name) {
        try {
            URL url = new URL(downloadUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //打开输入流
            InputStream is = conn.getInputStream();
            //获得长度
            int contentLength = conn.getContentLength();
            //Log.e(TAG, "contentLength = " + contentLength);
            //创建文件夹 MyDownLoad，在存储卡下
            String dirName = Environment.getExternalStorageDirectory() +dir;
            File file = new File(dirName);
            //不存在创建
            if (!file.exists()) {
                file.mkdir();
            }
            //下载后的文件名
            String fileName = dirName +name;
            File file1 = new File(fileName);
            if (file1.exists()) {
                file1.delete();
            }
            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            //完成后关闭流
            //Log.e(TAG, "download-finish");
            os.close();
            is.close();
			return 0;
        } catch (Exception e) {
            e.printStackTrace();
			return 1;
		}
    }
	
}
