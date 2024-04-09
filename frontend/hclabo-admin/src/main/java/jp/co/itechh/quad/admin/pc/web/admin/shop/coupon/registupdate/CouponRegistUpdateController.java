/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.coupon.registupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.admin.pc.web.admin.util.IdenticalDataCheckUtil;
import jp.co.itechh.quad.admin.utility.CouponUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponCheckRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponCodeResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponRegistRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponUpdateRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * クーポン登録更新コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/coupon")
@Controller
@SessionAttributes(value = "couponRegistUpdateModel")
@PreAuthorize("hasAnyAuthority('SHOP:8')")
public class CouponRegistUpdateController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponRegistUpdateController.class);

    // ACP0002xx
    /**
     * プロパティファイルで指定した文字以外をクーポンコードに入力した場合エラー
     */
    static final String MSGCD_CANNOT_USED_CHARACTER = "ACP000203";

    /**
     * 検索画面からの遷移時に情報が取得できなかった場合エラー
     */
    static final String MSGCD_DONOT_GET_COUPONDATA = "ACP000201";

    // ACP0003xx
    /**
     * 登録完了メッセージ
     */
    public static final String MSGCD_REGIST_COMPLETE = "ACP000301";

    public static final String FLAG_CHANGEENDTIME = "changeEndTime";

    public static final String FLAG_UPDATEFLAG = "updateFlag";

    public static final String FLAG_OPEN = "open";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 確認画面から
     */
    public static final String FLASH_FROM_CONFIRM = "fromConfirm";

    /**
     * helper
     */
    private final CouponRegistUpdateHelper couponRegistUpdateHelper;

    /**
     * クーポンAPI
     */
    private final CouponApi couponApi;

    /**
     * 日付関連ユーティリティクラス
     */
    private final DateUtility dateUtility;

    /**
     * クーポン関連ユーティリティクラス
     */
    private final CouponUtility couponUtility;

    /**
     * コンストラクタ
     *
     * @param couponRegistUpdateHelper
     * @param couponApi
     * @param dateUtility
     * @param couponUtility
     */
    @Autowired
    public CouponRegistUpdateController(CouponRegistUpdateHelper couponRegistUpdateHelper,
                                        CouponApi couponApi,
                                        DateUtility dateUtility,
                                        CouponUtility couponUtility) {
        this.couponRegistUpdateHelper = couponRegistUpdateHelper;
        this.couponApi = couponApi;
        this.dateUtility = dateUtility;
        this.couponUtility = couponUtility;
    }

    /**
     * 入力画面：画面初期表示。<br />
     *
     * <pre>
     * 更新時はクーポン情報を各項目にセット。
     * 新規登録時はクーポンコードの自動設定を行う。
     * </pre>
     *
     * @param couponRegistUpdateModel
     * @param model
     * @return 自画面
     */
    @GetMapping(value = "/registupdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "coupon/registupdate/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<Integer> seq,
                              @RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<String> from,
                              CouponRegistUpdateModel couponRegistUpdateModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (from.isPresent() && from.get().equals("confirm")) {
            return "coupon/registupdate/index";
        }
        if (!model.containsAttribute(FLASH_FROM_CONFIRM)) {
            clearModel(CouponRegistUpdateModel.class, couponRegistUpdateModel, model);
        }

        if (md.isPresent()) {
            // 新規登録初期表示時に以下の処理を行う
            try {
                couponRegistUpdateHelper.toPageForNewRegistPage(couponRegistUpdateModel);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("例外処理が発生しました", e);
                addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/coupon/";
            }

            // クーポンコードを自動生成
            String couponCode = makeCouponCode(error);
            if (error.hasErrors()) {
                return "coupon/registupdate/index";
            }

            // ページに生成したクーポンコードをセットする
            couponRegistUpdateHelper.toNewRegistPageForLoad(couponRegistUpdateModel, couponCode);

            // 初期表示時は「確認」ボタンを表示する
            couponRegistUpdateModel.setUpdateFlag(true);
        } else if (seq.isPresent()) {
            try {
                // 更新時に以下の処理を行う
                // クーポン情報を取得する クーポンSEQからクーポン情報を取得する
                couponRegistUpdateModel.setCouponSeq(seq.get());

                CouponResponse couponResponse = couponApi.getByCouponSeq(couponRegistUpdateModel.getCouponSeq());

                // 他のユーザによる削除対応
                // 指定されたクーポンSEQに対応するクーポンが存在しない場合、検索画面に遷移しエラーメッセージを表示する
                if (couponResponse == null) {
                    addMessage(MSGCD_DONOT_GET_COUPONDATA, redirectAttributes, model);
                    return "redirect:/coupon/";
                }

                // ページに取得したクーポン情報をセットする
                couponRegistUpdateHelper.toPageForLoad(couponResponse, couponRegistUpdateModel);

                // 変更可能のとき「確認」ボタンを表示
                if (canUpdate(couponRegistUpdateModel)) {
                    couponRegistUpdateModel.setUpdateFlag(true);
                } else {
                    couponRegistUpdateModel.setUpdateFlag(false);
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/coupon/";
            }

        } else {
            // 確認画面から戻った場合の処理
            // サブアプリスコープに格納している情報を表示するだけなので何もしない
        }

        return "coupon/registupdate/index";
    }

    /**
     * 確認ボタン押下時に確認画面へ遷移。<br />
     *
     * <pre>
     * 登録可能チェック後確認画面に遷移する。
     * </pre>
     *
     * @return クーポン登録確認画面
     */
    @PostMapping(value = "/registupdate", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "coupon/registupdate/index")
    public String doConfirm(@Validated CouponRegistUpdateModel couponRegistUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 実行前処理
        String check = preDoAction(couponRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "coupon/registupdate/index";
        }

        try {
            // クーポンコードの入力チェック
            if (!checkCouponcCode(couponRegistUpdateModel)) {
                throwMessage(MSGCD_CANNOT_USED_CHARACTER);
            }

            CouponEntity coupon = ApplicationContextUtility.getBean(CouponEntity.class);

            // 更新時は修正前の情報を修正前の情報を修正後にコピーする
            // 修正された内容のみ更新し差分を出すため
            if (couponRegistUpdateModel.getCouponSeq() != null) {
                coupon = CopyUtil.deepCopy(couponRegistUpdateModel.getPreUpdateCoupon());
            }

            // 確認画面遷移時に自動入力項目をセット
            couponRegistUpdateHelper.toPageForConfirm(coupon, couponRegistUpdateModel);

            String administratorId = this.getCommonInfo().getCommonInfoAdministrator().getAdministratorId();

            // クーポン登録可否チェック
            CouponCheckRequest couponCheckRequest =
                            couponRegistUpdateHelper.toCouponCheckRequest(couponRegistUpdateModel, administratorId);
            couponApi.check(couponCheckRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("couponDisplayName", "couponDisplayNamePC");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "coupon/registupdate/index";
        }

        return "redirect:/coupon/registupdate/confirm";
    }

    /**
     * キャンセルボタン押下時の処理。<br />
     *
     * <pre>
     * 検索画面へ遷移する。
     * 検索条件を元に再検索を行う。
     * </pre>
     *
     * @return 検索画面
     */
    @PostMapping(value = "/registupdate", params = "doCancel")
    public String doCancel(CouponRegistUpdateModel couponRegistUpdateModel,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        // 実行前処理
        String check = preDoAction(couponRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (couponRegistUpdateModel.getCouponSeq() != null) {
            redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
        }

        return "redirect:/coupon/";
    }

    /**
     * 確認画面：初期処理<br />
     *
     * <pre>
     * 更新時は変更前後で差異項目の背景色を変更する。
     * </pre>
     *
     * @param couponRegistUpdateModel
     * @return 自画面
     */
    @GetMapping(value = "/registupdate/confirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/coupon/registupdate/?from=confirm")
    public String doLoadConfirm(CouponRegistUpdateModel couponRegistUpdateModel, Model model) {

        // ブラウザバックの場合、処理しない
        if (couponRegistUpdateModel.getPostUpdateCoupon() == null) {
            return "redirect:/error";
        }

        // 更新時に修正前後の差分を求める
        if (couponRegistUpdateModel.isUpdate()) {
            // 変更内容に差異がある場合は背景色を変更する
            couponRegistUpdateModel.setModifiedItemNameList(DiffUtil.diff(couponRegistUpdateModel.getPreUpdateCoupon(),
                                                                          couponRegistUpdateModel.getPostUpdateCoupon()
                                                                         ));
        }

        // 開催中の管理用メモ以外の修正があった場合は変更確認ダイアログを表示する
        if (confirmUpdate(couponRegistUpdateModel)) {
            couponRegistUpdateModel.setOpen(true);
        } else {
            couponRegistUpdateModel.setOpen(false);
        }

        // 2画面を利用した登録更新時のセッション共有対策
        couponRegistUpdateModel.setDbSeq(couponRegistUpdateModel.getPostUpdateCoupon().getCouponSeq());

        return "coupon/registupdate/confirm";

    }

    /**
     * 登録ボタン押下時の処理。<br />
     *
     * <pre>
     * クーポン登録可能チェック後、登録更新処理を行う。
     * 新規登録時はクーポンテーブル・クーポンインデックステーブルに新規登録を行う。
     * 更新時はクーポンテーブルに新規登録し、クーポンインデックステーブルを更新する。
     * </pre>
     *
     * @return クーポン検索画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doOnceRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "coupon/registupdate/confirm")
    public String doOnceRegist(@Validated CouponRegistUpdateModel couponRegistUpdateModel,
                               BindingResult error,
                               RedirectAttributes redirectAttrs,
                               SessionStatus sessionStatus,
                               Model model) {

        // 実行前処理
        String check = preDoAction(couponRegistUpdateModel, redirectAttrs, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "coupon/registupdate/index";
        }

        try {
            String administratorId = this.getCommonInfo().getCommonInfoAdministrator().getAdministratorId();

            // 新規登録処理
            if (!couponRegistUpdateModel.isUpdate()) {

                // 新規登録時クーポン情報をクーポン・クーポンインデックスに新規反映する
                CouponRegistRequest couponRegistRequest = couponRegistUpdateHelper.toCouponRegistRquest(
                                couponRegistUpdateModel.getPostUpdateCoupon(), administratorId);
                couponApi.regist(couponRegistRequest);

                // 更新処理
            } else {

                // 更新時クーポン情報をクーポン新規反映・クーポンインデックス更新する
                CouponUpdateRequest couponUpdateRequest =
                                couponRegistUpdateHelper.toCouponUpdateRequest(couponRegistUpdateModel,
                                                                               administratorId
                                                                              );
                couponApi.update(couponRegistUpdateModel.getCouponSeq(), couponUpdateRequest);

                redirectAttrs.addFlashAttribute(FLASH_MD, MODE_LIST);
            }

            // 登録完了後メッセージを表示する
            addInfoMessage(MSGCD_REGIST_COMPLETE, new String[] {"クーポン"}, redirectAttrs, model);

            // Modelをセッションより破棄
            sessionStatus.setComplete();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "coupon/registupdate/confirm";
        }

        return "redirect:/coupon/";
    }

    /**
     * 修正ボタン押下時の処理。<br />
     *
     * <pre>
     * クーポン登録更新入力画面に遷移する。
     * </pre>
     *
     * @return クーポン登録更新画面
     */
    @PostMapping(value = "/registupdate/confirm", params = "doBack")
    public String doBack(CouponRegistUpdateModel couponRegistUpdateModel,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        // 実行前処理
        String check = preDoAction(couponRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }
        redirectAttributes.addFlashAttribute(FLASH_FROM_CONFIRM, true);

        return "redirect:/coupon/registupdate";
    }

    /**
     * クーポンコードを自動生成する。<br />
     *
     * <pre>
     * プロパティファイルの設定よりランダムに生成する。
     * </pre>
     *
     * @return 自動生成したクーポンコード
     */
    private String makeCouponCode(BindingResult error) {
        CouponCodeResponse couponCodeResponse = new CouponCodeResponse();
        try {
            couponCodeResponse = couponApi.code();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return couponCodeResponse.getCouponCode();
    }

    /**
     * クーポン変更可否チェック。<br />
     *
     * <pre>
     * 変更前クーポン終了日時が現在より過去であるかをチェックする。
     * 終了したクーポンへの変更をさせないため。
     * </pre>
     *
     * @return 変更可能な場合にtrueを返す
     */
    private boolean canUpdate(CouponRegistUpdateModel couponRegistUpdateModel) {

        Timestamp couponEndTime = null;
        if (ObjectUtils.isNotEmpty(couponRegistUpdateModel.getPreUpdateCoupon())
            && couponRegistUpdateModel.getPreUpdateCoupon().getCouponEndTime() != null) {
            couponEndTime = couponRegistUpdateModel.getPreUpdateCoupon().getCouponEndTime();
        }
        Timestamp currentTime = dateUtility.getCurrentTime();

        return couponEndTime != null && couponEndTime.compareTo(currentTime) > 0;

    }

    /**
     * 入力されたクーポンコードをチェックする。<br />
     *
     * <pre>
     * クーポンコードで利用可能な文字のみを利用しているかをチェックする。
     * [注意](?)・(￥)などの文字をクーポンコードに利用できる可能性があるため正規表現でのチェックは行っていない。
     * </pre>
     *
     * @return 利用可能文字のみの場合にtrueを返す
     */
    private boolean checkCouponcCode(CouponRegistUpdateModel couponRegistUpdateModel) {

        // 利用文字をadminSystem.propertiesから取得する
        String couponCodeCharacter = couponUtility.getCouponCodeUsableCharacter();

        // 入力されたクーポンコードを取得する
        char[] inputCouponCode = couponRegistUpdateModel.getCouponCode().toCharArray();

        // 入力された文字が利用可能文字のみであることをチェックする
        for (Character codeChar : inputCouponCode) {
            if (!couponCodeCharacter.contains(codeChar.toString())) {
                return false;
            }
        }

        // 利用不可能文字が0個の場合trueを返す
        return true;

    }

    /**
     * 確認ダイアログを表示するかをチェックする。<br />
     *
     * <pre>
     * 修正前後で管理用メモ以外が変更されているかを判断する。
     * 終了したクーポンはダイアログが表示される前にエラーチェックではじかれる。
     * </pre>
     *
     * @return 管理メモ以外の画面項目に差異がある場合はtrueを返す
     */
    private boolean confirmUpdate(CouponRegistUpdateModel couponRegistUpdateModel) {

        // 新規登録時はダイアログを表示しないのでfalseを返す
        if (couponRegistUpdateModel.getCouponSeq() == null) {
            return false;
        }

        // 開催前のクーポンはダイアログを表示しないのでfalseを返す
        if (dateUtility.compareTimestamp(couponRegistUpdateModel.getPreUpdateCoupon().getCouponStartTime()) > 0) {
            return false;
        }

        // 差分のあった項目名のリストを取得する
        List<String> itemNameList = new ArrayList<>();
        itemNameList.addAll(couponRegistUpdateModel.getModifiedItemNameList());

        // 差分に管理用メモが含まれている場合はリストから削除する
        for (Iterator<String> ite = itemNameList.iterator(); ite.hasNext(); ) {
            if (ite.next().equals("Coupon.memo")) {
                ite.remove();
            }
        }

        // 変更リストサイズが"0"ならば変更はないのでfalseを返す
        if (itemNameList.size() == 0) {
            return false;
        }

        return true;

    }

    /**
     * アクション実行前処理
     *
     * @param couponRegistUpdateModel
     * @param redirectAttributes
     * @param model
     */
    public String preDoAction(CouponRegistUpdateModel couponRegistUpdateModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        if (!IdenticalDataCheckUtil.checkIdentical(
                        couponRegistUpdateModel.getScSeq(), couponRegistUpdateModel.getDbSeq())) {
            addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/coupon/";
        }
        return null;
    }
}