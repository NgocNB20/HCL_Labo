/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ディープコピーを行うクラス
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/10 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
public class CopyUtil {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyUtil.class);

    /**
     * アクセサ接頭辞(is)
     */
    private static final String GET_PREFIX_IS = "is";

    /**
     * アクセサ接頭辞(get)
     */
    private static final String GET_PREFIX_GET = "get";

    /**
     * アクセサ接頭辞(void)
     */
    private static final String GET_PREFIX_VOID = "void";

    /**
     * 隠蔽コンストラクタ
     */
    private CopyUtil() {
    }

    /**
     * <pre>
     * src のプロパティ値を dest へコピーする。
     * ただし、dest 項目が null でない場合は、そのプロパティのみ値のコピーを行わない。
     * また、src, dest ともに、アクセッサのみを備えて
     * 対応するフィールドを持たないオブジェクトであっても正常に処理が行われる。
     * </pre>
     *
     * @param src            コピー元情報を持っているオブジェクト
     * @param dest           コピー先オブジェクト
     * @param ignoreProperty コピーしないフィールドがある場合に指定する
     */
    public static void copyPropertiesIfDestIsNull(Object src, Object dest, Set<String> ignoreProperty) {
        // ■□■□ 値の検証
        // （src または dest が null である場合、 NullPointerException をスローする）
        if (src == null || dest == null) {
            throw new NullPointerException();
        }

        // ■□■□ コピー元のオブジェクトの情報を取得する
        Class<?> srcClass = src.getClass();
        // public フィールド一覧を保持する
        List<Field> srcFieldList = getFieldList(srcClass.getDeclaredFields());

        // public メソッド一覧を保持する
        List<Method> srcAccessorList = getAccessorList(srcClass.getMethods());

        // ■□■□ コピー先のオブジェクト情報を取得する
        Class<?> destClass = dest.getClass();
        // public フィールド一覧を保持する
        List<Field> destFieldList = getFieldList(destClass.getDeclaredFields());

        // public メソッド一覧を保持する
        List<Method> destAccessorList = getAccessorList(destClass.getMethods());

        // ■□■□ コピー対象外のデータをコピー元情報から除外する
        removeIgnoreProperty(ignoreProperty, destFieldList, destAccessorList);

        // ■□■□ コピー処理
        // フィールドからコピー
        copyFromField(src, dest, srcFieldList, destFieldList);

        // アクセサからコピー
        copyFromMethod(src, dest, srcAccessorList, destAccessorList);
    }

    /**
     * publicなフィールドをリストで取得する
     *
     * @param fields Fieldの配列
     * @return Fieldのリスト
     */
    public static List<Field> getFieldList(Field[] fields) {
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if (field.getModifiers() == Modifier.PUBLIC) {
                // フィールド名、フィールドオブジェクトを保持
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    /**
     * publicで戻り値が void 型でない get アクセッサ (getXxx または isXxx) のメソッドをリストで取得する
     *
     * @param methods Methodの配列
     * @return get アクセッサのリスト
     */
    public static List<Method> getAccessorList(Method[] methods) {
        List<Method> accessorList = new ArrayList<>();
        for (Method method : methods) {
            if (method.getModifiers() == Modifier.PUBLIC && !GET_PREFIX_VOID.equals(method.getReturnType().getName())
                && (GET_PREFIX_GET.equals(method.getName().substring(0, GET_PREFIX_GET.length()))
                    || GET_PREFIX_IS.equals(method.getName().substring(0, GET_PREFIX_IS.length())))) {
                accessorList.add(method);
            }
        }
        return accessorList;
    }

    /**
     * フィールド一覧、アクセサ一覧より削除対象データを削除する
     *
     * @param ignoreProperty   削除対象一覧
     * @param destFieldList    フィールド一覧
     * @param destAccessorList アクセサ一覧
     */
    private static void removeIgnoreProperty(Set<String> ignoreProperty,
                                             List<Field> destFieldList,
                                             List<Method> destAccessorList) {
        if (ignoreProperty != null) {
            for (String value : ignoreProperty) {
                for (Field field : destFieldList) {
                    // ignorePropertyに設定されている名前と一致するものは除く
                    if (field.getName().equals(value)) {
                        destFieldList.remove(field);
                        break;
                    }
                }

                for (Method method : destAccessorList) {
                    // ignorePropertyに設定されている名前と一致するものは除く
                    String fieldName = getFieldNameFromGetterMethod(method);
                    if (fieldName.equals(value)) {
                        destAccessorList.remove(method);
                        break;
                    }
                }
            }
        }
    }

    /**
     * フィールドのコピーを行う
     *
     * @param src           コピー元オブジェクト
     * @param dest          コピー先オブジェクト
     * @param srcFieldList  コピー元フィールド一覧
     * @param destFieldList コピー先フィールド一覧
     */
    private static void copyFromField(Object src, Object dest, List<Field> srcFieldList, List<Field> destFieldList) {
        for (Field destField : destFieldList) {
            for (Field srcField : srcFieldList) {
                try {
                    // コピー先にデータがなく、フィールド名と型が同じ場合はコピーする
                    if (destField.get(dest) == null && (srcField.getName().equals(destField.getName()))
                        && (StringUtils.equals(srcField.getType().getName(), destField.getType().getName()))) {
                        destField.set(dest, srcField.get(src));
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.error("properties copy fail", e);
                }
            }
        }

    }

    /**
     * アクセサメソッドから値のコピーを行う
     *
     * @param src              コピー元オブジェクト
     * @param dest             コピー先オブジェクト
     * @param srcAccessorList  コピー元アクセサ一覧
     * @param destAccessorList コピー先アクセサ一覧
     */
    private static void copyFromMethod(Object src,
                                       Object dest,
                                       List<Method> srcAccessorList,
                                       List<Method> destAccessorList) {
        for (Method srcMethod : srcAccessorList) {
            for (Method destMethod : destAccessorList) {
                // フィールド名と型が同じ場合はコピーする
                if ((srcMethod.getName().equals(destMethod.getName())) && (StringUtils.equals(
                                srcMethod.getReturnType().getName(), destMethod.getReturnType().getName()))) {
                    try {
                        // getterメソッドより、コピー元、コピー先のデータを取得する
                        Object[] nullArray = null;
                        Object srcValue = srcMethod.invoke(src, nullArray);
                        Object destValue = destMethod.invoke(dest, nullArray);
                        // コピー先のデータに値がセットされていない場合コピー処理を行う
                        if (destValue == null) {
                            setFieldValueByName(destMethod, dest, srcValue);
                        }
                    } catch (Exception e) {
                        LOGGER.error("properties copy fail", e);
                    }
                    break;
                }
            }
        }
    }

    /**
     * getterメソッドの情報を元にsetterメソッドオブジェクトを生成し、実行する。
     *
     * @param getterMethod getterメソッド
     * @param destObj      コピー先オブジェクト
     * @param valueObj     コピーするオブジェクト
     */
    private static void setFieldValueByName(Method getterMethod, Object destObj, Object valueObj) {
        // getterメソッド情報を元にsetterメソッドを生成
        String fieldName = getFieldNameFromGetterMethod(getterMethod);
        // setメソッド名
        char firstChar = fieldName.charAt(0);
        char uFirstChar = Character.toUpperCase(firstChar);
        String setterMethodStr = "set" + fieldName.replaceFirst(String.valueOf(firstChar), String.valueOf(uFirstChar));
        try {
            Method setterMethod =
                            destObj.getClass().getMethod(setterMethodStr, new Class[] {getterMethod.getReturnType()});
            // setメソッド実行（オブジェクト、パラメータ）
            Object[] argValues = {valueObj};
            setterMethod.invoke(destObj, argValues);
        } catch (Exception e) {
            LOGGER.error("properties copy fail", e);
        }
    }

    /**
     * getterメソッドの情報を元にField名を取得する
     *
     * @param getterMethod getterメソッド
     * @return フィールド名
     */
    private static String getFieldNameFromGetterMethod(Method getterMethod) {
        String fieldName = null;
        if (GET_PREFIX_IS.equals(getterMethod.getName().substring(0, GET_PREFIX_IS.length()))) {
            fieldName = getterMethod.getName().substring(GET_PREFIX_IS.length());
        } else if (GET_PREFIX_GET.equals(getterMethod.getName().substring(0, GET_PREFIX_GET.length()))) {
            fieldName = getterMethod.getName().substring(GET_PREFIX_GET.length());
        }
        if (fieldName != null) {
            char firstChar = fieldName.charAt(0);
            char uFirstChar = Character.toLowerCase(firstChar);

            return fieldName.replaceFirst(String.valueOf(firstChar), String.valueOf(uFirstChar));
        } else {
            return "";
        }
    }

}