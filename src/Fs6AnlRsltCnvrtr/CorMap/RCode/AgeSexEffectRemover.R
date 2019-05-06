#
# 年齢と性別の影響を補正するための道具
#

removeAgeSexEffect <- function(vol, age, sex){
    df_for_lm <- data.frame(AGE=age, SEX=sex, Y=vol)

    #lm_rslt <- step(lm(Y ~ AGE + SEX + SEX:AGE, data=df_for_lm))
    lm_rslt <- step(lm(Y ~ AGE + SEX, data=df_for_lm))
    #lm_rslt <- lm(Y ~ AGE + SEX + SEX:AGE, data=df_for_lm)
    #lm_rslt <- lm(Y ~ AGE + SEX, data=df_for_lm)

    #print(summary(lm_rslt))
    #cat("\n");
    cat("Remove Age Sex Effenct...", "\n");
    #print(predict(lm_rslt))

    #original_vol < - vol
    predicted_Y <- predict(lm_rslt)

    a <- coef(summary(lm_rslt))
    #print(a)
    intrcpt <- a[1,1]
    AGE <- a[2, 1]

    cat("\n");
    cat("Y = " , AGE , "AGE + " , intrcpt, "\n")
    cat("\n");

    #
    # 年齢や性別に依らない部分を分離する
    #
    #
    ture_Y = df_for_lm$Y - predicted_Y
    #

    df_new <- data.frame(AGE=age, SEX=sex, OrigY=df_for_lm$Y, PredictedY=predicted_Y, TrueY=ture_Y)
    #cat("Y=", df_for_lm$Y, "\n")
    #cat("predicted_Y=", predicted_Y, "\n")
    #cat("ture_Y=", ture_Y, "\n")
    print(df_new)

    return(ture_Y)
}

#age <- c(4, 5, 6, 10, 6,2, 4, 5 , 6, 6,7, 4 )
#cat("age=", age, "\n")
#sex <- c(1, 2, 1, 1, 2,1, 1, 2 , 1, 1,2, 2 )
#cat("sex=", sex, "\n")
#vol <- c(10, 20, 39, 55 ,50, 40, 65, 40, 50, 45, 67, 40)
#delta <- rnorm(n=12, mean=4, sd=3)
#cat("delta=", delta, "\n")
#vol <- vol + delta
#cat("vol=", vol, "\n")
#removeAgeSexEffect(vol, age, sex)