/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 権限チェック用Utility
 *
 * @author kimura
 */
@Component
public class CheckPermissionUtility {

    /**
     * 遷移先URLを取得<br/>
     * ログインユーザーに付与されている権限グループを元にログイン初期画面を判定
     *
     * @return 遷移先URL
     */
    public String getTargetUrl() {

        // 権限グループなし
        List<GrantedAuthority> roles = (List<GrantedAuthority>) SecurityContextHolder.getContext()
                                                                                     .getAuthentication()
                                                                                     .getAuthorities();
        if (CollectionUtils.isEmpty(roles)) {
            return "/permissiondenied/";
        }

        // 権限グループ：受注管理
        Optional<GrantedAuthority> isRoleOrder = roles.stream()
                                                      .filter(i -> Arrays.asList("ORDER:8", "ORDER:4")
                                                                         .contains(i.getAuthority()))
                                                      .findFirst();
        if (isRoleOrder.isPresent()) {
            return "/order/";
        }

        // 権限グループ：会員管理
        Optional<GrantedAuthority> isRoleMember = roles.stream()
                                                       .filter(i -> Arrays.asList("MEMBER:8", "MEMBER:4")
                                                                          .contains(i.getAuthority()))
                                                       .findFirst();
        if (isRoleMember.isPresent()) {
            return "/memberinfo/";
        }

        // 権限グループ：商品管理
        Optional<GrantedAuthority> isRoleGoods = roles.stream()
                                                      .filter(i -> Arrays.asList("GOODS:8", "GOODS:4")
                                                                         .contains(i.getAuthority()))
                                                      .findFirst();
        if (isRoleGoods.isPresent()) {
            return "/goods/";
        }

        // 権限グループ：店舗管理
        Optional<GrantedAuthority> isRoleShop = roles.stream()
                                                     .filter(i -> Arrays.asList("SHOP:8", "SHOP:4")
                                                                        .contains(i.getAuthority()))
                                                     .findFirst();
        if (isRoleShop.isPresent()) {
            return "/news/";
        }

        // 権限グループ：レポート
        Optional<GrantedAuthority> isRoleReport = roles.stream()
                                                       .filter(i -> Arrays.asList("REPORT:8", "REPORT:4")
                                                                          .contains(i.getAuthority()))
                                                       .findFirst();
        if (isRoleReport.isPresent()) {
            return "/ordersales/";
        }

        // 権限グループ：システム設定
        Optional<GrantedAuthority> isRoleSetting = roles.stream()
                                                        .filter(i -> Arrays.asList("SETTING:8", "SETTING:4")
                                                                           .contains(i.getAuthority()))
                                                        .findFirst();
        if (isRoleSetting.isPresent()) {
            return "/shopinfo/";
        }

        // 権限グループ：運営者管理
        Optional<GrantedAuthority> isRoleAdmin = roles.stream()
                                                      .filter(i -> Arrays.asList("ADMIN:8", "ADMIN:4")
                                                                         .contains(i.getAuthority()))
                                                      .findFirst();
        if (isRoleAdmin.isPresent()) {
            return "/administrator/";
        }

        // 権限グループ：バッチ管理
        Optional<GrantedAuthority> isRoleBatch = roles.stream()
                                                      .filter(i -> Arrays.asList("BATCH:8", "BATCH:4")
                                                                         .contains(i.getAuthority()))
                                                      .findFirst();
        if (isRoleBatch.isPresent()) {
            return "/batchmanagement/";
        }

        // 権限グループがあるが、全て「権限なし」となった場合
        return "/permissiondenied/";
    }

}