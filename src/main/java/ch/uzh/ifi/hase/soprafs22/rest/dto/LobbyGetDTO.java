package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

public class LobbyGetDTO {
    private Long lobbyId;
    private String lobbyUuid;
    private Boolean isInGame;
    private List<User> users;


    public Long getLobbyId() {
        return this.lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyUuid() {
        return this.lobbyUuid;
    }

    public void setLobbyUuid(String lobbyUuid) {
        this.lobbyUuid = lobbyUuid;
    }

    public Boolean getIsInGame() {
        return this.isInGame;
    }

    public void setIsInGame(Boolean isInGame) {
        this.isInGame = isInGame;
    }

    public List<UserGetDTO> getUsers() {
        List<UserGetDTO> userDTOs = new ArrayList<>();
        for(User user : this.users){
            userDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userDTOs;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    
}
