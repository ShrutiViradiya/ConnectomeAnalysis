#
# 並列処理関連
#
#iteration_count <- 5000 # シミュレーション回数。マシンの能力に合わせて調節。
iteration_count <- 1000 # シミュレーション回数。マシンの能力に合わせて調節。
#iteration_count <- 100 # シミュレーション回数。マシンの能力に合わせて調節。
#iteration_count <- 10 # シミュレーション回数。マシンの能力に合わせて調節。
#iteration_count <- 5 # シミュレーション回数。マシンの能力に合わせて調節。
seed_of_rand <- 314159 # 乱数の種を投入
#core_count <- 2
core_count <- 16

#
# 比較対象グループの指定
#
#ComparisonPattern <- "MDDMET_vs_MDDVAL"
#ComparisonPattern <- "MDDMET_vs_NMLMET"
#ComparisonPattern <- "MDDMET_vs_NMLVAL"
#ComparisonPattern <- "MDDVAL_vs_NMLMET"
#ComparisonPattern <- "MDDVAL_vs_NMLVAL"
#ComparisonPattern <- "NMLMET_vs_NMLVAL"
ComparisonPattern <- "NML_vs_MDD"


TargetArea <- "deep+surf"
#TargetArea <- "deep"
#TargetArea <- "surf"
#TargetArea <- "hipposub"
#TargetArea <- "6nodes"

#
# Weightedなグラフを生成するか、Binarizedなグラフを生成するか
#
shouldMakeWeightedUndirectedGraph <- FALSE
shouldMakeBinaryUndirectedGraph <- TRUE
#shouldMakeDirectedGraph <- TRUE
#shouldMakeUndirectedGraph <- TRUE

#グラフ画像を出力するか否か
#shouldOutputImage <- FALSE
#shouldOutputImage <- TRUE

#
#------------------------------通常は以下の変更の必要なし----------------------------------
#
# <<surf>>
# R_TTMP, R_STMP, R_SPAR, R_SMAR, R_SFRT, R_RMFR, R_PTRI, R_PSTC, R_PREC, R_PORB, R_POPE, R_PCUN, R_PCAL,
# R_PARC, R_MTMP, R_MOFR, R_LOFR, R_LOCC, R_LING, R_ITMP, R_IPAR, R_INSU, R_FUSI, R_CUNE, R_CMFR, R_AMYG,
# L_TTMP, L_STMP, L_SPAR, L_SMAR, L_SFRT, L_RMFR, L_PTRI, L_PSTC, L_PREC, L_PORB, L_POPE, L_PCUN, L_PCAL,
# L_PARC, L_MTMP, L_MOFR, L_LOFR, L_LOCC, L_LING, L_ITMP, L_IPAR, L_INSU, L_FUSI, L_CUNE, L_CMFR, L_AMYG,
# BSTM

# <<deep>>
# R_THAL, R_RCIN, R_PUTA, R_PHIP, R_PCIN, R_PALL, R_ICIN, R_HIPP, R_ENTO, R_CCIN, R_CAUD, R_ACMB
# L_THAL, L_RCIN, L_PUTA, L_PHIP, L_PCIN, L_PALL, L_ICIN, L_HIPP, L_ENTO, L_CCIN, L_CAUD, L_ACMB

#
# 解析対象脳領域に従って体積情報の格納先を切り替える
#
# 深部領域＋皮質領を解析対象にしたい場合
if(TargetArea=="deep+surf") SubjectName_FilePath <- "../ConvertedData/surf+deep/SubjectName.txt"
if(TargetArea=="deep+surf") eTIV_FilePath <- "../ConvertedData/surf+deep/eTIV.txt"
if(TargetArea=="deep+surf") VolDataOfEachArea_FolderPath <- "../ConvertedData/surf+deep/VolDataOfEachArea/"
# 深部領域のみを解析対象にしたい場合
if(TargetArea=="deep") SubjectName_FilePath <- "../ConvertedData/deep/SubjectName.txt"
if(TargetArea=="deep") eTIV_FilePath <- "../ConvertedData/deep/eTIV.txt"
if(TargetArea=="deep") VolDataOfEachArea_FolderPath <- "../ConvertedData/deep/VolDataOfEachArea/"
# 皮質領域のみを解析対象にしたい場合
if(TargetArea=="surf") SubjectName_FilePath <- "../ConvertedData/surf/SubjectName.txt"
if(TargetArea=="surf") eTIV_FilePath <- "../ConvertedData/surf/eTIV.txt"
if(TargetArea=="surf") VolDataOfEachArea_FolderPath <- "../ConvertedData/surf/VolDataOfEachArea/"
# 海馬亜領域のみを解析対象にしたい場合
if(TargetArea=="hipposub") SubjectName_FilePath <- "../ConvertedData/hipposub/SubjectName.txt"
if(TargetArea=="hipposub") eTIV_FilePath <- "../ConvertedData/hipposub/eTIV.txt"
if(TargetArea=="hipposub") VolDataOfEachArea_FolderPath <- "../ConvertedData/hipposub/VolDataOfEachArea/"
# test
if(TargetArea=="6nodes") SubjectName_FilePath <- "../ConvertedData/6nodes/SubjectName.txt"
if(TargetArea=="6nodes") eTIV_FilePath <- "../ConvertedData/6nodes/eTIV.txt"
if(TargetArea=="6nodes") VolDataOfEachArea_FolderPath <- "../ConvertedData/6nodes/VolDataOfEachArea/"

#
#
# Subject関連情報の格納先
#
SubjectProfile_FilePath <- "../Data/SubjectProfile.txt"


#
# ConvertedDataフォルダ内での各症例の格納行数
#

#nmlmet-----ConvertedDataフォルダ内でXX行目に格納されている
# c01, c03, c04, c06, c07,
group_NMLMET <- c(1, 3, 4, 6, 7, 9, 10, 11, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 28, 32, 33, 36, 38, 40, 42, 43, 45, 46, 47, 48)

#nmlval-------ConvertedDataフォルダ内でXX行目に格納されている
group_NMLVAL <- c(2, 5, 8, 12, 13, 18, 19, 27, 29, 30, 31, 34, 35, 37, 41, 44)

#mddmet--------ConvertedDataフォルダ内でXX行目に格納されている
group_MDDMET <- c(79, 80, 81, 83, 85, 86, 88, 89, 90, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 104, 105, 106, 107)

#mddval------ConvertedDataフォルダ内でXX行目に格納されている
group_MDDVAL <- c(82, 84, 87, 91, 103, 108, 109, 110, 111, 112, 113)

#nml
group_NML <- c(1, 3, 4, 6, 7, 9, 10, 11, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 28, 32, 33, 36, 38, 40, 42, 43, 45, 46, 47, 48, 2, 5, 8, 12, 13, 18, 19, 27, 29, 30, 31, 34, 35, 37, 41, 44)

#mdd
group_MDD <- c(79, 80, 81, 83, 85, 86, 88, 89, 90, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 104, 105, 106, 107, 82, 84, 87, 91, 103, 108, 109, 110, 111, 112, 113)

#
# 比較対象グループの関連の変数設定
#

# MDDwithMET VS MDDwithVAL
if(ComparisonPattern=="MDDMET_vs_MDDVAL") original_groupAindexes <- group_MDDMET
if(ComparisonPattern=="MDDMET_vs_MDDVAL") original_groupBindexes <- group_MDDVAL
# AD VS MCI-NonConverter
if(ComparisonPattern=="MDDMET_vs_NMLMET") original_groupAindexes <- group_MDDMET
if(ComparisonPattern=="MDDMET_vs_NMLMET") original_groupBindexes <- group_NMLMET
# AD VS NML
if(ComparisonPattern=="MDDMET_vs_NMLVAL") original_groupAindexes <- group_MDDMET
if(ComparisonPattern=="MDDMET_vs_NMLVAL") original_groupBindexes <- group_NMLVAL
# MCI-Converter VS MCI-NonConverter
if(ComparisonPattern=="MDDVAL_vs_NMLMET") original_groupAindexes <- group_MDDVAL
if(ComparisonPattern=="MDDVAL_vs_NMLMET") original_groupBindexes <- group_NMLMET
# MCI-Converter VS NML
if(ComparisonPattern=="MDDVAL_vs_NMLVAL") original_groupAindexes <- group_MDDVAL
if(ComparisonPattern=="MDDVAL_vs_NMLVAL") original_groupBindexes <- group_NMLVAL
# MCI-NonConverter VS NML
if(ComparisonPattern=="NMLMET_vs_NMLVAL") original_groupAindexes <- group_NMLMET
if(ComparisonPattern=="NMLMET_vs_NMLVAL") original_groupBindexes <- group_NMLVAL
# NML vs MDD
if(ComparisonPattern=="NML_vs_MDD") original_groupAindexes <- group_NML
if(ComparisonPattern=="NML_vs_MDD") original_groupBindexes <- group_MDD

#
# Weightedなグラフを生成するか、Binarizedなグラフを生成するか
#
#if(isDirected == TRUE) EdgeType1 <- "directed"
#if(isDirected == FALSE) EdgeType1 <- "undirected"
#if(isWeighted == TRUE) EdgeType2 <- "weighted"
#if(isWeighted == FALSE) EdgeType2 <- "binary"

#
# 処理結果出力先
#
#ID <- paste(ComparisonPattern, "_", TargetArea, "_", EdgeType1, "_", EdgeType2 ,"_", Sys.Date(), sep="")
#ID <- paste(ComparisonPattern, "_", TargetArea, "_", Sys.Date(), sep="")
ID <- paste(ComparisonPattern, "_", TargetArea, sep="")
OUTPUT_PATH <- paste("../wd_", ID, "/", sep="")


#
# ログファイルの出力先
#
LOG_FILE_NAME <- "VolCorNetGenerator.log"






