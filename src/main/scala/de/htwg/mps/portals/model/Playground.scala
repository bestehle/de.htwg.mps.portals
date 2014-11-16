package de.htwg.mps.portals.model

import scala.io.Source._

// Possible moves.
sealed trait Move

final case object InvalidMove extends Move
final case object ValidMove extends Move

class Playground(val terrain : Map[Position, Terrain] = Map(), val player : Map[Position, Player] = Map()) {
  // set the move direction for a player
  def setMove(uuid : String, direction : Direction) : Playground = {
    val player = this.player.map{
      case (position, player) => if (uuid == player.uuid) (position, player.switchDirection(direction)) else (position, player) 
    }
    new Playground (terrain, player)
  }
  
  // move the following player
  def move(movePlayer : Player) : (Move, Playground) = {
    val from = movePlayer.position
    val to = movePlayer.nextPosition
    // check for valid positions
    (player.get(from), player.get(to), terrain.get(to)) match {
      // player is on a position which does not exist
      case (None, _, _)			=> (InvalidMove, this)
      // position to which to move does not exist on the terrain
      case (_, _, None)			=> (InvalidMove, this)
      // position exist and no other player is on the position
      case (p1, None, terrain)  => moveCheck(p1.get, terrain.get)
      // another player is already on the position to which to move
      case (p1, p2, _) 			=> collisionMove(p1.get, p2.get)
    }
  }

  // we have a collision move
  // This mean a player like to move to a place where another player is already
  private def collisionMove(from : Player, to : Player) : (Move, Playground) = (from, to) match {
    // Only a valid move if a Bot move to a Human
    case (_ : Bot, _ : Human) => (InvalidMove, this)
    // other moves are invalid
    case _					  => (InvalidMove, this)
  }
  
  // check if it is a valid move
  private def moveCheck(p : Player, t : Terrain)  : (Move, Playground) = {
    // is the terrain walkable by the player?
    if (t.walkableBy(p)) {
      val updatedPlayer = player - p.position + (p.nextPosition -> p.validMove)
      (ValidMove, new Playground(terrain, updatedPlayer))
    } else {
      (InvalidMove, this)
    }
  }

  // the complete playground as a formated string
  override def toString() = {
    var str = ""
    var y = 0
    val sorted = terrain.toSeq.sortWith(_._1 < _._1)
    sorted.foreach(x => {
      if (x._1.y > y) { y += 1; str += "\n" }
      player.get(x._1) match {
      	case Some(player) 	=> str += player.toString
      	case None	 		=> str += x._2
      }
    })
    str
  }

  // load a new playground
  def load(file: String): Playground = {
    val source = fromFile(file)
    val arr = source.map(_.toChar).toArray
    var terrain : Map[Position, Terrain] = Map()
    var player : Map[Position, Player] = Map()  
    var x = 0
    var y = 0

    arr.foreach(input =>
      input match {
        case '\n' => x = 0; y += 1
        case '\r' => None
        case _ 	  => {
          val position = new Position(x, y)
          terrain += (position -> Terrain(input))
          Player(input, position) match {
            case (Some(newPlayer)) => player += (position -> newPlayer)
            case _				   => None
          }
          x += 1
        }
      })
    source.close()
    new Playground(terrain, player)
  }
}