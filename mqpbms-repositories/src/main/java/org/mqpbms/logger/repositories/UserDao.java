/*
 * Multiple-Criteria Query Probability Based
 * Database Insider Attack Monitoring System.
 *
 * Project Name: mqpbms-parent
 * User: sky
 * Date: 2/16/14
 */

package org.mqpbms.logger.repositories;

import org.mqpbms.logger.models.User;

import java.util.Collection;

/**
 * User Table data access object.
 *
 * @author sky
 * @version 1/9/14
 */
public interface UserDao {

    /**
     * This method is to find a user from the user table.
     * If the user does not exist, it returns null.
     *
     * @param userName user name who executed the current query.
     * @return the user object if the user exist in the user table, if not, null.
     */
    User getUser(String userName);

    /**
     * Add a new user to the database.
     *
     * @param user a User.
     */
    void addUser(User user);

    /**
     * Get all users.
     *
     * @return collection of all users.
     */
    Collection<User> getAllUsers();
}
