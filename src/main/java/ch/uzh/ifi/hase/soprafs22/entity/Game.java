package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.Move;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "GAME")
@Transactional
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String uuid;
    
    @Column(nullable = false)
    private Boolean gameOver;

    @Column(nullable = false)
    private Boolean gameOn;
    
    @Column(nullable = false)
    private Integer roundsPlayed;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "PlayerState_id")
    private List<PlayerState> playerStates;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "BoardState_id")
    private BoardState boardstate;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "Deck_id")
    private Deck deck;

    @ElementCollection(targetClass = Color.class)
    @Column
    private List<Color> unusedColors;

    private Integer activePlayer;

    public Game() {}

    public Game(User player) {    
        this.gameOver = false;
        this.gameOn = false;
        this.roundsPlayed = 0;
        this.deck = new Deck();
        this.unusedColors = new ArrayList<Color>(Arrays.asList(Color.values()));
        this.playerStates = new ArrayList<PlayerState>();
        this.addPlayer(player);
        this.initBoardState();
        this.uuid = UUID.randomUUID().toString();
        this.activePlayer = 0;
    }

    /* Create balls for each player, store in boardstate */
    private void initBoardState(){
        Set<Ball> balls = new HashSet<Ball> ();

        // Information for initializing the balls with correct positions
        Hashtable<Color, List<Integer>> positionDict = new Hashtable<>();
        positionDict.put(Color.GREEN, new ArrayList<>(Arrays.asList(80, 81, 82, 83)));
        positionDict.put(Color.RED, new ArrayList<>(Arrays.asList(84, 85, 86, 87)));
        positionDict.put(Color.YELLOW, new ArrayList<>(Arrays.asList(88, 89, 90, 91)));
        positionDict.put(Color.BLUE, new ArrayList<>(Arrays.asList(92, 93, 94, 95)));

        // Add 4 balls for each playerColor
        for(Color color: Color.values()){
            for(int i = 0; i < 4; i++){
                balls.add(new Ball(color, positionDict.get(color).get(i)));
            }
        }
        this.boardstate = new BoardState(balls);
    }

    /* Create PlayerState for every player with 6 cards in playerHand, assign team and color randomly */
    private void initPlayerState(User player){
        PlayerHand playerHand = new PlayerHand();
        HashSet<Card> cards = new HashSet<>();

        for(int i = 0; i < 6; i++){
            cards.add(this.deck.drawCard());
        }

        playerHand.drawCards(cards);

        // Team assigned to user by order of joining, users join teams alternatingly
        Integer team = this.playerStates.size() % 2;
        // Pop one color from unused colors, fallback color is yellow (seems like a bad thing to do)
        Color userColor = unusedColors.isEmpty() ? Color.YELLOW : unusedColors.remove(0);

        this.playerStates.add(new PlayerState(player, team, userColor, true, playerHand));
    }

    /*  
     * If player is not in game yet: Add player, return true
     * If player was already in game: Do nothing, return false
     */
    public Boolean addPlayer(User player){
        if(!this.isFull() && !this.gameOn){
            // Check if user is already in this game, if so dont let user join
            Optional<Game> optGame= player.getGameById(this.id);
            if(!optGame.isEmpty()){
                System.out.println("Player is already in this game, no action required. Returned true");
                return false;
            }

            // Check that user is in no other active game
            // If currentGameId is not present then user is in no game -> proceed normally
            Optional<Long> optGameId = player.getCurrentGameId();
            if (optGameId.isPresent()) {
                if(!optGameId.get().equals(this.id)){
                    throw new Error("User is already in a different game");
                } else{
                    // User is already in this game
                    return true;
                }
            }
            
            this.initPlayerState(player);

            // If game is full, automatically start game
            if(this.isFull()){
                this.startGame();
            }
            return true;
        } else{
            // TODO: Should this throw error?
            System.out.println("Game has started / is full, Can't add new player with id " + player.getId());
            return false;
        }
    }

    public Boolean isFull(){
        return this.playerStates.size() >= 4;
    }

    public void startGame(){
        //Check if all Users are in no other active game, should also be checked when adding a player
        for(PlayerState playerState: this.playerStates){
            Optional<Long> optGame = playerState.getCurrentGameId();
            optGame.ifPresent((game) -> {
                throw new Error("A user is already in an active game, can't start this game");
            });
        }
        this.gameOn = true;
    }

    public void startNewRound(){
        this.roundsPlayed += 1;

        List<Integer> amounts = Arrays.asList(6, 5, 4, 3, 2);
        Integer numCardsToPlay = roundsPlayed % 5;

        // Draw new cards for each player
        for(PlayerState playerState : this.playerStates){
            HashSet<Card> cards = new HashSet<>();

            for(int i = 0; i < amounts.get(numCardsToPlay); i++){
                cards.add(this.deck.drawCard());
            }

            playerState.drawCards(cards);
        }
    }

    public Boolean makeMove(Move move){
        Boolean moveExecuted = false;

        Ball ball = null;
        for(Ball b: this.boardstate.getBalls()){
            if(b.getId().equals(move.getBallId())){
                ball = b;
                break;
            }
        }
        if(ball == null) return false;

        // Remove played card from hand
        PlayerHand hand = this.getNextTurn().getPlayerHand();
        hand.deleteCard(move.getPlayedCard());

        //FIXME: Verify that move is a valid move
        ball.setPosition(move.getDestinationTile());
        // Change activePlayer to next user
        //TODO: No idea if this works
        this.nextPlayer();
        return moveExecuted;        
    }

    private void nextPlayer(){
        // Increase as long as activeplayer has no cards
        for(int i = 0; i < 4; i++){
            this.activePlayer++;
            if(!this.getNextTurn().getPlayerHand().isEmpty()){
                return;
            }
        }
        this.activePlayer = null;
    }

    public PlayerState getNextTurn(){
        if(this.activePlayer == null) return null;
        return this.playerStates.get(this.activePlayer);
    }

    // return true if all players have no more cards
    public Boolean allPlayersFinished(){
        for(PlayerState playerstate: this.playerStates){
            if(!playerstate.getPlayerHand().getActiveCards().isEmpty()) return false;
        }
        return true;
    }

    // Check if all players in game are active in this game, only works if game is started already
    public Boolean checkAllPlayersInGame(){
        for(PlayerState playerState: this.playerStates){
            Optional<Long> optGameId = playerState.getCurrentGameId();
            if(optGameId.isPresent()){
                Long gameId = optGameId.get();
                if(gameId.equals(this.id)){
                    continue;
                } else {
                    System.out.println("User is active in a different game");
                    return false;
                }
            } else{
                System.out.println("Couldnt find an active game for user");
            }
        }
        return true;

        // Previous approach, only checks if all players are in a game, not if all are in this game
        /* for(PlayerState playerState: this.playerStates){
            if(!playerState.getIsPlaying()){return false;}
        }
        return true; */
    }

    public void pauseGame(){
        this.gameOn = false;
    }

    public void resumeGame(){
        this.gameOn = true;
    }

    public void endGame(){
        this.gameOver = true;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean isGameOver() {
        return this.gameOver;
    }

    public Boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Boolean isGameOn() {
        return this.gameOn;
    }

    public Boolean getGameOn() {
        return this.gameOn;
    }

    public void setGameOn(Boolean gameOn) {
        this.gameOn = gameOn;
    }

    public Integer getRoundsPlayed() {
        return this.roundsPlayed;
    }

    public void setRoundsPlayed(Integer roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public List<PlayerState> getPlayerStates() {
        return this.playerStates;
    }

    public void setPlayerStates(ArrayList<PlayerState> playerStates) {
        this.playerStates = playerStates;
    }

    public BoardState getBoardstate() {
        return this.boardstate;
    }

    public void setBoardstate(BoardState boardstate) {
        this.boardstate = boardstate;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public PlayerState getPlayerState(String playerName) {
        PlayerState state = null;
        for (PlayerState player : this.playerStates){
            if(player.getPlayer().getUsername().equals(playerName)) {
                return player;
            }
        }
        return null;
    }


    @JsonIgnore
    public Optional<Color> getUserColorById(Long id){
        for(PlayerState playerState: this.playerStates){
            if(playerState.getPlayer().getId() == id){
                return Optional.of(playerState.getColor());
            }
        }
        return Optional.empty();
    }

    public void surrenderCards(String username){
        PlayerState playerState = this.getPlayerState(username);
        playerState.getPlayerHand().setActiveCards(null);
    }
}
