package lib

import java.net.InetSocketAddress
import akka.actor.{Actor, Props}
import akka.io.{IO, Tcp}

class Server extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))

  def receive = {
    case b @ Bound(localAddress) =>
    // do some logging or setup ...

    case CommandFailed(_: Bind) => context stop self

    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[SimplisticHandler])
      val connection = sender()
      connection ! Register(handler)
  }

  def foo(a: String): Unit = {
    println("AAAA" + a );
  }
}

class SimplisticHandler extends Actor {
  import Tcp._
  def receive = {
    case Received(data) ⇒ sender() ! Write(data)
    case PeerClosed     ⇒ context stop self
  }
}

class Bar {
  def foo(a: String): Unit = {
    println("AAAA" + a );
  }
}
