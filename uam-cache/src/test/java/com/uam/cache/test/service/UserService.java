package com.uam.cache.test.service;

import com.uam.cache.test.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by XT on 2018/2/21.
 */
public interface UserService {
    public User get(String account);

    public List<User> getLlist();

    public Set<User> getSet();

    public Map<String, User> getMap();

    public void save(User user);

    public User get(int age);
}
