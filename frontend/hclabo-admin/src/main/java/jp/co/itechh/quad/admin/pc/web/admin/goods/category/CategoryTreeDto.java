package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeDto {
    private String categoryName;
    private String categoryId;
    private String displayName;
    private String error;
    private int serialNumber;
    private List<CategoryTreeDto> categoryTreeDtoList;
}
