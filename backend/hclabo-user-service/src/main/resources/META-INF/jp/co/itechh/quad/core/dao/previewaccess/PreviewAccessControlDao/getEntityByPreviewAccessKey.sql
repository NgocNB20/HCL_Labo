SELECT
    previewaccesscontrol.*
FROM
    previewaccesscontrol
WHERE
    previewaccesscontrol.previewaccesskey = /*previewAccessKey*/'0'
AND
    previewaccesscontrol.effectivetime >= /*currentTime*/0
