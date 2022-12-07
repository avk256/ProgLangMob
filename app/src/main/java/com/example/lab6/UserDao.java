package com.example.lab6;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY time DESC")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email LIKE :email ORDER BY time DESC")
    List<User> getAllByEmail(String email);

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    User findByName(String email);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
    @Query("DELETE FROM user")
    void deleteAll();
}
