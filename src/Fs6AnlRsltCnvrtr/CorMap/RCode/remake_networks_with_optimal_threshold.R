#
# エントロピーの考え方を使った、グラフの２値化スクリプト
# usage:
# Rscript remake_networks_with_optimal_threshold.R ../wd_NMLMET_vs_NMLVAL_deep+surf/weighted_undirected/
# Rscript remake_networks_with_optimal_threshold.R ../wd_NMLMET_vs_NMLVAL_6nodes/weighted_undirected/
#
library(igraph)
library(snow)

source("./edge_swapping_randomization.R")
#source("./writeoutGraphAsPngFile.R")

n_cluster <-16 #CPUコア数
    shouldMultiThreads <- T # Fはデバッグモード

#
# 引数kはエッジ本数
#
#rank() : 数値ベクトルのランク(順位)統計量を計算。タイ(同一の値の組)は平均化される。
# q[,i]：i番目の症例のエッジの重み一覧
# q_rank：エッジの重み順位一覧
# q_bin：わずかでも重みがあれば１で表現されたエッジの重み一覧

calc_entropy <- function(k){
    ########################################
    # 実測グラフのエントロピー計算コア部分 #
    # Calc. entropy of actual networks     #
    ########################################
    # 残すエッジの本数kでBinarized
    q_rank_bin <- c()
    for(i in 1:s){
        q_rank_bin <- cbind(q_rank_bin, ifelse((q_rank[,i] >= k) & (q[,i] > 0), 1, 0 ))
    }
    p_j_k <- table(rowSums(q_rank_bin)) / full_edge_count
    h_real <- -sum(p_j_k * log(p_j_k)) # エントロピー計算

    #############################################
    # ランダムグラフのエントロピー計算コア部分  #
    # Calc. entropy of randomized newtorks      #
    #############################################
    # Generate random networks
    q_rank_bin_rand <- c()

    # 2症例ずつ取り出して平均エントロピーを考える場合
    for(chosen_graph_number in 1:2){

        # 多数の症例があり、網羅的に平均エントロピーを計算するのが大変な場合
        # n_rand はエントロピー計算の際のランダムネットワークの生成回数。「症例を１つ選んで、グチャグチャにして、エントロピーを計算」という作業を何回繰り返すか。
        #n_rand <- 5
        #for(i in 1:n_rand) {
        #chosen_graph_number <- sample(1:s, 1) # s人から１人選び出す

        cat("chosen_graph_number=", chosen_graph_number,"\n")
        q_rank_bin_rand_one_sample <- q_rank_bin[,chosen_graph_number] #ランダムに１症例を選び

        ###################################################
        #print(nodepair_mtx[q_rank_bin_rand_one_sample == 1, ])
        #print(class(nodepair_mtx[q_rank_bin_rand_one_sample == 1, ]))
        d <- as.data.frame(nodepair_mtx[q_rank_bin_rand_one_sample == 1, ]) #実存エッジ一覧を取り出し
        ###################################################
        # エッジ本数が１のときエッジリストからigraphオブジェクトが生成できない件
        ###################################################
        # ノードペアの書かれたデータフレーム（df）を使って
        # igraphオブジェクトを生成する際に、
        # データフレーム列数が2未満（length(df[1,]) < 2）のとき、すなわちエッジ数が１または０本のとき、
        # うまくigraphオブジェクトが生成できないようだ。
        # 例えば、
        # $ df <- as.data.frame(nodepair_mtx[vec == 1,])
        # $ g <- simplify(graph.data.frame(d,directed=F),remove.multiple=T,remove.loops=T) #グラフを描く
        # とすると
        #    graph.data.frame(d, directed = F) でエラー:
        #     the data frame should contain at least two columns
        #    呼び出し:  simplify -> is_igraph -> %in% -> match -> graph.data.frame
        # というエラーが出るようだ。なのでこれを回避するコードが必要。
        # たとえば次のように回避する。
        if( length(d[1,]) == 1 ){
            var <- nodepair_mtx[q_rank_bin_rand_one_sample == 1, ]
            adjVCT <- c(0, 1, 1, 0)
            adjMTX <- matrix(adjVCT, nrow=2, byrow=T)
            g <- graph.adjacency(adjMTX, mode="undirected", weighted=NULL)
            V(g)$name <- var
            #print(g)
        }else if( length(d[1,]) == 0 ){
            # この中が上手く走るかは未確認
            var <- nodepair_mtx[q_rank_bin_rand_one_sample == 1, ]
            adjVCT <- c(0, 0, 0, 0)
            adjMTX <- matrix(adjVCT, nrow=2, byrow=T)
            g <- graph.adjacency(adjMTX, mode="undirected", weighted=NULL)
        }else{
            g <- simplify(graph.data.frame(d,directed=F),remove.multiple=T,remove.loops=T) #グラフを描く
        }
        ###################################################
        #g <- simplify(graph.data.frame(d,directed=F),remove.multiple=T,remove.loops=T) #グラフを描く

        # add nodes with degree 0
        node_degree_0_names <- setdiff(node_names, V(g)$name)
        g_temp <- add.vertices(g,length(node_degree_0_names))
        V(g_temp)$name <- c(V(g)$name,node_degree_0_names)
        g <- g_temp

        # generate a randomized network
        # using configuration model
        #deg <- degree(g)
        #g_rand <- degree.sequence.game(deg,m = "simple.no.multiple")
        #V(g_rand)$name <- V(g)$name

        # using degree-preserving edge swapping algorithm based on Maslov and Sneppen (2002)
        #g_rand <- edge_swapping_randomization(g)

        # using degree-preserving simple edge swapping algorithm
        g_rand <- rewire(g, with=keeping_degseq(niter=ecount(g)*10,loops=F))

        edgelist_g_rand <- get.edgelist(g_rand)
        num_edge <- ecount(g_rand)
        vec <- numeric(full_edge_count)
        for(j in 1:num_edge){
            idx <- which((nodepair_mtx[,1]==edgelist_g_rand[j,1] & nodepair_mtx[,2]==edgelist_g_rand[j,2]) | (nodepair_mtx[,2]==edgelist_g_rand[j,1] & nodepair_mtx[,1]==edgelist_g_rand[j,2]))
            vec[idx] <- 1
        }
        q_rank_bin_rand <- cbind(q_rank_bin_rand,vec)
    }

    p_j_k_rand <- table(rowSums(q_rank_bin_rand)) / full_edge_count
    h_rand <- -sum(p_j_k_rand * log(p_j_k_rand))

    ####################################################################################
    # 残すエッジ数kにおける                                                            #
    # 実測グラフ達のエントロピーの平均とランダムグラフ達のエントロピーの平均の差の算出 #
    ####################################################################################
    diff_h <- h_real - h_rand

    #
    # 閾値、エントロピー差異、実測グラフ達のエントロピー平均、ランダムグラフ達のエントロピー平均
    # をベクトルとして返す。
    return(c(k, diff_h, h_real, h_rand))
}


# file_set: ２値化対象隣接行列ファイル（*.,mtx）
# output_folder_path: 出力先
# shouldOutputPng: 画像ファイルも出力するか
# n_rand: number of randomized networks
# n_cluster: number of threads for parallel computing

shouldOutputPng = FALSE
folder <- commandArgs()[6] #読み取り先

# 出力先の準備
output_folder_path <- paste(folder, "../", basename(folder), "_thresholded/", sep="")
if(!exists(output_folder_path)){
    dir.create(output_folder_path)
    cat("出力先「", output_folder_path,"」を生成しました。", "\n")
}else{
    cat("出力先「", output_folder_path,"」は既に存在します。", "\n")
}

# 処理対象mtxファイルの一覧取得
all_file_set <- system(paste("ls ",folder,"*.mtx",sep=""),intern=T)
file_set <- c()

all_file_set_size <- length(all_file_set)

cat("全グラフ数=",all_file_set_size , "\n")
if(all_file_set_size==0){
    stop("「*.mtx」ファイルがありません。")
}

# ２ファイルずつ処理する。
for( i in 1 : (all_file_set_size/2)){
    cat("", i, " / ", (all_file_set_size/2),"\n")
    file_set <- c(all_file_set[[2*i-1]], all_file_set[[2*i]])
    print(file_set)

    cat("####################################################", "\n")
    cat("# 行列表現グラフファイル⇒リスト表現グラフファイル #", "\n")
    cat("####################################################", "\n")
    num_subject <- length(file_set)
    # ノード名とノード数の取得
    filename <- file_set[[1]]

    d <- as.matrix(read.table(filename,header=T, sep=" "))
    g <- simplify(graph.adjacency(d,mode="undirected",weighted=NULL),remove.multiple=T,remove.loops=T)


    sort_node_name <- sort(V(g)$name)
    cat("ノード名=", sort_node_name, "\n")
    node_num <- vcount(g)
    cat("ノード数=", node_num, "\n")
    weight_list <- c()
    nodepair_list <- c()
    graph_names <- c()
    for(s in 1:num_subject){
        # read graph
        filename <- file_set[[s]]
        cat("filename=", filename, "を読み込み中…", "\n")

        graph_names <- c(graph_names, gsub("\\.[0-9A-Za-z]+$", "", basename(filename)))

        df <- read.table(filename,header=T) #テキストファイル⇒データフレーム
        d <- as.matrix(df) #データフレーム⇒行列
        g <- graph.adjacency(d,mode="undirected",weighted=TRUE)
        g <- simplify(g,remove.multiple=T,remove.loops=T)

        df_edges <- as_data_frame (g, what="edges") # 「from」「to」「weight」からなるデータフレーム
        nodepair_list_tmp <- c()
        weights <- c()
        for(from in sort_node_name){
            for(to in sort_node_name){
                # nodepair を保持したベクトル生成
                nodepair_list_tmp <- c(nodepair_list_tmp, paste(from,">",to, sep=""))
                # weight を保持したベクトル生成
                weight <- df_edges[ df_edges$from==from & df_edges$to==to, ][[3]]
                if(length(weight)>0){
                    weights <- c(weights , weight)
                }else{
                    weights <- c(weights , 0.0)
                }
            }
        }
        weight_list <- cbind(weight_list, weights)

        if(length(nodepair_list)==0) nodepair_list <- nodepair_list_tmp
    }

    df_nodepair_list <- data.frame(nodepair_list)
    write.table(df_nodepair_list, file = paste(output_folder_path, "nodepair.list", sep=""), append = FALSE, quote = FALSE, row.names = FALSE, col.names=FALSE)
    cat(paste(output_folder_path, "nodepair.list", sep=""), "へ書き出しました。", "\n")

    df_weight_list <- data.frame(weight_list)
    names(df_weight_list) <- graph_names
    write.table(df_weight_list, file = paste(output_folder_path, "weight.list", sep=""), append = FALSE, quote = FALSE, row.names = FALSE)
    cat(paste(output_folder_path, "weight.list", sep=""), "へ書き出しました。", "\n")

    cat("#########################################", "\n")
    cat("# nodepair_list と weight_list のロード #", "\n")
    cat("#########################################", "\n")
    nodepair_mtx <- as.matrix(read.table(paste(output_folder_path, "nodepair.list", sep=""),header=F,sep=">"))
    cat("---- ", "nodepair_mtx", " ----", "\n")
    print(nodepair_mtx)
    cat("\n")
    #cat("---- ", "nodepair_mtx[q_rank_bin[,1] == 1, ]", " ----", "\n")
    #print(nodepair_mtx[q_rank_bin[,1] == 1, ]) #実存エッジ一覧を取り出し
    #cat("\n")

    node_names <- unique(c(nodepair_mtx))
    cat("node_names: ")
    print(node_names)

    q <- as.matrix(read.table(paste(output_folder_path, "weight.list", sep=""), header=T, sep=" "))  # q : エッジの重み一覧
    dim_q <- dim(q) # 総エッジ数（含weight=0）× 症例数
    #cat("dim_q=",dim_q, "\n")
    full_edge_count <- dim_q[[1]] # 総エッジ数（含weight=0）　理論上のFULLエッジ数
    s <<- dim_q[[2]] # 症例数
    cat("c (理論上のFULLエッジ数) =", full_edge_count, "    ", "s (症例数) =", s, "\n");
    cat("\n")

    #---------
    #
    cat("##############################", "\n")
    cat("# エッジの重みでランキング化 #", "\n")
    cat("##############################", "\n")
    q_bin <- c()
    q_rank <- c()
    for(i in 1:s){
        #rank() : 数値ベクトルのランク(順位)統計量を計算。タイ(同一の値の組)は平均化される。
        # q[,i]：i番目の症例のエッジの重み一覧
        # q_rank：エッジの重み順位一覧
        # q_bin：わずかでも重みがあれば１で表現されたエッジの重み一覧
        q_rank <- cbind(q_rank, rank( q[,i], ties.method = "first") )
        q_bin <- cbind(q_bin,ifelse(q[,i]>0, 1, 0))
    }
    # まとめ
    nodepair_mtx_2 <- as.matrix(read.table(paste(output_folder_path, "nodepair.list", sep=""),header=F,sep=""))
    cat("---- ", "q (weight) 一覧", " ----", "\n")
    rownames(q) <- nodepair_mtx_2[,1]
    print(head(q))
    cat("～略～\n")
    print(tail(q))
    cat("\n")
    cat("---- ", "q_rank (重みランキング) 一覧", " ----", "\n")
    q_rank_mtx <- as.data.frame(q_rank)
    rownames(q_rank_mtx) <- nodepair_mtx_2[,1]
    colnames(q_rank_mtx) <- colnames(q)
    print(head(q_rank_mtx))
    cat("～略～\n")
    print(tail(q_rank_mtx))
    cat("\n")
    cat("---- ", "q_bin (二値化された重みランキング) 一覧", " ----", "\n")
    q_bin_mtx <- as.data.frame(q_bin)
    rownames(q_bin_mtx) <- nodepair_mtx_2[,1]
    colnames(q_bin_mtx) <- colnames(q)
    #print(q_bin_mtx)
    print(head(q_bin_mtx))
    cat("～略～\n")
    print(tail(q_bin_mtx))
    cat("\n")
    #cat("---- ", "各グラフの総エッジ数", " ----", "\n")
    #print(sum(q_bin_mtx))
    #cat("\n")

    diff_h_min <- 999


    cat("####################################", "\n")
    cat("# calc. entropy and find optimal k #", "\n")
    cat("####################################", "\n")

    #
    #エントロピーを計算する閾値の範囲を算出する
    #
    c_start <- full_edge_count - 2 # c=理論上のFULLエッジ数よりも２本だけ少ない数のエッジ。
    # すなわち理論上のFULLエッジ数よりも２本だけ少ない数のエッジ数を持つネットワークとは、
    # エッジスワップ可能なフラフのうちもっとも密なグラフを想定していると思われる。
    #
    # colSums(q_bin)：実存するエッジ数（weight=0は含まず） 症例の数だけ列がある
    # max(colSums(q_bin))：もっとも実存エッジ数が多い症例の実存エッジ数
    # full_edge_count - max(colSums(q_bin))：現実と理論の差？
    #
    #cat(colSums(q_bin), "\n")
    #cat(max(colSums(q_bin)), "\n")
    c_end <- full_edge_count - max(colSums(q_bin))
    cat("c_start=",c_start," ", "c_end=",c_end,"\n")

    #
    # calc_entropy関数内で使う関数、変数の並列処理登録
    #
    cl <- makeCluster(n_cluster, type = "SOCK")
    clusterExport(cl, "q")
    clusterExport(cl, "s")
    clusterExport(cl, "full_edge_count")
    clusterExport(cl, "q_rank")
    # clusterExport(cl, "n_rand")
    clusterExport(cl, "nodepair_mtx")
    clusterExport(cl, "simplify")
    clusterExport(cl, "graph.data.frame")
    clusterExport(cl, "node_names")
    clusterExport(cl, "V")
    clusterExport(cl, "add.vertices")
    clusterExport(cl, "V<-")
    clusterExport(cl, "edge_swapping_randomization")
    clusterExport(cl, "vcount")
    clusterExport(cl, "is.directed")
    clusterExport(cl, "as.directed")
    clusterExport(cl, "delete.edges")
    clusterExport(cl, "E")
    clusterExport(cl, "is.mutual")
    clusterExport(cl, "get.adjacency")
    clusterExport(cl, "get.edgelist")
    clusterExport(cl, "as.undirected")
    clusterExport(cl, "graph.adjacency")
    clusterExport(cl, "ecount")
    clusterExport(cl, "degree")
    clusterExport(cl, "degree.sequence.game")
    clusterExport(cl, "rewire")
    clusterExport(cl, "keeping_degseq")

    #
    # c_start～c_endの範囲でエントロピーを計算
    #
    # c_start <- c - 2 # c=理論上のFULLエッジ数よりも２本だけ少ない数のエッジ。
    # colSums(q_bin)：実存するエッジ数（weight=0は含まず） 症例の数だけ列がある
    # max(colSums(q_bin))：もっとも実存エッジ数が多い症例の実存エッジ数
    # c - max(colSums(q_bin))：現実と理論の差？
    # c_end <- c - max(colSums(q_bin))

    #
    if(shouldMultiThreads){
        # 並列処理
        sum <- as.data.frame(t(parSapply(cl, c_start:c_end, calc_entropy))) #並列計算
    }else{
        # 非並列処理（デバッグ用）
        tmp<-c()
        for(k in c_start:c_end){
            cat("k=", k, "\n")
            tmp <- cbind(tmp, calc_entropy(k)) #エントロピー計算
        }
        sum <- as.data.frame(t(tmp))
    }

    #
    # まとめ
    # 閾値、エントロピー差異、実測グラフのエントロピー、ランダムグラフのエントロピー
    names(sum) <- c("k","h_diff","h_real","h_rand")
    cat("\n")
    print(sum)
    cat("\n")

    #
    # extract optimal k
    # 実測グラフ達のエントロピーの平均値とランダムグラフ達のエントロピーの平均値の差が最小のときの閾値k（残すエッジの本数）を
    # 最適な閾値と考え取り出す。
    # なぜランダムネットワークと近しいエントロピーをもつ時が最適なのか？
    # エッジ数を揃えた実測グラフ達のエントロピーの平均が
    # 同ノード数かつ同エッジ数のランダムグラフ達のエントロピーの平均と
    # 同じになるなるようなときを基準とすることによって、
    # 乱雑な状態を基準として、個々の実測グラフがもつ情報量を比較しやすくするため。
    #
    # ※エントロピー（＝情報量）はXの取る値の「乱雑さ」を表す尺度である
    #
    k_opt <- sum[which(sum$h_diff == min(sum$h_diff)),]$k
    # k_optが２つ以上見つかることがあるので、その際は１つでいいので、大きい方を採用
    k_opt <- max(k_opt)
    cat("k_opt=", k_opt, "\n");
    cat("\n")

    cat("##########################################", "\n")
    cat("# remake networks with optimal threshold #", "\n")
    cat("##########################################", "\n")
    #
    # 最適な閾値で二値化したグラフを生成する。
    #
    folder <- output_folder_path
    for(i in 1:s){
        vec <- ifelse(q_rank[,i]>=k_opt & q[,i]>0, 1, 0)
        d <- as.data.frame(nodepair_mtx[vec == 1,])

        print(d)

        if( length(d[1,]) == 1 ){
            # 「エッジ本数が１のときエッジリストからigraphオブジェクトが生成できない件」を参照
            var <- nodepair_mtx[vec == 1, ]
            adjVCT <- c(0, 1, 1, 0)
            adjMTX <- matrix(adjVCT, nrow=2, byrow=T)
            g <- graph.adjacency(adjMTX, mode="undirected", weighted=TRUE)
            V(g)$name <- var
        }else{
            g <- simplify( graph.data.frame(d, directed=F), remove.multiple=T,remove.loops=T)
            #上記の命令文で生成されるgではweightが０なので、次の命令文でweightを与える。
            E(g)$weight <- rep(1, ecount(g)) #
        }

        # add nodes with degree 0
        node_degree_0_names <- setdiff(node_names, V(g)$name)
        cat("node_degree_0_names:", "\n")
        print(node_degree_0_names)
        g_temp <- add.vertices(g, length(node_degree_0_names))
        V(g_temp)$name <- c(V(g)$name, node_degree_0_names)
        ig <- g_temp

        #
        #ノードの並び替え
        #cat("ノードの並び替え", "\n")
        source("./utils.R")
        ig <- sortNodes(ig)
        #cat("E(ig)$weight=")
        #print(E(ig)$weight)


        # 書き出し先の用意
        output_file_name <- paste(colnames(q)[[i]],"_thresholding_kopt_",k_opt ,sep="")
        writeout_ig_to_txt(ig,  output_fld_path=output_folder_path, output_file_name=output_file_name)
    }

}
