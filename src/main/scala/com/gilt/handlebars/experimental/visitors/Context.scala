package com.gilt.handlebars.experimental.visitors

object Context {
  def createEmpty[T]: Context[T] = new EmptyContext[T]
  def createRoot[T](model: T): Context[T] = new RootContext(model, new EmptyContext[T])
  def createChild[T](model: T, parent: Context[T]): Context[T] = new SimpleContext[T](model, parent)
}

sealed trait Context[+T] extends Loggable {
  val isRoot = this.isInstanceOf[RootContext[T]]
  val isEmpty = this.isInstanceOf[EmptyContext[T]]
  val model: T
  val parent: Context[T]

  def asOption: Option[Context[T]] = if (isEmpty) None else Some(this)
  def notEmpty[A](fallback: Context[A]): Context[A] = if (isEmpty) fallback else this.asInstanceOf[Context[A]]

  override def toString = "Context model[%s] parent[%s]".format(model, parent)
}

class EmptyContext[+T] extends Context[T] {
  val model: T =  null.asInstanceOf[T]
  val parent: Context[T] = null.asInstanceOf[Context[T]]
}

case class RootContext[+T](override val model: T, override val parent: Context[T]) extends Context[T]
case class SimpleContext[+T](override val model: T, override val parent: Context[T]) extends Context[T]
