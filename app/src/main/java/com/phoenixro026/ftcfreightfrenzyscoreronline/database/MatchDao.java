package com.phoenixro026.ftcfreightfrenzyscoreronline.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MatchDao {
    @Query("SELECT * FROM `match` ORDER BY id DESC")
    LiveData<List<Match>> getAllDesc();

    @Query("SELECT * FROM `match`")
    LiveData<List<Match>> getAll();

    @Query("SELECT * FROM `match` WHERE id IN (:userIds)")
    List<Match> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM `match` WHERE teamName LIKE :name AND " +
            "teamCode LIKE :code LIMIT 1")
    Match findByName(String name, String code);

    @Query("SELECT * FROM `match` WHERE id LIKE :position")
    Match getByPosition(int position);

    @Query("SELECT COUNT(id) FROM `match`")
    int getCount();

    @Insert
    void insertAll(Match... matches);

    @Insert
    void insert(Match match);

    @Update
    void update(Match match);

    @Query("DELETE FROM `match` WHERE id = :userId")
    void deleteByUserId(int userId);
}
