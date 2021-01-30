package net.verany.api.redis.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VeranyMessageOutEvent extends Event {

  private String message;
  
  public VeranyMessageOutEvent(String message)
  {
    this.message = message;
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  private static final HandlerList handlers = new HandlerList();
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public HandlerList getHandlers()
  {
    return getHandlerList();
  }
}
