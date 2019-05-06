# Usage:
# constants.Rを適切に編集した上で、「Rscript CorMapGenerator.R」
#

#
#
# 観測値から求めた相関ネットワークの生成
# 戻り値：なし
# GroupAとGroupBのグラフを書き出す。
#
outputObservedNetworks <- function(){
    #
    # 必要なライブラリをロード
    #
    library(bootstrap)

    #
    # 関連するRコードをインポート
    #
    source("CorMapGeneratorSub.R")
    source("constants.R")
    source("utils.R")

    #
    # 全体のログファイルの初期化
    logfile_path <-  paste(OUTPUT_FLD_PATH, LOG_FILE_NAME, sep="")
    dir.create(OUTPUT_FLD_PATH)
    if(file.exists(logfile_path)) file.remove(logfile_path)
    file.create(logfile_path)
    LogFile <- file(logfile_path, "a")

    #
    # Group A Graph、Group B Graphの生成
    #
    cat("Original Group A (index =", ORIGINAL_GROUP_A_INDEXES, ")\n")
    #set_CorValList_and_NodeNameList(group_indexes=ORIGINAL_GROUP_A_INDEXES, flagDebug=F, p_value_threshold = 1.000)
    #ig_of_observed_group_a <- getWeightedUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    #ig_of_observed_group_a <- sortNodes(ig_of_observed_group_a)
    #writeout_ig_to_txt(ig_of_observed_group_a,  output_fld_path=paste(OUTPUT_FLD_PATH, "weighted_undirected/",sep=""), output_file_name="ObservedGroupAGraph")
    set_CorValList_and_NodeNameList(group_indexes=ORIGINAL_GROUP_A_INDEXES, flagDebug=F, p_value_threshold = P_VALUE_THRESHOLD)
    ListOfCorVal
    NodeNameList
    ig_of_observed_group_a <- getBinaryUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    ig_of_observed_group_a <- sortNodes(ig_of_observed_group_a)
    writeout_ig_to_txt(ig_of_observed_group_a,  output_fld_path=paste(OUTPUT_FLD_PATH, "binary_undirected/",sep=""), output_file_name="ObservedGroupAGraph")
    cat("\n")

    #stop()

    cat("Original Group B (index =", ORIGINAL_GROUP_B_INDEXES, ")\n")
    #set_CorValList_and_NodeNameList(group_indexes=ORIGINAL_GROUP_B_INDEXES, flagDebug=F, p_value_threshold = 1.000)
    #ig_of_observed_group_b <- getWeightedUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    #ig_of_observed_group_b <- sortNodes(ig_of_observed_group_b)
    #writeout_ig_to_txt(ig_of_observed_group_b,  output_fld_path=paste(OUTPUT_FLD_PATH, "weighted_undirected/",sep=""), output_file_name="ObservedGroupBGraph")
    set_CorValList_and_NodeNameList(group_indexes=ORIGINAL_GROUP_B_INDEXES, flagDebug=F, p_value_threshold = P_VALUE_THRESHOLD)
    ListOfCorVal
    NodeNameList
    ig_of_observed_group_b <- getBinaryUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    ig_of_observed_group_b <- sortNodes(ig_of_observed_group_b)
    writeout_ig_to_txt(ig_of_observed_group_b,  output_fld_path=paste(OUTPUT_FLD_PATH, "binary_undirected/",sep=""), output_file_name="ObservedGroupBGraph")
    cat("\n")

    #stop()

    # ログファイルへの書き出し
    writeLines(paste("ORIGINAL_GROUP_A_INDEXES: ", paste(ORIGINAL_GROUP_A_INDEXES, collapse=" ")), LogFile)
    writeLines(paste("ORIGINAL_GROUP_B_INDEXES: ", paste(ORIGINAL_GROUP_B_INDEXES, collapse=" ")), LogFile)
    close(LogFile)
    cat("\n")
    cat("outputObservedNetworks() has finished.\n")
    return()
}

#
# 並べ替えサンプリングによる擬似体積相関ネットワークの生成関数のコア部分
# (並列処理用に繰り返し部分を分離)
# 戻り値：なし
# SimulatedGraphの書き出し
#
outputRandomGroupingNetwork <- function(i){
    cat("----- outputRandomGroupingNetwork() -----\n")

    #
    # 必要なライブラリをロード
    #
    library(bootstrap)

    #
    # 関連するRコードをインポート
    #
    source("CorMapGeneratorSub.R")
    source("constants.R")
    source("utils.R")

    #
    # 何回目の繰り返しか確認
    #
    cat("■ iteration number: ", i, "\n" )
    logfile_path <-  paste(OUTPUT_FLD_PATH, LOG_FILE_NAME, sep="")
    LogFile <- file(logfile_path, "a")
    writeLines(paste("■ iteration number: ", i, sep=""), LogFile )

    #
    #ランダムサンプリングによる群分け
    #
    #例えば1～37の番号が振られたサンプルから18標本無作為に選び出しグループAとするには
    #groupAindexes <- sample( c(1:37), 18 )
    #と記述する
    simulated_group_a_indexes <- sample(c(ORIGINAL_GROUP_A_INDEXES, ORIGINAL_GROUP_B_INDEXES), length(ORIGINAL_GROUP_A_INDEXES))

    #そしてグループAとして選ばれなかった残りのサンプルを選び出しグループBとするには
    #groupBindexes <- setdiff( c(1:37), groupAindexes )
    #と記述する
    simulated_group_b_indexes <- setdiff(c(ORIGINAL_GROUP_A_INDEXES, ORIGINAL_GROUP_B_INDEXES), simulated_group_a_indexes)

    #シミュレート（ランダムサンプリングで各人を２群どちらかに割り振り、２つのネットワークを作り、出力）
    cat("Group A (index =", simulated_group_a_indexes, ")\n")
    #set_CorValList_and_NodeNameList(group_indexes=simulated_group_a_indexes, flagDebug=F, p_value_threshold = 1.000)
    #ig_of_simulated_group_a <- getWeightedUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    #ig_of_simulated_group_a <- sortNodes(ig_of_simulated_group_a)
    #writeout_ig_to_txt(ig_of_simulated_group_a,  output_fld_path=paste(OUTPUT_FLD_PATH, "weighted_undirected/",sep=""), output_file_name=paste("Simulated_it",i,"_GroupAGraph",sep=""))
    set_CorValList_and_NodeNameList(group_indexes=simulated_group_a_indexes, flagDebug=F, p_value_threshold = P_VALUE_THRESHOLD)
     ListOfCorVal
    NodeNameList
    ig_of_simulated_group_a <- getBinaryUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    ig_of_simulated_group_a <- sortNodes(ig_of_simulated_group_a)
    writeout_ig_to_txt(ig_of_simulated_group_a,  output_fld_path=paste(OUTPUT_FLD_PATH, "binary_undirected/",sep=""), output_file_name=paste("Simulated_it",i,"_GroupAGraph",sep=""))
    cat("\n")


    cat("Group B (index =", simulated_group_b_indexes, ")\n")
    #set_CorValList_and_NodeNameList(group_indexes=simulated_group_b_indexes, flagDebug=F, p_value_threshold = 1.000)
    #ig_of_simulated_group_b <- getWeightedUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    #ig_of_simulated_group_b <- sortNodes(ig_of_simulated_group_b)
    #writeout_ig_to_txt(ig_of_simulated_group_b,  output_fld_path=paste(OUTPUT_FLD_PATH, "weighted_undirected/",sep=""), output_file_name=paste("Simulated_it",i,"_GroupBGraph",sep=""))
    set_CorValList_and_NodeNameList(group_indexes=simulated_group_b_indexes, flagDebug=F, p_value_threshold = P_VALUE_THRESHOLD)
     ListOfCorVal
    NodeNameList
    ig_of_simulated_group_b <- getBinaryUndirectedNetwork(list_of_cor_val = ListOfCorVal, list_of_node_name = NodeNameList, flagDebug=F)
    ig_of_simulated_group_b <- sortNodes(ig_of_simulated_group_b)
    writeout_ig_to_txt(ig_of_simulated_group_b,  output_fld_path=paste(OUTPUT_FLD_PATH, "binary_undirected/",sep=""), output_file_name=paste("Simulated_it",i,"_GroupBGraph",sep=""))


    # ログの出力
    writeLines(paste("simulated_it",i, "_groupAindexes: ", paste(simulated_group_a_indexes, collapse=" "), sep=""),LogFile)
    writeLines(paste("simulated_it",i, "_groupBindexes: ", paste(simulated_group_b_indexes, collapse=" "), sep=""),LogFile)
    close(LogFile)

    cat("----- outputRandomGroupingNetwork() has finished. -----\n")
    return ()
}




cat("#################################\n")
cat("### Observed Graph Generation ###\n")
cat("#################################\n")
outputObservedNetworks()


cat("###################################\n")
cat("### Permutated Graph Generation ###\n")
cat("###################################\n")
#outputRandomGroupingNetwork(1) #test line

library("snow")
cl <- makeCluster(CPU_CORE_COUNT, type="SOCK")

clusterExport(cl, "outputRandomGroupingNetwork")
clusterExport(cl, "OUTPUT_FLD_PATH")
clusterExport(cl, "LOG_FILE_NAME")
clusterExport(cl, "ORIGINAL_GROUP_A_INDEXES")
clusterExport(cl, "ORIGINAL_GROUP_B_INDEXES")
clusterExport(cl, "getWeightedUndirectedNetwork")
clusterExport(cl, "getBinaryUndirectedNetwork")
clusterExport(cl, "set_CorValList_and_NodeNameList")
clusterExport(cl, "SBJECT_PROFILE_FILE_PATH")
clusterExport(cl, "SUBJECT_NAME_FILE_PATH")
clusterExport(cl, "ETIV_FILE_PATH")
clusterExport(cl, "VOL_DATA_OF_EACH_AREA_FLD_PATH")
clusterExport(cl, "getCorrelationValue")
clusterExport(cl, "z_scorelize")
clusterExport(cl, "writeout_ig_to_txt")
clusterExport(cl, "get_ig_from_mtx")
clusterExport(cl, "graph.adjacency")
clusterExport(cl, "as_adjacency_matrix")
clusterExport(cl, "simplify")
clusterExport(cl, "sortNodes")
clusterExport(cl, "V")
clusterExport(cl, "vcount")
clusterExport(cl, "as_data_frame")
clusterExport(cl, "graph.data.frame")
clusterExport(cl, "E")
clusterExport(cl, "E<-")


system.time(diff_values <- parSapply(cl, 1:ITERATION_COUNT, outputRandomGroupingNetwork))
stopCluster(cl)
cat("CorMapGenerator.R has finished.", "\n")

