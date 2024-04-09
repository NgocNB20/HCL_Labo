SELECT *
FROM
    MailTemplate
WHERE
    ShopSeq = /*shopSeq*/0 AND
    MailTemplateType = /*mailTemplateType.value*/0
ORDER BY
    MailTemplateDefaultFlag DESC,
    MailTemplateSeq ASC
LIMIT 1
