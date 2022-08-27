package ar.edu.itba.paw.model;

import java.util.List;

public class UserList {
    private final List<User> userList;
    private final int pages;

    public UserList(List<User> userList, int pages) {
        this.userList = userList;
        this.pages = pages;
    }

    public List<User> getUserList() {
        return userList;
    }

    public int getPages() {
        return pages;
    }
}
