package jp.co.itechh.quad.ddd.infrastructure.addressbook.dao;

import jp.co.itechh.quad.ddd.infrastructure.addressbook.dbentity.AddressBookDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * 住所録Dao
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface AddressBookDao {

    /**
     * インサート
     *
     * @param addressBookDbEntity 住所録DBエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true, sqlFile = true)
    int insert(AddressBookDbEntity addressBookDbEntity);

    /**
     * 住所IDで住所録取得
     *
     * @param addressId 住所ID
     * @return 住所録DBエンティティ
     */
    @Select
    AddressBookDbEntity getByAddressId(String addressId);

    /**
     * 更新
     *
     * @param addressBookDbEntity 住所録DBエンティティ
     * @return 更新件数
     */
    @Update
    int update(AddressBookDbEntity addressBookDbEntity);

    /**
     * 住所IDで削除
     *
     * @param addressId 住所ID
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteByAddressId(String addressId);

    /**
     * 顧客IDで公開中住所録リスト取得
     *
     * @param customerId 顧客ID
     * @return 住所録DBエンティティリスト
     */
    @Select
    List<AddressBookDbEntity> getOpenAddressListByCustomerId(String customerId,
                                                             String exclusionAddressId,
                                                             SelectOptions selectOptions);

    /**
     * 顧客IDでデフォルト指定住所録取得
     *
     * @param customerId 顧客ID
     * @return 住所録DBエンティティ
     */
    @Select
    AddressBookDbEntity getDefaultByCustomerId(String customerId);

    /**
     * 住所IDでデフォルト指定フラグON
     *
     * @param addressId 住所ID
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateDefaultByAddressId(String addressId);

    /**
     * 顧客IDで住所録一括非公開
     *
     * @param customerId 顧客ID
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateAllClose(String customerId);

    /**
     * 顧客IDで住所録一括デフォルト指定フラグOFF
     *
     * @param customerId 顧客ID
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateAllNotDefault(String customerId);

}
