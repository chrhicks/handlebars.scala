package com.gilt.handlebars.experimental.visitors

import reflect.runtime.universe._
import java.util.concurrent.ConcurrentHashMap


object ReflectUtils extends Loggable {
  private val cache = new ConcurrentHashMap[(Class[_], String), (Mirror, TermSymbol)]

  def invoke(obj: Any, prop: String): Any = {
    val (mirror, termSymbol) = getEntry((obj.getClass, prop))
    val instanceMirror = mirror.reflect(obj)
    val fieldMirror = instanceMirror.reflectField(termSymbol)
    val result = fieldMirror.get

    result match {
      case Some(o) => if (isPrimitiveType(o)) o else result
      case None => ""
      case _ => result
    }
  }

  def getEntry(key: (Class[_], String)): (Mirror, TermSymbol) = {
    val (classObj, prop) = key
    val entry = cache.get(key)
    if (entry == null) {
      val mirror = runtimeMirror(classObj.getClassLoader)
      val classSymbol = mirror.classSymbol(classObj)
      val tType = classSymbol.toType
      val propTermSymbol = tType.declaration(newTermName(prop)).asTerm
      cache.putIfAbsent(key, (mirror, propTermSymbol))
      (mirror, propTermSymbol)
    } else {
      entry
    }
  }

  def isPrimitiveType(obj: Any) = obj.isInstanceOf[Int] || obj.isInstanceOf[Long] || obj.isInstanceOf[Float] ||
    obj.isInstanceOf[BigDecimal] || obj.isInstanceOf[Double] || obj.isInstanceOf[String]
}
