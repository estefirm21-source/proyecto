package com.greencert.db.dao;

import com.greencert.db.model.EmissionRecord;
import java.util.List;

public interface EmissionRecordDAO {
    void save(EmissionRecord record);
    List<EmissionRecord> findAll();
    void delete(int id);
}
