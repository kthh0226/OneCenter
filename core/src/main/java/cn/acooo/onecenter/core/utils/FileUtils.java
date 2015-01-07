package cn.acooo.onecenter.core.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;


public class FileUtils {
	
	/** 
     * the traditional io way  
     * @param filename 
     * @return 
     * @throws IOException 
     */  
    public static byte[] toByteArray(String filename) throws IOException{
          
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());  
        BufferedInputStream in = null;  
        try{  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while(-1 != (len = in.read(buffer,0,buf_size))){  
                bos.write(buffer,0,len);  
            }  
            return bos.toByteArray();  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                in.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        }  
    }  
    
    /** 
     * NIO way 
     * @param filename 
     * @return 
     * @throws IOException 
     */  
    public static byte[] toByteArray2(String filename)throws IOException{  
          
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
          
        FileChannel channel = null;  
        FileInputStream fs = null;  
        try{  
            fs = new FileInputStream(f);  
            channel = fs.getChannel();  
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)channel.size());  
            while((channel.read(byteBuffer)) > 0){  
                // do nothing  
//              System.out.println("reading");  
            }  
            return byteBuffer.array();  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                channel.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
            try{  
                fs.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    /** 
     * Mapped File  way 
     * MappedByteBuffer 可以在处理大文件时，提升性能 
     * @param filename 
     * @return 
     * @throws IOException 
     */  
    public static byte[] toByteArrayByLargeFile(String filename)throws IOException{  
          
        FileChannel fc = null;  
        try{  
            fc = new RandomAccessFile(filename,"r").getChannel();  
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();  
            byte[] result = new byte[(int)fc.size()];  
            if (byteBuffer.remaining() > 0) {  
                byteBuffer.get(result, 0, byteBuffer.remaining());  
            }  
            return result;  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                fc.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	/**
	 * 保存文件
	 * @param stream
	 * @param path
	 * @throws IOException
	 */
    public static void SaveFileFromInputStream(InputStream stream,String path,String fileName) throws IOException
    {      
    	File pathfile = new File(path);
    	if(!pathfile.exists()){
    		if(!pathfile.mkdirs()){
    			throw new RuntimeException("mkdir error,path="+path);
    		}
    	}
    	String fullName = path+fileName;
    	File file = new File(fullName);
    	if(!file.exists()){
    		if(!file.createNewFile()){
    			throw new RuntimeException("create new file error,name=="+fullName);
    		}
    	}

        FileOutputStream fs = new FileOutputStream(fullName);
        byte[] buffer =new byte[1024*1024];
        int byteread = 0; 
        while ((byteread=stream.read(buffer))!=-1)
        {
           fs.write(buffer,0,byteread);
           fs.flush();
        } 
        fs.close();
        stream.close();      
    }      
	
    /**
     * 
     * @param bytes
     * @param path
     * @param fileName
     * @throws IOException
     */
    public static File SaveFileFromInputStream(byte[] bytes,String path,String fileName) throws IOException
    {      
    	File pathfile = new File(path);
    	if(!pathfile.exists()){
    		if(!pathfile.mkdirs()){
    			throw new RuntimeException("mkdir error,path="+path);
    		}
    	}
    	String fullName = path+fileName;
    	File file = new File(fullName);
    	if(!file.exists()){
    		if(!file.createNewFile()){
    			throw new RuntimeException("create new file error,name=="+fullName);
    		}
    	}

        FileOutputStream fs = new FileOutputStream(fullName);
        fs.write(bytes);
        fs.flush();
        fs.close();

        return file;
    }

    /**
     * 下载文件
     * @param url            http://www.xxx.com/img/333.jpg
     * @param destFileName   xxx.jpg/xxx.png/xxx.txt
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void getFileByHttp(String url, String destFileName)
            throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        File file = new File(destFileName);
        FileOutputStream fout = new FileOutputStream(file);
        try {
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
            }
            fout.flush();
        } finally {
            // 关闭低层流。
            in.close();
            fout.close();
            httpClient.getConnectionManager().shutdown();
        }
    }
}
