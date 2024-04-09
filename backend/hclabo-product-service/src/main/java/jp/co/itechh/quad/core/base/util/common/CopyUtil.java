/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.common;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * ディープコピーを行うクラス
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/10 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
public class CopyUtil {

    /**
     * 隠蔽コンストラクタ
     */
    private CopyUtil() {
    }

    /**
     * ディープコピーを行う
     *
     * @param o   コピー元のオブジェクト.
     * @param <T> コピー元の型
     * @return コピーされた新しいオブジェクト.
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(Serializable o) {

        Object newObject = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(o);
            out.close();
            byte[] bytes = bout.toByteArray();
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            newObject = in.readObject();
            in.close();
            return (T) newObject;
        } catch (Exception e) {
            // アプリケーションログ出力Helper取得
            ApplicationLogUtility applicationLogUtility =
                            ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            applicationLogUtility.writeExceptionLog(new RuntimeException("deep copy fail", e));
        }
        return (T) newObject;
    }

}