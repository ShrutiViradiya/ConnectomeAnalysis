# Usage:
# Variables.Rを適切に編集した上で、
# > Rscript VolMeanSummarizer_v2.R

#
# 並べ替えサンプリングによる擬似グルーピングした場合の体積平均算出計算のコア部分
# (並列処理用に繰り返し部分を分離)
#
iteration_core_func <- function(i){
    cat("----- iteration_core_func() -----\n")

    #
    # 必要なライブラリをロード
    #
    library(bootstrap)
    source("Variables.R")
    source("VolMeanSummarizerSub.R")

    cat("■ iteration number: ", i, "\n" )

    #ランダムサンプリング
    #例えば1～37の番号が振られたサンプルから18標本無作為に選び出しグループAとするには
    #groupAindexes <- sample( c(1:37), 18 )
    #と記述する
    simulated_groupAindexes <- sample(c(original_groupAindexes, original_groupBindexes), length(original_groupAindexes))

    #そしてグループAとして選ばれなかった残りのサンプルを選び出しグループBとするには
    #groupBindexes <- setdiff( c(1:37), groupAindexes )
    #と記述する
    simulated_groupBindexes <- setdiff(c(original_groupAindexes, original_groupBindexes), simulated_groupAindexes)

    #シミュレート（ランダムサンプリングで各人を２群どちらかに割り振り、２つのネットワークを作り、出力）
    cat("Group A (index =", simulated_groupAindexes, ")\n")
    OUTPUT_FILE_NAME <- "VolMeanOfEachArea.txt"
    SimulatedGroupAVols <- calcEachAreaVolMean(groupIndexes=simulated_groupAindexes)

    cat("\n")
    cat("Group B (index =", simulated_groupBindexes, ")\n")
    SimulatedGroupBVols <- calcEachAreaVolMean(groupIndexes=simulated_groupBindexes)

    writeOutEachAreaVols(GroupAVols=SimulatedGroupAVols, GroupBVols=SimulatedGroupBVols, output_folder_path=OUTPUT_PATH, output_file_name=OUTPUT_FILE_NAME, row_name_A=paste("Simulated_it",i,"_GroupAGraph",sep=""), row_name_B=paste("Simulated_it",i,"_GroupBGraph",sep=""))

    cat("----- iteration_core_func() has finished. -----\n")
    return ()
}


#
# 必要なライブラリをロード
#
library(bootstrap)
source("Variables.R")
source("VolMeanSummarizerSub.R")


#
#書き出し先の初期化
#
OUTPUT_FILE_NAME <- "VolMeanOfEachArea.txt"
VolSummaryFilePath <- paste(OUTPUT_PATH, OUTPUT_FILE_NAME, sep="")
dir.create(OUTPUT_PATH)
if(file.exists(VolSummaryFilePath)) file.remove(VolSummaryFilePath)
file.create(VolSummaryFilePath)

#
# 各脳領域の名称を書き出す（１行目の書き出し）
#
writeOutEachAreaName(output_folder_path=OUTPUT_PATH, output_file_name=OUTPUT_FILE_NAME, row_name="AreaName", flagDebug=TRUE)

cat("#################################\n")
cat("### Observed Graph Generation ###\n")
cat("#################################\n")

#
# ObservedGroupA と B に関して各脳領域の体積平均を求め、書き出す
#
cat("Original Group A (index =", original_groupAindexes, ")\n")
ObservedGroupAVols <- calcEachAreaVolMean(groupIndexes=original_groupAindexes)
cat("Original Group B (index =", original_groupBindexes, ")\n")
ObservedGroupBVols <- calcEachAreaVolMean(groupIndexes=original_groupBindexes)
writeOutEachAreaVols(ObservedGroupAVols, ObservedGroupBVols, output_folder_path=OUTPUT_PATH, output_file_name=OUTPUT_FILE_NAME, row_name_A="ObservedGroupA", row_name_B="ObservedGroupB")


cat("#######################################\n")
cat("### Permutated Vol Mean Calculation ###\n")
cat("#######################################\n")
#iteration_core_func(1)

library("snow")
#source("Variables.R")
cl <- makeCluster(core_count, type="SOCK")
clusterExport(cl, "iteration_core_func")
system.time(diff_values <- parSapply(cl, 1:iteration_count, iteration_core_func))
stopCluster(cl)

