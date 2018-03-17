#########################################################################
#
# 関連するRコードをインポート
#
source("utils.R")
source("0_constants.R")
#########################################################################


#
# Z score 計算メソッド
#
z_scorelize <- function(vec){
    z_score <- (vec-mean(vec))/sd(vec)
    return(z_score)
}


#
# ２つの脳領域体積データ間の相関係数を算出するメソッド
# スピアマンの順位相関係数 ピアソンの積率相関係数 の両方対応版
#
getCorrelationValue <- function(area_i_vols, area_j_vols, flagDebug = FALSE, p_value_threshold){
    area_i_z_score <- z_scorelize(area_i_vols)
    area_j_z_score <- z_scorelize(area_j_vols)

    #相関係数検定
    cor_test_result <- cor.test(area_i_z_score, area_j_z_score, method="spearman") #スピアマンの順位相関係数、ノンパラメトリック検定
    #cor_test_result <- cor.test(area_i_z_score, area_j_z_score, method="pearson") #ピアソンの積率相関係数、パラメトリック検定
    if(flagDebug==TRUE) cat("area_i_z_score:", paste(area_i_z_score, collapse=", "), "\n")
    if(flagDebug==TRUE) cat("area_j_z_score:", paste(area_j_z_score, collapse=", "), "\n")
    if(flagDebug==TRUE) cat("cor_test_result:","\n")
    if(flagDebug==TRUE) print(cor_test_result)
    if(flagDebug==TRUE) cat("cor_test_result.value=", cor_test_result$p.value, "\n")

    #p値が欠損値なために出現するエラーを回避するためのif文
    if(anyNA(cor_test_result$p.value)){
        #なんか相関係数が1または-1のときに「p-value = NA」となる時がある。
        #たとえば
        #g <- makeNetwork(c(71, 66, 73, 67, 62, 60, 74, 75, 76, 63, 72, 61), T, "")
        #としたときのi=1, j=13のとき。
        cor_test_result$p.value = 0.00
    }

    #p値を基準に相関係数の取り出しにフィルタをかける
    #p_value_threshold = 1.00  とすれば、フィルタなしと同等
    #p_value_threshold <- 1.00
    #p_value_threshold <- 0.001
    if(cor_test_result$p.value < p_value_threshold){
        return (cor_test_result$estimate)
    }else{
        return (0)
    }
}

#
# 全脳領域間の相関係数のリストを生成するメソッド
# 「ListOfCorVal」という変数と「NodeNameList」という変数に値を格納することを目的としたメソッド
#
set_CorValList_and_NodeNameList <- function(group_indexes, flagDebug=FALSE, p_value_threshold=1.00){
    cat("* function \"set_CorValList_and_NodeNameList()\" has started", "\n")
    source("AgeSexEffectRemover_v2.R")

    #
    # subject_names
    #
    #df <- read.table(DATA_ORDER_INFO, header=F, sep="", stringsAsFactors=TRUE)
    #subject_names <- df[[1]]
    df <- read.table("./data01/df.txt", header=T, sep="\t", stringsAsFactors=TRUE)
    #if(flagDebug==TRUE) cat("df:", "\n")
    #if(flagDebug==TRUE) print(df)
    #if(flagDebug==TRUE) cat("\n")

    subject_names <- df$ID
    subgroup_sbj_names <- subject_names[group_indexes]
    cat("subgroup_sbj_names:", "\n")
    cat("   " , paste(subgroup_sbj_names, collapse=" "), "\n")
    #cat("    以上　", length(subgroup_sbj_names), " の症例から体積相関係数の計算をする。", "\n")
    cat("   from above ", length(subgroup_sbj_names), " cases, calculate volume correlations.", "\n")

    #
    # eTIV
    #
    #df <- read.table(ETIV_FILE_PATH, header=F, sep="", stringsAsFactors=TRUE)
    #etiv <- df[[1]]
    etiv <- df$eTIV
    subgroup_etiv <- etiv[group_indexes]
    if(flagDebug==TRUE) cat("subgroup_etiv:", "\n    ")
    if(flagDebug==TRUE) print(subgroup_etiv)
    df_sbjname_etiv <- data.frame(GIVEN_ID=subgroup_sbj_names, TIV=subgroup_etiv)

    #
    # 年齢と性別情報のロード
    #
    #df_sbjname_sex_age <- read.table(SBJECT_PROFILE_FILE_PATH, header=T, sep="\t", stringsAsFactors=TRUE)
    df_sbjname_sex_age <- data.frame(GIVEN_ID=df$ID, AGE=df$AGE, SEX=df$SEX)
    df_sbjname_etiv_sex_age <- merge(df_sbjname_etiv, df_sbjname_sex_age, all=FALSE)
    #if(flagDebug==TRUE) cat("df_sbjname_etiv_sex_age:", "\n")
    #if(flagDebug==TRUE) print(df_sbjname_etiv_sex_age)
    #if(flagDebug==TRUE) cat("\n")

    #
    # 処理対象脳領域ファイル一覧の生成
    #
    #folder <- VOL_DATA_OF_EACH_AREA_FLD_PATH
    #if(flagDebug==TRUE) cat("体積データフォルダ", folder, "\n")
    #children <- list.files(folder)
    #filtered_file_list <- c()
    #for( i in grep("\\.txt$", children)){
    #    filtered_file_list <- c(filtered_file_list, children[i])
    #}
    #area_file_set <- filtered_file_list
    area_abbr_list <- colnames(df)[7:83]
    area_abbr_list <- sort(area_abbr_list, decreasing=TRUE) #ソート
    area_abbr_list_size <- length(area_abbr_list)
    if(flagDebug==TRUE) cat("The list of volume data file: ", "\n")
    if(flagDebug==TRUE) print(area_abbr_list)
    if(flagDebug==TRUE) cat("以上　", area_abbr_list_size, "個の脳領域ファイル\n\n")
    if(flagDebug==TRUE) cat("The above is the list of ", area_abbr_list_size, " brain area files\n\n")
    if(flagDebug==TRUE) Sys.sleep(1)

    #
    # Groupについて体積相関ネットワークを作る
    #
    edge_value_list <- c()
    node_name_list <- c()

    cat("        Edge Values are creating ----- ")

    #area_abbr_list_size <- 5
    cat("area_abbr_list_size==", area_abbr_list_size, "-----")
    for( i in 1:area_abbr_list_size){

        # NodeNameに関する情報収集
        area1_name <- area_abbr_list[[i]]
        #area1_name_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", area1_name) #拡張子除去
        #node_name_list <- c(node_name_list, area1_name_without_ext) # 脳領域名を集める
        node_name_list <- c(node_name_list, area1_name) # 脳領域名を集める

        for(j in 1:area_abbr_list_size){

            # NodeNameに関する情報収集
            area2_name <- area_abbr_list[[j]]

            # 処理状況表示
            if(flagDebug==FALSE){
                if(j== 1){
                    cat(area_abbr_list_size - i + 1, " ")
                }
            }
            if(flagDebug==TRUE) cat(area1_name, "と", area2_name , "の体積相関 ----- ", "\n")
            if(flagDebug==TRUE) cat("-----", "i=", i, "/", area_abbr_list_size, "  ", "j=", j, "/", area_abbr_list_size,"-----","\n")

            #
            # area1(i番目の脳領域)に関するロード
            #
            #df <- read.table(paste(folder, area1_name, sep=""), header=F, sep="", stringsAsFactors=TRUE)
            #area_i_vols_all <- df[[1]] #i番目の領域の全症例の体積データ
            area_i_vols_all <- df[, i+6]
            area_i_vols <- area_i_vols_all[group_indexes] #とある領域のGroupの体積データ
            if(flagDebug==TRUE) cat("area_i_vols:", "\n")
            if(flagDebug==TRUE) print(area_i_vols)
            if(flagDebug==TRUE) Sys.sleep(1)

            #
            # area2(j番目の脳領域)に関するロード
            #
            #df <- read.table(paste(folder, area2_name, sep=""), header=F, sep="", stringsAsFactors=TRUE)
            #area_j_vols_all <- df[[1]] ##j番目の領域の全症例の体積データ
            area_j_vols_all <- df[,j+6] ##j番目の領域の全症例の体積データ
            area_j_vols <- area_j_vols_all[group_indexes] #とある領域のGroupの体積データ
            if(flagDebug==TRUE) cat("area_j_vols:", "\n")
            if(flagDebug==TRUE) print(area_j_vols)
            if(flagDebug==TRUE) Sys.sleep(1)
            if(flagDebug==TRUE) cat("\n")

            #
            # i番目とj番目の脳領域情報を保持するデータフレーム生成
            # この段階で各脳領域体積はeTIVで補正しておく
            #
            #df_sbjname_ivol_jvol <- data.frame(GIVEN_ID=subgroup_sbj_names, I_VOL=area_i_vols, J_VOL=area_j_vols)
            #df_sbjname_ivol_jvol <- data.frame(GIVEN_ID=subgroup_sbj_names, I_VOL_ADJ=area_i_vols/subgroup_etiv, J_VOL_ADJ=area_j_vols/subgroup_etiv)
            df_sbjname_ivol_jvol <- data.frame(GIVEN_ID=subgroup_sbj_names, I_VOL_ADJ=area_i_vols/subgroup_etiv, J_VOL_ADJ=area_j_vols/subgroup_etiv)
            if(flagDebug==TRUE) cat("df_sbjname_ivol_jvol:", "\n")
            if(flagDebug==TRUE) print(df_sbjname_ivol_jvol)
            if(flagDebug==TRUE) cat("\n")

            #
            # GLMを使って各脳領域の体積を年齢と性別で補正する
            #

            # ID TIV SEX AGE I_VOL_ADJ J_VOL_ADJ からなるデータフレーム
            df_for_lm <- merge(df_sbjname_etiv_sex_age, df_sbjname_ivol_jvol, all=FALSE)
            if(flagDebug==TRUE) cat("df_for_lm:", "\n")
            if(flagDebug==TRUE) print(df_for_lm)
            if(flagDebug==TRUE) cat("\n")

            #cat("is.element(df_for_lm$SEX, \"M\")=",is.element(df_for_lm$SEX, "M"),"\n")
            #cat("is.element(df_for_lm$SEX, \"F\")=",is.element(df_for_lm$SEX, "F"),"\n")
            #cat("levels(df_for_lm$SEX)=",levels(df_for_lm$SEX),"\n")
            #cat("nlevels(df_for_lm$SEX)=",nlevels(df_for_lm$SEX),"\n")
            #cat("sd(df_for_lm$SEX)=",sd(df_for_lm$SEX),"\n")

            #
            # 年齢、性別の影響を取り除いた部分のみを計算対象とする
            #
            i_vol_adj_by_tiv_sex_age = removeAgeSexEffect(value=df_for_lm$I_VOL_ADJ, age=df_for_lm$AGE, sex=df_for_lm$SEX)
            j_vol_adj_by_tiv_sex_age = removeAgeSexEffect(value=df_for_lm$J_VOL_ADJ, age=df_for_lm$AGE, sex=df_for_lm$SEX)

            df_for_cor_calc <- data.frame(GIVEN_ID=subgroup_sbj_names, I_VOL_ADJ_BY_TIVSEXAGE=i_vol_adj_by_tiv_sex_age, J_VOL_ADJ_BY_TIVSEXAGE=j_vol_adj_by_tiv_sex_age)

            #
            # ２つの脳領域体積データの相関係数を算出
            #
            correlation_value <- getCorrelationValue(df_for_cor_calc$I_VOL_ADJ_BY_TIVSEXAGE, df_for_cor_calc$J_VOL_ADJ_BY_TIVSEXAGE, p_value_threshold = p_value_threshold, flagDebug=flagDebug)

            # 相関係数の計算結果
            if(flagDebug==TRUE) cat("This group's correlation between ", area1_name, " and ", area2_name, " is ", correlation_value, "\n")
            if(flagDebug==TRUE) cat("\n")

            # EdgeValueに関する情報収集
            edge_value_list <- cbind(edge_value_list, correlation_value)

        }#end of for
    }#end of for
    cat(" -----Edge Values generation has done.\n")
    #
    # ノード名一覧をグローバル変数として登録しておく。(外部関数でも利用するため。）
    #
    ListOfCorVal <<- edge_value_list

    NodeNameList <<- node_name_list
    return(0)
}

###########################################################



#
#
# 観測値から求めた相関ネットワークの生成
# GroupAとGroupBのグラフを書き出す。
#
cat("* CorMapGenerator_v2\" has started", "\n")
cat("=================================\n")
cat("=== Observed Graph Generation ===\n")
cat("=================================\n")
#
# 必要なライブラリをロード
#
if(!require(bootstrap)) install.packages("bootstrap")
library(bootstrap)

#
# ログファイルへのストリームを開く
#
LogFile <- file(LOG_FILE_PATH_1, "a")




#
# Group A Graph の生成
#
loaded_df <- read.table("./data01/df.txt", header=T, sep="\t", stringsAsFactors=TRUE)
ORIGINAL_GROUP_A_INDEXES <- which(loaded_df$Val66Met == "Met+")
cat("Original Group A (index =", ORIGINAL_GROUP_A_INDEXES, ")\n")
writeLines(paste("ORIGINAL_GROUP_A_INDEXES: ", paste(ORIGINAL_GROUP_A_INDEXES, collapse=" ")), LogFile)
set_CorValList_and_NodeNameList(group_indexes=ORIGINAL_GROUP_A_INDEXES, flagDebug=F, p_value_threshold=P_VALUE_THRESHOLD)
ig_of_observed_group_a <- getWeightedUndirectedNetwork(edge_value_list = ListOfCorVal, node_name_list = NodeNameList, flagDebug=F)
ig_of_observed_group_a <- sortNodes(ig_of_observed_group_a)
writeoutIgraphObjToTxtFile(ig_of_observed_group_a,  output_folder_path=paste(OUTPUT_FLD_PATH_1, sep=""), output_file_name="ObservedGroupAGraph_weighted", isWeighted=T)
writeoutIgraphObjToTxtFile(ig_of_observed_group_a,  output_folder_path=paste(OUTPUT_FLD_PATH_1, sep=""), output_file_name="ObservedGroupAGraph_binarized", isWeighted=F)
cat("\n")

#
# Group B Graph の生成
#
ORIGINAL_GROUP_B_INDEXES <- which(loaded_df$Val66Met == "Met-")
cat("Original Group B (index =", ORIGINAL_GROUP_B_INDEXES, ")\n")
writeLines(paste("ORIGINAL_GROUP_B_INDEXES: ", paste(ORIGINAL_GROUP_B_INDEXES, collapse=" ")), LogFile)
set_CorValList_and_NodeNameList(group_indexes=ORIGINAL_GROUP_B_INDEXES, flagDebug=F, p_value_threshold=P_VALUE_THRESHOLD)
ig_of_observed_group_b <- getWeightedUndirectedNetwork(edge_value_list = ListOfCorVal, node_name_list = NodeNameList, flagDebug=F)
ig_of_observed_group_b <- sortNodes(ig_of_observed_group_b)
writeoutIgraphObjToTxtFile(ig_of_observed_group_b,  output_folder_path=paste(OUTPUT_FLD_PATH_1, sep=""), output_file_name="ObservedGroupBGraph_weighted", isWeighted=T)
writeoutIgraphObjToTxtFile(ig_of_observed_group_b,  output_folder_path=paste(OUTPUT_FLD_PATH_1, sep=""), output_file_name="ObservedGroupBGraph_binarized", isWeighted=F)
cat("\n")


# ログファイルへの書き出し
close(LogFile)
cat("\n")
cat("outputObservedNetworks() has finished.\n")
return()




