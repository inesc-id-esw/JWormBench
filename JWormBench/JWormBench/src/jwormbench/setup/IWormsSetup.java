package jwormbench.setup;

import jwormbench.core.ICoordinate;

public interface IWormsSetup extends Iterable<IWormsSetup.WormProperties>{

  public static class WormProperties{
    public final int id, headSize, speed, groupId, bodyLength;
    public final String name;
    public final ICoordinate[] body;
    public final ICoordinate[] head;
    public WormProperties(int id, int headSize, int speed, int groupId,
        int bodyLength, String name, ICoordinate[] body, ICoordinate[] head) {
      super();
      this.id = id;
      this.headSize = headSize;
      this.speed = speed;
      this.groupId = groupId;
      this.bodyLength = bodyLength;
      this.name = name;
      this.body = body;
      this.head = head;
    }
  }
}