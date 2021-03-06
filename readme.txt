脚本：
1.SpliteFileAndMakeScoreTable
将文件拆分，并生成score
调用时需对ParseTool操作：
放开
if (contents[3] != null && !contents[3].isEmpty()) {
	user.setUserGeoHash(contents[3].trim());
}
if (contents[4] != null && !contents[4].isEmpty()) {
	user.setItemCategory(contents[4].trim());
}
if (contents[5] != null && !contents[5].isEmpty()) {
	user.setTime(contents[5].trim());
}
注释：
if (contents[2] != null && !contents[2].isEmpty()) {
	user.setBehaviorType(Integer.valueOf(contents[2].trim()));
}
if (contents[n-1] != null && !contents[n-1].isEmpty()) {
	user.setCount(Integer.valueOf(contents[n-1].trim()));
}
if (contents[n-1] != null && !contents[n-1].isEmpty()) {
	user.setWeight(Double.valueOf(contents[n-1].trim()));
}
2.ReduceFileTest
对拆分后的小文件排序
调用时需对ParseTool操作：
放开
if (contents[2] != null && !contents[2].isEmpty()) {
	user.setBehaviorType(Integer.valueOf(contents[2].trim()));
}
if (contents[n-1] != null && !contents[n-1].isEmpty()) {
	user.setCount(Integer.valueOf(contents[n-1].trim()));
}
注释：
if (contents[n-1] != null && !contents[n-1].isEmpty()) {
	user.setWeight(Double.valueOf(contents[n-1].trim()));
}
if (contents[3] != null && !contents[3].isEmpty()) {
	user.setUserGeoHash(contents[3].trim());
}
if (contents[4] != null && !contents[4].isEmpty()) {
	user.setItemCategory(contents[4].trim());
}
if (contents[5] != null && !contents[5].isEmpty()) {
	user.setTime(contents[5].trim());
}
3.PredictTest
使用score及排序后的小文件，对userId进行预测
调用时需对ParseTool操作：
放开
if (contents[n-1] != null && !contents[n-1].isEmpty()) {
	user.setWeight(Double.valueOf(contents[n-1].trim()));
}
注释：
if (contents[2] != null && !contents[2].isEmpty()) {
	user.setBehaviorType(Integer.valueOf(contents[2].trim()));
}
if (contents[n-1] != null && !contents[n-1].isEmpty()) {
	user.setCount(Integer.valueOf(contents[n-1].trim()));
}
if (contents[3] != null && !contents[3].isEmpty()) {
	user.setUserGeoHash(contents[3].trim());
}
if (contents[4] != null && !contents[4].isEmpty()) {
	user.setItemCategory(contents[4].trim());
}
if (contents[5] != null && !contents[5].isEmpty()) {
	user.setTime(contents[5].trim());
}
4.MakeTestSet
生成测试集
5.MatchTest2
统计准确率、召回率、F值
6.MakeSet
生成集合：userSet、itemSet
7.MakeScoreTable(其基础为MakeSet及单个用户文件)
将用户文件(小文件)进行遍历计算相似度，生成score
此方法由于多次读文件时间开销较大，适合分布式操作.

流程：
1.SpliteFileAndMakeScoreTable
将文件拆分，并生成score
2.ReduceFileTest
对拆分后的小文件排序
3.PredictTest
使用score及排序后的小文件，对userId进行预测
4.MakeTestSet
生成测试集
5.MatchTest2
统计准确率、召回率、F值

script:
运行顺序：
1.runSpliteFileAndMakeScoreTable.sh //map文件并生成user-user的score
2.runSortFile.sh  //对map后的文件排序，主要对user内的item的score排序
3.runPredict.sh   //预测，生成预测列表user-item
4.runMakeTestSet.sh  //生成测试集
5.runSpliteFile.sh   //对测试集文件进行map
6.runMatch.sh     //将预测列表与测试集进行匹配，计算预测准确率及召回率


基于用户的协同过滤推荐算法实现步骤为：
1).实时统计user对item的打分，从而生成user-item表（即构建用户-项目评分矩阵）；
2).计算各个user之间的相似度，从而生成user-user的得分表，并进行排序；
3).对每一user的item集合排序；
4).针对预推荐的user，在user-user的得分表中选择与该用户最相似的N个用户，并在user-item表中选择这N个用户中已排序好的item集合中的topM；
5).此时的N*M个商品即为该用户推荐的商品集。

基于项目的协同过滤推荐算法实现步骤为：
1).实时统计user对item的打分，从而生成user-item表（即构建用户-项目评分矩阵）；
2).计算各个item之间的相似度，从而生成item-item的得分表，并进行排序；
3).对每一user的item集合排序；
4).针对预推荐的user，在该用户已选择的item集合中，根据item-item表选择与已选item最相似的N个item；
5).此时的N个商品即为该用户推荐的商品集。

