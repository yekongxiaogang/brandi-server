package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

@Entity
public class PlayerState {

    @Id
    @GeneratedValue
    private Long id;

    //FIXME: Does this mean that User gets deleted when one of his games is deleted?
    @ManyToOne(cascade = CascadeType.ALL)
    private User player;
    
    @Column(nullable=false)
    private Boolean isPlaying;
    
    @Column(nullable=false)
    private Integer team;
    
    @Column(nullable=false)
    private Boolean playerStatus;
    
    @OneToOne(cascade = CascadeType.ALL)
    private PlayerHand playerHand;

    public PlayerState(){}

    public PlayerState(User player, Integer team, Boolean playerStatus, PlayerHand playerHand) {
        //FIXME: player probably doesnt need to be the whole user because this includes pwd and token
        this.player = player;
        this.isPlaying = true;
        this.team = team;
        this.playerStatus = playerStatus;
        this.playerHand = playerHand;
    }

    public Boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Integer getTeam() {
        return this.team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Boolean getPlayerStatus() {
        return this.playerStatus;
    }

    public void setPlayerStatus(Boolean playerStatus) {
        this.playerStatus = playerStatus;
    }

    public PlayerHand getPlayerHand() {
        return this.playerHand;
    }

    public void drawCards(ArrayList<Card> cards){
        this.playerHand.drawCards(cards);
    }

    // Returns PlayerGetDTO 
    public UserGetDTO getPlayer() {
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(this.player);
    }



    /* public void setPlayerHand(PlayerHand playerHand) {
        this.playerHand = playerHand;
    } */

}
