package com.jc.idGen.gen;

/**
 * Created by starmezhh on 16/7/18.
 */
public interface IdGenerator {

    Long genLongId() throws Exception;

    void init();

}
