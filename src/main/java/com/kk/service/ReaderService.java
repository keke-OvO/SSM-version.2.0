package com.kk.service;

import com.kk.pojo.Reader;

import java.util.List;
import java.util.Map;

public interface ReaderService {
    Reader select(Reader rd);

    void add(Reader rd);

    Reader check(Map map);

    void altpwd(Reader rd);

    void del(int id);

    List allReader();
}
