package com.bh.drillingcommons.managers;

import java.util.List;

public interface ICrudManager<T> {
    /**
     * This workflow will return all the <T> we currently have.
     *
     * @return
     */
    List<T> getAll();

    /**
     * This workflow will return a specific <T> identified by the given id
     * 
     * @param id
     * @return
     */
    T get(String id);
    
    /**
     * This workflow will create a new <T> if it does not already exist or
     * it will update the existing <T>.
     * 
     * @param sample
     * @return
     * @throws ServiceException 
     */
    T upsert(T sample);

    /**
     * This workflow will delete the given <T>. Returning True if the <T> was successfully deleted.
     *
     * @param id
     */
    boolean delete(String id);

    /**
     * This method will be used for determining if the upsert is an insert or an update
     * 
     * @param user
     * @return
     */
    
}
