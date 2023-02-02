package org.springframework.transaction.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.TransactionDefinition;

/**
 * Describes a transaction attribute on an individual method or on a class.
 * 描述 单个方法 或 类上的事务属性 <br/><br/>
 *
 * <hr>
 *
 * <p>At the class level, this annotation applies as a default to all methods of
 * the declaring class and its subclasses. Note that it does not apply to ancestor
 * classes up the class hierarchy; methods need to be locally redeclared in order
 * to participate in a subclass-level annotation.<br/><br/>
 *
 * TODO (重要)：在「类级别」，此注释作为默认值应用于声明类及其子类的所有方法。
 * TODO (重要)：请注意，它不适用于类层次结构中的祖先类; 方法需要在本地重新声明才能参与子类级别的注释。<br/><br/>
 *
 * <hr>
 *
 * <p>This annotation type is generally directly comparable to Spring's
 * {@link org.springframework.transaction.interceptor.RuleBasedTransactionAttribute}
 * class, and in fact {@link AnnotationTransactionAttributeSource} will directly
 * convert the data to the latter class, so that Spring's transaction support code
 * does not have to know about annotations. If no custom rollback rules apply,
 * the transaction will roll back on {@link RuntimeException} and {@link Error}
 * but not on checked exceptions. <br/><br/>
 *
 * 这种注解类型一般可以直接与 Spring 的 org.springframework.transaction.interceptor.RuleBasedTransactionAttribute 类相媲美，
 * 实际上 AnnotationTransactionAttributeSource 会将数据直接转换为后一类，这样 Spring 的事务支持代码就不必知道注解了。
 * 如果未应用自定义回滚规则，则事务将在运行时异常和错误时回滚，但不在选中的异常上回滚。<br/><br/>
 *
 * <hr>
 *
 * <p>For specific information about the semantics of this annotation's attributes,
 * consult the {@link org.springframework.transaction.TransactionDefinition} and
 * {@link org.springframework.transaction.interceptor.TransactionAttribute} javadocs.<br/><br/>
 *
 * 有关此注解属性语义的特定信息，请参阅 TransactionDefinition 和
 * org.springframework.transaction.interceptor.TransactionAttribute javadocs。<br/><br/>
 *
 * <hr>
 *
 * <p>This annotation commonly works with thread-bound transactions managed by
 * {@link org.springframework.transaction.PlatformTransactionManager}, exposing a
 * transaction to all data access operations within the current execution thread.
 * <b>Note: This does NOT propagate to newly started threads within the method.</b><br/><br/>
 *
 * 此注释通常适用于由 org.springframework.transaction.PlatformTransactionManager
 * 管理的线程绑定事务，将事务公开给当前执行线程中的所有数据访问操作。注意：这不会传播到方法中新启动的线程。<br/><br/>
 *
 * <hr>
 *
 * <p>Alternatively, this annotation may demarcate a reactive transaction managed
 * by {@link org.springframework.transaction.ReactiveTransactionManager} which
 * uses the Reactor context instead of thread-local attributes. As a consequence,
 * all participating data access operations need to execute within the same
 * Reactor context in the same reactive pipeline.<br/><br/>
 *
 * 或者，此注释可以划分由org.springframework.transaction.ReactiveTransactionManager管理的反应式事务，
 * 该事务使用Actor上下文而不是线程本地属性。因此，所有参与的数据访问操作都需要在同一反应式管道的同一 Reactor 上下文中执行。<br/><br/>
 *
 * <hr>
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 1.2
 * @see org.springframework.transaction.interceptor.TransactionAttribute
 * @see org.springframework.transaction.interceptor.DefaultTransactionAttribute
 * @see org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {

	/**
	 * Alias for {@link #transactionManager}.
	 * @see #transactionManager
	 */
	@AliasFor("transactionManager")
	String value() default "";

	/**
	 * A <em>qualifier</em> value for the specified transaction.
	 * <p>May be used to determine the target transaction manager, matching the
	 * qualifier value (or the bean name) of a specific
	 * {@link org.springframework.transaction.TransactionManager TransactionManager}
	 * bean definition.
	 * @since 4.2
	 * @see #value
	 * @see org.springframework.transaction.PlatformTransactionManager
	 * @see org.springframework.transaction.ReactiveTransactionManager
	 */
	@AliasFor("value")
	String transactionManager() default "";

	/**
	 * The transaction propagation type.
	 * <p>Defaults to {@link Propagation#REQUIRED}.
	 * @see org.springframework.transaction.interceptor.TransactionAttribute#getPropagationBehavior()
	 */
	Propagation propagation() default Propagation.REQUIRED;

	/**
	 * The transaction isolation level.
	 * <p>Defaults to {@link Isolation#DEFAULT}.
	 * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
	 * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
	 * transactions. Consider switching the "validateExistingTransactions" flag to
	 * "true" on your transaction manager if you'd like isolation level declarations
	 * to get rejected when participating in an existing transaction with a different
	 * isolation level.
	 * @see org.springframework.transaction.interceptor.TransactionAttribute#getIsolationLevel()
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setValidateExistingTransaction
	 */
	Isolation isolation() default Isolation.DEFAULT;

	/**
	 * The timeout for this transaction (in seconds).
	 * <p>Defaults to the default timeout of the underlying transaction system.
	 * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
	 * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
	 * transactions.
	 * @see org.springframework.transaction.interceptor.TransactionAttribute#getTimeout()
	 */
	int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

	/**
	 * A boolean flag that can be set to {@code true} if the transaction is
	 * effectively read-only, allowing for corresponding optimizations at runtime.
	 * <p>Defaults to {@code false}.
	 * <p>This just serves as a hint for the actual transaction subsystem;
	 * it will <i>not necessarily</i> cause failure of write access attempts.
	 * A transaction manager which cannot interpret the read-only hint will
	 * <i>not</i> throw an exception when asked for a read-only transaction
	 * but rather silently ignore the hint.
	 * @see org.springframework.transaction.interceptor.TransactionAttribute#isReadOnly()
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isCurrentTransactionReadOnly()
	 */
	boolean readOnly() default false;

	/**
	 * Defines zero (0) or more exception {@link Class classes}, which must be
	 * subclasses of {@link Throwable}, indicating which exception types must cause
	 * a transaction rollback.
	 * <p>By default, a transaction will be rolling back on {@link RuntimeException}
	 * and {@link Error} but not on checked exceptions (business exceptions). See
	 * {@link org.springframework.transaction.interceptor.DefaultTransactionAttribute#rollbackOn(Throwable)}
	 * for a detailed explanation.
	 * <p>This is the preferred way to construct a rollback rule (in contrast to
	 * {@link #rollbackForClassName}), matching the exception class and its subclasses.
	 * <p>Similar to {@link org.springframework.transaction.interceptor.RollbackRuleAttribute#RollbackRuleAttribute(Class clazz)}.
	 * @see #rollbackForClassName
	 * @see org.springframework.transaction.interceptor.DefaultTransactionAttribute#rollbackOn(Throwable)
	 */
	Class<? extends Throwable>[] rollbackFor() default {};

	/**
	 * Defines zero (0) or more exception names (for exceptions which must be a
	 * subclass of {@link Throwable}), indicating which exception types must cause
	 * a transaction rollback.
	 * <p>This can be a substring of a fully qualified class name, with no wildcard
	 * support at present. For example, a value of {@code "ServletException"} would
	 * match {@code javax.servlet.ServletException} and its subclasses.
	 * <p><b>NB:</b> Consider carefully how specific the pattern is and whether
	 * to include package information (which isn't mandatory). For example,
	 * {@code "Exception"} will match nearly anything and will probably hide other
	 * rules. {@code "java.lang.Exception"} would be correct if {@code "Exception"}
	 * were meant to define a rule for all checked exceptions. With more unusual
	 * {@link Exception} names such as {@code "BaseBusinessException"} there is no
	 * need to use a FQN.
	 * <p>Similar to {@link org.springframework.transaction.interceptor.RollbackRuleAttribute#RollbackRuleAttribute(String exceptionName)}.
	 * @see #rollbackFor
	 * @see org.springframework.transaction.interceptor.DefaultTransactionAttribute#rollbackOn(Throwable)
	 */
	String[] rollbackForClassName() default {};

	/**
	 * Defines zero (0) or more exception {@link Class Classes}, which must be
	 * subclasses of {@link Throwable}, indicating which exception types must
	 * <b>not</b> cause a transaction rollback.
	 * <p>This is the preferred way to construct a rollback rule (in contrast
	 * to {@link #noRollbackForClassName}), matching the exception class and
	 * its subclasses.
	 * <p>Similar to {@link org.springframework.transaction.interceptor.NoRollbackRuleAttribute#NoRollbackRuleAttribute(Class clazz)}.
	 * @see #noRollbackForClassName
	 * @see org.springframework.transaction.interceptor.DefaultTransactionAttribute#rollbackOn(Throwable)
	 */
	Class<? extends Throwable>[] noRollbackFor() default {};

	/**
	 * Defines zero (0) or more exception names (for exceptions which must be a
	 * subclass of {@link Throwable}) indicating which exception types must <b>not</b>
	 * cause a transaction rollback.
	 * <p>See the description of {@link #rollbackForClassName} for further
	 * information on how the specified names are treated.
	 * <p>Similar to {@link org.springframework.transaction.interceptor.NoRollbackRuleAttribute#NoRollbackRuleAttribute(String exceptionName)}.
	 * @see #noRollbackFor
	 * @see org.springframework.transaction.interceptor.DefaultTransactionAttribute#rollbackOn(Throwable)
	 */
	String[] noRollbackForClassName() default {};

}
