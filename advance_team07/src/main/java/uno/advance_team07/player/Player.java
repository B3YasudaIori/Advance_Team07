package uno.advance_team07.player;

public class Player {

  private String username;
  private int balance;
  private boolean active;

  public Player() {
  }

  public Player(String username, int balance, boolean active) {
    this.username = username;
    this.balance = balance;
    this.active = active;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}