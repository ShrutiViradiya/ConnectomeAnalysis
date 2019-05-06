
# 引数には　"network_mes_rslt.txt"　"VolMeanOfEachArea.txt"　を保持するフォルダを指定する。
# ex) 
# Rscript ./CountExceedingDifferences_v3.R ./wd_NML_vs_MDD_deep+surf/
# Rscript ./CountExceedingDifferences_v3.R ./wd_NMLMET_vs_NMLVAL_deep+surf/


#
# 解析対象の設定
#
OUTPUT_FOLDER_PATH <- commandArgs()[6]
INPUT_FOLDER_PATH <- commandArgs()[6]
file_set <- system(paste("ls ",INPUT_FOLDER_PATH,"*.txt",sep=""),intern=T)

for(target_file_path in file_set){
#
# 出力先ファイルの初期化
#
OutputFileNemeBase <- basename(target_file_path)
OutputFileNemeBase <- gsub("\\.[0-9A-Za-z]+$", "", OutputFileNemeBase) #拡張子を一旦除去

#p値格納用
OutputFileNeme <- paste(OutputFileNemeBase, ".pvals", sep="")
OutputFilePath <-  paste(OUTPUT_FOLDER_PATH, "CountExceedingDifferencesRslt_", OutputFileNeme, sep="")
if(!file.exists(OUTPUT_FOLDER_PATH)) dir.create(OUTPUT_FOLDER_PATH)
if(file.exists(OutputFilePath)) file.remove(OutputFilePath)
file.create(OutputFilePath)
OutputFile <- file(OutputFilePath, "a")

#シミュレーション値格納用
OutputFileNeme2 <- paste(OutputFileNemeBase, ".simvals", sep="")
OutputFilePath2 <-  paste(OUTPUT_FOLDER_PATH, "CountExceedingDifferencesRslt_", OutputFileNeme2, sep="")
if(!file.exists(OUTPUT_FOLDER_PATH)) dir.create(OUTPUT_FOLDER_PATH)
if(file.exists(OutputFilePath2)) file.remove(OutputFilePath2)
file.create(OutputFilePath2)
OutputFile2 <- file(OutputFilePath2, "a")

#
# 読み取り準備
#
#target_file_path <- paste(OUTPUT_FOLDER_PATH, target_file_path, sep="")
df <- read.table(target_file_path, header=T, sep=" ", stringsAsFactors=F)
#head(df)

cat("#######################################\n")
cat("###   Count Exceeding Differences   ###\n")
cat("#######################################\n")

names_of_col <- colnames(df)
value_of_ObservedGroupAGraph <-c()
value_of_ObservedGroupBGraph <-c()
ObservedDiff <-c()
rslt_str_for_outputfile <-""
rslt_str_for_outputfile2 <-""
for( c in 2:ncol(df)){

    cat("解析対象指標: ", names_of_col[c], "\n")
    rslt_str_for_outputfile <- paste(rslt_str_for_outputfile, "解析対象指標: ", names_of_col[c], " ", sep="")
    rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "解析対象指標: ", names_of_col[c], " ", sep="")
    
    value_of_ObservedGroupAGraph <- as.matrix(df[1,c])
    value_of_ObservedGroupBGraph <- as.matrix(df[2,c])
    #ObservedDiff <- value_of_ObservedGroupAGraph - value_of_ObservedGroupBGraph
    ObservedDiff <- as.numeric(as.matrix(df[1,c])) - as.numeric(as.matrix(df[2,c]))
    cat("    ObservedDiff:", ObservedDiff, " (GroupA=", value_of_ObservedGroupAGraph, ", ", "GroupB=", value_of_ObservedGroupBGraph, ") ", "\n")
    rslt_str_for_outputfile <- paste(rslt_str_for_outputfile, "ObservedDiff:", ObservedDiff, " (GroupA=", value_of_ObservedGroupAGraph, ", ", "GroupB=", value_of_ObservedGroupBGraph, ") ", sep="")
    rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "ObservedDiff:", ObservedDiff, " (GroupA=", value_of_ObservedGroupAGraph, ", ", "GroupB=", value_of_ObservedGroupBGraph, ") ", sep="")
    
    Simulated_diff_values <- c()
    r <- 3
    while(r < nrow(df)){
        #cat("r=", r, "\n")
        diff <- as.numeric(as.matrix(df[r,c])) - as.numeric(as.matrix(df[r+1,c]))
        r <- r + 2
        Simulated_diff_values <- c(Simulated_diff_values, diff)
    }
    cat("here", "\n")
    cat("    Simulated_diff_values: " , paste(Simulated_diff_values, collapse=" "), "\n")
    rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "Simulated_diff_values : ", paste(Simulated_diff_values, collapse=" "),sep="")

#
# ★どう数えるか
#
exceeded_count_a <- sum(abs(ObservedDiff) > abs(Simulated_diff_values)) #絶対値
exceeded_count_b <- sum(abs(ObservedDiff) < abs(Simulated_diff_values)) #絶対値
exceeded_count_c <- sum(ObservedDiff > Simulated_diff_values) #非絶対値
exceeded_count_d <- sum(ObservedDiff < Simulated_diff_values) #非絶対値

iteration_count <- ((nrow(df)-2)/2)

p_value_a <- exceeded_count_a / iteration_count # 並べ替えp値。N回中何回観測値の絶対値以下であったかを数える。
#cat("    |diff. of obs. value| > |diff. of sim. value| の回数：",iteration_count,"回中 ",exceeded_count_a,"回 (p値= ", p_value_a,") \n")
rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "|diff. of obs. value| > |diff. of sim. value| の回数：",iteration_count,"回中 ",exceeded_count_a, "回 (p値= ", p_value_a, " )", sep="")


p_value_b <- exceeded_count_b / iteration_count # 並べ替えp値。N回中何回観測値の絶対値以上であったかを数える。
cat("    |diff. of obs. value| < |diff. of sim. value| の回数：",iteration_count,"回中 ",exceeded_count_b,"回 (p値= ", p_value_b," )\n")
rslt_str_for_outputfile <- paste(rslt_str_for_outputfile, "|diff. of obs. value| < |diff. of sim. value| の回数：",iteration_count,"回中 ",exceeded_count_b, "回 (p値= ", p_value_b, " )", sep="")
rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "|diff. of obs. value| < |diff. of sim. value| の回数：",iteration_count,"回中 ",exceeded_count_b, "回 (p値= ", p_value_b, " )", sep="")


p_value_c <- exceeded_count_c / iteration_count # 並べ替えp値。N回中何回観測値以下であったかを数える。
#cat("    diff. of obs. value > diff. of sim. value の回数：",iteration_count,"回中 ",exceeded_count_c,"回 (p値= ", p_value_c," )\n")
rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "diff. of obs. value > diff. of sim. value の回数：",iteration_count,"回中 ",exceeded_count_c, "回 (p値= ", p_value_c, " )", sep="")


p_value_d <- exceeded_count_d / iteration_count # 並べ替えp値。N回中何回観測値以上であったかを数える。
#cat("    diff. of obs. value  < diff. of sim. value の回数：",iteration_count,"回中 ",exceeded_count_d,"回 (p値= ", p_value_d," )\n")
rslt_str_for_outputfile2 <- paste(rslt_str_for_outputfile2, "diff. of obs. value < diff. of sim. value の回数：",iteration_count,"回中 ",exceeded_count_d, "回 (p値= ", p_value_d, " )", sep="")


#OutputFileへの書き出し
writeLines(rslt_str_for_outputfile, OutputFile)
rslt_str_for_outputfile <-""

#OutputFile2への書き出し
writeLines(rslt_str_for_outputfile2, OutputFile2)
rslt_str_for_outputfile2 <-""
}
close(OutputFile)
close(OutputFile2)
}
