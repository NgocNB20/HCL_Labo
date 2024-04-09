SELECT
    mailtemplate.*
FROM
    MailTemplate
WHERE
    shopSeq = /*shopSeq*/0
ORDER BY
    mailTemplateType desc,
    mailTemplateDefaultFlag asc,
    registTime desc
