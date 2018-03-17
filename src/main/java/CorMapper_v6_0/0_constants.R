#########################################################################
# constants for 
#   1_CorMapGenerator.R,
#   1_PrmtttPrep.R
#   2_CostCorrection.R
#
# 比較パターンの設定（フォルダ名として使う）
#
COMPARISON_PATTERN <- "BDNF_NML_MET_vs_VAL"
#COMPARISON_PATTERN <- "BDNF_NML_GG_vs_GA"
#COMPARISON_PATTERN <- "BDNF_NML_GA_vs_AA"
#COMPARISON_PATTERN <- "BDNF_NML_GG_vs_AA"

#
# 解析対象脳領域に従って体積情報の格納先を切り替える
# 深部領域＋皮質領を解析対象にしたい場合
#
TARGET_AREA <- "deep+surf"
#TARGET_AREA <- "6nodes"
if(TARGET_AREA=="deep+surf") statsdata_converted <- "./data00/BDNFV66M/surf+deep"
# テスト用：６領域のみを解析対象としたい場合
if(TARGET_AREA=="6nodes") statsdata_converted <- "./data00/BDNFV66M/6nodes"

#
# 脳体積データの格納先
#
DATA_ORDER_INFO <- paste(statsdata_converted, "/DataOrderInfo.txt", sep="")
ETIV_FILE_PATH <- paste(statsdata_converted, "/eTIV.txt", sep="")
VOL_DATA_OF_EACH_AREA_FLD_PATH <- paste(statsdata_converted, "/VolDataOfEachArea/", sep="")

#
# Subject関連情報の格納先
#
SBJECT_PROFILE_FILE_PATH <- "./data00/Profile_with_givenid.txt"

#
# ConvertedDataフォルダ内での各症例の格納行数
#
group_nmlGG <-  c(2, 5, 8, 12, 13, 18, 19, 27, 29, 30, 31, 34, 35, 37, 41, 44)
group_nmlGA <-  c(1, 3, 7, 9, 10, 11, 14, 15, 16, 17, 20, 21, 22, 23, 25, 26, 28, 33, 36, 38, 40, 42, 45, 46, 47, 48)
group_nmlAA <-  c(4, 6, 24, 32, 43)
group_nmlNA <-  c(39, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78)
group_mddAA <-  c(79, 81, 89, 90, 95, 96, 98, 101, 107)
group_mddGA <-  c(80, 83, 85, 86, 88, 92, 93, 94, 97, 99, 100, 102, 104, 105, 106)
group_mddGG <-  c(82, 84, 87, 91, 103, 108, 109, 110, 111, 112, 113)
group_mddNA <-  c(114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151)
group_NMLMET <- c(group_nmlGA, group_nmlAA)
group_NMLVAL <- c(group_nmlGG)
group_MDDMET <- c(group_mddGA, group_mddAA)
group_MDDVAL <- c(group_mddGG)
group_NML <- c(group_nmlGG, group_nmlGA, group_nmlAA)
group_MDD <- c(group_mddAA, group_mddGA, group_mddGG)


#
# 比較グルーピングの定義
#
if(COMPARISON_PATTERN=="BDNF_NML_MET_vs_VAL") ORIGINAL_GROUP_A_INDEXES <- group_NMLMET
if(COMPARISON_PATTERN=="BDNF_NML_MET_vs_VAL") ORIGINAL_GROUP_B_INDEXES <- group_NMLVAL
if(COMPARISON_PATTERN=="BDNF_NML_GG_vs_GA") ORIGINAL_GROUP_A_INDEXES <- group_nmlGG
if(COMPARISON_PATTERN=="BDNF_NML_GG_vs_GA") ORIGINAL_GROUP_B_INDEXES <- group_nmlGA
if(COMPARISON_PATTERN=="BDNF_NML_GA_vs_AA") ORIGINAL_GROUP_A_INDEXES <- group_nmlGA
if(COMPARISON_PATTERN=="BDNF_NML_GA_vs_AA") ORIGINAL_GROUP_B_INDEXES <- group_nmlAA
if(COMPARISON_PATTERN=="BDNF_NML_GG_vs_AA") ORIGINAL_GROUP_A_INDEXES <- group_nmlGG
if(COMPARISON_PATTERN=="BDNF_NML_GG_vs_AA") ORIGINAL_GROUP_B_INDEXES <- group_nmlAA

##########################################################################
# constants for 
#     1_CorMapGenerator.R, 1_PrmtttPrep.R
#
# どの程度の相関係数を有意とするかのP値の閾値
# 採用する相関の閾値設定
#
#P_VALUE_THRESHOLD <- 0.05 / ( 77 * 77 - 77 ) / 2 #Bonferroni補正
P_VALUE_THRESHOLD <- 1.00 #「p value = 1」とすれば、フィルタなし（閾値なし）と同等
#P_VALUE_THRESHOLD <- 0.05

##################################################
# 出力フォルダ
#
# 1_CorMapGenerator.R, 1_PrmtttPrep.R
OUTPUT_PARENT_FLD_PATH_1 <- "./data01/"
if(!file.exists(OUTPUT_PARENT_FLD_PATH_1)) dir.create(OUTPUT_PARENT_FLD_PATH_1)
OUTPUT_FLD_PATH_1 <- paste(OUTPUT_PARENT_FLD_PATH_1, COMPARISON_PATTERN, "_", TARGET_AREA, "/", sep="")
if(!file.exists(OUTPUT_FLD_PATH_1)) dir.create(OUTPUT_FLD_PATH_1)

# 2_CostCorrection.R
OUTPUT_PARENT_FLD_PATH_2 <- "./data02/"
if(!file.exists(OUTPUT_PARENT_FLD_PATH_2)) dir.create(OUTPUT_PARENT_FLD_PATH_2)
OUTPUT_FLD_PATH_2 <- paste(OUTPUT_PARENT_FLD_PATH_2, COMPARISON_PATTERN, "_", TARGET_AREA, "/", sep="")
if(!file.exists(OUTPUT_FLD_PATH_2)) dir.create(OUTPUT_FLD_PATH_2)

# 3_WeightNormalizatioin.R
OUTPUT_PARENT_FLD_PATH_3 <- "./data03/"
if(!file.exists(OUTPUT_PARENT_FLD_PATH_3)) dir.create(OUTPUT_PARENT_FLD_PATH_3)
OUTPUT_FLD_PATH_3 <- paste(OUTPUT_PARENT_FLD_PATH_3, COMPARISON_PATTERN, "_", TARGET_AREA, "/", sep="")
if(!file.exists(OUTPUT_FLD_PATH_3)) dir.create(OUTPUT_FLD_PATH_3)

# 5_MakeEachAreaVolSummary.R
OUTPUT_PARENT_FLD_PATH_5 <- "./data05/"
if(!file.exists(OUTPUT_PARENT_FLD_PATH_5)) dir.create(OUTPUT_PARENT_FLD_PATH_5)
OUTPUT_FLD_PATH_5 <- paste(OUTPUT_PARENT_FLD_PATH_5, COMPARISON_PATTERN, "_", TARGET_AREA, "/", sep="")
if(!file.exists(OUTPUT_FLD_PATH_5)) dir.create(OUTPUT_FLD_PATH_5)

# 6_MakeImageFiles.R
OUTPUT_PARENT_FLD_PATH_6 <- "./data06/"
if(!file.exists(OUTPUT_PARENT_FLD_PATH_6)) dir.create(OUTPUT_PARENT_FLD_PATH_6)
OUTPUT_FLD_PATH_6 <- paste(OUTPUT_PARENT_FLD_PATH_6, COMPARISON_PATTERN, "_", TARGET_AREA, "/", sep="")
if(!file.exists(OUTPUT_FLD_PATH_6)) dir.create(OUTPUT_FLD_PATH_6)


# 9_TestCode.R 
OUTPUT_PARENT_FLD_PATH_9 <- "./data09/"
if(!file.exists(OUTPUT_PARENT_FLD_PATH_9)) dir.create(OUTPUT_PARENT_FLD_PATH_9)
OUTPUT_FLD_PATH_9 <- paste(OUTPUT_PARENT_FLD_PATH_9, COMPARISON_PATTERN, "_", TARGET_AREA, "/", sep="")
if(!file.exists(OUTPUT_FLD_PATH_9)) dir.create(OUTPUT_FLD_PATH_9)

####################################
# 4-1_CalcMesOfBinaryAndUndirectedNet_v1.R
# 4-2_CalcMesOfWeightedAndUndirectedNet_v1.R
# 4-3_AveragingRows.R
# 4-4_CalcDiffBetweenAandB.R
# 4-5_CountExceedingDiff.R
#

# WeightNormalization前のファイルについて
# ネットワーク特徴量を計算したい時。（OUTPUT_FLD_PATH_2に格納されている）

# WeightNormalization後のファイルについて
# ネットワーク特徴量を計算したい時。（OUTPUT_FLD_PATH_3に格納されている）

# Binarized Network ファイルについて
# ネットワーク特徴量を計算したい時。

# Weighted Network ファイルについて
# ネットワーク特徴量を計算したい時。

#
#
isAboutRawWeightedNetwork = F
isAboutRawBinarizedNetwork = T
isAboutCostcorrectedWeightNormalizedWeightedNetwork=F
isAboutCostcorrectedWeightNormalizedBinarizedNetwork=F



#########################################################################
#
# 全体のログファイルの初期化
#
# constants for 1_CorMapGenerator.R
log_file_name <- "1_CorMapGenerator.R.log"
LOG_FILE_PATH_1 <-  paste(OUTPUT_FLD_PATH_1, log_file_name, sep="")
if(file.exists(LOG_FILE_PATH_1)) file.remove(LOG_FILE_PATH_1)
file.create(LOG_FILE_PATH_1)

#   1_PrmtttPrep.R
log_file_name <- "1_PrmtttPrep.R.log"
LOG_FILE_PATH_2 <-  paste(OUTPUT_FLD_PATH_1, log_file_name, sep="")
if(file.exists(LOG_FILE_PATH_2)) file.remove(LOG_FILE_PATH_2)
file.create(LOG_FILE_PATH_2)

##########################################################################
# constants for
#   1_PrmtttPrep.R
#   4-3_AveragingRows.R
#   4-4_CalcDiffBetweenAandB.R
#
#並べ替え検定のシミュレーション回数。マシンの能力に合わせて調節。
#

SIMULATION_START_NUMBER <- 1

#SIMULATION_START_NUMBER <- 10
#SIMULATION_END_NUMBER <- 10
#SIMULATION_END_NUMBER <- 100

SIMULATION_END_NUMBER <- 1000

#SIMULATION_END_NUMBER <- 5000

#########################################################################
# constants for
#   1_PrmtttPrep.R
#   4-1_CalcMesOfBinaryAndUndirectedNet_v6.R
#
# number of threads for parallel computing
#
#CPU_CORE_COUNT <- 3
#CPU_CORE_COUNT <- 16
CPU_CORE_COUNT <- 26

##############################################

