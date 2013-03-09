package com.gilt.handlebars.experimental.visitors

import org.slf4j.{LoggerFactory, Logger}

import com.gilt.handlebars.Handlebars.Helper
import com.gilt.handlebars.experimental.visitors.Context

import com.gilt.handlebars._
import collection.JavaConversions._
import com.gilt.handlebars.Comment
import com.gilt.handlebars.Content
import com.gilt.handlebars.Mustache
import com.gilt.handlebars.Program

object ScalaReflectVisitor {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def apply[T](base: T, helpers: Map[String,Helper[T]] = Map.empty[String,Helper[T]]) = {
    new ScalaReflectVisitor[T](Context.createRoot(base))
  }
}

class ScalaReflectVisitor[T](context: Context[T]) extends com.gilt.handlebars.experimental.visitors.HandlebarsVisitor with Loggable {
  import ScalaReflectVisitor._

  override def visit(node: Node): String = node match {
    case Content(value) => value
    case Comment(_) => ""
    case Program(value, _) => value.map(visit(_)).mkString
    case Mustache(path, params, escaped) => resolvePath(path.value).model.toString
    case _ => toString

//    case Content(content) => content
//    case Identifier(ident) => context.invoke(ident).getOrElse("").toString
//    case Path(path) => resolvePath(path).definedOrEmpty.context.toString
//    case Comment(_) => ""
//    case Partial(partial) => compilePartial(partial)
//    case Mustache(stache, params, escaped) => resolveMustache(stache, params, escape = escaped)
//    case Section(stache, value, inverted) => renderSection(stache.value, stache.parameters, value, inverted)
//    case Program(children, _) => children.map(visit).mkString
//    case _ => toString
  }

  def resolvePath(path: List[Identifier]): Context[Any] = {
    path.foldLeft(Context.createEmpty[Any]) { (pathContext, identifier) =>
      info("folding: identifier[%s] on context - %s".format(identifier, pathContext))

      val ctx = pathContext.notEmpty(context)

      info("ctx[%s]".format(ctx))

      if ("..".equals(identifier.value)) {
        ctx.parent
      } else {
        ctx.model match {
          case _ => Context.createChild[Any](ReflectUtils.invoke(ctx.model, identifier.value), ctx)
        }
      }
    }.asOption.getOrElse(Context.createEmpty[Any])
  }
}
