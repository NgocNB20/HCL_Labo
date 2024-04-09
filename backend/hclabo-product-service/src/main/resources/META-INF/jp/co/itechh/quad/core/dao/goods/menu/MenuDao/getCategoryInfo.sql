WITH RECURSIVE get_category_info(id, dto, dtype) AS (
    SELECT
                category_tree_dto-> 'categoryId' AS id,
                jsonb_array_elements(
                        case jsonb_typeof(category_tree_dto->'categoryTreeDtoList')
                            when 'array' then category_tree_dto->'categoryTreeDtoList'
                            else '[]' end) AS dto,
                jsonb_typeof(category_tree_dto->'categoryTreeDtoList') AS dtype
    FROM (SELECT menu.categorytree AS category_tree_dto from menu where menu.menuid = /*menuId*/1001) category_tree_dto
    UNION ALL
    SELECT
                dto->'categoryId' AS id,
                jsonb_array_elements(
                    case jsonb_typeof(dto->'categoryTreeDtoList')
                         when 'array' then dto->'categoryTreeDtoList'
                         else '[]' end) AS dto,
                jsonb_typeof(dto->'categoryTreeDtoList') AS dtype
    FROM get_category_info
    WHERE dtype = 'array'
)
SELECT
    distinct category.*
FROM get_category_info
    JOIN category
        ON get_category_info.dto->>'categoryId' = category.categoryid
        WHERE 1=1
        /*%if conditionStatus != null*/
            AND (category.categoryopenstarttimepc IS NULL OR category.categoryopenstarttimepc <= /*targetTime*/0)
            AND (category.categoryopenendtimepc IS NULL OR category.categoryopenendtimepc >= /*targetTime*/0)
            AND category.categoryopenstatuspc = '1'
        /*%end*/
