/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * ネットワークに関連するヘルパークラス
 *
 * @author tm27400
 * @author Kaneko (itec) 2012/08/16 UtilからHelperへ変更
 *
 */
@Component
public class NetworkUtility {

    /** コンストラクタの説明・概要 */
    public NetworkUtility() {
    }

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtility.class);

    /**
     * ホスト名を取得する
     *
     * @return ホスト名
     */
    public String getLocalHostName() {
        String hostName = "localhost";

        try {
            hostName = InetAddress.getLocalHost().getHostName();

            // localhost を受け取った場合は別の nic の名前を取る
            if (hostName.toLowerCase().equals("localhost")) {
                hostName = getAnotherLocalHostName();
            }

        } catch (Exception e) {
            // ignore
            LOGGER.warn("ローカルホスト名を解決できません。");
        }

        return hostName;
    }

    /**
     * localhost でない別のホスト名を取得する
     *
     * @return ホスト名
     */
    protected String getAnotherLocalHostName() {
        String hostName = "localhost";

        try {

            for (Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces(); enu.hasMoreElements(); ) {
                NetworkInterface nic = enu.nextElement();

                LOOPEXIT:

                for (Enumeration<InetAddress> enume = nic.getInetAddresses(); enume.hasMoreElements(); ) {
                    InetAddress address = enume.nextElement();
                    String name = address.getHostName();
                    // ローカルホストと ip は無視する localhost, localhost.localdomain
                    if (name.toLowerCase().equals("localhost") || name.toLowerCase().equals("localhost.localdomain")
                        || name.toLowerCase().matches("^[0-9a-f]{1,4}\\:.*$") || name.matches(
                                    "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        continue LOOPEXIT;
                    }
                    hostName = name;
                    break;
                }
            }

        } catch (Exception e) {
            // ignore
            LOGGER.warn("ローカルホスト名を解決できません。");
        }

        return hostName;
    }
}