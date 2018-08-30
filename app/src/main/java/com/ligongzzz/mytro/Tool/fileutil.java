package com.ligongzzz.mytro.Tool;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.ligongzzz.mytro.*;
import java.io.*;
import java.util.*;
import android.view.Display.*;


public class fileutil{
// 文件写操作函数(0添加 1覆盖 2查验 3删除)
    public static int write(String content,String address,int mode) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 如果sdcard存在
            File file = new File(Environment.getExternalStorageDirectory()
								 .toString()
								 + File.separator+address); // 定义File类对象
            if (!file.getParentFile().exists()) { // 父文件夹不存在
                file.getParentFile().mkdirs(); // 创建文件夹
            }
			if(file.exists()&&mode==1){
				file.delete();
			}
			else if(file.exists()&&mode==2){
				return 1;
			}
			else if(file.exists()==false&&mode==2){
				return 0;
			}
			
			if(mode==3){
				file.delete();
				return 0;
			}
			
            PrintStream out = null; // 打印流对象用于输出
            try {
                out = new PrintStream(new FileOutputStream(file, true)); // 追加文件
                out.println(content);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close(); // 关闭打印流
                }
            }
        } else { // SDCard不存在，使用Toast提示用户
            //Toast.makeText(MainActivity.this, "保存失败，SD卡不存在！", Toast.LENGTH_LONG).show();
			return 1;
        }
		return 0;
    }

    // 文件读操作函数
    public static List read(String address) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 如果sdcard存在
            File file = new File(Environment.getExternalStorageDirectory()
								 .toString()
								 + File.separator+address); // 定义File类对象
            if (!file.getParentFile().exists()) { // 父文件夹不存在
                file.getParentFile().mkdirs(); // 创建文件夹
            }
            Scanner scan = null; // 扫描输入
            //StringBuilder sb = new StringBuilder();
			List list=new ArrayList();
            try {
                scan = new Scanner(new FileInputStream(file)); // 实例化Scanner
				scan.useDelimiter("\n");
                while (true) { // 循环读取
                    //sb.append(scan.next() + "\n"); // 设置文
					String temp=scan.next();
					if(temp.equals("")){
						break;
					}
					list.add(temp);
					if(scan.hasNext()==false){
						break;
					}
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (scan != null) {
                    scan.close(); // 关闭打印流
                }
            }
        } else { // SDCard不存在，使用Toast提示用户
            //Toast.makeText(this, "读取失败，SD卡不存在！", Toast.LENGTH_LONG).show();
			return null;
        }
        return null;
    }
	
	private String SDCardRoot;

    public fileutil(){
        //得到当前外部存储设备的目录
        SDCardRoot= Environment.getExternalStorageDirectory()+File.separator;
        //File.separator为文件分隔符”/“,方便之后在目录下创建文件
    }

    //在SD卡上创建文件
    public File createFileInSDCard(String fileName,String dir) throws IOException {
        File file=new File(SDCardRoot+dir+File.separator+fileName);
        file.createNewFile();
        return file;
    }

    //在SD卡上创建目录
    public File createSDDir(String dir)throws IOException{
        File dirFile=new File(SDCardRoot+dir);
        dirFile.mkdir();//mkdir()只能创建一层文件目录，mkdirs()可以创建多层文件目录
        return dirFile;
    }

    //判断文件是否存在
    public boolean isFileExist(String fileName,String dir){
        File file=new File(SDCardRoot+dir+File.separator+fileName);
        return file.exists();
    }

    //将一个InoutStream里面的数据写入到SD卡中
    public File write2SDFromInput(String fileName,String dir,InputStream input){
        File file=null;
        OutputStream output=null;
        try {
            //创建目录
            createSDDir(dir);
            //创建文件
            file=createFileInSDCard(fileName,dir);
            //写数据流
            output=new FileOutputStream(file);
            byte buffer[]=new byte[4*1024];//每次存4K
            int temp;
            //写入数据
            while((temp=input.read(buffer))!=-1){
                output.write(buffer,0,temp);
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("写数据异常："+e);
        }
        finally{
            try {
                output.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        return file;
    }
	
	//删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

}
