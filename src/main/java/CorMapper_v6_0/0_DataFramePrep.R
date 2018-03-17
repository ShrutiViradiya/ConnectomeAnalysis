flagDebug = T
#
# 年齢と性別情報のロード
#
df_sbjname_sex_age <- read.table("./data00/SubjectProfile.txt", header=T, sep="\t", stringsAsFactors=TRUE)
df_regional_volume <- read.table("./data00/StartPointDataFrame.txt", header=T, sep="\t", stringsAsFactors=TRUE)
#df_sbjname_sex_age_vol <- merge(df_sbjname_sex_age, df_regional_volume, all=FALSE)
df <- merge(df_sbjname_sex_age, df_regional_volume, all=FALSE)
df <- df[df$DISEASE=="nml",]
#df <- df[df$Val66Met=="Met+" | df$Val66Met=="Met-", ]
df <- df[!is.na(df$Val66Met), ]

if(flagDebug==TRUE) cat("df:", "\n")
if(flagDebug==TRUE) print(df)
if(flagDebug==TRUE) cat("\n")

OUTPUT_FLD_PATH <- "./data01/"
if(!file.exists(OUTPUT_FLD_PATH)) dir.create(OUTPUT_FLD_PATH)
write.table(df, paste(OUTPUT_FLD_PATH, "df.txt", sep=""), append=F, quote=F, col.names=T, sep="\t", row.names=F)

loaded_df <- read.table(paste(OUTPUT_FLD_PATH, "df.txt", sep=""), header=T, sep="\t", row.names=1, stringsAsFactors=TRUE)
#loaded_df <- read.table("./data01/df.txt", header=T, sep=" ", stringsAsFactors=TRUE)
if(flagDebug==TRUE) cat("loaded_df:", "\n")
if(flagDebug==TRUE) print(loaded_df)
if(flagDebug==TRUE) cat("\n")

