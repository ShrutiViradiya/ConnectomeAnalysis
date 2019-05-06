library(igraph)

#
# 「*.mtx」ファイルからigraphオブジェクトを生成する
#
# 第１引数：隣接行列で表現されたグラフデータを保持するテキストファイルへのパス
# 戻り値：igraph object
#
makeIgraphObjectFromTxtFile <- function(txt_file_path){
    # ノード名とノード数の取得
    filename <- txt_file_path
    cat("filename=", filename, "\n")
    df <- as.matrix(read.table(filename, header=T, sep=" "))#テキストファイル⇒データフレーム⇒行列
    g <- simplify(graph.adjacency(df, mode="undirected", weighted=NULL ), remove.multiple=T, remove.loops=T)
    sort_node_name <- sort(V(g)$name)
    cat("ノード名=", sort_node_name, "\n")
    node_num <- vcount(g)
    cat("ノード数=", node_num, "\n")
    return(g)
}

#
# igraphObjを行列に変換するメソッド
# 第一引数：igraphObj
# 戻り値：行列
#
getMatrixFromIgraphObj <- function(g){
    #mtx <- as.matrix(get.adjacency(g))

    mtx <- as_adjacency_matrix (g, attr="weight")
    return(mtx)
}

#
# 隣接行列をigraphObjに変換するメソッド
# 第一引数：隣接行列
# 戻り値：igraphObj
#
#
get_ig_from_mtx <- function(adjmtx, directed = T, weighted = T){
    cat("get_ig_from_mtx()")
    cat("\n", "adjmtx=", "\n")
    print(adjmtx)
    cat("\n")

    adjmtx[upper.tri(adjmtx, diag=TRUE)] <- 0 #matrixを左下半分のみにする(三角行列を利用)
    # ～memo～
    # 三角行列を利用した０化や絶対値のみ取り扱うとか色々ややこしい。
    # 三角行列化あり　＆ 方向性あり　⇒　対称性のマイナス値を含んだグラフ
    # 三角行列化あり　＆ mode="undirected"　⇒　対称性のマイナス値を省いたグラフ(マイナス値は０に変換)
    # 三角行列化なし　＆ 方向性あり　⇒　対称性のマイナス値を含んで、かつ足し合わされたグラフ
    # 三角行列化なし　＆ mode="undirected"　⇒　対称性のマイナス値を含んだグラフ

    # 三角行列化あり　＆ mode="undirected"　＆ 絶対値変換⇒　対称性のマイナス値を省いたグラフ(マイナス値は反転)

    if(directed == T){
        if(weighted == T){
            #directed == T    weighted == T
            ig <- graph.adjacency(adjmtx, mode="directed", weighted=F) #対称性のマイナス値を含んだグラフ、重み付け有り
        }else{
            #directed == T    weighted == F
            ## adjmtxを「1 or 0 or -1」化する
            rownum <- nrow(adjmtx)
            colnum <- ncol(adjmtx)
            rownames <- rownames(adjmtx)
            colnames <- colnames(adjmtx)
            vec <- as.vector(adjmtx)
            cat(vec,"\n")
            newvec <- c()
            for(i in vec){
                if(i>0){
                    newvec <- c(newvec, 1)
                }else if(i==0){
                    newvec <- c(newvec, 0)
                }else{
                    newvec <- c(newvec, -1)
                }
            }
            cat(newvec,"\n")
            adjmtx <- matrix(newvec, nrow=rownum, ncol=colnum)
            rownames(adjmtx) <- rownames
            colnames(adjmtx) <- colnames
            ig <- graph.adjacency(adjmtx, mode="directed", weighted=T) #対称性のマイナス値を含んだグラフ、重み付け無し
        }
    }else{
        adjmtx <- abs(adjmtx)
        if(weighted == T){
            #directed == F   weighted == T
            ig <- graph.adjacency(adjmtx, mode="directed", weighted=T) #対称性のマイナス値を省いたグラフ(マイナス値は反転)、重み付け有り
        }else{
            #directed == F   weighted == F
            # adjmtxを「1 or 0」化する
            rownum <- nrow(adjmtx)
            colnum <- ncol(adjmtx)
            rownames <- rownames(adjmtx)
            colnames <- colnames(adjmtx)
            vec <- as.vector(adjmtx)
            cat(vec,"\n")
            newvec <- c()
            for(i in vec){
                if(i==0){
                    newvec <- c(newvec, 0)
                }else{
                    newvec <- c(newvec, 1)
                }
            }
            cat(newvec,"\n")
            adjmtx <- matrix(newvec, nrow=rownum, ncol=colnum)
            rownames(adjmtx) <- rownames
            colnames(adjmtx) <- colnames
            cat("\n","adjmtx=", "\n")
            print(adjmtx)
            ig <- graph.adjacency(adjmtx, mode="directed", weighted=T) #対称性のマイナス値を省いたグラフ(マイナス値は反転)、重み付け無し
}
    }
    return(ig)
}

#
# igraphオブジェクトで表現されたグラフ（g）をPNGファイルへ描き出す
# 第１引数：igrph object
# 第２引数：出力先フォルダパス
# 第３引数：出力ファイル名（拡張子不要）
# 第４引数：PNGファイルの生成直後にPNGファイルを閲覧するか否か
#
#
writeoutIgraphObjAsPngFile <- function(g, output_fld_path, OUTPUT_IMG_FILE_NAME, shouldShowPng){
    # 書き出し先の用意
    if(!exists(output_fld_path)) dir.create(output_fld_path)
    output_img_file_path <- paste(output_fld_path, OUTPUT_IMG_FILE_NAME, ".png", sep="")

    cat("    グラフの書き出し ...\n")
    png(filename=output_img_file_path, width=1500, height=1500)

    # レイアウトの定義
    cat("Construct Node Info Dataframe ...")
    df_node_info <- read.table ("./NodeInfo_Base.nodes", header=T, sep=" ", as.is=T)
    #head(df_node_info)
    cat("done", "\n")
    cordinateX <- c()
    cordinateY <- c()
    for( vname in V(g)$name){
        cordinateX <- c( cordinateX, df_node_info[df_node_info$SimpleId==vname, ]$X)
        cordinateY <- c( cordinateY, df_node_info[df_node_info$SimpleId==vname, ]$Y)
    }
    l <- cbind (cordinateX, cordinateY)  # レイアウト座標の生成
    cat("Layout has been constructed.", "\n")

    # Edgeの調節
    #E (g)$arrow.size <- 2

    # グラフの描画
    plot (g, layout=l)
    par(new=T) # 上書きON
    title(main=OUTPUT_IMG_FILE_NAME, sub=output_fld_path) # タイトルを書き込み
    par(new=F) # 上書きOFFf_group)
    dev.off()

    # グラフの表示
    if(shouldShowPng) system(paste("geeqie ", output_img_file_path, sep=""))

    return()
}

#
# 隣接行列で表現されたグラフ（mtx）をテキストファイル（*.mtx）へ書き出す
# 第１引数：行列オブジェクト
# 第２引数：出力先フォルダパス
# 第３引数：出力ファイル名（拡張子不要）
#
writeoutMtxGraphAsMtxFile <- function(mtx_of_Group, output_fld_path, output_file_name){
    # 書き出し先の用意
    if(!exists(output_fld_path)) dir.create(output_fld_path)
    output_file_path <- paste(output_fld_path, output_file_name, ".mtx", sep="")

    # matrixファイルの書き出し
    write.table(mtx_of_Group, output_file_path, quote=FALSE)

    return(0)
}


#
# igraphオブジェクトを書き出す(igraphObj⇒*.mtxファイル)
#
#
writeout_ig_to_txt <-function(ig, output_fld_path, output_file_name){
    # 書き出し先の用意
    if(!exists(output_fld_path)) dir.create(output_fld_path)
    output_file_path <- paste(output_fld_path, output_file_name, ".mtx", sep="")
    #cat(output_file_path, "へ書き出します。", "\n")

    # ig -> mtx -> df -> txt (重み付け)
    #（隣接行列の下半分と上半分は統合される（足し合わされる）。）
    write.table(as.matrix(as_adjacency_matrix (ig, attr="weight")), output_file_path, quote=F)

    cat(output_file_path, "へ書き出しました。", "\n")

    return(0)
}


#
# 隣接行列表現グラフファイル（*.mtx）からリスト表現グラフファイル（*.nodepairs, *.weights）の生成
#
#
writeoutNodepairListAndWeightListFiles <- function(mtx_file_path, output_folder_path){
    cat("mtx_file_path=", mtx_file_path, "\n")

    # read graph
    df <- read.table(mtx_file_path,header=T) #テキストファイル⇒データフレーム
    d <- as.matrix(df) #データフレーム⇒行列
    g <- graph.adjacency(d,mode="undirected",weighted=TRUE)
    g <- simplify(g,remove.multiple=T,remove.loops=T)

    sort_node_name <- sort(V(g)$name)
    cat("ノード名=", sort_node_name, "\n")
    node_num <- vcount(g)
    cat("ノード数=", node_num, "\n")

    df_edges <- as_data_frame (g, what="edges") # 「from」「to」「weight」からなるデータフレーム
    nodepairs <- c()
    weights <- c()
    for(from in sort_node_name){
        for(to in sort_node_name){
            # nodepair を保持したベクトル生成
            nodepairs <- c(nodepairs, paste(from,">",to, sep=""))
            # weight を保持したベクトル生成
            weight <- df_edges[ df_edges$from==from & df_edges$to==to, ][[3]]
            if(length(weight)>0){
                weights <- c(weights , weight)
            }else{
                weights <- c(weights , 0.0)
            }
        }
    }

    nodepair_file_name <- paste(gsub("\\.[0-9A-Za-z]+$", "", basename(mtx_file_path)), ".nodepairs", sep="")
    weight_file_name <- paste(gsub("\\.[0-9A-Za-z]+$", "", basename(mtx_file_path)), ".weights", sep="")

    df_nodepairs <- data.frame(nodepairs)
    write.table(df_nodepairs, file = paste(output_folder_path, nodepair_file_name, sep=""), append = FALSE, quote = FALSE, row.names = FALSE, col.names=FALSE)

    df_weights <- data.frame(weights)
    write.table(df_weight_list, file = paste(output_folder_path, weight_file_name, sep=""), append = FALSE, quote = FALSE, row.names = FALSE)

    return()
}

#
# ノード名に従って、ノードを並べ替えるメソッド
#
sortNodes <- function(ig){
    sorted_node_names <- sort(V(ig)$name)
    cat("ノード名=", sorted_node_names, "\n")
    node_num <- vcount(ig)
    cat("ノード数=", node_num, "\n")
    weight_list <- c()
    nodepair_list <- c()
    print(E(ig)$weight)

    df_edges <- as_data_frame (ig, what="edges") # 「from」「to」「weight」からなるデータフレームnodepairs <- c()
    print(df_edges)
    weights <- c()
    nodepairs <- c()
    vec_from <- c()
    vec_to <- c()
    vec_weight <- c()
    for(from in sorted_node_names){
        for(to in sorted_node_names){
            # nodepair を保持したベクトル生成
            nodepairs <- c(nodepairs, paste(from,">",to, sep=""))
            # weight を保持したベクトル生成
            weight <- df_edges[ df_edges$from==from & df_edges$to==to, ][[3]]

            vec_from <- c(vec_from, from)
            vec_to <- c(vec_to, to)
            if(length(weight)>0){
                vec_weight <- c(vec_weight, weight)
            }else{
                vec_weight <- c(vec_weight, 0.0)
            }
        }
    }
    df <- data.frame(from=vec_from, to=vec_to, weight=vec_weight)
    #cat("\n -------line213-----------", "\n")
    #print(df)
    #cat("\n -------------------------", "\n")
    ## エッジを描く
    ig <- graph.data.frame(df[1:2], directed=F)

    # ノード間の重複したエッジや自己ループを取り除く
    #ig <- simplify(ig, remove.multiple=T, remove.loops=T)

    # エッジの重みの読み込み
    #print(df[[3]])
    E(ig)$weight <- df[[3]]

    return(ig)
}


# Usage:
#g <- makeIgraphObject("../wd_NMLMET_vs_NMLVAL_6nodes/weighted_directed_thresholded/ObservedGroupAGraph_thresholding_kopt_31.mtx")
#writeoutGraphAsPngFile(g, "../wd_NMLMET_vs_NMLVAL_6nodes/weighted_directed_thresholded/", "ObservedGroupAGraph_thresholding_kopt_31", T)
#g <- makeIgraphObject("../wd_NMLMET_vs_NMLVAL_6nodes/weighted_directed_thresholded/ObservedGroupBGraph_thresholding_kopt_31.mtx")
#writeoutGraphAsPngFile(g, "../wd_NMLMET_vs_NMLVAL_6nodes/weighted_directed_thresholded/", "ObservedGroupBGraph_thresholding_kopt_31", T)
