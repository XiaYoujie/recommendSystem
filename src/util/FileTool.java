package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entity.Item;
import entity.Score;
import entity.User;


public class FileTool {
	
	public static FileReader fr=null;
	public static BufferedReader br=null;
	public static String line=null;
	
	public static FileOutputStream fos1 = null,fos2 = null,fos3 = null;
	public static PrintStream ps1 = null,ps2 = null,ps3 = null;
	
	public static int count = 0;
	
	/** 
	 * 初始化写文件器(单一指针)
	 * */
	public static void initWriter1(String writePath) {
		try {
			//FileOutputStream创建一个文件并向文件中写数据,字节流
			//如果该流在打开文件进行输出前，目标文件不存在，那么该流会创建该文件
			fos1 = new FileOutputStream(writePath);
			//PrintStream 是用来装饰其它输出流。它能为其他输出流添加了功能，使它们能够方便地打印各种数据值表示形式
			ps1 = new PrintStream(fos1);
			//创建写文件器ps1完成
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器(单一指针)
	 * */
	public static void closeRedaer() {
		try {
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器(单一指针)
	 * */
	public static void closeWriter1() {
		try {
			ps1.close();
			fos1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 初始化写文件器(双指针)
	 * */
	public static void initWriter2(String writePath1,String writePath2) {
		try {
			fos1 = new FileOutputStream(writePath1);
			ps1 = new PrintStream(fos1);
			fos2 = new FileOutputStream(writePath2);
			ps2 = new PrintStream(fos2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器(双指针)
	 * */
	public static void closeWriter2() {
		try {
			ps1.close();
			fos1.close();
			ps2.close();
			fos2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 初始化写文件器(三指针)
	 * */
	public static void initWriter3(String writePath1,String writePath2,String writePath3) {
		try {
			fos1 = new FileOutputStream(writePath1);
			ps1 = new PrintStream(fos1);
			fos2 = new FileOutputStream(writePath2);
			ps2 = new PrintStream(fos2);
			fos3 = new FileOutputStream(writePath3);
			ps3 = new PrintStream(fos3);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器(三指针)
	 * */
	public static void closeWriter3() {
		try {
			ps1.close();
			fos1.close();
			ps2.close();
			fos2.close();
			ps3.close();
			fos3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static List readFileOne(String path,boolean isTitle,String token,String pattern) throws Exception {
		List<Object> ret = new ArrayList<Object>();
		
		fr = new FileReader(path);//path为user.csv，以字符流读入
		br = new BufferedReader(fr);//减少io次数，有readline方法
		int count = 0,i = 0;
		
		if (isTitle) {
			line = br.readLine();
			count++;
		}
		
		while((line = br.readLine()) != null){//br.readline读完一行，再执行会自动到下一行
			String[] strArr = line.split(token);//strArr是{用户id，景点id，行为类型，地理信息，景点类型，时间戳}
			switch (pattern) {
			case "item":
				ret.add(ParseTool.parseItem(strArr));
				break;
			case "user":
				ret.add(ParseTool.parseUser(strArr));//往ret里添加user对象
				break;
			case "score":
				ret.add(ParseTool.parseScore(strArr));
			default:
				ret.add(line);
				break;
			}
			count++;
			if (count/100000 == 1) {
				i++;
				System.out.println(100000*i);
				count = 0;
			}
		}
		
		closeRedaer();
		
		return ret;
	}
	public static void makeSampleData(String inputPath,boolean isTitle,String outputPath,int threshold) throws Exception {
		//1.FileReader不能一行行读，BufferedReader可以一行行地读
		//2.BufferedReader可以一行行地读效率高，因为减少了IO的次数
		//读取文件,不存在文件就会自动创建
		fr = new FileReader(inputPath);
		//对fr进行包装修饰，提高效率（所以套用它）
		//BufferReader不仅可以提高效率，一般还用于字符流数据和字节流的转换，不过里面要套一个new InputStreamReader（new InputStream）
		br = new BufferedReader(fr);
		initWriter1(outputPath);//user文件是要写入的文件，创建写文件器ps1
		
		if (isTitle) {//去掉title
			line = br.readLine();
		}
		int count = 0;
		while((line = br.readLine()) != null){//读到文件尾
			ps1.println(line);//将line通过println函数写入ps1文件器，ps1写入user文件
			count++;
			if (count == threshold) {
				break;
			}
		}
		closeRedaer();
	}
	public static List<String> traverseFolder(String dir) {
        File file = new File(dir);
        String[] fileList = null;
        if (file.exists()) {
        	fileList = file.list();
        }
        List<String> list = new ArrayList<String>();
        for(String path : fileList){
        	list.add(path);
        }
        return list;
    }
	public static Map<String, List<Score>> loadScoreMap(String path,boolean isTitle,String token) throws Exception {
		fr = new FileReader(path);
		br = new BufferedReader(fr);
		
		if (isTitle) {
			line = br.readLine();
		}
		
		Map<String, List<Score>> scoreMap = new HashMap<String, List<Score>>();
		
		while((line = br.readLine()) != null){
			String[] arr = line.split(token);
			Score score = ParseTool.parseScore(arr);
			List<Score> temp = new ArrayList<Score>();
			if (scoreMap.containsKey(score.getUserId())) {
				temp = scoreMap.get(score.getUserId());
			}
			temp.add(score);
			scoreMap.put(score.getUserId(), temp);
		}
		closeRedaer();
		return scoreMap;
	}
	
	public static Map<String, List<String>> loadPredictData(String path,boolean isTitle,String token) throws Exception {
		fr = new FileReader(path);
		br = new BufferedReader(fr);
		
		if (isTitle) {
			line = br.readLine();
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		while((line = br.readLine()) != null){
			String[] arr = line.split(token);
			String userId = arr[0];
			String itemId = arr[1];
			List<String> temp = new ArrayList<String>();
			if (map.containsKey(userId)) {
				temp = map.get(userId);
			}
			temp.add(itemId);
			map.put(userId, temp);
			count++;
		}
		
		closeRedaer();
		return map;
	}
	
	public static Map<String, List<String>> loadTestData(Map<String, List<String>> predictMap, String dir, boolean isTitle, String token) throws Exception {
		
		List<String> fileList = traverseFolder(dir);
		Set<String> predictKeySet = predictMap.keySet();
		Map<String, List<String>> testMap = new HashMap<String, List<String>>();
		for(String predictKey : predictKeySet){
			if (fileList.contains(predictKey)) {
				List<String> itemList = loadTestData(dir + predictKey, isTitle, token);
				testMap.put(predictKey, itemList);
			}
		}
		return testMap;
	}
	
	public static List<String> loadTestData(String path, boolean isTitle, String token) throws Exception {
		fr = new FileReader(path);
		br = new BufferedReader(fr);
		
		if (isTitle) {
			line = br.readLine();
		}
		
		List<String> list = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		while((line = br.readLine()) != null){
			String[] arr = line.split(token);
			set.add(arr[1]);
			count++;
		}
		closeRedaer();
		for(String item : set){
			list.add(item);
		}
		return list;
	}
	
	public static Map<String, Double> loadUser_ItemData(String path,boolean isTitle,String token) throws Exception {
		fr = new FileReader(path);
		br = new BufferedReader(fr);
		
		if (isTitle) {
			line = br.readLine();
		}
		Map<String, Double> map = new HashMap<String, Double>();
		while((line = br.readLine()) != null){
			String[] arr = line.split(token);
			String itemId = arr[1];
			double score = Double.valueOf(arr[2]);
			if(map.containsKey(itemId)){
				double temp = map.get(itemId);
				if (temp > score) {
					score = temp;
				}
			}
			map.put(itemId, score);
		}
		closeRedaer();
		return map;
	}
	
	public static Map<String, Set<String>> loadTestUser(String path,boolean isTitle,String token) throws Exception {
		fr = new FileReader(path);
		br = new BufferedReader(fr);
		int count = 0,i = 0;
		
		if (isTitle) {
			line = br.readLine();
			count++;
		}
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		while((line = br.readLine()) != null){
			String[] arr = line.split(token);
			String userId = arr[0];
			String itemId = arr[1];
			Set<String> set = new HashSet<String>();
			if (map.containsKey(userId)) {
				set = map.get(userId);
				set.add(itemId);
			}
			map.put(userId, set);
			count++;
			if (count/100000 == 1) {
				i++;
				System.out.println(100000*i);
				count = 0;
			}
		}
		closeRedaer();
		return map;
	}
	
}
