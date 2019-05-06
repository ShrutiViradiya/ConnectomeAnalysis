#
# Rscript showNetwork.R ../wd_NMLMET_vs_NMLVAL_deep+surf/binary_undirected/ObservedGroupAGraph.mtx A
# Rscript showNetwork.R ../wd_NMLMET_vs_NMLVAL_deep+surf/binary_undirected/ObservedGroupBGraph.mtx B
# Rscript showNetwork.R ../wd_NML_vs_MDD_deep+surf/binary_undirected/ObservedGroupAGraph.mtx NML
# Rscript showNetwork.R ../wd_NML_vs_MDD_deep+surf/binary_undirected/ObservedGroupBGraph.mtx MDD
readtarget <- commandArgs()[6] #読み取り先
name <- commandArgs()[7] #出力ファイル名
source("utils.R")

ig <- makeIgraphObjectFromTxtFile(readtarget)
cat("\n","degree=", "\n")
            library("igraph")
            deg <- degree(ig, mode="all")
            print(deg)
writeoutIgraphObjAsPngFile(ig, output_fld_path="../" ,OUTPUT_IMG_FILE_NAME=name, shouldShowPng=F)
