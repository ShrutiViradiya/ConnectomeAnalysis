if(!require(igraph)) install.packages("igraph")
library(igraph)

source("utils.R")
source("0_constants.R")
source("HeatMapGeneration.R")

#
# 出力先の用意
#
output_folder_path <- OUTPUT_FLD_PATH_6

#
# raw, weighted
#
input_folder_path <- OUTPUT_FLD_PATH_1
filename_without_ext <- "ObservedGroupAGraph_weighted"
g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, filename_without_ext, ".mtx", sep=""), isWeighted=T)
writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""))
#writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))
filename_without_ext <- "ObservedGroupBGraph_weighted"
g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, filename_without_ext, ".mtx", sep=""), isWeighted=T)
writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""))
#writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))

#
# raw, binarized
#
input_folder_path <- OUTPUT_FLD_PATH_1
filename_without_ext <- "ObservedGroupAGraph_binarized"
g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, filename_without_ext, ".mtx", sep=""), isWeighted=T)
writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""))
writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))
filename_without_ext <- "ObservedGroupBGraph_binarized"
g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, filename_without_ext, ".mtx", sep=""), isWeighted=T)
writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""))
writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))

#
# density corrected (=cost corrected), weighted
#
input_folder_path <- paste(OUTPUT_FLD_PATH_2, sep="")
children <- list.files(input_folder_path)
filtered_file_name_list <- c()
for( i in grep("Obs.+[0-9\\.]+_weighted\\.mtx$", children)){
    filtered_file_name_list <- c(filtered_file_name_list, children[i])
}
cat("filtered_file_name_list=", filtered_file_name_list, "\n")
for( file_name in filtered_file_name_list){
    filename_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", file_name)
    g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, file_name, sep=""), isWeighted=T)
    writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""))
    writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))
}

#
# density corrected (=cost corrected), binarized
# density corrected (=cost corrected) & strength averaged (=weight normalized) , binarized と同等
#
input_folder_path <- paste(OUTPUT_FLD_PATH_2, sep="")
children <- list.files(input_folder_path)
filtered_file_name_list <- c()
for( i in grep("Obs.+[0-9\\.]+_binarized\\.mtx$", children)){
    filtered_file_name_list <- c(filtered_file_name_list, children[i])
}
cat("filtered_file_name_list=", filtered_file_name_list, "\n")
for( file_name in filtered_file_name_list){
    filename_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", file_name)
    g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, file_name, sep=""), isWeighted=T)
    writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""))
    writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))
}


#
# density corrected (=cost corrected) & strength averaged (=weight normalized) , weighted
#
input_folder_path <- paste(OUTPUT_FLD_PATH_3, sep="")
children <- list.files(input_folder_path)
filtered_file_name_list <- c()
for( i in grep("Obs.+[0-9\\.]+_weighted_normalized\\.mtx$", children)){
    filtered_file_name_list <- c(filtered_file_name_list, children[i])
}
cat("filtered_file_name_list=", filtered_file_name_list, "\n")
for( file_name in filtered_file_name_list){
    filename_without_ext <- gsub("\\.[0-9A-Za-z]+$", "", file_name)
    g <- makeIgraphObjFromTxtFile(txt_file_path=paste(input_folder_path, file_name, sep=""), isWeighted=T)
    writeoutIgraphObjAsHeatMap2(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_htmp", sep=""), zero_to_one_scale=F)
    #writeoutIgraphObjAsPngFile(g=g, output_folder_path=output_folder_path, output_file_name=paste(filename_without_ext, "_net", sep=""))
}

