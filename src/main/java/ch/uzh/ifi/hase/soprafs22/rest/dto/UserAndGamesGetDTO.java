package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;

/*
 * Used to get user data + data of all the games he's in 
 * Needed because otherwise, when getting user, we also get the users games, which all include PlayerStates which include Users which include Games and so on
 */

public class UserAndGamesGetDTO {

  private Long id;
  private String username;
  private UserStatus status;
  private ArrayList<Game> games;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public ArrayList<Game> getGames() {
    return this.games;
  }

  public void setGames(ArrayList<Game> games) {
    this.games = games;
  }

}
