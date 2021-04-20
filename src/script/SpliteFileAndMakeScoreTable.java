package script;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entity.Item;
import entity.Score;
import entity.User;
import service.CalculateSimilarity;
import service.DataProcess;
import util.FileTool;

public class SpliteFileAndMakeScoreTable {
	
	public static void main(String[] args) throws Exception {
		//String inputDir = "data/fresh_comp_offline/";
		//String outputDir = "data/fresh_comp_offline/sample/";
		//String inputDir = "data/fresh_comp_offline/sample/";
		//String outputDir = "data/fresh_comp_offline/sample/out/";
		String inputDir = "data/sample1/";
		String outputDir = "data/sample1/out/";
		String outputDir1 = "data/sample1/out1/";
//		String inputDir = args[0];
//		String outputDir = args[1];
		//String userPath = inputDir + "tianchi_fresh_comp_train_user.csv";
//		String userPath = inputDir + args[2];
		String userPath = inputDir + "user.csv";
		String outputPath = outputDir1+"user-user";
        String outputPath1 = outputDir1+"user";
//		String outputPath = args[3];
		//String outputPath = outputDir + "user.csv";
		FileTool.makeSampleData(userPath, false, outputPath1, 10000);
//		List<Object> itemList = FileTool.readFileOne(itemPath, true, ",", "item");
		//List<User> userList = FileTool.readFileOne(userPath, false, ",", "user");
		//userList中的每一项user为{用户id，景点id,behaviorType ,count}默认只输出这四个,count是行为总次数
		List<User> userList = FileTool.readFileOne(userPath, false, ",", "user");
		Set<String> userSet = new HashSet<String>();
		Set<String> itemSet = new HashSet<String>();
		Map<String, Map<String, List<User>>> userMap = DataProcess.mapByUser(userList,userSet,itemSet);
		userList.clear();
		DataProcess.output(userMap, outputDir);

		//生成userToItem的打分表
		Map<String, Map<String, Double>> scoreTable = DataProcess.makeScoreTable(userMap);
		DataProcess.output(scoreTable, outputDir1 + "scoreTable.csv" , userSet, itemSet, ",");
		userMap.clear();
		FileTool.initWriter1(outputPath);
		CalculateSimilarity.execute(scoreTable, userSet, itemSet);
		FileTool.closeWriter1();

		
	}

}
