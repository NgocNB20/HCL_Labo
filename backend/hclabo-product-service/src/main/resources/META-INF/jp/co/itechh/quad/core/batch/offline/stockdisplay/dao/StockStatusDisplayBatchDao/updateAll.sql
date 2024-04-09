update stockStatusDisplay  set
                               stockStatusPc = stockStatus.stockStatusPc,
                               updateTime = /*today*/'2010-01-01'
    from(
    select
        goodsGroupStatus.goodsGroupSeq,
        -- 商品グループ単位の在庫ステータス表示順を在庫ステータスへ変換
        case
        when goodsGroupStatus.stockStatusOrdinalPc = 10 then 0
        when goodsGroupStatus.stockStatusOrdinalPc = 20 then 1
        when goodsGroupStatus.stockStatusOrdinalPc = 30 then 2
        when goodsGroupStatus.stockStatusOrdinalPc = 50 then 3
        when goodsGroupStatus.stockStatusOrdinalPc = 60 then 4
        when goodsGroupStatus.stockStatusOrdinalPc = 80 then 5
        end as stockStatusPc
    from(
        select
            goodsStatus.goodsGroupSeq,
            -- 商品グループ単位で在庫ステータス表示順PCを集約
            max(goodsStatus.unitStockStatusOrdinalPc) as stockStatusOrdinalPc
        from(
            -- PC・MBのステータスを、 『0:非販売-10』、『1:販売期間終了-20』、『2:販売前-30』、『3:在庫なし-50』、『4:在庫あり-70』、『5:残りわずか-80』 いずれかにステータスを振り分け
            select
                openGoods.goodsGroupSeq,
                case
				-- PCのステータスの決定
			    when openGoods.saleStatusPc = '0'
			        -- 販売状態が「非販売」 ⇒ 『0:非販売』
			        then 10
			    when openGoods.saleStatusPc = '1'
			        -- 販売状態が「販売中」、かつ、販売終了日が現在日付より過去の場合 ⇒『1:販売期間終了』
			        and (openGoods.saleEndTimePc < /*today*/'2010-01-01') then 20
			    when openGoods.saleStatusPc = '1'
			        -- 販売状態が「販売中」、かつ、販売開始日が現在日付より未来の場合 ⇒『2:販売前』
			        and (openGoods.saleStartTimePc > /*today*/'2010-01-01') then 30
			    when openGoods.saleStatusPc = '1'
			        -- 販売状態が「販売中」、かつ、(「在庫管理する」、かつ、販売可能在庫 <=0)  ⇒『3:在庫なし』
			        and (openGoods.stockManagementFlag = '1' and openGoods.salesPossibleStock <= 0) then 50
			    when openGoods.saleStatusPc = '1'
			        -- 販売状態が「販売中」、かつ、(「在庫管理しない」、または、販売可能在庫 > 残少表示在庫数)の場合 ⇒『4:在庫あり』
			        and (openGoods.stockManagementFlag = '0' or openGoods.salesPossibleStock > openGoods.remainderFewStock) then 60
			    when openGoods.saleStatusPc = '1'
			        -- 販売状態が「販売中」、かつ、(販売可能在庫 <= 残少表示在庫数、かつ、販売可能在庫 > 0)場合 ⇒ 『5:残りわずか』
			        and (openGoods.salesPossibleStock <= openGoods.remainderFewStock and openGoods.salesPossibleStock > 0) then 80
		        end as unitStockStatusOrdinalPc
                from(
                    -- 「公開中」商品であるデータを抜粋
                    select
                    goods.goodsGroupSeq,
                    goods.stockManagementFlag,
                    goods.saleStartTimePc,
                    goods.saleEndTimePc,
                    goods.saleStatusPc,
                    goodsstockdisplay.goodsSeq,
                    goodsstockdisplay.realStock - goodsstockdisplay.orderReserveStock - goodsstockdisplay.safetyStock as salesPossibleStock,
                    goodsstockdisplay.remainderFewStock
                    from
                        goods,
                        goodsGroup,
                        goodsstockdisplay
                    where
                        goodsstockdisplay.goodsSeq = goods.goodsSeq
                        and goods.goodsGroupSeq = goodsGroup.goodsGroupSeq
                        and (
                            -- 公開状態PCが「公開中」で公開期間内である場合
                            (goodsGroup.goodsOpenStatusPc = '1')
                            and (goodsGroup.openStartTimePc <= /*today*/'2010-01-01' or goodsGroup.openStartTimePc is null)
                            and (goodsGroup.openEndTimePc >= /*today*/'2010-01-01' or goodsGroup.openEndTimePc is null)
                        )
                    ) as openGoods
            ) as goodsStatus
            group by goodsStatus.goodsGroupSeq
        ) as goodsGroupStatus
    ) as stockStatus
where stockStatus.goodsGroupSeq = stockStatusDisplay.goodsGroupSeq
