package com.gilt.handlebars.experimental.visitors

import org.slf4j.{LoggerFactory, Logger}
import com.gilt.handlebars.Handlebars._
import com.gilt.handlebars.Node

/**
 * User: chicks
 * Date: 2/27/13
 */
trait HandlebarsVisitor {
  def visit(node: Node): String
}