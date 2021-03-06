package de.htwg.mps.portals.actor

import com.escalatesoft.subcut.inject.AutoInjectable
import com.escalatesoft.subcut.inject.Injectable
import de.htwg.mps.portals.controller.Event
import de.htwg.mps.portals.controller.IController
import de.htwg.mps.portals.model.Direction
import de.htwg.mps.portals.util.Observer
import com.escalatesoft.subcut.inject.BindingModule
import akka.actor.ActorSystem
import akka.actor.Props
import de.htwg.mps.portals.controller.NewGame
import de.htwg.mps.portals.controller.GameWon
import de.htwg.mps.portals.controller.GameLost
import de.htwg.mps.portals.controller.Wait
import de.htwg.mps.portals.model.Player
import de.htwg.mps.portals.controller.Update
import akka.actor.ActorRef
import de.htwg.mps.portals.controller.NewGame
import com.typesafe.config.ConfigValueFactory
import com.typesafe.config.ConfigFactory

class AktorSystem(implicit val bindingModule: BindingModule) extends Observer[Event] with Injectable {
  val controller = inject[IController]
  controller.add(this)

  var masterId = 1;

  // Create an Akka system
  val config = ConfigFactory.load()
     .withValue("akka.loglevel", ConfigValueFactory.fromAnyRef("OFF"))
     .withValue("akka.stdout-loglevel", ConfigValueFactory.fromAnyRef("OFF"))
  val system = ActorSystem("WorkerSystem", config)

  // create the master
  var master = system.actorOf(Props(new MasterActor(masterId)), name = "master" + masterId)

  def createMaster() {
    system.stop(master);
    masterId += 1
    master = system.actorOf(Props(new MasterActor(masterId)), name = "master" + masterId)
  }

  def move(id: String, direction: Direction) {
    master ! PlayerMove(id, direction)
  }

  def update(e: Event) = {
    e match {
      case x: NewGame =>
        createMaster()
        controller.playground.player.foreach(x => master ! CreatePlayer(x._2.uuid))
        master ! GameEvent(x)
      case x: Update   => master ! PlayerMove(x.move.player.uuid, x.move.player.direction)
      case x: GameWon  => master ! GameEvent(x)
      case x: GameLost => master ! GameEvent(x)
      case x: Wait     => None
    }
  }

}