package script;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import service.DataProcess;
import util.FileTool;
import entity.Score;


public class PredictTest {
	public static void main(String[] args) throws Exception {
		//String inputDir = "data/fresh_comp_offline/";
		//String outputDir = "data/fresh_comp_offline/sample/";
		//String inputDir = "data/fresh_comp_offline/sample/";
		//String outputDir = "data/fresh_comp_offline/sample/out/";
//		String inputDir = args[0];
//		String outputDir = args[1];
		String inputDir = "data/sample1/";
		String outputDir = "data/sample1/sorteduser/";
		//String userPath = inputDir + "tianchi_fresh_comp_train_user.csv";
		String inputPath = inputDir + "out1/user-user";//user-user是用户和用户之间的相似度
		String outputPath = outputDir + "predict";
		String userDir = outputDir;
		
		Map<String, List<Score>> scoreMap = FileTool.loadScoreMap(inputPath, false, "\t");
		DataProcess.sortScoreMap(scoreMap);//对user-user相似度排序
		List<String> fileNameList = FileTool.traverseFolder(userDir);
		Map<String, Set<String>> predictMap = DataProcess.predict(scoreMap, fileNameList, userDir, 5, 5);
		FileTool.initWriter1(outputPath);
		DataProcess.outputRecommendList(predictMap);
		FileTool.closeWriter1();
		scoreMap.clear();
	}
}
