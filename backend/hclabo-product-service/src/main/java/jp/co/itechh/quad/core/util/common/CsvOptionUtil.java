package jp.co.itechh.quad.core.util.common;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CsvOptionUtil {

    private static final String SERIAL_VERSION = "serialVersion";

    /**
     *
     * @param clazz クラス
     * @return
     */
    public static List<OptionContentDto> getListOptionContentDto(Class<?> clazz) {

        List<OptionContentDto> result = new LinkedList<>();

        List<Field> fields = getFieldList(clazz);
        int index = 1;
        for (Field field : fields) {
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }

            if (field.getAnnotation(CsvColumn.class) != null) {
                OptionContentDto optionContentDto = new OptionContentDto();
                optionContentDto.setDefaultOrder(index);
                index++;
                optionContentDto.setOrder(field.getAnnotation(CsvColumn.class).order());
                optionContentDto.setDefaultColumnLabel(field.getAnnotation(CsvColumn.class).columnLabel());
                optionContentDto.setColumnLabel(StringUtils.EMPTY);
                optionContentDto.setOutFlag(true);
                optionContentDto.setItemName(field.getName());
                result.add(optionContentDto);
            }

        }

        return result;
    }

    /**
     * CSVダウンロードオプションに変換
     *
     * @param itemNameArray
     * @param orderArray
     * @param columnLabelArray
     * @param outFlagArray
     * @return
     */
    public static Map<String, OptionContentDto> getMapOptionContentDto(String itemNameArray,
                                                                       String orderArray,
                                                                       String columnLabelArray,
                                                                       String outFlagArray) {
        List<String> itemNameList =
                        Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(itemNameArray, ","));
        List<String> orderList = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(orderArray, ","));
        List<String> columnLabelList =
                        Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(columnLabelArray, ","));
        List<String> outFlagList = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(outFlagArray, ","));

        Map<String, OptionContentDto> csvDownloadOption = new LinkedHashMap<>();
        for (int i = 0; i < itemNameList.size(); i++) {
            OptionContentDto option = new OptionContentDto();
            option.setItemName(itemNameList.get(i));
            option.setOrder(Integer.parseInt(orderList.get(i)));
            option.setOutFlag(Boolean.parseBoolean(outFlagList.get(i)));
            option.setColumnLabel(columnLabelList.get(i));
            csvDownloadOption.put(itemNameList.get(i), option);
        }

        return csvDownloadOption;
    }

    /**
     *
     * @param clazz クラス
     * @return
     */
    public static Map<String, OptionContentDto> getDefaultMapOptionContentDto(Class<?> clazz) {

        Map<String, OptionContentDto> csvDownloadOption = new LinkedHashMap<>();

        List<Field> fields = getFieldList(clazz);

        for (Field field : fields) {
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }

            if (field.getAnnotation(CsvColumn.class) != null) {
                OptionContentDto optionContentDto = new OptionContentDto();
                optionContentDto.setOrder(field.getAnnotation(CsvColumn.class).order());
                optionContentDto.setColumnLabel(field.getAnnotation(CsvColumn.class).columnLabel());
                optionContentDto.setOutFlag(true);
                optionContentDto.setItemName(field.getName());
                csvDownloadOption.put(field.getName(), optionContentDto);
            }
        }

        return csvDownloadOption;
    }

    /**
     * 出力対象のCSVDtoクラスの全フィールドをList形式で取得する
     *
     * @param clazz
     * @return
     */
    private static List<Field> getFieldList(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }
}