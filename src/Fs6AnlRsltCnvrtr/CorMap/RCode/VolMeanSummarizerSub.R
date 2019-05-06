#
# Group の体積平均を生成
# 年齢、性別を補正した体積の平均値を得るメソッド
# 引数 groupIndexes：Groupingするサンプル番号。VolDataOfEachAreaフォルダ内の何番目のファイルを使って体積平均を生成するか。
# 引数 flagDebug：デバックON/OFFフラッグ
# 引数 output_folder_path：出力フォルダパス
# 引数 output_file_name：出力ファイル名
#
calcEachAreaVolMean <- function(groupIndexes, flagDebug=FALSE){
    cat("network_generator.calcEachAreaVolMean() has started", "\n")

    #
    # SubjectName
    #
    df <- read.table(SubjectName_FilePath, header=F, sep="", stringsAsFactors=TRUE)
    SubjectName <- df[[1]]
    SubGroup_SbjName <- SubjectName[groupIndexes]
    #cat("SubGroup_SbjName:", "\n")
    cat("    " , paste(SubGroup_SbjName, collapse=" "), "\n")
    cat("    以上　", length(SubGroup_SbjName), " の症例の各脳領域の体積平均の算出を開始します。", "\n")

    #
    # eTIV
    #
    df <- read.table(eTIV_FilePath, header=F, sep="", stringsAsFactors=TRUE)
    eTIV <- df[[1]]
    SubGroup_eTIV <- eTIV[groupIndexes]
    if(flagDebug==TRUE) cat("SubGroup_eTIV:", "\n    ")
    if(flagDebug==TRUE) print(SubGroup_eTIV)
    df_sbjname_etiv <- data.frame(ID=SubGroup_SbjName, TIV=SubGroup_eTIV)

    #
    # 年齢と性別情報のロード
    #
    df_sbjname_sex_age <- read.table(SubjectProfile_FilePath, header=T, sep="\t", stringsAsFactors=TRUE)
    df_sbjname_etiv_sex_age <- merge(df_sbjname_etiv, df_sbjname_sex_age, all=FALSE)
    if(flagDebug==TRUE) cat("df_sbjname_etiv_sex_age:", "\n")
    if(flagDebug==TRUE) print(df_sbjname_etiv_sex_age)
    if(flagDebug==TRUE) cat("\n")


    #
    # 処理対象脳領域ファイル一覧の生成
    #
    folder <- VolDataOfEachArea_FolderPath
    if(flagDebug==TRUE) cat("体積データフォルダ", folder, "\n")
    area_file_set <- system(paste("find", folder, "-name", "'*.txt'", "|", "sed", "'s!^.*/!!'", sep=" "), intern=T)
    area_file_set <- sort(area_file_set, decreasing=TRUE) #ソート
    area_file_set_size <- length(area_file_set)
    if(flagDebug==TRUE) cat("体積データファイル一覧：", "\n")
    if(flagDebug==TRUE) print(area_file_set)
    if(flagDebug==TRUE) cat("以上　", area_file_set_size, "個の脳領域ファイル\n\n")
    if(flagDebug==TRUE) Sys.sleep(1)

    #
    # Groupについて年齢、性別を考慮して体積平均を求める
    #
    list_of_mean_val <- c()
    list_of_node_name <- c()

    cat("        Mean Values の算出中 ----- ")
    area_file_set_size <- area_file_set_size
    #area_file_set_size <- 10
    #area_file_set_size <- 5
    cat("ノード数=", area_file_set_size, "-----")

    for( i in 1:area_file_set_size){

        # NodeNameに関する情報収集
        area1_file_name <- area_file_set[[i]]
        area1_file_name_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", area1_file_name) #拡張子除去
        list_of_node_name <- c(list_of_node_name, area1_file_name_without_ext) # 脳領域名を集める


        # 処理状況表示
        if(flagDebug==TRUE) cat(area1_file_name, "の体積平均 ----- ", "\n")
        if(flagDebug==TRUE) cat("-----", "i=", i, "/", area_file_set_size, "-----","\n")

        #
        # area1(i番目の脳領域)に関するロード
        #
        df <- read.table(paste(folder, area1_file_name, sep=""), header=F, sep="", stringsAsFactors=TRUE)
        area_i_vols_all <- df[[1]] #i番目の領域の全症例の体積データ
        area_i_vols <- area_i_vols_all[groupIndexes] #とある領域のGroupの体積データ
        if(flagDebug==TRUE) cat("area_i_vols:", "\n")
        if(flagDebug==TRUE) print(area_i_vols)
        if(flagDebug==TRUE) Sys.sleep(1)

        #
        # i番目の脳領域情報を保持するデータフレーム生成
        # この段階で各脳領域体積はeTIVで補正しておく
        #
        df_sbjname_ivol <- data.frame(ID=SubGroup_SbjName, I_VOL_ADJ=area_i_vols/SubGroup_eTIV)
        if(flagDebug==TRUE) cat("df_sbjname_ivol:", "\n")
        if(flagDebug==TRUE) print(df_sbjname_ivol)
        if(flagDebug==TRUE) cat("\n")

        #
        # GLMを使って各脳領域の体積を年齢と性別で補正する
        #

        # ID TIV SEX AGE I_VOL_ADJ からなるデータフレーム
        df_for_lm <- merge(df_sbjname_etiv_sex_age, df_sbjname_ivol, all=FALSE)
        if(flagDebug==TRUE) cat("df_for_lm:", "\n")
        if(flagDebug==TRUE) print(df_for_lm)
        if(flagDebug==TRUE) cat("\n")

        #cat("is.element(df_for_lm$SEX, \"M\")=",is.element(df_for_lm$SEX, "M"),"\n")
        #cat("is.element(df_for_lm$SEX, \"F\")=",is.element(df_for_lm$SEX, "F"),"\n")
        #cat("levels(df_for_lm$SEX)=",levels(df_for_lm$SEX),"\n")
        #cat("nlevels(df_for_lm$SEX)=",nlevels(df_for_lm$SEX),"\n")
        #cat("sd(df_for_lm$SEX)=",sd(df_for_lm$SEX),"\n")

        # LMモデル作り
        if(sd(df_for_lm$SEX)==0){#女性または男性しか含まれていない集団を対象とした場合
        lm_rslt_of_ivol <- lm(I_VOL_ADJ ~ AGE, data = df_for_lm)
        #lm_rslt <- step(lm(I_VOL_ADJ ~ AGE, data = df_for_lm))
        }else{
            lm_rslt_of_ivol <- lm(I_VOL_ADJ ~ SEX + AGE + SEX*AGE, data = df_for_lm)
            #lm_rslt <- step(lm(I_VOL_ADJ ~ SEX + AGE + SEX*AGE, data = df_for_lm))
        }
        if(flagDebug==TRUE) cat("lm_rslt_of_ivol:", "\n")
        if(flagDebug==TRUE) print(summary(lm_rslt_of_ivol))
        if(flagDebug==TRUE) cat("\n")
        #年齢、性別で補正したI番目の体積値
        if(flagDebug==TRUE) cat("Predicted I_VOL_ADJ (predict関数を使った方法):", "\n")
        if(flagDebug==TRUE) print(predict(lm_rslt_of_ivol)) #補正した値
        if(flagDebug==TRUE) cat("\n")

        df_for_mean_calc <- data.frame(ID=SubGroup_SbjName, I_VOL_ADJ_BY_TIVSEXAGE=predict(lm_rslt_of_ivol))

        #
        # 脳領域体積データの平均を算出
        #
        #correlation_value <- getCorrelationValue(df_for_cor_calc$I_VOL_ADJ_BY_TIVSEXAGE, df_for_cor_calc$J_VOL_ADJ_BY_TIVSEXAGE, isWeighted=isWeighted, flagDebug=flagDebug)
        mean_value <- mean(df_for_mean_calc$I_VOL_ADJ_BY_TIVSEXAGE)
        if(flagDebug==TRUE) cat("This group's mean of  ", area1_file_name, " is ", mean_value, "\n")
        if(flagDebug==TRUE) cat("\n")

        # 結果を出力に備えまとめる
        list_of_mean_val <- cbind(list_of_mean_val, mean_value)

    }#end of for
    cat(" -----Mean Values の算出の完了\n")
    cat("network_generator.calcEachAreaVolMean() has finished.", "\n\n")
    if(flagDebug==TRUE) print(list_of_mean_val)
    return(list_of_mean_val)
}

#
# Group A と B についてまとめて書き出すメソッド
#
writeOutEachAreaVols <- function(GroupAVols="", GroupBVols="", flagDebug=FALSE, output_folder_path="./", output_file_name="", row_name_A="", row_name_B=""){

    VolSummaryFilePath <- paste(output_folder_path, output_file_name, sep="")
    VolSummaryFile <- file(VolSummaryFilePath, "a")
    writeLines(paste(row_name_A, paste(GroupAVols, collapse=" ")), VolSummaryFile)
    writeLines(paste(row_name_B, paste(GroupBVols, collapse=" ")), VolSummaryFile)
    close(VolSummaryFile)

}


#
# 体積平均を求める各脳領域の名称(1行目)を書き出し
# 引数 flagDebug：デバックON/OFFフラッグ
# 引数 output_folder_path：出力フォルダパス
# 引数 output_file_name：出力ファイル名
#
writeOutEachAreaName <- function( flagDebug=FALSE, output_folder_path = "./", output_file_name = "", row_name = ""){
    cat("network_generator.writeOutEachAreaName() has started", "\n")

    #
    # 処理対象脳領域ファイル一覧の生成
    #
    folder <- VolDataOfEachArea_FolderPath
    if(flagDebug==TRUE) cat("体積データフォルダ", folder, "\n")
    area_file_set <- system(paste("find", folder, "-name", "'*.txt'", "|", "sed", "'s!^.*/!!'", sep=" "), intern=T)
    area_file_set <- sort(area_file_set, decreasing=TRUE) #ソート
    area_file_set_size <- length(area_file_set)
    if(flagDebug==TRUE) cat("体積データファイル一覧：", "\n")
    if(flagDebug==TRUE) print(area_file_set)
    if(flagDebug==TRUE) cat("以上　", area_file_set_size, "個の脳領域ファイル\n\n")
    if(flagDebug==TRUE) Sys.sleep(1)

    list_of_node_name <- c()

    area_file_set_size <- area_file_set_size
    #area_file_set_size <- 10
    #area_file_set_size <- 5
    cat("ノード数=", area_file_set_size, "-----")
    for( i in 1:area_file_set_size){
        # NodeNameに関する情報収集
        area1_file_name <- area_file_set[[i]]
        area1_file_name_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", area1_file_name) #拡張子除去
        list_of_node_name <- c(list_of_node_name, area1_file_name_without_ext) # 脳領域名を集める
    }

    #
    # 書き出し
    #
    VolSummaryFilePath <- paste(output_folder_path, output_file_name, sep="")
    VolSummaryFile <- file(VolSummaryFilePath, "a")
    writeLines(paste(row_name, paste(list_of_node_name, collapse=" ")), VolSummaryFile)
    close(VolSummaryFile)

    cat("network_generator.makeMean() has finished.", "\n\n")
    #if(flagDebug==TRUE) print(list_of_node_name)
    return(list_of_node_name)
}



