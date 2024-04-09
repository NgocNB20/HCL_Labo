UPDATE stocksetting
SET stockmanagementflag = /*stockManagementFlag*/0,
    updatetime = CURRENT_TIMESTAMP
WHERE
        goodsseq = /*goodsSeq*/0
