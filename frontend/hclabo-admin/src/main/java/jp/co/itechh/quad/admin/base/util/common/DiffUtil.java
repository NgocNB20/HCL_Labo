/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.util.common;

import jp.co.itechh.quad.admin.annotation.diff.DiffIgnore;
import jp.co.itechh.quad.admin.constant.type.EnumType;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * オブジェクトの相違点を検出するクラス
 *
 * @author tm27400
 * @author tomo (itec) 2012/02/01 チケット #2811 対応
 * @author harada 2012/09/13 AbstractEnumTypeを継承したクラスがDtoとして判定されるのを回避する対応
 * @author tomo (itec) 2012/10/17 同じFQCNの タイプセーフenumの比較NG結果 と 旧enumの比較NG結果 が同じ内容になるように改修
 */
public class DiffUtil {

    /**　区切り文字　*/
    public static final String SEPARATOR = ".";

    /**
     * DTOオブジェクトの比較を行う。<br />
     * 相違点がない場合は空のリスト(nullではない)を返す<br />
     * <br />
     *
     * <pre>
     * DTOの比較で相違点が見つかった場合の返却リスト例
     *  SimpleClassName.field1Name
     *  SimpleClassName.field2Name.childFieldName
     *
     * <hr />
     *
     * 配列の比較で相違点が見つかった場合の返却リスト例
     *  int[2][0]
     *  int[2][1]
     *
     * <hr />
     *
     * Listの比較で相違点が見つかった場合の返却リスト例
     *  ArrayList[0]
     *  ArrayList[1].entrysFieldName
     *
     * <hr />
     *
     * Mapの比較で相違点が見つかった場合の返却リスト例
     *  TreeMap.SIZE
     *  HashMap["Key"].ValueClass.fieldName
     *  HashMap["OuterKey"].LinkedHashMap["InnerKey"].ValueClass.fieldName
     *
     * <hr />
     *
     * Setの比較で相違点が見つかった場合の返却リスト例
     *  HashSet.SIZE
     *  HashSet[String].NOT_FOUND … String はエントリの単純クラス名
     *
     * <hr />
     *
     * Enumの比較で相違点が見つかった場合の返却リスト例
     *  SimpleClassName
     *  SimpleClassName.value
     *  SimpleClassName.label
     *
     * </pre>
     *
     * <ul>
     * <li>LinkedHashMap, LinkedHashSet 等の連続性の比較は出来ません。</li>
     * <li>Set 同士の比較は簡易比較(同じ内容or別内容orサイズ違い)のみとなります。エントリ内の相違箇所は指摘できません。</li>
     * <li>ディレクトリ構成の比較(Fileオブジェクト比較）は未検証になります。使用する際は試験を作成＆実施してください。</li>
     * </ul>
     *
     * @param original  比較元オブジェクト
     * @param modified  比較先オブジェクト
     * @return 比較結果
     */
    public static List<String> diff(final Object original, final Object modified) {

        // 両方 null 値の場合
        if (original == null && modified == null) {
            return Arrays.asList(new String[] {});
        }

        // どちらかが null 値の場合
        if (original == null || modified == null) {
            return Arrays.asList("NULL");
        }

        // クラスが異なる場合
        if (!StringUtils.equals(original.getClass().getName(), modified.getClass().getName())) {
            return Arrays.asList(original.getClass().getSimpleName());
        }

        // ID が同じ場合
        if (System.identityHashCode(original) == System.identityHashCode(modified)) {
            return Arrays.asList(new String[] {});
        }

        final List<String> diffList = new ArrayList<>();
        final String parent = original.getClass().getSimpleName();

        // ディレクトリ構造を比較する場合
        if (original instanceof File && modified instanceof File) {
            compareDirectoryTree((File) original, (File) modified, diffList);
            Collections.sort(diffList);
            return diffList;
        }

        try {

            synchronized (original) {
                synchronized (modified) {
                    compareObject(original, modified, diffList, parent, new LinkedHashSet<>(), new LinkedHashSet<>());
                }
            }

        } catch (final CyclicReferenceException e) {
            diffList.add(parent + ".CYCLIC_REFERENCE");
        } catch (final AbortComparingException e) {
            // 処理を中断してそこまでの結果を返すための例外であるため、特別な処理は不要
        }

        return diffList;
    }

    /**
     * オブジェクト同士の比較を行う。
     *
     * @param value1    比較元
     * @param value2    比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean compareObject(final Object value1,
                                         final Object value2,
                                         final List<String> diffList,
                                         final String self,
                                         final Set<Object> leftHold,
                                         final Set<Object> rightHold) {

        boolean match = false;

        switch (checkType(value1, value2)) {
            case BOTH_NULL:
                match = true;
                break;
            case EIHTER_NULL:
                match = false;
                diffList.add(self);
                // Enumクラス判定
                if (isEnumClass(value1) || isEnumClass(value2)) {
                    // 比較元または比較先がEnumクラスの場合、不一致項目一覧へ「value」「label」を追加
                    diffList.add(self + ".value");
                    diffList.add(self + ".label");
                }
                break;
            case DIFFERENT_CLASS:
                match = false;
                diffList.add(self);
                break;
            case LIST:
                holdObjects(value1, value2, leftHold, rightHold, diffList, self);
                match = compareList(value1, value2, diffList, self, leftHold, rightHold);
                releaseObjects(value1, value2, leftHold, rightHold);
                break;
            case MAP:
                holdObjects(value1, value2, leftHold, rightHold, diffList, self);
                match = compareMap(value1, value2, diffList, self, leftHold, rightHold);
                releaseObjects(value1, value2, leftHold, rightHold);
                break;
            case OBJECT_ARRAY:
                holdObjects(value1, value2, leftHold, rightHold, diffList, self);
                match = compareArray(value1, value2, diffList, self, leftHold, rightHold);
                releaseObjects(value1, value2, leftHold, rightHold);
                break;
            case PRIMITIVE_ARRAY:
                holdObjects(value1, value2, leftHold, rightHold, diffList, self);
                match = comparePrimitiveArray(value1, value2, diffList, self, leftHold, rightHold);
                releaseObjects(value1, value2, leftHold, rightHold);
                break;
            case ENUM:
                // Enum は循環参照のチェックは行わない。
                match = compareEnum(value1, value2, diffList, self);
                break;
            case DTO:
                holdObjects(value1, value2, leftHold, rightHold, diffList, self);
                match = compareDto(value1, value2, diffList, self, leftHold, rightHold);
                releaseObjects(value1, value2, leftHold, rightHold);
                break;
            case SET:
                holdObjects(value1, value2, leftHold, rightHold, diffList, self);
                match = compareSet(value1, value2, diffList, self, leftHold, rightHold);
                releaseObjects(value1, value2, leftHold, rightHold);
                break;
            case COMPARABLE:
                match = compareComparable(value1, value2, diffList, self);
                break;
            default:
                match = value1.equals(value2);
                if (!match) {
                    diffList.add(self);
                }
        }

        return match;
    }

    /**
     * リスト同士の比較を行う。
     *
     * @param obj1      比較元
     * @param obj2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean compareList(final Object obj1,
                                       final Object obj2,
                                       final List<String> diffList,
                                       final String self,
                                       final Set<Object> leftHold,
                                       final Set<Object> rightHold) {

        final List<?> list1 = (List<?>) obj1;
        final List<?> list2 = (List<?>) obj2;
        boolean totalMatch = true;

        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {

            final Object value1 = list1.get(i);
            final Object value2 = list2.get(i);
            String child = self + "[" + i + "]";
            boolean match = false;

            try {
                match = compareObject(value1, value2, diffList, child, leftHold, rightHold);
            } catch (CyclicReferenceException cre) {
                // 比較中のオブジェクトで循環参照を検出した場合、不一致項目に情報を登録し、比較処理を中断する
                diffList.add(self + "[" + i + "].CYCLIC_REFERENCE");
                throw new AbortComparingException();
            }

            // 一つでも同一値でないフィールドがある場合はこのオブジェクトは不一致とする
            if (!match) {
                totalMatch = false;
            }
        }

        // サイズが一致しない場合は不一致とする
        if (list1.size() != list2.size()) {
            totalMatch = false;
            diffList.add(self + ".SIZE");
        }

        return totalMatch;
    }

    /**
     * マップ同士の比較を行う。
     *
     * @param obj1      比較元
     * @param obj2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean compareMap(final Object obj1,
                                      final Object obj2,
                                      final List<String> diffList,
                                      final String self,
                                      final Set<Object> leftHold,
                                      final Set<Object> rightHold) {

        final Map<?, ?> map1 = (Map<?, ?>) obj1;
        final Map<?, ?> map2 = (Map<?, ?>) obj2;
        boolean totalMatch = true;

        for (final Iterator<?> iter = map1.keySet().iterator(); iter.hasNext(); ) {

            final Object key = iter.next();
            String id = null;
            if (key != null) {
                id = "\"" + key + "\"";
            } else {
                id = "null";
            }
            final Object value1 = map1.get(key);
            final Object value2 = map2.get(key);
            String child = self + "[" + id + "]";
            if (value1 != null && value2 != null && !Collection.class.isAssignableFrom(value1.getClass())) {
                child += "." + value1.getClass().getSimpleName();
            }
            boolean match = false;

            try {
                match = compareObject(value1, value2, diffList, child, leftHold, rightHold);
            } catch (CyclicReferenceException cre) {
                // 比較中のオブジェクトで循環参照を検出した場合、不一致項目に情報を登録し、比較処理を中断する
                diffList.add(self + "[" + id + "]" + ".CYCLIC_REFERENCE");
                throw new AbortComparingException();
            }

            // 一つでも同一値でないフィールドがある場合はこのオブジェクトは不一致とする
            if (!match) {
                totalMatch = false;
            }
        }

        // サイズが一致しない場合は不一致とする
        if (map1.size() != map2.size()) {
            totalMatch = false;
            diffList.add(self + ".SIZE");
        }

        return totalMatch;
    }

    /**
     * セット同士の比較を行う。<br/>
     *
     * <pre>
     * 「同じ内容のSetオブジェクトかどうか」と「セットのサイズが同じか」の確認のみ可能。
     * ※差異項目の指摘は出来ない。
     * </pre>
     *
     * @param obj1      比較元
     * @param obj2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean compareSet(final Object obj1,
                                      final Object obj2,
                                      final List<String> diffList,
                                      final String self,
                                      final Set<Object> leftHold,
                                      final Set<Object> rightHold) {

        // 循環参照によるStackOverFlowを回避するために、ハッシュ系オブジェクトではなく、リストで処理する
        Set<?> set1 = (Set<?>) obj1;
        Set<?> set2 = (Set<?>) obj2;
        List<?> list1 = new ArrayList<Object>(set1);
        List<?> list2 = new ArrayList<Object>(set2);
        boolean totalMatch = true;

        //
        // 比較対象の Set 内に同じ内容のオブジェクトがあるかどうかの確認を行う。
        // 比較ループ処理内で別オブジェクトを確認した場合、常に「差異あり」情報がdiffListに登録されてしまうため、
        // 引数の diffList ではなくダミーのリストを使用して確認する。
        //

        List<String> dummyDiffList = new ArrayList<>();

        for (Object o1 : list1) {
            boolean foundSameEntry = false;

            try {
                for (Iterator<?> iter = list2.iterator(); iter.hasNext(); ) {
                    Object o2 = iter.next();
                    // 同じ内容のオブジェクトを見つけた場合
                    if (compareObject(o1, o2, dummyDiffList, self, leftHold, rightHold)) {
                        foundSameEntry = true;
                        iter.remove();
                        break;
                    }
                }
            } catch (CyclicReferenceException cre) {
                // 比較中のオブジェクトで循環参照を検出した場合：
                // ・ダミーのdiffListに情報を登録してもすぐに破棄される。
                // ・Setの中のSetを比較している場合もある。その場合は、このメソッドの引数の diffList もダミーである。
                // 上記の点を考慮して、循環参照はここでハンドリングせず、呼び出し元に処理を任せる。
                throw cre;
            }

            if (!foundSameEntry) {
                totalMatch = false;
                diffList.add(self + "[" + o1.getClass().getSimpleName() + "].NOT_FOUND");
            }
        }

        // サイズが一致しない場合は不一致とする
        if (set1.size() != set2.size()) {
            totalMatch = false;
            diffList.add(self + ".SIZE");
        }

        return totalMatch;
    }

    /**
     * オブジェクト配列同士の比較を行う。<br/>
     *
     * @param obj1      比較元
     * @param obj2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean compareArray(final Object obj1,
                                        final Object obj2,
                                        final List<String> diffList,
                                        final String self,
                                        final Set<Object> leftHold,
                                        final Set<Object> rightHold) {

        final Object[] array1 = (Object[]) obj1;
        final Object[] array2 = (Object[]) obj2;
        boolean totalMatch = true;

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {

            final String child = self.replaceAll("\\[\\]", "") + "[" + i + "]";
            final Object value1 = array1[i];
            final Object value2 = array2[i];
            final boolean match = compareObject(value1, value2, diffList, child, leftHold, rightHold);

            // 一つでも同一値でないフィールドがある場合はこのオブジェクトは不一致とする
            if (!match) {
                totalMatch = false;
            }
        }

        // 配列長が一致しない場合は不一致とする
        if (array1.length != array2.length) {
            totalMatch = false;
            diffList.add(self + ".SIZE");
        }

        return totalMatch;

    }

    /**
     * プリミティブ配列同士の比較を行う。<br/>
     *
     * @param obj1      比較元
     * @param obj2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean comparePrimitiveArray(final Object obj1,
                                                 final Object obj2,
                                                 final List<String> diffList,
                                                 final String self,
                                                 final Set<Object> leftHold,
                                                 final Set<Object> rightHold) {

        boolean match = true;

        if (obj1 instanceof boolean[]) {

            final boolean[] b1 = (boolean[]) obj1;
            final boolean[] b2 = (boolean[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof byte[]) {

            final byte[] b1 = (byte[]) obj1;
            final byte[] b2 = (byte[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof short[]) {

            final short[] b1 = (short[]) obj1;
            final short[] b2 = (short[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof int[]) {

            final int[] b1 = (int[]) obj1;
            final int[] b2 = (int[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof long[]) {

            final long[] b1 = (long[]) obj1;
            final long[] b2 = (long[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof char[]) {

            final char[] b1 = (char[]) obj1;
            final char[] b2 = (char[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof float[]) {

            final float[] b1 = (float[]) obj1;
            final float[] b2 = (float[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        } else if (obj1 instanceof double[]) {

            final double[] b1 = (double[]) obj1;
            final double[] b2 = (double[]) obj2;
            for (int i = 0; i < Math.min(b1.length, b2.length); i++) {
                if (b1[i] != b2[i]) {
                    diffList.add(self.replaceAll("\\[\\]", "") + "[" + i + "]");
                    match = false;
                }
            }

            if (b1.length != b2.length) {
                diffList.add(self + ".SIZE");
                match = false;
            }

        }

        return match;
    }

    /**
     * Enum同士の比較を行う。<br/>
     *
     * @param enu1      比較元
     * @param enu2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @return 値が同じである場合 true
     * @since HM3.2.5
     */
    private static boolean compareEnum(final Object enu1, final Object enu2, final List<String> diffList, String self) {

        // Enumクラス（列挙子）およびAbstractEnumTypeクラスは、
        // 定数（public static finalなフィールド）と同等であるため、参照比較のみを行う。
        if (enu1 != enu2) {
            diffList.add(self);
            diffList.add(self + ".value");
            diffList.add(self + ".label");
            return false;
        }

        return true;
    }

    /**
     * DTO同士の比較を行う。<br/>
     *
     * @param dto1     比較元
     * @param dto2     比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @param leftHold  循環参照回避用オブジェクト
     * @param rightHold 循環参照回避用オブジェクト
     * @return 値が同じである場合 true
     */
    private static boolean compareDto(final Object dto1,
                                      final Object dto2,
                                      final List<String> diffList,
                                      final String self,
                                      final Set<Object> leftHold,
                                      final Set<Object> rightHold) {

        final Map<String, Object> propMap1 = getPropertyMap(dto1);
        final Map<String, Object> propMap2 = getPropertyMap(dto2);
        boolean totalMatch = true;

        for (final Iterator<String> iter = propMap1.keySet().iterator(); iter.hasNext(); ) {

            final String key = iter.next();
            final String child = self + "." + key;
            final Object value1 = propMap1.get(key);
            final Object value2 = propMap2.get(key);

            boolean match = compareObject(value1, value2, diffList, child, leftHold, rightHold);

            // 一つでも同一値でないフィールドがある場合はこのオブジェクトは不一致とする
            if (!match) {
                totalMatch = false;
            }
        }

        return totalMatch;

    }

    /**
     * Comparable実装オブジェクト同士の比較を行う。<br/>
     *
     * @param obj1      比較元
     * @param obj2      比較先
     * @param diffList  不一致項目一覧
     * @param self      項目の位置情報
     * @return 値が同じである場合 true
     */
    @SuppressWarnings("unchecked")
    private static boolean compareComparable(final Object obj1,
                                             final Object obj2,
                                             final List<String> diffList,
                                             final String self) {
        final Comparable<Comparable<?>> comp1 = (Comparable<Comparable<?>>) obj1;
        final Comparable<Comparable<?>> comp2 = (Comparable<Comparable<?>>) obj2;

        if (comp1.compareTo(comp2) == 0) {
            return true;
        } else {
            diffList.add(self);
            return false;
        }
    }

    /**
     * 比較タイプを判定する。<br/>
     *
     * @param value1    比較対照オブジェクト１
     * @param value2    比較対照オブジェクト２
     * @return 判定した比較タイプ
     */
    private static Precomp checkType(final Object value1, final Object value2) {

        if (value1 == null && value2 == null) {
            return Precomp.BOTH_NULL;
        } else if ((value1 != null && value2 == null) || (value1 == null && value2 != null)) {
            return Precomp.EIHTER_NULL;
        } else if (value1 != null && value2 != null && !StringUtils.equals(
                        value1.getClass().getName(), value2.getClass().getName())) {
            return Precomp.DIFFERENT_CLASS;
        } else if (isEnumClass(value1)) {
            return Precomp.ENUM;
        } else if (value1.getClass().getName().startsWith("jp.co.itechh.quad.admin")) {
            return Precomp.DTO;
        } else if (value1 instanceof Comparable && value2 instanceof Comparable) {
            return Precomp.COMPARABLE;
        } else if (value1 instanceof List) {
            return Precomp.LIST;
        } else if (value1 instanceof Map) {
            return Precomp.MAP;
        } else if (value1 instanceof Set) {
            return Precomp.SET;
        } else if (value1 instanceof Object[]) {
            return Precomp.OBJECT_ARRAY;
        } else if (value1.getClass().getName().contains("[")) {
            return Precomp.PRIMITIVE_ARRAY;
        } else if (value1.getClass().getName().startsWith("jp.co.itechh.quad")) {
            return Precomp.DTO;
        }

        return Precomp.OTHER;
    }

    /**
     * Enumクラス判定。<br/>
     *
     * @param obj   オブジェクト
     * @return オブジェクトがEnumクラスまたはAbstractEnumTypeクラスの子クラスである場合 true
     */
    private static boolean isEnumClass(final Object obj) {
        if (obj != null && (obj.getClass().isEnum() || EnumType.class.isAssignableFrom(obj.getClass()))) {
            return true;
        }
        return false;
    }

    /**
     * 循環参照回避用登録。<br/>
     *
     * @param obj1      左オブジェクト
     * @param obj2      右オブジェクト
     * @param leftHold  使用中左オブジェクトセット
     * @param rightHold 使用中右オブジェクトセット
     * @param diffList  不一致項目の一覧
     * @param self      項目の位置情報
     */
    private static void holdObjects(final Object obj1,
                                    final Object obj2,
                                    final Set<Object> leftHold,
                                    final Set<Object> rightHold,
                                    final List<String> diffList,
                                    final String self) {

        final String id1 = obj1.getClass().getSimpleName() + ":" + System.identityHashCode(obj1);
        final String id2 = obj2.getClass().getSimpleName() + ":" + System.identityHashCode(obj2);

        if (leftHold.contains(id1) || rightHold.contains(id2)) {
            throw new CyclicReferenceException();
        }

        leftHold.add(obj1.getClass().getSimpleName() + ":" + System.identityHashCode(obj1));
        rightHold.add(obj2.getClass().getSimpleName() + ":" + System.identityHashCode(obj2));

    }

    /**
     * 循環参照回避用解除。<br/>
     *
     * @param obj1      左オブジェクト
     * @param obj2      右オブジェクト
     * @param leftHold  使用中左オブジェクトセット
     * @param rightHold 使用中右オブジェクトセット
     */
    private static void releaseObjects(final Object obj1,
                                       final Object obj2,
                                       final Set<Object> leftHold,
                                       final Set<Object> rightHold) {
        final String id1 = obj1.getClass().getSimpleName() + ":" + System.identityHashCode(obj1);
        final String id2 = obj2.getClass().getSimpleName() + ":" + System.identityHashCode(obj2);

        leftHold.remove(id1);
        rightHold.remove(id2);

    }

    /**
     * DTO のプロパティを取得する。<br/>
     *
     * @param obj   オブジェクト
     * @return プロパティマップ
     */
    public static Map<String, Object> getPropertyMap(final Object obj) {

        final Map<String, Object> propertyMap = new LinkedHashMap<>();

        final Set<String> recursiveFields = new LinkedHashSet<>();

        Class<?> clazz = obj.getClass();

        //
        // スーパークラスをさかのぼってフィールドを取得する
        //

        while (true) {
            if (clazz == Object.class) {
                break;
            }
            final java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            for (final java.lang.reflect.Field field : fields) {

                final String name = field.getName();

                // シリアルフィールドは取得しない
                if ("serialVersionUID".equals(name)) {
                    continue;
                }

                // Enum 用特殊フィールドは取得しない
                if (name.startsWith("$SWITCH_TABLE") || "ENUM$VALUES".equals(name)) {
                    continue;
                }

                // DiffIgnoreアノテーションのついたフィールドはチェック対象外とする
                DiffIgnore diffIgnore = field.getAnnotation(DiffIgnore.class);
                if (diffIgnore != null) {
                    continue;
                }

                // パブリックフィールドは直接値を取得する
                if (java.lang.reflect.Modifier.isPublic(field.getModifiers())) {
                    try {
                        propertyMap.put(name, field.get(obj));
                        continue;
                    } catch (Exception e) {
                        // フィールド値取得失敗時は後続処理で対応
                    }
                }

                recursiveFields.add(name);

            }

            clazz = clazz.getSuperclass();
        }

        //
        // get/is アクセッサ経由でプロパティ値を取得するフィールド
        //

        for (final String fieldName : recursiveFields) {

            // final String fieldName = f.getName();
            final char head = fieldName.charAt(0);
            final String capitalizedFieldName = fieldName.replaceFirst(Character.toString(head),
                                                                       Character.toString(Character.toUpperCase(head))
                                                                      );

            try {

                final String getterName = "get" + capitalizedFieldName;
                final java.lang.reflect.Method m = obj.getClass().getMethod(getterName);
                final Object returnValue = m.invoke(obj);
                propertyMap.put(fieldName, returnValue);

            } catch (final Exception e) {

                try {
                    final String getterName = "is" + capitalizedFieldName;
                    final java.lang.reflect.Method m = obj.getClass().getMethod(getterName);
                    final Object returnValue = m.invoke(obj);
                    propertyMap.put(fieldName, returnValue);
                } catch (final Exception e2) {
                    // getメソッドもisメソッドも存在しない。
                    // このフィールドは内部処理用のものとみなし値の取得は行わない
                }

            }

        }

        return propertyMap;
    }

    /**
     * 比較結果をソートする
     *
     * @param resultList    比較結果
     */
    public static void sortResult(final List<String> resultList) {
        Collections.sort(resultList, new Comparator<String>() {

            @Override
            public int compare(final String o1, final String o2) {

                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 != null && o2 == null) {
                    return -1;
                }
                if (o1 == null && o2 != null) {
                    return 1;
                }

                final Integer dot1 = countDot(o1);
                final Integer dot2 = countDot(o2);

                return dot1.compareTo(dot2);
            }
        });
    }

    /**
     * 文字列に含まれるドットの数をカウントする。<br/>
     *
     * @param value 文字列
     * @return ドットの数
     */
    private static Integer countDot(final String value) {
        int pos = 0;
        int count = 0;
        while (value.indexOf('.', pos++) != -1) {
            count++;
        }
        return Integer.valueOf(count);
    }

    /**
     * ディレクトリ構造の比較を行う。<br/>
     * ファイル数とファイル名の比較は行うが、ファイル内容の同一性は確認しない。
     *
     * @param org       比較元
     * @param mod       比較先
     * @param diffList  相違点リスト
     * @return 再帰処理時用戻り値
     */
    private static boolean compareDirectoryTree(final File org, final File mod, final List<String> diffList) {

        // 比較元が存在しない場合
        if (!org.exists()) {
            diffList.add(org.getAbsolutePath() + " <NOT EXISTED> ");
            return false;
        }

        // 比較先が存在しない場合
        if (mod == null) {

            diffList.add(org.getAbsolutePath() + " <NO PAIR> ");

        } else {

            // タイプが違うまたは名称が違う場合
            if (org.isDirectory() && !mod.isDirectory()) {
                diffList.add(org.getAbsolutePath() + " <ANOTHER IS FILE> ");
                return false;
            }

            // タイプが違うまたは名称が違う場合
            if (org.isFile() && !mod.isFile()) {
                diffList.add(org.getAbsolutePath() + " <ANOTHER IS DIRECTORY> ");
                return false;
            }

        }

        // 同一ファイル名出ない場合
        if (mod != null && !org.getName().equals(mod.getName())) {
            diffList.add(org.getAbsolutePath() + " <NAME MISMATCH> " + mod.getAbsolutePath());
        }

        // ディレクトリの場合は更に掘る
        if (org.isDirectory()) {

            boolean same = true;

            final Map<String, File> map1 = getFileMap(org);
            final Map<String, File> map2 = getFileMap(mod);

            for (final String name : map1.keySet()) {

                final File file1 = map1.get(name);
                final File file2 = map2.get(name);

                if (!compareDirectoryTree(file1, file2, diffList)) {
                    same = true;
                }
            }

            if (map2.size() != 0 && map1.size() != map2.size()) {
                diffList.add(org.getAbsolutePath() + " <TOTAL COUNT MISMATCH> " + map1.size() + ":" + map2.size());
                same = false;
            }

            return same;
        }

        // 一致する場合
        return true;
    }

    /**
     * ディレクトリ下のファイル一覧マップを作成する。<br/>
     *
     * @param dir   一覧が欲しいディレクトリ
     * @return 一覧マップ
     */
    private static Map<String, File> getFileMap(final File dir) {
        final Map<String, File> map = new HashMap<>();

        if (dir == null) {
            return map;
        }

        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (final File f : fileList) {
                map.put(f.getName(), f);
            }
        }

        return map;
    }

    /**
     * 文字連結<br/>
     * メッセージ or 対象フィールド文字列。<br />
     *
     * @param str1 文字列１
     * @param str2 文字列２
     * @return 連結文字列
     */
    public static String joinStr(String str1, String str2) {
        return str1 + SEPARATOR + str2;
    }

    /**
     * 差分チェック<br/>
     *
     * @param diffList 差分リスト
     * @param objName 差分オブジェクト名
     * @return true = 差分あり、false = 差分なし
     */
    public static boolean isDiff(List<String> diffList, String objName) {
        if (diffList == null) {
            return false;
        }
        return diffList.contains(objName);
    }

    /**
     * 差分チェック<br/>
     *
     * @param diffList 差分リスト
     * @param objNameList 差分オブジェクト名のリスト
     * @return true = 差分あり、false = 差分なし
     */
    public static boolean isDiff(List<String> diffList, List<String> objNameList) {
        for (String objName : objNameList) {
            if (isDiff(diffList, objName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 循環参照検出時にスローする例外<br/>
     * @author tomo (itec) 2012/02/01 チケット #2811 対応
     */
    private static class CyclicReferenceException extends RuntimeException {
        /** シリアル */
        private static final long serialVersionUID = 8361736055848124555L;
    }

    /**
     * 比較処理中断時にスローする例外<br/>
     * @author tomo (itec) 2012/02/01 チケット #2811 対応
     */
    private static class AbortComparingException extends RuntimeException {
        /** シリアル */
        private static final long serialVersionUID = 1376622359246317430L;
    }

    /**
     * 比較に使用する列挙子<br/>
     */
    private static enum Precomp {
        /** どちらも NULL 値 */
        BOTH_NULL,
        /** どちらかが NULL 値 */
        EIHTER_NULL,
        /** 型が違う */
        DIFFERENT_CLASS,
        /** compare で比較 */
        COMPARABLE,
        /** list 同士の比較 */
        LIST,
        /** map 同士の比較 */
        MAP,
        /** set 同士の比較 */
        SET,
        /** dto 同士の比較 */
        DTO,
        /** オブジェクト配列同士の比較 */
        OBJECT_ARRAY,
        /** プリミティブ型配列同士の比較 */
        PRIMITIVE_ARRAY,
        /** ENUM 同士の比較 */
        ENUM,
        /** equals で比較 */
        OTHER
    }

    /**
     * 隠蔽されたコンストラクタ<br/>
     */
    private DiffUtil() {
    }
}