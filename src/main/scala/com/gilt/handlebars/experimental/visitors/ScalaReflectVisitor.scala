package com.gilt.handlebars.experimental.visitors

import com.gilt.handlebars.Handlebars._
import com.gilt.handlebars.Node

/**
 * User: chicks
 * Date: 2/27/13
 */
class ScalaReflectVisitor extends HandlebarsVisitor {
  def apply[T](base: T, helpers: Map[String,Helper[T]] = Map.empty[String,Helper[T]]) = {
    // Nothing for now
  }

  def visit(node: Node): String = "Method 'visit' Not Implemented"
}
