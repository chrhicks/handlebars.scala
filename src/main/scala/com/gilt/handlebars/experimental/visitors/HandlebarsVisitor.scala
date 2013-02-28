package com.gilt.handlebars.experimental.visitors

import org.slf4j.{LoggerFactory, Logger}
import com.gilt.handlebars.Handlebars._
import com.gilt.handlebars.Node

/**
 * User: chicks
 * Date: 2/27/13
 */
trait HandlebarsVisitor {
  def apply[T](base: T, helpers: Map[String,Helper[T]] = Map.empty[String,Helper[T]])

  def visit(node: Node): String
}