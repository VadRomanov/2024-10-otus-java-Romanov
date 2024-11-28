package ru.otus.service;

import com.google.common.collect.Lists;

import java.util.List;

public class TestService<T> {

    public List<T> reverser(List<T> sourceList) {
        return Lists.reverse(sourceList);
    }

}
