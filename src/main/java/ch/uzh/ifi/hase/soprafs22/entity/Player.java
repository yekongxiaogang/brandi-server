package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Color;

// TODO: Remove all occurences of player, remove Player class
public class Player {
    private String username;
    private Integer id;
    private String uuid;
    private String password;
    private Color color;

    public Player(String username, Integer id, String uuid, String password, Color color) {
        this.username = username;
        this.id = id;
        this.uuid = uuid;
        this.password = password;
        this.color = color;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getId() {
        return this.id;
    }

}