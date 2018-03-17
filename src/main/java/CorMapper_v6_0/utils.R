if(!require(igraph)) install.packages("igraph")
library(igraph)

#
# 「*.mtx」ファイルからigraphオブジェクトを生成する
#
# 第１引数：隣接行列で表現されたグラフデータを保持するテキストファイルへのパス
# 第２引数：エッジに重みありかなしか
# 戻り値：igraph object
#
makeIgraphObjFromTxtFile <- function(txt_file_path, isWeighted=F){
    # ノード名とノード数の取得
    filename <- txt_file_path
    cat("filename=", filename, "\n")
    df <- as.matrix(read.table(filename, header=T, sep=" "))#テキストファイル⇒データフレーム⇒行列
    if(isWeighted == F) g <- simplify(graph.adjacency(df, mode="undirected", weighted=NULL ), remove.multiple=T, remove.loops=T)
    if(isWeighted == T) g <- simplify(graph.adjacency(df, mode="undirected", weighted=T ), remove.multiple=T, remove.loops=T)
    sort_node_name <- sort(V(g)$name)
    cat("sort_node_name=", sort_node_name, "\n")
    node_num <- vcount(g)
    cat("node_num=", node_num, "\n")
    edge_cout <- ecount(g)
    cat("edge_cout=", edge_cout, "\n")
    return(g)
}

#
# igraphObjを行列に変換するメソッド
# 第一引数：igraphObj
# 戻り値：行列(adjmtx)
#
getMatrixObjFromIgraphObj <- function(g){
    #adjmtx <- as.matrix(get.adjacency(g))
    adjmtx <- as_adjacency_matrix (g, attr="weight")
    return(adjmtx)
}


#
# 隣接行列をigraphObjに変換するメソッド
# 第一引数：隣接行列
# 戻り値：igraphObj
#
#
getIgraphObjFromMatrixObj <- function(adjmtx, directed = F, weighted = T){
    #cat("getIgraphObjFromMatrixObj()")
    #cat("\n", "adjmtx=", "\n")
    #print(adjmtx)
    #cat("\n")

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
            #cat(vec,"\n")
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
            #cat(newvec,"\n")
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
            #cat(vec,"\n")
            newvec <- c()
            for(i in vec){
                if(i==0){
                    newvec <- c(newvec, 0)
                }else{
                    newvec <- c(newvec, 1)
                }
            }
            #cat(newvec,"\n")
            adjmtx <- matrix(newvec, nrow=rownum, ncol=colnum)
            rownames(adjmtx) <- rownames
            colnames(adjmtx) <- colnames
            #cat("\n","adjmtx=", "\n")
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
writeoutIgraphObjAsPngFile <- function(g, output_folder_path, output_file_name, shouldShowPng=F,
node_info_file_path="./NodeInfo_Base_v2.txt", title="", sub_title=""){
    cat("* writeoutIgraphObjAsPngFile() has started ...\n")
    # 書き出し先の用意
    if(!exists(output_folder_path)) dir.create(output_folder_path)
    output_img_file_path <- paste(output_folder_path, output_file_name, ".png", sep="")

    png(filename=output_img_file_path, width=1000, height=1000)

    # レイアウトの定義
    cat("    Construct Node Info Dataframe ...")
    df_node_info <- read.table(node_info_file_path, header=T, sep=" ", as.is=T)
    #head(df_node_info)
    cat("done", "\n")
    cordinateX <- c()
    cordinateY <- c()
    for( vname in V(g)$name){
        cordinateX <- c( cordinateX, df_node_info[df_node_info$SimpleId==vname, ]$X)
        cordinateY <- c( cordinateY, df_node_info[df_node_info$SimpleId==vname, ]$Y)
    }
    l <- cbind (cordinateX, cordinateY)  # レイアウト座標の生成
    cat("    Layout has been constructed.", "\n")

    # Edgeの調節
    #E (g)$arrow.size <- 2

    # グラフの描画
    plot (g, layout=l)
    par(new=T) # 上書きON

    # タイトルを書き込み
    if(title == "") title <- output_file_name
    if(sub_title == "") sub_title <- output_folder_path
    title(main=title, sub=sub_title)
    par(new=F) # 上書きOFFf_group)
    dev.off()

    # グラフの表示
    if(shouldShowPng) system(paste("geeqie ", output_img_file_path, sep=""))

    return()
}





#
# igraphオブジェクトを書き出す(igraphObj⇒*.mtxファイル)
# 第２引数：出力先フォルダパス
# 第３引数：出力ファイル名（拡張子不要）
#
writeoutIgraphObjToTxtFile <-function(ig, output_folder_path, output_file_name, isWeighted=F){
    #cat("* writeout_ig_to_txt() has sterted.", "\n")

    # 書き出し先の用意
    if(!exists(output_folder_path)) dir.create(output_folder_path)
    output_file_path <- paste(output_folder_path, output_file_name, ".mtx", sep="")
    #cat(output_file_path, "へ書き出します。", "\n")

    #print(E(ig)$weight)

    if(isWeighted){
        # ig -> mtx -> df -> txt (重み付け)
        write.table(as.matrix(as_adjacency_matrix (ig, attr="weight")), output_file_path, quote=F)
    }else{
        temp_mat <- as.matrix(as_adjacency_matrix(ig, attr="weight"))
        temp_mat <- as.matrix((temp_mat > 0) + 0)
        write.table(temp_mat, output_file_path, quote=F)
    }
    #cat("output_file_path=", output_file_path, "\n")
    #cat("* writeout_ig_to_txt() has fineshed.", "\n")
    return(0)
}

#
# 隣接行列で表現されたグラフ（adjmtx）をテキストファイル（*.mtx）へ書き出す
# 第１引数：行列オブジェクト
# 第２引数：出力先フォルダパス
# 第３引数：出力ファイル名（拡張子不要）
#
writeoutMatrixObjToTxtFile <- function(adjmtx, output_folder_path, output_file_name, directed=F, isWeighted=T){
    # 書き出し先の用意
    if(!exists(output_folder_path)) dir.create(output_folder_path)
    output_file_path <- paste(output_folder_path, output_file_name, ".mtx", sep="")
    #cat(output_file_path, "へ書き出します。", "\n")

    # これでいいのではないか？→ write.table(adjmtxd, output_file_path, quote=F)

    new_g <- getIgraphObjFromMatrixObj(adjmtx, directed, isWeighted)
    writeoutIgraphObjToTxtFile(new_g, output_folder_path, output_file_name, isWeighted=isWeighted)

    return(0)
}

#
# 隣接行列表現グラフファイル（*.mtx）からリスト表現グラフファイル（*.nodepairs, *.weights）の生成
#
#
writeoutNodepairListAndWeightListFiles <- function(mtx_file_path, output_folder_path){
    cat("writeoutNodepairListAndWeightListFiles() has started...", "\n")
    cat("mtx_file_path=", mtx_file_path, "\n")

    # read graph
    df <- read.table(mtx_file_path,header=T) #テキストファイル⇒データフレーム
    d <- as.matrix(df) #データフレーム⇒行列
    g <- graph.adjacency(d,mode="undirected",weighted=TRUE)
    g <- simplify(g,remove.multiple=T,remove.loops=T)

    sort_node_name <- sort(V(g)$name)
    cat("sort_node_name=", sort_node_name, "\n")
    node_num <- vcount(g)
    cat("node_num=", node_num, "\n")

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
    write.table(df_weights, file = paste(output_folder_path, weight_file_name, sep=""), append = FALSE, quote = FALSE, row.names = FALSE, col.names=FALSE)

    cat("writeoutNodepairListAndWeightListFiles() has finished...", "\n")
    return()
}

#
# リスト表現グラフファイル（*.nodepairs, *.weights）から隣接行列表現グラフファイル（*.mtx）の生成
#
getIgraphObjFromNodepairAndWeightListFile <- function(nodepairs_file_path, weights_file_path, output_folder_path){
    df_nodepairs <- read.table(nodepairs_file_path, header=F, sep=">", stringsAsFactors=FALSE)
    df_weights <- read.table(weights_file_path, header=F, sep=" ", stringsAsFactors=FALSE)
    g <- graph.data.frame(df_nodepairs[,1:2], directed=F)# エッジを描く
    # ノード間の重複したエッジや自己ループを取り除く
    g <- simplify(g, remove.multiple=T, remove.loops=T)
    # エッジの重みの読み込み
    E(g)$weight <- df_weights[,3]
    return(g)
}

#
# 引数 nodepairs_vec: ノードペアリスト。複数の「NodeA>NodeB」という文字列で構成されたベクトル。
# 引数 weights_vec: 重みリスト。
# 引数 all_node_names_vec: 全ノードの名前リスト
# 引数 sep: 引数nodepairs_vecにおける（始点ノードと終点ノード間の）区切り文字。デフォルト「>」。
# 引数 isWeighted: weightedなグラフか否か。weightedならT、binarizedならF。デフォルトは「T」。
#
getIgraphObjFromNodepairVectorAndWeightVector <- function(nodepairs_vec, weights_vec, all_node_names_vec, sep=">", isWeighted=T){
    nodepairs_squared <- strsplit(nodepairs_vec, sep)
    src_vec <- c()
    dest_vec <- c()
    for(i in 1:length(nodepairs_squared)){
        src_vec <- c(src_vec, nodepairs_squared[[i]][1])
        dest_vec <- c(dest_vec, nodepairs_squared[[i]][2])
    }
    #cat("@getIgraphObjFromNodepairVectorAndWeightVector()", "\n")
    #cat("src_vec=", src_vec, "\n")
    #cat("dest_vec=", dest_vec, "\n")
    #cat("weights_vec=", weights_vec, "\n")

    #データ型を整える
    src_vec <- as.character(src_vec)
    dest_vec <- as.character(dest_vec)
    weights_vec <- as.numeric(weights_vec)

    # weightedなグラフではない場合weights_vecを２値化する
    if(isWeighted == F){
        binarized_weight_vec <- c()
        for(i in 1:length(weights_vec)){
            if(weights_vec[i] == 0 ){
                binarized_weight_vec <- c(binarized_weight_vec, 0)
            }else{
                binarized_weight_vec <- c(binarized_weight_vec, 1)
            }
        }
        weights_vec <- binarized_weight_vec
    }

    # 始点、終点、重み からなるデータフレームを構築
    #df <- data.frame(SRC=src_vec, DEST=dest_vec, WEIGHT=weights_vec)
    # g <- graph.data.frame(df[,1:2], directed=F, vertices=all_node_names_vec)

    # エッジを描く
    # ノードラベルによるエッジリストの場合、エッジを持たないノードが無視されるが、
    # それを回避すべく、ノードラベルの一覧表（ vertices=all_node_names_vec）を読み込ませる。
    # ただし、
    #
    # g <- graph.data.frame(df[,1:2], directed=F, vertices=all_node_names_vec)
    #
    # ↑の行で挙動がおかしくなっている。
    # vertices引数を投入した場合の挙動がおかしい。
    # vertices変数に投げ込んだものでノード名が上書きされてしまう。
    # この挙動については、「MakeIgraphObjFromEdgeListTest.R」というファイルで実験したが、
    # 再現できない。ノード数が多いときのみ起こるエラーなのか？。
    # ともかくこのへんな挙動を回避するため、以下の様なコードとした。

    # ノード欠落を防ぐため、自己ループに関する情報をあえて構築する
    src_of_self_loop <- all_node_names_vec
    dest_of_self_loop <- all_node_names_vec
    weight_srt_of_self_loop <- rep(0, length(all_node_names_vec))

    # Edge List からのネットワーク生成
    weight_num <- as.numeric(c(weights_vec, weight_srt_of_self_loop))
    df <- data.frame(SRC=c(src_vec, src_of_self_loop), DEST=c(dest_vec, dest_of_self_loop), WEIGHT=weight_num)
    #cat("df@getIgraphObjFromNodepairVectorAndWeightVector()", "\n")
    #print(df)
    g <- graph.data.frame(df[,1:2], directed=F)


    # ノード間の重複したエッジや自己ループを取り除く
    #g <- simplify(g, remove.multiple=T, remove.loops=T) #これを書くと edge pair と weight の紐付けがずれる


    # エッジの重みの読み込み
    E(g)$weight <- df$WEIGHT

    #writeoutIgraphObjToTxtFile(g, "./data07/", "g_at_getIgraphObjFromNodepairVectorAndWeightVector", isWeighted=T)

    return(g)
}

#
# ノード名に従って、ノードを並べ替えるメソッド
#
sortNodes <- function(ig){
    cat("======== sortNodes() has started...========", "\n")
    sorted_node_names <- sort(V(ig)$name)
    #cat("sorted_node_names=", sorted_node_names, "\n")
    node_num <- vcount(ig)
    #cat("node_num=", node_num, "\n")
    weight_list <- c()
    nodepair_list <- c()
    #cat("E(ig)$weight=", "\n")
    #print(E(ig)$weight)

    # 「from」「to」「weight」からなるデータフレーム df_edges
    df_edges <- as_data_frame (ig, what="edges")
    #print(df_edges)

    # エッジが存在しないと（ E(ig)$weight=NULL だと）
    # 「from」「to」の２列のみからなるdfとなってしまう。
    # このエラーを回避する
    #if(E(ig)$weight==NULL){
    if(ncol(df_edges)==2){
        df_edges <- data.frame(from=df_edges$from, to=df_edges$to, weight=rep(0.0, length(df_edges$from)))
    }

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

    cat("======== sortNodes() has finished... ========", "\n")
    return(ig)
}

#
# Weighted かつ Undirected な Group Graph の生成
# 引数 edge_value_list: エッジの重みの一覧。値が無いものは０が割り振られ、欠損値がないよう表現されている。length(node_name_list) x length(node_name_list) の行列に変換する為の
# 引数 flagDebug：デバックON/OFFフラッグ
# 戻り値：igraphオブジェクト
#
getWeightedUndirectedNetwork <- function(edge_value_list, node_name_list, flagDebug=FALSE){
    cat(" getWeightedUndirectedNetwork() has started", "\n")

    #
    # グラフの種類に応じて、相関係数ベクトル、行列データ生成、igraphオブジェクト生成、ノード名設定、グラフ書き出し
    #
    # 重み付けあり 有向性 なグラフ
    edge_value_list_weighted_directed <- edge_value_list #相関係数のベクトル生成
    mtx_of_Group_weighted_directed <- matrix(edge_value_list_weighted_directed, nrow=length(node_name_list), ncol=length(node_name_list)) #行列データの生成
    rownames(mtx_of_Group_weighted_directed) <- node_name_list
    colnames(mtx_of_Group_weighted_directed) <- node_name_list

    # igraphオブジェクトの生成
    g <- getIgraphObjFromMatrixObj(mtx_of_Group_weighted_directed, directed=F, weighted=T)
    #print(E(g)$weight)

    cat("\n", " getWeightedUndirectedNetwork() has finished.", "\n\n")
    return(g)
}

#
# Binarized かつ Undirected な Group Graph の生成
# 引数 edge_value_list: エッジの重みの一覧。値が無いものは０が割り振られ、欠損値がないよう表現されている。length(node_name_list) x length(node_name_list) の行列に変換する為の
# 引数 flagDebug：デバックON/OFFフラッグ
# 戻り値：igraphオブジェクト
#
getBinaryUndirectedNetwork <- function(edge_value_list, node_name_list, flagDebug=FALSE){
    cat(" getBinarizedUndirectedNetwork() has started", "\n")
    #
    # グラフの種類に応じて、相関係数ベクトル、行列データ生成、igraphオブジェクト生成、ノード名設定、グラフ書き出し
    #
    # 重み付けあり 有向性 なグラフ
    edge_value_list_weighted_directed <- edge_value_list #相関係数のベクトル生成
    mtx_of_Group_weighted_directed <- matrix(edge_value_list_weighted_directed, nrow=length(node_name_list), ncol=length(node_name_list)) #行列データの生成
    rownames(mtx_of_Group_weighted_directed) <- node_name_list
    colnames(mtx_of_Group_weighted_directed) <- node_name_list

    # igraphオブジェクトの生成（方向なし、重み無し）
    g <- getIgraphObjFromMatrixObj(mtx_of_Group_weighted_directed, directed=F, weighted=F)
    #print(E(g)$weight)

    cat("\n", " getBinarizedUndirectedNetwork() has finished.", "\n\n")
    return(g)
}

#
# Adjacent Matrix の形で保存されたテキストファイルからネットワークを生成し、
# ヒートマップとして表現する。
#
# 参考資料：
# C:\Users\issey\Documents\Dropbox\【本】\統計_Rグラフィックスクックブック\本文\288.jpg
#
#
writeoutIgraphObjAsHeatMap <- function(g, output_folder_path, output_file_name, title="", sub_title=""){

    #g <- sortNodes(g)

    #
    #igraphオブジェクトをEdgeListデータフレームに変換
    #
    df <- as_data_frame (g, what="edges")
    cat(" Data Frame generated from igraph object", "\n")
    print(head(df))
    #print(df)
    #cat("ncol(df)=", ncol(df), "\n")
    #cat("rownames(df)=", rownames(df) , "\n")
    #cat("colnames(df)=", colnames(df), "\n")
    #cat("df$from=" , df$from, "\n")
    #cat("\n")
    #cat("df$to=" , df$to, "\n")
    #cat("\n")

    #ヒートマップ描画用のデータフレームを組み立ててゆく
    #dfとしてロードされたエッジリストに存在するノード間についてはdf$weightを割り当て
    #リストにないノード間にはweight=0のエッジを張る

    x_values <- c()
    y_values <- c()
    z_values <- c()
    for(i in V(g)$name){
        for(j in V(g)$name){
            x_values <- c(x_values, i)
            y_values <- c(y_values, j)
            weight <- df[df$from==i & df$to==j,]$weight
            t_weight <- df[df$from==j & df$to==i,]$weight #対角線に対して対称な値
            # debug用
            #if(i=="Left-Putamen" && j=="ctx-rh-superiortemporal"){
            #    cat("i=", i , " ", "j=", j, "\n") #iやjにはノード名が入る
            #    cat("df[df$from==i & df$to==j,]$weight=" , df[df$from==i & df$to==j,]$weight, "\n")
            #    #cat("df$to=" , df$to, "\n")
            #    #cat("df$from=" , df$from, "\n")
            #    cat("length(weight)=", length(weight), "\n")
            #}
            if(length(weight)!=0){ #weightに値が入っているなら
            z_values <- c(z_values, weight)
            }else if(length(t_weight)!=0){ #t_weightに値が入っているなら
            z_values <- c(z_values, t_weight)
            }else{
                z_values <- c(z_values, 0)
            }
        }
    }
    #cat("x_values=", x_values, "\n")
    #cat("y_values=", y_values, "\n")
    #cat("z_values=", z_values, "\n")
    df_for_plot <- data.frame(NODE_i=x_values, NODE_j=y_values, WEIGHT=z_values)
    cat("df_for_plot=", "\n")
    #print(df_for_plot)
    print(head(df_for_plot))
    cat("\n")


    #
    # ヒートマップの描画
    #
    if(!require(ggplot2)) install.packages("ggplot2")
    library(ggplot2)

    #cat("as.numeric(df_for_plot$NODE_i)=", as.numeric(df_for_plot$NODE_i), "\n")
    #cat("df_for_plot$NODE_i=", df_for_plot$NODE_i, "\n")
    #データフレーム名をそのまま使う
    #p <- ggplot(df_for_plot, aes(x=NODE_i, y=NODE_j, fill=WEIGHT))


    #X軸のラベルを数字化
    #p <- ggplot(df_for_plot, aes(x=as.numeric(NODE_i), y=NODE_j, fill=WEIGHT))

    #X, Y軸のラベルを数字化
    p <- ggplot(df_for_plot, aes(x=as.numeric(NODE_i), y=as.numeric(NODE_j), fill=WEIGHT))

    #p <- p + geom_tile()# または
    p <- p + geom_raster()

    #p <- p + scale_y_reverse() #Y軸の向きを反転

    #目盛記号、目盛ラベル、目盛線の非表示
    #p <- p + scale_x_continuous(breaks=NULL)
    #p <- p + scale_y_continuous(breaks=NULL)



    vec_for_labels <- V(g)$name
    vec_for_breaks <- c(1:length(vec_for_labels))
    #p <- p + scale_x_discrete(breaks=vec_for_breaks, labels=vec_for_labels)
    p <- p + scale_x_continuous(breaks=vec_for_breaks, labels=vec_for_labels)
    p <- p + scale_y_continuous(breaks=vec_for_breaks, labels=vec_for_labels)

    p <- p + theme(axis.text.y = element_text(angle=40, hjust=1, vjust=1))
    p <- p + theme(axis.text.x = element_text(angle=40, hjust=1, vjust=1))

    #背景色を白に
    p <- p + theme_bw()

    #
    # 体裁変更
    #
    max_weight <- max(df_for_plot$WEIGHT)
    min_weight <- min(df_for_plot$WEIGHT)
    mid_weight <- (max_weight-min_weight)/2
    #mid_weight <- (max_weight-min_weight)*2/5
    #mid_weight <- mean(df_for_plot$WEIGHT)
    #mid_weight <- median(df_for_plot$WEIGHT)
    #mid_weight <- quantile(df_for_plot$WEIGHT)[[5]]
    cat("min_weight=", min_weight, " ")
    cat("mid_weight=", mid_weight, " ")
    cat("max_weight=", max_weight, "\n")

    if(!require(scales)) install.packages("scales")
    library(scales)

    # 基本色（赤と青）
    #p <- p + scale_fill_gradient2(midpoint=mid_weight, mid="gray70", limit=c(mid_weight, max_weight))

    # 白黒
    #p <- p + scale_fill_gradient2(midpoint=mid_weight, mid="gray50", limit=c(mid_weight, max_weight), low="black", high="white")

    # グレースケール
    #p <- p + scale_fill_gradient2(midpoint=mid_weight, mid="gray35", limit=c(mid_weight, max_weight), low="gray70", high="white")

    # 中間に白を挟んだグラデーション(mutedでちょっと暗め)
    #p <- p + scale_fill_gradient2(midpoint=mid_weight, limit=c(min_weight, max_weight), mid="white", low=muted("blue"), high=muted("red"))

    # 中間に黄色を挟んだグラデーション
    #p <- p + scale_fill_gradient2(midpoint=mid_weight, limit=c(min_weight, max_weight), mid="yellow", low=muted("blue"), high=muted("red"))

    # 中間に緑色を挟んだグラデーション
    p <- p + scale_fill_gradient2(midpoint=mid_weight, limit=c(min_weight, max_weight), mid="green", low=muted("blue"), high="red")

    #n個の色のグラデーション
    #p <- p + scale_fill_gradient2(midpoint=mid_weight, mid="gray70", limit=c(mid_weight, max_weight), colours = c("darkred", "orange", "yellow", "white"))

    # 出力
    output_img_file_path <- paste(output_folder_path, output_file_name, ".png", sep="")
    png(filename=output_img_file_path, width=1200, height=1200)
    plot (p)
    dev.off()


}


