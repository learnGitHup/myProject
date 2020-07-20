package com.example.common.ftp.client;

import com.example.common.ftp.core.FTPClientPool;
import com.example.common.ftp.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * ftp客户端辅助bean  
 * @author jelly
 *
 */
@Slf4j
@Component
public class FTPClientHelper {

	private FTPClientPool ftpClientPool;

    public void setFtpClientPool(FTPClientPool ftpClientPool) {
		this.ftpClientPool = ftpClientPool;
	}
	
    /**
     * 下载remote文件流  
     * @param remote 远程文件
     * @return 字节数据
     * @throws Exception
     */
	public byte[] retrieveFileStream(String remote) throws Exception {
		FTPClient client=null;
		InputStream in =null;
	    try {
	    	  long start =System.currentTimeMillis();
	    	   client=	ftpClientPool.borrowObject();
	    	   in=client.retrieveFileStream(remote);
             
	    	  long end =System.currentTimeMillis();
	    	  log.info("ftp下载耗时(毫秒):"+(end-start));
	    	
	    	  if(in != null){
	    		  return   ByteUtil.inputStreamToByteArray(in);
	    	  }else{
	    		  return new byte[0];
	    	  }
	    	  
	    	 
		}catch(Exception e){
			log.error("获取ftp下载流异常",e);
		}finally{
			  if (in != null) {  
	                in.close();  
	            } 
			  if(client != null){
				  client.logout();  
				  client.disconnect();
			  }
			  
			ftpClientPool.returnObject(client);
		}
	    return null;
	}
	
	/**
	 * 创建目录    单个不可递归
	 * @param pathname 目录名称
	 * @return
	 * @throws Exception
	 */
	public boolean makeDirectory(String pathname) throws Exception {
		
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.makeDirectory(pathname);
		} catch(Exception e){
			log.error("创建目录失败",e);
			throw e;
		} finally{
			ftpClientPool.returnObject(client);
		}
	}
	
	/**
	 * 删除目录，单个不可递归
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	public   boolean removeDirectory(String pathname) throws Exception {
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.removeDirectory(pathname);
		} catch(Exception e){
			log.error("删除目录失败",e);
			throw e;
		}  finally{
			ftpClientPool.returnObject(client);
		}
	}
	
	/**
	 * 删除文件 单个 ，不可递归 
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFile(String pathname) throws Exception {
		
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.deleteFile(pathname);
		} catch(Exception e){
			log.error("创建文件失败",e);
			throw e;
		} finally{
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 删除指定目录下的多个文件
	 * @param remote ftp目录
	 * @param filenames 文件名集合
	 * @return
	 */
	public boolean deleteFile(String remote, List<String> filenames){
		FTPClient client=null;
		try {
			client=	ftpClientPool.borrowObject();
			client.changeWorkingDirectory(remote);
			for (String filename : filenames) {
				client.dele(filename);
				log.info("ftp文件:{},删除成功!",filename);
			}
			return true;
		} catch(Exception e){
			log.error("ftp删除文件失败",e);
			return false;
		} finally{
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 上传文件 目录不存在则递归创建
	 * @param remote 文件在ftp的保存路径--不以 / 结尾 例：/aa/bb/cc
	 * @param file	要上传到ftp的文件
	 * @param ftpRootPath ftp的宿主目录
	 * @return
	 * @throws Exception
	 */
	public boolean storeFile(String remote, File file,String ftpRootPath){
		FTPClient client=null;
		FileInputStream fileInputStream =null;
		boolean result = true;
		try {
			fileInputStream = new FileInputStream(file);
			client=	ftpClientPool.borrowObject();
			CreateDirecroty(remote,client,ftpRootPath);
			client.changeWorkingDirectory(remote);
			result = client.storeFile(file.getName(), fileInputStream);
		} catch(Exception e){
			log.error("上传ftp失败，路径:{},文件:{}", remote,file.getName(),e);
			result = false;
		}  finally{
			if(fileInputStream!=null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					log.error("上传ftp关闭流异常",e);
				}
			}
			ftpClientPool.returnObject(client);
			try {
				client.disconnect();
				client = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

//			log.info("ftp上传成功，路径为{}",remote);
		}
		return result;
	}



	/**
	 * add by Si LingChao  2019/12/02
	 * 上传文件 目录不存在则递归创建
	 * @param remote 文件在ftp的保存路径--不以 / 结尾 例：/aa/bb/cc
	 * @param inputStream	要上传到ftp的文件流
	 * @param ftpRootPath ftp的宿主目录
	 * 路方的文件
	 * @return
	 * @throws Exception
	 */
	public boolean storeFile(String remote, InputStream inputStream, String ftpRootPath, String fileName) {
		FTPClient client=null;
		try {
			client=	ftpClientPool.borrowObject();
			CreateDirecroty(remote,client,ftpRootPath);
			client.changeWorkingDirectory(remote);
			return client.storeFile(fileName, inputStream);
		} catch(Exception e){
			log.error("上传ftp失败，路径:{},文件:{}", remote,fileName,e);
			return false;
		}  finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("上传ftp关闭流异常",e);
				}
			}
			ftpClientPool.returnObject(client);
			try {
				client.disconnect();
				client = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("ftp上传成功，路径为{}",remote);
		}
	}




	/***
	 * 上传Ftp文件
	 *
	 * @param localFile 当地文件
	 * @param remotePath 上传服务器路径 - 应该以/结束
	 * @return true or false
	 */
	public boolean uploadFile(File localFile, String remotePath) {
		FTPClient ftpClient = null;
		BufferedInputStream inStream = null;
		try {
			//从池中获取对象
			ftpClient = ftpClientPool.borrowObject();
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				log.warn("ftpServer refused connection, replyCode:"+ replyCode);
				return false;
			}
			// 改变工作路径
			ftpClient.changeWorkingDirectory(remotePath);
			inStream = new BufferedInputStream(new FileInputStream(localFile));
			log.info("start upload... "+localFile.getName());

			final int retryTimes = 3;

			for (int j = 0; j <= retryTimes; j++) {
				boolean success = ftpClient.storeFile(localFile.getName(), inStream);
				if (success) {
					log.info("upload file success!"+localFile.getName());
					return true;
				}
				log.warn("upload file failure! try uploading again...  times"+ j);
			}

		} catch (FileNotFoundException e) {
			log.error("file not found!"+localFile);
		} catch (Exception e) {
			log.error("upload file failure!", e);
		} finally {
			//将对象放回池中
			ftpClientPool.returnObject(ftpClient);
		}
		return false;
	}
	

	/**
	 * 如果ftp服务器没有指定路径则创建
	 *
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	private boolean CreateDirecroty(String remote,FTPClient ftpClient,String ftpRootPath) throws IOException {
		boolean success = true;
		String directory = remote + "/";
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory),ftpClient)) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			String path = "";
			String paths = "";
			while (true){
				boolean b = ftpClient.changeWorkingDirectory(ftpRootPath);
				log.info("切换根路径目录：{},结果：{}",ftpRootPath,b);
				if (b){
					break;
				}
			}
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
				path = path + "/" + subDirectory;
				if (!existFile(path,ftpClient)) {
					if (makeDirectory(subDirectory,ftpClient)) {
						changeWorkingDirectory(subDirectory,ftpClient);
					} else {
						changeWorkingDirectory(subDirectory,ftpClient);
					}
				} else {
					changeWorkingDirectory(subDirectory,ftpClient);
				}

				paths = paths + "/" + subDirectory;
				start = end + 1;
				end = directory.indexOf("/", start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	// 判断ftp服务器文件是否存在
	private boolean existFile(String path,FTPClient ftpClient) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}

	// 改变目录路径
	private boolean changeWorkingDirectory(String directory,FTPClient ftpClient) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
//				log.info("ftp进入文件夹{}成功",directory);
			} else {
//				log.info("ftp进入文件夹{}失败！开始创建文件夹",directory);
			}
		} catch (IOException e) {
			log.error("切换ftp路径异常",e);
		}
		return flag;
	}

	// 创建目录
	private boolean makeDirectory(String dir,FTPClient ftpClient) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			/*if (flag) {
				log.info("ftp创建文件夹{}成功",dir);
			} else {
				log.info("ftp创建文件夹{}失败",dir);
			}*/
		} catch (Exception e) {
			log.error("ftp创建文件夹异常",e);
		}
		return flag;
	}


	/**
	 * 下载文件--路径不以 / 结尾
	 * @param remotePath
	 * @return
	 */
	public boolean downloadFile(String remotePath){
		FTPClient client=null;
		try {
			client=	ftpClientPool.borrowObject();
			// 转移到FTP服务器目录至指定的目录下
			boolean gbk = client.changeWorkingDirectory(new String(remotePath.getBytes("gbk"), "iso-8859-1"));
			FTPFile[] ftpFiles = client.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				if(ftpFile.getName().endsWith(".json")){
					File localFile = new File(remotePath + "/" + ftpFile.getName());
					if(!localFile.exists()){
						localFile.createNewFile();
					}
					OutputStream is = new FileOutputStream(localFile);
					client.retrieveFile(ftpFile.getName(), is);
					is.close();
					log.info("文件:{}从ftp下载成功!",localFile.getAbsolutePath());
				}

			}
			return true;
		}catch(Exception e){
			log.error("ftp下载文件异常",e);
			return false;
		}finally{
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 判断文件夹是否存在
	 * @param ftpPath
	 * @return
	 */
	public boolean isExsits(String ftpPath){
		FTPClient ftpClient = null;
		try {
			//从池中获取对象
			ftpClient = ftpClientPool.borrowObject();
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				log.warn("ftpServer refused connection, replyCode:"+ replyCode);
				return false;
			}
			FTPFile[] files =ftpClient.listFiles(ftpPath);
			if(files!=null&&files.length>0){
				System.out.println("files size:"+files[0].getSize());
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			log.error("upload file failure!", e);
		} finally {
			//将对象放回池中
			ftpClientPool.returnObject(ftpClient);
		}
		return false;
	}


	/**
	 * 下载文件
	 *
	 * 不进行json文件的判断
	 *
	 * @param remotePath 文件路径（含文件名）
	 *
	 * @author Deng Xianghai
	 * @return 文件File
	 */
	public boolean downloadFtpFile(String remotePath){

		//将文件名和路径分离
		//获取文件名
		String fileName=remotePath.substring(remotePath.lastIndexOf(File.separator)+1,remotePath.length());
		//获取文件所在文件夹路径
		String filePath=remotePath.substring(0,remotePath.lastIndexOf(File.separator));

		FTPClient client=null;
		try {
			client=	ftpClientPool.borrowObject();
			while (true){
				boolean b = client.changeWorkingDirectory("/home/etc/ftp");
				//boolean b = client.changeWorkingDirectory("/home/ftp_recv");
				log.info("切换根路径--目录：{},结果：{}","/home/etc/ftp",b);
				if (b){
					break;
				}
			}
			// 转移到FTP服务器目录至指定的目录下
			client.changeWorkingDirectory(new String(filePath.substring(1).getBytes("gbk"),"iso-8859-1"));
			FTPFile[] ftpFiles = client.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				//如果文件夹下文件名
				if(ftpFile.getName().equals(fileName)){

					File localFile = new File(remotePath);
					File local=new File(filePath);
					if(!localFile.exists()){
						local.mkdirs();
						localFile.createNewFile();
					}
					OutputStream is = new FileOutputStream(localFile);
					client.retrieveFile(ftpFile.getName(), is);
					is.close();
					log.info("文件:从ftp下载成功!",localFile.getAbsolutePath());
					return true;
				}
			}
			return false;
		}catch(Exception e){
			log.error("ftp下载文件异常",e);
			return false;
		}finally{
			ftpClientPool.returnObject(client);
		}
	}


	/**
	 * FTP下载文件的操作
	 * ---添加根路径的参数
	 * @param remotePath
	 * @param ftpRootPath
	 * @return
	 */
	public boolean downloadFtpFile(String remotePath,String ftpRootPath){
		//将文件名和路径分离
		//获取文件名
		String fileName=remotePath.substring(remotePath.lastIndexOf("/")+1,remotePath.length());
		//获取文件所在文件夹路径
		String filePath=remotePath.substring(0,remotePath.lastIndexOf("/"));

		FTPClient client=null;
		try {
			client=	ftpClientPool.borrowObject();
			while (true){
				boolean b = client.changeWorkingDirectory(ftpRootPath);
				log.info("切换根路径目录：{},结果：{}",ftpRootPath,b);
				if (b){
					break;
				}
			}
			// 转移到FTP服务器目录至指定的目录下
			client.changeWorkingDirectory(new String(filePath.substring(1).getBytes("gbk"),"iso-8859-1"));
			FTPFile[] ftpFiles = client.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				//如果文件夹下文件名
				if(ftpFile.getName().equals(fileName)){

					File localFile = new File(remotePath);
					File local=new File(filePath);
					if(!localFile.exists()){
						local.mkdirs();
						localFile.createNewFile();
					}
					OutputStream is = new FileOutputStream(localFile);
					client.retrieveFile(ftpFile.getName(), is);
					is.close();
					log.info("文件:从ftp下载成功!",localFile.getAbsolutePath());
					return true;
				}
			}
			return false;
		}catch(Exception e){
			log.error("ftp下载文件异常",e);
			return false;
		}finally{
			ftpClientPool.returnObject(client);
		}
	}
   
}
