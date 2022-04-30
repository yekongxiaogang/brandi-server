package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.HashSet;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
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

    @Column(nullable = false)
    private Color color;
    
    //TODO: Is this the same as isPlaying?
    @Column(nullable=false)
    private Boolean playerStatus;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PlayerHand playerHand;

    public PlayerState(){}

    public PlayerState(User player, Integer team, Color color, Boolean playerStatus, PlayerHand playerHand) {
        this.player = player;
        this.isPlaying = true;
        this.team = team;
        this.playerStatus = playerStatus;
        this.playerHand = playerHand;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @JsonIgnore
    public PlayerHand getPlayerHand() {
        return this.playerHand;
    }

    public void drawCards(HashSet<Card> cards){
        this.playerHand.drawCards(cards);
    }

    // Returns PlayerGetDTO 
    public UserGetDTO getPlayer() {
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(this.player);
    }

    @JsonIgnore
    public Optional<Long> getCurrentGameId(){
        return this.player.getCurrentGameId();
    }
}
