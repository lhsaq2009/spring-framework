package org.springframework.context.annotation

/**
 * Extension for [AnnotationConfigApplicationContext] allowing
 * `AnnotationConfigApplicationContext { ... }` style initialization.
 *
 * @author Sebastien Deleuze
 * @since 5.0
 */
@Deprecated("Use regular apply method instead.", replaceWith = ReplaceWith("AnnotationConfigApplicationContext().apply(configure)"))
fun AnnotationConfigApplicationContext(configure: AnnotationConfigApplicationContext.() -> Unit) =
		AnnotationConfigApplicationContext().apply(configure)
