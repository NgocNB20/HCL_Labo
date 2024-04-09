package jp.co.itechh.quad.core.dao.authentication;

import jp.co.itechh.quad.core.entity.authentication.PersistentRememberMeTokenEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * PersistentRememberMeTokenDao
 *
 * @author Doan Thang (VJP)
 */
@Dao
@ConfigAutowireable
public interface PersistentRememberMeTokenDao {
    @Insert
    int insert(PersistentRememberMeTokenEntity persistentRememberMeTokenEntity);

    @Update
    int update(PersistentRememberMeTokenEntity persistentRememberMeTokenEntity);

    @Select
    PersistentRememberMeTokenEntity select(String series);

    @Delete
    int delete(PersistentRememberMeTokenEntity persistentRememberMeTokenEntity);

    @Select
    List<PersistentRememberMeTokenEntity> getUsers(String username);

}
