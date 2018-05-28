package com.nilo.dms.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface CommonDao {

    Long lastFoundRows();
}
