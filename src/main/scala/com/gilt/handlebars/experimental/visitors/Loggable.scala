package com.gilt.handlebars.experimental.visitors

import org.slf4j.LoggerFactory

trait Loggable {
  private val logger = LoggerFactory.getLogger(getClass)

  def debug(message: String, t: Any*) = {
    if (logger.isDebugEnabled) {
      logger.debug(message, t)
    }
  }

  def debug(message: String, t: Throwable) = {
    if (logger.isDebugEnabled) {
      logger.debug(message, t)
    }
  }

  def info(message: String, t: Any*) = logger.info(message, t)

  def info(message: String, t: Throwable) = logger.info(message, t)

  def warn(message: String, t: Any*) = logger.warn(message, t)

  def warn(message: String, t: Throwable) = logger.warn(message, t)

  def error(message: String, t: Any*) = logger.error(message, t)

  def error(message: String, t: Throwable) = logger.error(message, t)
}
