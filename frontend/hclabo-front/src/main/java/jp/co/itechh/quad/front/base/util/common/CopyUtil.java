/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.base.util.common;

import jp.co.itechh.quad.front.base.exception.AppLevelException;
import jp.co.itechh.quad.front.base.util.seasar.StringUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * ディープコピーを行うクラス
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/10 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
public class CopyUtil {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyUtil.class);

    /** アクセサ接頭辞(is) */
    private static final String GET_PREFIX_IS = "is";

    /** アクセサ接頭辞(get) */
    private static final String GET_PREFIX_GET = "get";

    /** アクセサ接頭辞(set) */
    private static final String GET_PREFIX_SET = "set";

    /**
     * 隠蔽コンストラクタ<br/>
     */
    private CopyUtil() {
    }

    /**
     * ディープコピーを行う
     * @param o コピー元のオブジェクト.
     * @param <T> コピー元の型
     * @return コピーされた新しいオブジェクト.
     *
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

    /**
     * <pre>
     * src のプロパティ値を dest へコピーする。
     * src のフィールドの型が一致しない場合、
     * dest のフィールドが String 型であれば value 値を文字列へ変換してコピーする
     * また、src, dest ともに、アクセッサのみを備えて
     * 対応するフィールドを持たないオブジェクトであっても正常に処理が行われる。
     * </pre>
     * @param src コピー元情報を持っているオブジェクト
     * @param dest コピー先オブジェクト
     * @param ignoreFieldNameSet コピーしないフィールドがある場合に指定する
     * @return コピー済みフィールド名リスト
     */
    public static List<String> copyProperties(Object src, Object dest, Set<String> ignoreFieldNameSet) {

        List<String> copiedFeildNameList = new ArrayList<>();

        // コピー先のオブジェクトの情報を取得する
        Class<?> destClazz = dest.getClass();
        // 子クラス ⇒ 親クラスの順にフィールドを取得する
        List<Field> destFields = new ArrayList<>();
        addDeclaredFields(destClazz, destFields);

        // コピー対象外フィールドを除去
        removeIgnoreProperty(ignoreFieldNameSet, destFields);

        // コピー処理
        for (Field field : destFields) {
            String fieldName = field.getName();

            // set メソッドを探す
            java.lang.reflect.Method method = null;
            try {
                method = destClazz.getMethod(GET_PREFIX_SET + StringUtil.capitalize(fieldName), field.getType());
            } catch (final NoSuchMethodException e) {
                // setメソッドが見つからない場合は後続で処理するため例外処理は行わない。
            }

            // 値を取得する
            Object value = null;
            try {
                value = getCopyValue(fieldName, src);
            } catch (AppLevelException ae) {
                // 値が取得できない場合、以降の処理は行わない。
                continue;
            }
            if (value != null && String.class.equals(field.getType())) {
                value = value.toString();
            }

            try {
                // 修飾子がパブリックかどうかを確認
                if (method != null && java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                    // アクセッサを使って値をセットする
                    method.invoke(dest, value);
                    copiedFeildNameList.add(fieldName);
                    continue;
                }

                // 修飾子がパブリックでないフィールドにはアクセスさせない
                if (java.lang.reflect.Modifier.isPublic(field.getModifiers())) {
                    // フィールドに直接、値をセットする
                    field.set(dest, value);
                    copiedFeildNameList.add(fieldName);
                    continue;
                }
            } catch (final Exception e) {
                LOGGER.warn(destClazz.getName() + "#" + fieldName + "に値をセットできない。", e);
            }
        }
        return copiedFeildNameList;
    }

    /**
     * <pre>
     * src のプロパティ値を dest へコピーする。
     * ただし、dest 項目が null でない場合は、そのプロパティのみ値のコピーを行わない。
     * また、src, dest ともに、アクセッサのみを備えて
     * 対応するフィールドを持たないオブジェクトであっても正常に処理が行われる。
     * </pre>
     * @param src コピー元情報を持っているオブジェクト
     * @param dest コピー先オブジェクト
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
     * @param methods Methodの配列
     * @return get アクセッサのリスト
     */
    public static List<Method> getAccessorList(Method[] methods) {
        List<Method> accessorList = new ArrayList<>();
        for (Method method : methods) {
            if (method.getModifiers() == Modifier.PUBLIC && !StringUtils.equals(
                            "void", method.getReturnType().getName()) && (
                                GET_PREFIX_GET.equals(method.getName().substring(0, GET_PREFIX_GET.length()))
                                || GET_PREFIX_IS.equals(method.getName().substring(0, GET_PREFIX_IS.length())))) {
                accessorList.add(method);
            }
        }
        return accessorList;
    }

    /**
     * フィールド一覧、アクセサ一覧より削除対象データを削除する
     * @param ignoreProperty 削除対象一覧
     * @param destFieldList フィールド一覧
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
     * フィールド一覧、アクセサ一覧より削除対象データを削除する
     * @param ignoreFieldNameSet 削除対象一覧
     * @param destFieldList フィールド一覧
     */
    private static void removeIgnoreProperty(Set<String> ignoreFieldNameSet, List<Field> destFieldList) {
        if (ignoreFieldNameSet != null) {
            for (String ignoreFieldName : ignoreFieldNameSet) {
                for (Field field : destFieldList) {
                    // ignoreFieldNameSetに設定されている名前と一致するものは除く
                    if (field.getName().equals(ignoreFieldName)) {
                        destFieldList.remove(field);
                        break;
                    }
                }
            }
        }
    }

    /**
     * フィールドのコピーを行う
     * @param src コピー元オブジェクト
     * @param dest コピー先オブジェクト
     * @param srcFieldList コピー元フィールド一覧
     * @param destFieldList コピー先フィールド一覧
     */
    private static void copyFromField(Object src, Object dest, List<Field> srcFieldList, List<Field> destFieldList) {
        for (Field destField : destFieldList) {
            for (Field srcField : srcFieldList) {
                try {
                    // コピー先にデータがなく、フィールド名と型が同じ場合はコピーする
                    if (destField.get(dest) == null && (srcField.getName().equals(destField.getName()))
                        && (StringUtils.equals(srcField.getType().getName(), (destField.getType().getName())))) {
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
     * @param src コピー元オブジェクト
     * @param dest コピー先オブジェクト
     * @param srcAccessorList コピー元アクセサ一覧
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
     * @param getterMethod getterメソッド
     * @param destObj コピー先オブジェクト
     * @param valueObj コピーするオブジェクト
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

    /**
     *
     * コピー元より値を取得する<br/>
     *
     * @param fieldName 値を取得するフィールド名
     * @param src コピー元情報を持っているオブジェクト
     * @return 取得した値
     */
    private static Object getCopyValue(String fieldName, Object src) {
        // get メソッドを探す
        Object invoker = null;
        try {
            invoker = src.getClass().getMethod("get" + StringUtil.capitalize(fieldName));
        } catch (NoSuchMethodException e) {
            // getメソッドが見つからない場合は後続で処理するため例外処理は行わない。
        }

        if (invoker == null) {
            // get メソッドが無かった場合、フィールドを探す
            try {
                invoker = src.getClass().getField(fieldName);
            } catch (NoSuchFieldException | SecurityException e1) {
                // フィールドが見つからない場合は後続で処理するため例外処理は行わない。
            }
        }

        if (invoker == null) {
            // getメソッドもフィールドもが見つからない場合はExceptionをスロー
            throw new AppLevelException();
        }

        // 値を取得する
        // 修飾子がパブリックでない、アクセサやフィールドにはアクセスさせない
        if (java.lang.reflect.Modifier.isPublic(invoker.getClass().getModifiers())) {
            // フィールドから直接またはアクセッサを使って値をゲットする
            try {
                if (invoker instanceof java.lang.reflect.Field) {
                    return ((java.lang.reflect.Field) invoker).get(src);
                } else if (invoker instanceof java.lang.reflect.Method) {
                    return ((java.lang.reflect.Method) invoker).invoke(src);
                } else {
                    LOGGER.warn(src.getClass().getName() + "#" + fieldName + "に値はセットできない。");
                }
            } catch (final Exception e) {
                LOGGER.warn(src.getClass().getName() + "#" + fieldName + "に値はセットできない。");
            }
        }
        // 値が取得できない場合はExceptionをスロー
        throw new AppLevelException();
    }

    /**
     *
     * 指定されたClassオブジェクトが表すクラスのスーパークラスを含めて<br/>
     * 宣言フィールドをリストに設定する。<br/>
     *
     * @param clazz Classオブジェクト
     * @param fields Fieldのリスト
     */
    private static void addDeclaredFields(Class<?> clazz, List<Field> fields) {

        if (fields == null) {
            throw new IllegalArgumentException("fieldsにインスタンスが設定されていません。");
        }

        // 子クラスのフィールドから順に取得
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        // スーパークラスを取得
        Class<?> superClazz = clazz.getSuperclass();

        // スーパークラスがあり、かつ、スーパークラスがObjectでない場合は再帰処理
        if (superClazz != null && superClazz != Object.class) {
            addDeclaredFields(superClazz, fields);
        }
    }

}