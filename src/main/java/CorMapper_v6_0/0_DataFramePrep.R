flagDebug = T
source("0_constants.R")

# 年齢と性別情報のロード
df_sbjname_sex_age <- read.table("./data00/SubjectProfile.txt", header=T, sep="\t", stringsAsFactors=TRUE)

# 体積情報のロード
df_regional_volume <- read.table("./data00/StartPointDataFrame.txt", header=T, sep="\t", stringsAsFactors=TRUE)

# 結合
df <- merge(df_sbjname_sex_age, df_regional_volume, all=FALSE)
df <- df[df$DISEASE=="nml",]
df <- df[!is.na(df$Val66Met), ]
if(flagDebug==TRUE) cat("df:", "\n")
if(flagDebug==TRUE) print(df)
if(flagDebug==TRUE) cat("\n")

# 書き出し
if(!file.exists(OUTPUT_PARENT_FLD_PATH_1)) dir.create(OUTPUT_PARENT_FLD_PATH_1)
write.table(df, paste(OUTPUT_PARENT_FLD_PATH_1, "df.txt", sep=""), append=F, quote=F, col.names=T, sep="\t", row.names=F)

#
# 以下テストコード
#
#loaded_df <- read.table(paste(OUTPUT_PARENT_FLD_PATH_1, "df.txt", sep=""), header=T, sep="\t", row.naes=1, stringsAsFactors=TRUE)
loaded_df <- read.table(paste(OUTPUT_PARENT_FLD_PATH_1, "df.txt", sep=""), header=T, sep="\t", stringsAsFactors=TRUE)

if(flagDebug==TRUE) cat("loaded_df:", "\n")
if(flagDebug==TRUE) print(loaded_df)
if(flagDebug==TRUE) cat("\n")

#ORIGINAL_GROUP_A_INDEXES <- subset(loaded_df, Val66Met == "Met+")
ORIGINAL_GROUP_A_INDEXES <- which(loaded_df$Val66Met == "Met+")
cat("ORIGINAL_GROUP_A_INDEXES=", ORIGINAL_GROUP_A_INDEXES, "\n")
