#
# Z score 計算メソッド
#
z_scorelize <- function(vec){
    z_score <- (vec-mean(vec))/sd(vec)
    return(z_score)
}


#
# ２つの脳領域体積データ間の相関係数を算出するメソッド
#
getCorrelationValue <- function(area_i_vols, area_j_vols, flagDebug = FALSE, p_value_threshold = 1.00){
    area_i_z_score <- z_scorelize(area_i_vols)
    area_j_z_score <- z_scorelize(area_j_vols)

    #ピアソンの積率相関係数
    cor_test_result <- cor.test(area_i_z_score, area_j_z_score)
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
    # p_value_threshold = 1.00  とすれば、フィルタなしと同等
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
#
set_CorValList_and_NodeNameList <- function(groupIndexes, flagDebug=FALSE, p_value_threshold = 0.001){
    cat("set_CorValList_and_NodeNameList() has started", "\n")

    #
    # SubjectName
    #
    df <- read.table(SubjectName_FilePath, header=F, sep="", stringsAsFactors=TRUE)
    SubjectName <- df[[1]]
    SubGroup_SbjName <- SubjectName[groupIndexes]
    #cat("SubGroup_SbjName:", "\n")
    cat("    " , paste(SubGroup_SbjName, collapse=" "), "\n")
    cat("    以上　", length(SubGroup_SbjName), " の症例から体積相関係数の計算をする。", "\n")

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
    # Groupについて体積相関ネットワークを作る
    #
    list_of_cor_val <- c()
    list_of_node_name <- c()

    cat("        Edge Values の作成中 ----- ")
    area_file_set_size <- area_file_set_size
    #area_file_set_size <- 10
    #area_file_set_size <- 5
    cat("ノード数=", area_file_set_size, "-----")

    for( i in 1:area_file_set_size){

        # NodeNameに関する情報収集
        area1_file_name <- area_file_set[[i]]
        area1_file_name_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", area1_file_name) #拡張子除去
        list_of_node_name <- c(list_of_node_name, area1_file_name_without_ext) # 脳領域名を集める

        for(j in 1:area_file_set_size){

            # NodeNameに関する情報収集
            area2_file_name <- area_file_set[[j]]

            # 処理状況表示
            if(flagDebug==FALSE){
                if(j== 1){
                    cat(area_file_set_size - i + 1, " ")
                }
            }
            if(flagDebug==TRUE) cat(area1_file_name, "と", area2_file_name , "の体積相関 ----- ", "\n")
            if(flagDebug==TRUE) cat("-----", "i=", i, "/", area_file_set_size, "  ", "j=", j, "/", area_file_set_size,"-----","\n")

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
            # area2(j番目の脳領域)に関するロード
            #
            df <- read.table(paste(folder, area2_file_name, sep=""), header=F, sep="", stringsAsFactors=TRUE)
            area_j_vols_all <- df[[1]] ##j番目の領域の全症例の体積データ
            area_j_vols <- area_j_vols_all[groupIndexes] #とある領域のGroupの体積データ
            if(flagDebug==TRUE) cat("area_j_vols:", "\n")
            if(flagDebug==TRUE) print(area_j_vols)
            if(flagDebug==TRUE) Sys.sleep(1)
            if(flagDebug==TRUE) cat("\n")

            #
            # i番目とj番目の脳領域情報を保持するデータフレーム生成
            # この段階で各脳領域体積はeTIVで補正しておく
            #
            #df_sbjname_ivol_jvol <- data.frame(ID=SubGroup_SbjName, I_VOL=area_i_vols, J_VOL=area_j_vols)
            df_sbjname_ivol_jvol <- data.frame(ID=SubGroup_SbjName, I_VOL_ADJ=area_i_vols/SubGroup_eTIV, J_VOL_ADJ=area_j_vols/SubGroup_eTIV)
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

            # LMモデル作り
            if(sd(df_for_lm$SEX)==0){#女性または男性しか含まれていない集団を対象とした場合
            lm_rslt_of_ivol <- lm(I_VOL_ADJ ~ AGE, data = df_for_lm)
            #lm_rslt <- step(lm(I_VOL_ADJ ~ AGE, data = df_for_lm))
            lm_rslt_of_jvol <- lm(J_VOL_ADJ ~ AGE, data = df_for_lm)
            #lm_rslt <- step(lm(I_VOL_ADJ ~ AGE, data = df_for_lm))
            }else{
                lm_rslt_of_ivol <- lm(I_VOL_ADJ ~ SEX + AGE + SEX*AGE, data = df_for_lm)
                #lm_rslt <- step(lm(I_VOL_ADJ ~ SEX + AGE + SEX*AGE, data = df_for_lm))
                lm_rslt_of_jvol <- lm(J_VOL_ADJ ~ SEX + AGE + SEX*AGE, data = df_for_lm)
                #lm_rslt <- step(lm(I_VOL_ADJ ~ SEX + AGE + SEX*AGE, data = df_for_lm))
            }
            if(flagDebug==TRUE) cat("lm_rslt_of_ivol:", "\n")
            if(flagDebug==TRUE) print(summary(lm_rslt_of_ivol))
            if(flagDebug==TRUE) cat("\n")
            if(flagDebug==TRUE) cat("lm_rslt_of_jvol:", "\n")
            if(flagDebug==TRUE) print(summary(lm_rslt_of_jvol))
            if(flagDebug==TRUE) cat("\n")
            #年齢、性別で補正したI番目の体積値
            if(flagDebug==TRUE) cat("Predicted I_VOL_ADJ (predict関数を使った方法):", "\n")
            if(flagDebug==TRUE) print(predict(lm_rslt_of_ivol)) #補正した値
            if(flagDebug==TRUE) cat("\n")
            #年齢、性別で補正したJ番目の体積値
            if(flagDebug==TRUE) cat("Predicted J_VOL_ADJ (predict関数を使った方法):", "\n")
            if(flagDebug==TRUE) print(predict(lm_rslt_of_jvol)) #補正した値
            if(flagDebug==TRUE) cat("\n")

            df_for_cor_calc <- data.frame(ID=SubGroup_SbjName, I_VOL_ADJ_BY_TIVSEXAGE=predict(lm_rslt_of_ivol), J_VOL_ADJ_BY_TIVSEXAGE=predict(lm_rslt_of_jvol))

            #
            # ２つの脳領域体積データの相関係数を算出
            #
            correlation_value <- getCorrelationValue(df_for_cor_calc$I_VOL_ADJ_BY_TIVSEXAGE, df_for_cor_calc$J_VOL_ADJ_BY_TIVSEXAGE, p_value_threshold = p_value_threshold, flagDebug=flagDebug)

            # 相関係数の計算結果
            if(flagDebug==TRUE) cat("This group's correlation between ", area1_file_name, " and ", area2_file_name, " is ", correlation_value, "\n")
            if(flagDebug==TRUE) cat("\n")

            # EdgeValueに関する情報収集
            list_of_cor_val <- cbind(list_of_cor_val, correlation_value)

        }#end of for
    }#end of for
    cat(" -----Edge Values の生成の完了\n")
    #
    # ノード名一覧をグローバル変数として登録しておく。(外部関数でも利用するため。）
    #
    ListOfCorVal <<- list_of_cor_val

    NodeNameList <<- list_of_node_name
    return(0)
}


#
# Weighted かつ Directed な Group Graph の生成
# 年齢、性別を補正した体積を使ってグラフを生成するメソッド
# 引数 groupIndexes：Groupingするサンプル番号。VolDataOfEachAreaフォルダ内の何番目のファイルを使ってグラフを生成するか。
# 引数 flagDebug：デバックON/OFFフラッグ
# 引数 OUTPUT_FLD_PATH：出力フォルダパス
# 引数 OUTPUT_FILE_NAME：出力ファイル名（行列テキストファイル）
# 戻り値：igraphオブジェクト
#
getWeightedUndirectedNetwork <- function(list_of_cor_val, list_of_node_name, flagDebug=FALSE){
    cat("getWeightedUndirectedNetwork() has started", "\n")

    #
    # グラフの種類に応じて、相関係数ベクトル、行列データ生成、igraphオブジェクト生成、ノード名設定、グラフ書き出し
    #
    # 重み付けあり 有向性 なグラフ
    list_of_cor_val_weighted_directed <- list_of_cor_val #相関係数のベクトル生成
    mtx_of_Group_weighted_directed <- matrix(list_of_cor_val_weighted_directed, nrow=length(list_of_node_name), ncol=length(list_of_node_name)) #行列データの生成
    rownames(mtx_of_Group_weighted_directed) <- list_of_node_name
    colnames(mtx_of_Group_weighted_directed) <- list_of_node_name

    # igraphオブジェクトの生成
    g <- get_ig_from_mtx(mtx_of_Group_weighted_directed, directed=F, weighted=T)
    #print(E(g)$weight)

    cat("\n", "getWeightedUndirectedNetwork() has finished.", "\n\n")
    return(g)
}


getBinaryUndirectedNetwork <- function(list_of_cor_val, list_of_node_name, flagDebug=FALSE){
    cat("getBinarizedUndirectedNetwork() has started", "\n")
    #
    # グラフの種類に応じて、相関係数ベクトル、行列データ生成、igraphオブジェクト生成、ノード名設定、グラフ書き出し
    #
    # 重み付けあり 有向性 なグラフ
    list_of_cor_val_weighted_directed <- list_of_cor_val #相関係数のベクトル生成
    mtx_of_Group_weighted_directed <- matrix(list_of_cor_val_weighted_directed, nrow=length(list_of_node_name), ncol=length(list_of_node_name)) #行列データの生成
    rownames(mtx_of_Group_weighted_directed) <- list_of_node_name
    colnames(mtx_of_Group_weighted_directed) <- list_of_node_name

    # igraphオブジェクトの生成
    g <- get_ig_from_mtx(mtx_of_Group_weighted_directed, directed=F, weighted=F)
    #print(E(g)$weight)

    cat("\n", "getBinarizedUndirectedNetwork() has finished.", "\n\n")
    return(g)
}

