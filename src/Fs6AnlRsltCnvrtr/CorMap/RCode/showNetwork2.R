# constants.Rを適切に編集した上で、
# Rscript showNetwork.R

#readtarget <- commandArgs()[6] #読み取り先
#name <- commandArgs()[7] #出力ファイル名

source("utils.R")
source("constants.R")

readtarget <- paste(OUTPUT_FLD_PATH, "binary_undirected/","ObservedGroupAGraph.mtx", sep="")
name <- strsplit(COMPARISON_PATTERN, "_vs_")[[1]][1]
ig <- makeIgraphObjectFromTxtFile(readtarget)
#cat("\n","degree=", "\n")
#library("igraph")
#deg <- degree(ig, mode="all")
#print(deg)
#sub_title_str <- paste(ORIGINAL_GROUP_A_INDEXES, collapse=" ")
sub_title_str <- ""
writeoutIgraphObjAsPngFile(ig, output_fld_path=paste(OUTPUT_FLD_PATH, "binary_undirected/", sep="") ,
 OUTPUT_IMG_FILE_NAME=name, shouldShowPng=F, sub_title=sub_title_str)

readtarget <- paste(OUTPUT_FLD_PATH, "binary_undirected/","ObservedGroupBGraph.mtx", sep="")
name <- strsplit(COMPARISON_PATTERN, "_vs_")[[1]][2]
ig <- makeIgraphObjectFromTxtFile(readtarget)
#sub_title_str <- paste(ORIGINAL_GROUP_B_INDEXES, collapse=" ")
sub_title_str <- ""
writeoutIgraphObjAsPngFile(ig, output_fld_path=paste(OUTPUT_FLD_PATH, "binary_undirected/", sep=""),
 OUTPUT_IMG_FILE_NAME=name, shouldShowPng=F, sub_title=sub_title_str)
