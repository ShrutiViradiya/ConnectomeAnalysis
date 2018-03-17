#
# 年齢と性別の影響を補正するための道具
#
#

removeAgeSexEffect <- function(value, age, sex, debug_flag=F, rep_size=500){
    if(debug_flag) cat("Remove Age Sex Effenct...", "\n");

    #
    # sexが「0/1」以外（例えば「m/f」「M/F」など）で表現している場合は書き直す
    #
    new_sex <- c()
    if(nlevels(sex)!=2){
        if(debug_flag) cat("removeAgeSexEffect() has failed! Please check \"sex\" level number")
        stop(-1)
    }else{

        for(i in 1:length(sex)){
            if(sex[[i]] == levels(sex)[[1]]){
                new_sex <- c(new_sex, 0)
            }else{
                new_sex <- c(new_sex, 1)
            }
        }
    }
    sex <- new_sex

    df_for_lm <- data.frame(AGE=age, SEX=sex, Y=value)

    #欠損値を含む行の除去
    df_for_lm <- na.omit(df_for_lm) #欠損値の除去
    if(debug_flag) cat("df_for_lm=", "\n")
    if(debug_flag) print(df_for_lm)
    if(debug_flag) cat("sd(df_for_lm$SEX)=", sd(df_for_lm$SEX), "\n")

    if(sd(df_for_lm$SEX)==0){
        #女性または男性しか含まれていない集団を対象とした場合
        #lm_rslt <- lm(Y ~ AGE, data = df_for_lm)
        #lm_rslt <- step(lm(Y ~ AGE, data = df_for_lm))
        capture.output(lm_rslt <- step(lm(Y ~ AGE, data = df_for_lm)))
    }else{
        #lm_rslt_of <- lm( Y ~ SEX + AGE + SEX:AGE, data = df_for_lm)
        #lm_rslt <- step(lm( Y ~ SEX + AGE + SEX:AGE, data = df_for_lm))
        capture.output(lm_rslt <- step(lm( Y ~ SEX + AGE + SEX:AGE, data = df_for_lm)))
        #lm_rslt <- step(lm(Y ~ AGE + SEX, data=df_for_lm))
        #lm_rslt <- lm(Y ~ AGE + SEX, data=df_for_lm)
    }

    #
    # 推定された係数をDataFrameとして取り出す。
    #
    coefs <-coef(summary(lm_rslt))
    if(debug_flag) cat("coefs=", "\n")
    if(debug_flag) print(coefs)
    #
    #係数の存在確認
    #
    has_INTRCPT_coef <-  length(which(row.names(coefs) == "(Intercept)"))!=0
    has_AGE_coef <- length(which(row.names(coefs) == "AGE"))!=0
    has_SEX_coef <- length(which(row.names(coefs) == "SEX"))!=0
    #
    #切片や係数の平均値、標準偏差の収集
    #
    #切片
    if(has_INTRCPT_coef){
        # 切片があるとき
        row_number_of_INTRCPT <- which(row.names(coefs) == "(Intercept)") #格納されている行番号
        mean_of_INTRCPT <- coefs[row_number_of_INTRCPT, 1] #平均値の取得
        sd_of_INTRCPT <- coefs[row_number_of_INTRCPT, 2]#標準偏差の取得
        #if(debug_flag) cat("mean_of_INTRCPT=" , mean_of_INTRCPT, "\n")
        #stop(-1)
    }else{
        # 切片がないとき
        mean_of_INTRCPT <- 0
        sd_of_INTRCPT <- 0
    }
    if(has_AGE_coef){
        row_number_of_AGE <- which(row.names(coefs) == "AGE")
        mean_of_AGE <- coefs[row_number_of_AGE, 1]
        sd_of_AGE <- coefs[row_number_of_AGE, 2]
    }else{
        mean_of_AGE <- 0
        sd_of_AGE <- 0
    }
    if(has_SEX_coef){
        row_number_of_SEX <- which(row.names(coefs) == "SEX")
        mean_of_SEX <- coefs[row_number_of_SEX, 1]
        sd_of_SEX <- coefs[row_number_of_SEX, 2]
    }else{
        mean_of_SEX <- 0
        sd_of_SEX <- 0
    }

    #
    #係数の例を多数個生成する
    #
    #rep_size <- 1000
    #切片の候補を多数個生成
    INTRCPT_list <- rnorm(rep_size, mean = mean_of_INTRCPT, sd = sd_of_INTRCPT)
    if(debug_flag) cat(length(INTRCPT_list), " INTERCPT coef candidates have generated...", "\n")
    #AGEの係数の候補を多数個生成
    AGE_coef_list <- rnorm(rep_size, mean = mean_of_AGE, sd = sd_of_AGE)
    if(debug_flag) cat(length(AGE_coef_list), " AGE coef candidates have generated...", "\n")
    #SEXの係数の候補を多数個生成
    SEX_coef_list <- rnorm(rep_size, mean = mean_of_SEX, sd = sd_of_SEX)
    if(debug_flag) cat(length(SEX_coef_list), " SEX coef candidates have generated...", "\n")

    #
    #年齢、性別で補正した体積値を格納するベクトル用意
    #
    List_of_Observed_Y_without_AGE_and_SEX_effect <- rep(0, nrow(df_for_lm))

    # ------------------ここから繰り返す-------------------------
    for(rep in 1:rep_size){
        if(debug_flag) cat("repeatation time =  ", rep, "\n")

        #########################
        # 実測値のうち年齢と性別に依らない部分を算出する関数の定義。
        get_Obsrved_Y_without_AGE_and_SEX_effect <- function(observed_vol_list, age_list, sex_list){
            #係数をサンプリングして決める
            INTRCPT <- sample(INTRCPT_list, 1, replace=F)
            AGE_coef <- sample(AGE_coef_list, 1, replace=F)
            SEX_coef <- sample(SEX_coef_list, 1, replace=F)

            if(AGE_coef==0 & SEX_coef==0){
                observed_Y_without_AGE_and_SEX_effect_list <- observed_vol_list
            }else if(AGE_coef==0){
                observed_Y_without_AGE_and_SEX_effect_list <- observed_vol_list - (SEX_coef * sex_list)
            }else if(SEX_coef==0){
                observed_Y_without_AGE_and_SEX_effect_list <- observed_vol_list - (AGE_coef * age_list)
            }else{
                observed_Y_without_AGE_and_SEX_effect_list <- observed_vol_list - (AGE_coef * age_list + SEX_coef * sex_list)
            }
            return(observed_Y_without_AGE_and_SEX_effect_list)
        }
        ########################

        ########################
        #
        # 補正観測値平均と予測値の取得
        #
        #年齢・性別の影響と取り除いた観測値達を取得し…
        Obsrved_Ys_without_AGE_and_SEX_effect <- get_Obsrved_Y_without_AGE_and_SEX_effect(df_for_lm$Y, df_for_lm$AGE, df_for_lm$SEX)
        if(debug_flag) cat("Obsrved_Ys_without_AGE_and_SEX_effect=", Obsrved_Ys_without_AGE_and_SEX_effect,"\n")
        #グローバル変数へ足し合わせる…
        List_of_Observed_Y_without_AGE_and_SEX_effect <- List_of_Observed_Y_without_AGE_and_SEX_effect + Obsrved_Ys_without_AGE_and_SEX_effect

        # ------------------ここまで繰り返す-------------------------
    }
    adj_vols <- List_of_Observed_Y_without_AGE_and_SEX_effect/rep_size
    if(debug_flag) cat("adj_vols=", "\n")
    if(debug_flag) print(adj_vols)
    return(adj_vols)
}

